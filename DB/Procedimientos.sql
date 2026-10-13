
USE TecnoStore_JAQO;


DELIMITER ¬¬

-- P1. Registrar venta simple
DROP PROCEDURE IF EXISTS sp_registrar_venta¬¬
CREATE PROCEDURE sp_registrar_venta(
    IN  p_cliente_id  INT,
    IN  p_empleado_id INT,
    IN  p_celular_id  INT,
    IN  p_cantidad    INT,
    OUT p_venta_id    INT,
    OUT p_total       DOUBLE
)
BEGIN
    DECLARE v_stock  INT;
    DECLARE v_activo BOOLEAN;
    DECLARE v_tipo   VARCHAR(10);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT tipo INTO v_tipo FROM personas WHERE id = p_cliente_id;
    IF v_tipo IS NULL OR v_tipo <> 'CLIENTE' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El cliente no existe o no es CLIENTE';
    END IF;

    SELECT activo INTO v_activo FROM empleados WHERE persona_id = p_empleado_id;
    IF v_activo IS NULL OR v_activo = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El empleado no existe o está inactivo';
    END IF;

    SELECT stock INTO v_stock FROM celulares WHERE id = p_celular_id;
    IF v_stock IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El celular no existe';
    END IF;

    IF p_cantidad > v_stock THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente';
    END IF;

    INSERT INTO ventas (cliente_id, empleado_id, fecha)
    VALUES (p_cliente_id, p_empleado_id, NOW());

    SET p_venta_id = LAST_INSERT_ID();

    INSERT INTO detalle_ventas (venta_id, celular_id, cantidad, precio_unitario, moneda)
    SELECT p_venta_id, p_celular_id, p_cantidad, precio, moneda
      FROM celulares WHERE id = p_celular_id;

    SET p_total = fn_total_venta(p_venta_id);
    COMMIT;
END¬¬

-- P2. Registrar venta múltiple con JSON
DROP PROCEDURE IF EXISTS sp_registrar_venta_multiple¬¬
CREATE PROCEDURE sp_registrar_venta_multiple(
    IN  p_cliente_id  INT,
    IN  p_empleado_id INT,
    IN  p_items_json  JSON,
    OUT p_venta_id    INT,
    OUT p_total       DOUBLE
)
BEGIN
    DECLARE v_idx      INT DEFAULT 0;
    DECLARE v_len      INT;
    DECLARE v_celular  INT;
    DECLARE v_cantidad INT;
    DECLARE v_stock    INT;
    DECLARE v_activo   BOOLEAN;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT activo INTO v_activo FROM empleados WHERE persona_id = p_empleado_id;
    IF v_activo IS NULL OR v_activo = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El empleado no existe o está inactivo';
    END IF;

    INSERT INTO ventas (cliente_id, empleado_id, fecha)
    VALUES (p_cliente_id, p_empleado_id, NOW());
    SET p_venta_id = LAST_INSERT_ID();

    SET v_len = JSON_LENGTH(p_items_json);

    WHILE v_idx < v_len DO
        SET v_celular  = JSON_EXTRACT(p_items_json, CONCAT('$[', v_idx, '].celular_id'));
        SET v_cantidad = JSON_EXTRACT(p_items_json, CONCAT('$[', v_idx, '].cantidad'));

        SELECT stock INTO v_stock FROM celulares WHERE id = v_celular;
        IF v_stock IS NULL OR v_cantidad > v_stock THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Stock insuficiente para uno de los items';
        END IF;

        INSERT INTO detalle_ventas (venta_id, celular_id, cantidad, precio_unitario, moneda)
        SELECT p_venta_id, v_celular, v_cantidad, precio, moneda
          FROM celulares WHERE id = v_celular;

        SET v_idx = v_idx + 1;
    END WHILE;

    SET p_total = fn_total_venta(p_venta_id);
    COMMIT;
END¬¬

-- P3. Anular venta
DROP PROCEDURE IF EXISTS sp_anular_venta¬¬
CREATE PROCEDURE sp_anular_venta(IN p_venta_id INT)
BEGIN
    DECLARE v_existe INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT COUNT(*) INTO v_existe FROM ventas WHERE id = p_venta_id;
    IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La venta no existe';
    END IF;

    DELETE FROM detalle_ventas WHERE venta_id = p_venta_id;
    DELETE FROM ventas WHERE id = p_venta_id;

    COMMIT;
END¬¬

-- P4. Reponer stock
DROP PROCEDURE IF EXISTS sp_reponer_stock¬¬
CREATE PROCEDURE sp_reponer_stock(
    IN p_celular_id INT,
    IN p_cantidad   INT
)
BEGIN
    IF p_cantidad <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La cantidad debe ser mayor a cero';
    END IF;

    UPDATE celulares SET stock = stock + p_cantidad WHERE id = p_celular_id;

    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El celular no existe';
    END IF;
END¬¬

-- P5. Actualizar precios por porcentaje
DROP PROCEDURE IF EXISTS sp_actualizar_precios¬¬
CREATE PROCEDURE sp_actualizar_precios(
    IN p_marca_id   INT,
    IN p_porcentaje DECIMAL(5,2)
)
BEGIN
    DECLARE v_count INT;

    IF p_porcentaje <= -100 OR p_porcentaje > 100 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Porcentaje fuera de rango (-99 a 100)';
    END IF;

    UPDATE celulares
       SET precio = ROUND(precio * (1 + p_porcentaje/100), 2)
     WHERE (p_marca_id IS NULL OR marca_id = p_marca_id);

    SET v_count = ROW_COUNT();
    SELECT CONCAT(v_count, ' celulares actualizados') AS resultado;
END¬¬

-- P6. Crear celular
DROP PROCEDURE IF EXISTS sp_crear_celular¬¬
CREATE PROCEDURE sp_crear_celular(
    IN  p_marca_id     INT,
    IN  p_modelo       VARCHAR(80),
    IN  p_precio_id    BIGINT,
    IN  p_so_id        INT,
    IN  p_gama_id      INT,
    IN  p_stock        INT,
    IN  p_stock_minimo INT,
    OUT p_celular_id   INT
)
BEGIN

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- 1. Validar marca
    IF NOT EXISTS (SELECT 1 FROM marcas WHERE id = p_marca_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La marca no existe';
    END IF;

    -- 2. Validar precio
    IF NOT EXISTS (SELECT 1 FROM precios WHERE id = p_precio_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El precio no existe';
    END IF;

    -- 3. Validar sistema operativo
    IF NOT EXISTS (SELECT 1 FROM sistemas_operativos WHERE id = p_so_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El sistema operativo no existe';
    END IF;

    -- 4. Validar gama
    IF NOT EXISTS (SELECT 1 FROM gamas WHERE id = p_gama_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La gama no existe';
    END IF;

    -- 5. Validar modelo
    IF p_modelo IS NULL OR TRIM(p_modelo) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El modelo es obligatorio';
    END IF;

    IF CHAR_LENGTH(TRIM(p_modelo)) < 2 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El modelo debe tener al menos 2 caracteres';
    END IF;

    -- 6. Validar stock
    IF p_stock IS NULL OR p_stock < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El stock no puede ser negativo';
    END IF;

    IF p_stock_minimo IS NULL OR p_stock_minimo < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El stock mínimo no puede ser negativo';
    END IF;

    -- 7. Validar unicidad (marca + modelo)
    IF EXISTS (
        SELECT 1 FROM celulares
         WHERE marca_id = p_marca_id
           AND modelo = TRIM(p_modelo)
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ya existe un celular con esa marca y modelo';
    END IF;

    -- 8. Insertar
    INSERT INTO celulares
        (marca_id, modelo, precio_id, so_id, gama_id, stock, stock_minimo)
    VALUES
        (p_marca_id, TRIM(p_modelo), p_precio_id, p_so_id, p_gama_id,
         p_stock, p_stock_minimo);

    SET p_celular_id = LAST_INSERT_ID();

    COMMIT;
END¬¬

-- P7. Actualizar celular
DROP PROCEDURE IF EXISTS sp_actualizar_celular¬¬
CREATE PROCEDURE sp_actualizar_celular(
    IN p_id            INT,
    IN p_marca_id      INT,
    IN p_modelo        VARCHAR(80),
    IN p_precio_id     BIGINT,
    IN p_so_id         INT,
    IN p_gama_id       INT,
    IN p_stock         INT,
    IN p_stock_minimo  INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- Validar existencia
    IF NOT EXISTS (SELECT 1 FROM celulares WHERE id = p_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El celular no existe';
    END IF;

    -- Validar FK
    IF NOT EXISTS (SELECT 1 FROM marcas WHERE id = p_marca_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La marca no existe';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM precios WHERE id = p_precio_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El precio no existe';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM sistemas_operativos WHERE id = p_so_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El SO no existe';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM gamas WHERE id = p_gama_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La gama no existe';
    END IF;

    -- Validar stock
    IF p_stock < 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El stock no puede ser negativo';
    END IF;

    -- Validar unicidad (excluyendo el mismo)
    IF EXISTS (
        SELECT 1 FROM celulares
         WHERE marca_id = p_marca_id
           AND modelo = TRIM(p_modelo)
           AND id <> p_id
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ya existe otro celular con esa marca y modelo';
    END IF;

    -- Actualizar
    UPDATE celulares
       SET marca_id = p_marca_id,
           modelo = TRIM(p_modelo),
           precio_id = p_precio_id,
           so_id = p_so_id,
           gama_id = p_gama_id,
           stock = p_stock,
           stock_minimo = p_stock_minimo
     WHERE id = p_id;

    COMMIT;
END¬¬


-- P6. Crear cliente (persona + cliente)
DROP PROCEDURE IF EXISTS sp_crear_cliente¬¬
CREATE PROCEDURE sp_crear_cliente(
    IN  p_nombre         VARCHAR(100),
    IN  p_identificacion VARCHAR(30),
    IN  p_correo         VARCHAR(120),
    IN  p_telefono       VARCHAR(20),
    IN  p_contraseña     VARCHAR(20),
    OUT p_persona_id     INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    INSERT INTO personas (nombre, identificacion, correo, telefono, tipo)
    VALUES (p_nombre, p_identificacion, p_correo, p_telefono, 'CLIENTE');

    SET p_persona_id = LAST_INSERT_ID();

    INSERT INTO clientes (persona_id, contraseña) VALUES (p_persona_id, p_contraseña);

    COMMIT;
END¬¬

DROP PROCEDURE IF EXISTS sp_actualizar_cliente¬¬
CREATE PROCEDURE sp_actualizar_cliente(
    IN p_persona_id     INT,
    IN p_nombre         VARCHAR(100),
    IN p_identificacion VARCHAR(30),
    IN p_correo         VARCHAR(120),
    IN p_telefono       VARCHAR(20)
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT COUNT(*) INTO v_existe
      FROM personas
     WHERE id = p_persona_id AND tipo = 'CLIENTE';

    IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El cliente no existe';
    END IF;

    IF p_nombre IS NULL OR TRIM(p_nombre) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El nombre es obligatorio';
    END IF;

    IF CHAR_LENGTH(TRIM(p_nombre)) < 3 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El nombre debe tener al menos 3 caracteres';
    END IF;

    IF CHAR_LENGTH(TRIM(p_nombre)) > 100 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El nombre no puede superar 100 caracteres';
    END IF;

    IF TRIM(p_nombre) NOT REGEXP '^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El nombre solo admite letras y espacios';
    END IF;

    IF p_identificacion IS NULL OR TRIM(p_identificacion) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La identificación es obligatoria';
    END IF;

    IF TRIM(p_identificacion) NOT REGEXP '^[0-9]{6,15}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La identificación debe tener entre 6 y 15 dígitos';
    END IF;

    IF EXISTS (
        SELECT 1 FROM personas
         WHERE identificacion = TRIM(p_identificacion)
           AND id <> p_persona_id
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La identificación ya está registrada por otro usuario';
    END IF;

    IF p_correo IS NULL OR TRIM(p_correo) = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El correo es obligatorio';
    END IF;

    IF TRIM(p_correo) NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El correo tiene un formato inválido';
    END IF;

    IF EXISTS (
        SELECT 1 FROM personas
         WHERE correo = LOWER(TRIM(p_correo))
           AND id <> p_persona_id
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El correo ya está registrado por otro usuario';
    END IF;

    UPDATE personas
       SET nombre         = TRIM(p_nombre),
           identificacion = TRIM(p_identificacion),
           correo         = LOWER(TRIM(p_correo)),
           telefono       = IF(TRIM(p_telefono) = '', NULL, TRIM(p_telefono))
     WHERE id = p_persona_id
       AND tipo = 'CLIENTE';

    COMMIT;
END¬¬


-- P7. Crear empleado (persona + empleado)
DROP PROCEDURE IF EXISTS sp_crear_empleado¬¬
CREATE PROCEDURE sp_crear_empleado(
    IN  p_nombre         VARCHAR(100),
    IN  p_identificacion VARCHAR(30),
    IN  p_correo         VARCHAR(120),
    IN  p_telefono       VARCHAR(20),
    IN  p_cargo          VARCHAR(20),
    IN  p_salario        DOUBLE,
    IN  p_fecha_ingreso  DATE,
    IN  p_contraseña     VARCHAR(20),
    OUT p_persona_id     INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    INSERT INTO personas (nombre, identificacion, correo, telefono, tipo)
    VALUES (p_nombre, p_identificacion, p_correo, p_telefono, 'EMPLEADO');

    SET p_persona_id = LAST_INSERT_ID();

    INSERT INTO empleados (persona_id, cargo, salario,  fecha_ingreso, activo, contraseña)
    VALUES (p_persona_id, p_cargo, p_salario, p_fecha_ingreso, TRUE, p_contraseña);

    COMMIT;
END¬¬

-- P8. Reporte de ventas (devuelve filas)
DROP PROCEDURE IF EXISTS sp_reporte_ventas¬¬
CREATE PROCEDURE sp_reporte_ventas()
BEGIN
    SELECT
        v.id AS venta_id,
        v.fecha,
        pc.nombre AS cliente,
        pe.nombre AS empleado,
        m.nombre  AS marca,
        ce.modelo AS modelo,
        d.cantidad,
        d.precio_unitario,
        ROUND(d.cantidad * d.precio_unitario, 2) AS subtotal_linea
    FROM ventas v
    JOIN personas pc      ON pc.id = v.cliente_id
    JOIN personas pe      ON pe.id = v.empleado_id
    JOIN detalle_ventas d ON d.venta_id = v.id
    JOIN celulares ce     ON ce.id = d.celular_id
    JOIN marcas m         ON m.id = ce.marca_id
    ORDER BY v.fecha DESC, v.id DESC;
END¬¬

-- P9. Actualizar empleado (persona + empleado)
DROP PROCEDURE IF EXISTS sp_actualizar_empleado¬¬
CREATE PROCEDURE sp_actualizar_empleado(
    IN p_persona_id      INT,
    IN p_nombre          VARCHAR(100),
    IN p_identificacion  VARCHAR(30),
    IN p_correo          VARCHAR(120),
    IN p_telefono        VARCHAR(20),
    IN p_cargo           VARCHAR(20),
    IN p_salario         DOUBLE
)
BEGIN
    DECLARE v_existe INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- Verificar que el empleado existe
    SELECT COUNT(*) INTO v_existe
      FROM personas WHERE id = p_persona_id AND tipo = 'EMPLEADO';

    IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El empleado no existe';
    END IF;

    -- Validar salario
    IF p_salario <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El salario debe ser mayor a cero';
    END IF;

    

    -- Actualizar personas
    UPDATE personas
       SET nombre = p_nombre,
           identificacion = p_identificacion,
           correo = LOWER(p_correo),
           telefono = p_telefono
     WHERE id = p_persona_id;

    -- Actualizar empleados
    UPDATE empleados
       SET cargo = p_cargo,
           salario = p_salario
     WHERE persona_id = p_persona_id;

    COMMIT;
END¬¬


DROP PROCEDURE IF EXISTS sp_eliminar_celular¬¬
CREATE PROCEDURE sp_eliminar_celular(IN p_id INT)
BEGIN
    DECLARE v_ventas INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    -- Validar existencia
    IF NOT EXISTS (SELECT 1 FROM celulares WHERE id = p_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El celular no existe';
    END IF;

    -- Validar que no tenga ventas
    SELECT COUNT(*) INTO v_ventas
      FROM detalle_ventas WHERE celular_id = p_id;

    IF v_ventas > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede eliminar: el celular tiene ventas asociadas';
    END IF;

    DELETE FROM celulares WHERE id = p_id;

    COMMIT;
END¬¬

DELIMITER ;