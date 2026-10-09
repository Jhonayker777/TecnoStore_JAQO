
USE TecnoStore_JAQO;


DELIMITER ¬¬

-- P1. Registrar venta simple
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

-- P6. Crear cliente (persona + cliente)
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

-- P7. Crear empleado (persona + empleado)
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


DELIMITER ¬¬

CREATE PROCEDURE sp_actualizar_empleado(
    IN p_persona_id      INT,
    IN p_nombre          VARCHAR(100),
    IN p_identificacion  VARCHAR(30),
    IN p_correo          VARCHAR(120),
    IN p_telefono        VARCHAR(20),
    IN p_cargo           VARCHAR(20),
    IN p_salario         DOUBLE,
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
           salario = p_salario,
           moneda = UPPER(p_moneda),
           fecha_ingreso = p_fecha_ingreso,
           activo = p_activo
     WHERE persona_id = p_persona_id;

    COMMIT;
END¬¬


DELIMITER ;