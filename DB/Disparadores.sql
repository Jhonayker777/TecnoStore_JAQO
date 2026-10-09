
USE TecnoStore_JAQO;

DELIMITER ¬¬

-- T1. BEFORE INSERT personas
CREATE TRIGGER trg_persona_before_insert
BEFORE INSERT ON personas
FOR EACH ROW
BEGIN
    SET NEW.nombre = TRIM(NEW.nombre);
    SET NEW.correo = LOWER(TRIM(NEW.correo));

    IF NOT fn_nombre_valido(NEW.nombre) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Nombre inválido (mínimo 3 letras, sin números)';
    END IF;

    IF NOT fn_identificacion_valida(NEW.identificacion) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Identificación inválida (6 a 15 dígitos)';
    END IF;

    IF NOT fn_correo_valido(NEW.correo) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Correo con formato inválido';
    END IF;

    IF NOT fn_telefono_valido(NEW.telefono) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Teléfono inválido';
    END IF;
END¬¬

-- T2. BEFORE UPDATE personas
CREATE TRIGGER trg_persona_before_update
BEFORE UPDATE ON personas
FOR EACH ROW
BEGIN
    SET NEW.nombre = TRIM(NEW.nombre);
    SET NEW.correo = LOWER(TRIM(NEW.correo));

    IF NOT fn_nombre_valido(NEW.nombre) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Nombre inválido';
    END IF;

    IF NOT fn_identificacion_valida(NEW.identificacion) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Identificación inválida';
    END IF;

    IF NOT fn_correo_valido(NEW.correo) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Correo con formato inválido';
    END IF;

    IF NOT fn_telefono_valido(NEW.telefono) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Teléfono inválido';
    END IF;
END¬¬

-- T3. BEFORE INSERT empleados
CREATE TRIGGER trg_empleado_before_insert
BEFORE INSERT ON empleados
FOR EACH ROW
BEGIN
    DECLARE v_tipo VARCHAR(10);

    SELECT tipo INTO v_tipo FROM personas WHERE id = NEW.persona_id;
    IF v_tipo <> 'EMPLEADO' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La persona no está registrada como EMPLEADO';
    END IF;

    IF NEW.salario <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El salario debe ser mayor a cero';
    END IF;

    IF NEW.fecha_ingreso > CURDATE() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La fecha de ingreso no puede ser futura';
    END IF;

    IF NEW.fecha_ingreso < '2000-01-01' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Fecha de ingreso fuera de rango';
    END IF;
END¬¬

-- T4. BEFORE UPDATE empleados
CREATE TRIGGER trg_empleado_before_update
BEFORE UPDATE ON empleados
FOR EACH ROW
BEGIN
    IF NEW.salario <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El salario debe ser mayor a cero';
    END IF;

    IF NEW.fecha_ingreso > CURDATE() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La fecha de ingreso no puede ser futura';
    END IF;

    IF NEW.activo = OLD.activo THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'ya se encuentra en el estado actual';
    END IF;

END¬¬

-- T5. BEFORE INSERT clientes
CREATE TRIGGER trg_cliente_before_insert
BEFORE INSERT ON clientes
FOR EACH ROW
BEGIN
    DECLARE v_tipo VARCHAR(10);
    SELECT tipo INTO v_tipo FROM personas WHERE id = NEW.persona_id;
    IF v_tipo <> 'CLIENTE' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La persona no está registrada como CLIENTE';
    END IF;
END¬¬

-- T6. BEFORE INSERT celulares
CREATE TRIGGER trg_celular_before_insert
BEFORE INSERT ON celulares
FOR EACH ROW
BEGIN
    IF NEW.precio <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El precio debe ser mayor a cero';
    END IF;

    IF NEW.precio > 99999999 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El precio excede el máximo permitido';
    END IF;

    IF NEW.stock < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El stock no puede ser negativo';
    END IF;

    IF NEW.stock > 100000 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El stock excede el máximo permitido';
    END IF;

    IF NOT fn_moneda_valida(NEW.moneda) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Moneda inválida';
    END IF;

    IF CHAR_LENGTH(TRIM(NEW.modelo)) < 2 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El modelo debe tener al menos 2 caracteres';
    END IF;

    SET NEW.modelo = TRIM(NEW.modelo);
END¬¬

-- T7. BEFORE UPDATE celulares
CREATE TRIGGER trg_celular_before_update
BEFORE UPDATE ON celulares
FOR EACH ROW
BEGIN
    IF NEW.precio <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El precio debe ser mayor a cero';
    END IF;

    IF NEW.stock < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El stock no puede ser negativo';
    END IF;

    IF NOT fn_moneda_valida(NEW.moneda) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Moneda inválida';
    END IF;

    IF OLD.precio <> NEW.precio THEN
        INSERT INTO auditoria_celulares
            (celular_id, campo, valor_antes, valor_despues, usuario_bd)
        VALUES (OLD.id, 'precio',
                CAST(OLD.precio AS CHAR),
                CAST(NEW.precio AS CHAR),
                USER());
    END IF;
END¬¬

-- T8. AFTER INSERT celulares
CREATE TRIGGER trg_celular_after_insert
AFTER INSERT ON celulares
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_celulares
        (celular_id, campo, valor_antes, valor_despues, usuario_bd)
    VALUES (NEW.id, 'creacion', NULL,
            CONCAT('precio=', NEW.precio, ' stock=', NEW.stock), USER());
END¬¬

-- T9. BEFORE DELETE celulares
CREATE TRIGGER trg_celular_before_delete
BEFORE DELETE ON celulares
FOR EACH ROW
BEGIN
    DECLARE v_ventas INT;
    SELECT COUNT(*) INTO v_ventas
      FROM detalle_ventas WHERE celular_id = OLD.id;

    IF v_ventas > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede eliminar: el celular tiene ventas asociadas';
    END IF;
END¬¬

-- T10. BEFORE DELETE empleados
CREATE TRIGGER trg_empleado_before_delete
BEFORE DELETE ON empleados
FOR EACH ROW
BEGIN
    DECLARE v_ventas INT;
    SELECT COUNT(*) INTO v_ventas
      FROM ventas WHERE empleado_id = OLD.persona_id;

    IF v_ventas > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede eliminar: el empleado tiene ventas registradas';
    END IF;
END¬¬

-- T11. BEFORE INSERT ventas
CREATE TRIGGER trg_venta_before_insert
BEFORE INSERT ON ventas
FOR EACH ROW
BEGIN
    DECLARE v_activo BOOLEAN;
    DECLARE v_tipo   VARCHAR(10);

    SELECT activo INTO v_activo FROM empleados WHERE persona_id = NEW.empleado_id;
    IF v_activo IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El empleado no existe';
    END IF;

    IF v_activo = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El empleado está inactivo';
    END IF;

    SELECT tipo INTO v_tipo FROM personas WHERE id = NEW.cliente_id;
    IF v_tipo IS NULL OR v_tipo <> 'CLIENTE' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El cliente no existe o no es CLIENTE';
    END IF;

    IF NEW.fecha > NOW() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La fecha de venta no puede ser futura';
    END IF;
END¬¬

-- T12. BEFORE INSERT detalle_ventas
CREATE TRIGGER trg_detalle_before_insert
BEFORE INSERT ON detalle_ventas
FOR EACH ROW
BEGIN
    DECLARE v_stock  INT;
    DECLARE v_moneda CHAR(3);

    SELECT stock, moneda INTO v_stock, v_moneda
      FROM celulares WHERE id = NEW.celular_id;

    IF v_stock IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El celular no existe';
    END IF;

    IF NEW.cantidad <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La cantidad debe ser mayor a cero';
    END IF;

    IF NEW.cantidad > 1000 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La cantidad excede el máximo permitido';
    END IF;

    IF NEW.cantidad > v_stock THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente para la venta';
    END IF;

    IF NEW.precio_unitario <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El precio unitario debe ser mayor a cero';
    END IF;

    IF NEW.moneda <> v_moneda THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La moneda del detalle no coincide con el celular';
    END IF;
END¬¬

-- T13. AFTER INSERT detalle_ventas
CREATE TRIGGER trg_detalle_after_insert
AFTER INSERT ON detalle_ventas
FOR EACH ROW
BEGIN
    UPDATE celulares
       SET stock = stock - NEW.cantidad
     WHERE id = NEW.celular_id;

    INSERT INTO auditoria_celulares
        (celular_id, campo, valor_antes, valor_despues, usuario_bd)
    SELECT NEW.celular_id, 'stock',
           CAST(stock + NEW.cantidad AS CHAR),
           CAST(stock AS CHAR),
           USER()
      FROM celulares WHERE id = NEW.celular_id;
END¬¬

-- T14. AFTER DELETE detalle_ventas
CREATE TRIGGER trg_detalle_after_delete
AFTER DELETE ON detalle_ventas
FOR EACH ROW
BEGIN
    UPDATE celulares
       SET stock = stock + OLD.cantidad
     WHERE id = OLD.celular_id;
END¬¬

-- T15. BEFORE UPDATE detalle_ventas
CREATE TRIGGER trg_detalle_before_update
BEFORE UPDATE ON detalle_ventas
FOR EACH ROW
BEGIN
    IF OLD.precio_unitario <> NEW.precio_unitario THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El precio unitario histórico no puede modificarse';
    END IF;

    IF OLD.celular_id <> NEW.celular_id THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede cambiar el celular de un detalle';
    END IF;

    IF NEW.cantidad <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La cantidad debe ser mayor a cero';
    END IF;
END¬¬

-- T16. AFTER UPDATE detalle_ventas
CREATE TRIGGER trg_detalle_after_update
AFTER UPDATE ON detalle_ventas
FOR EACH ROW
BEGIN
    IF NEW.cantidad <> OLD.cantidad THEN
        UPDATE celulares
           SET stock = stock - (NEW.cantidad - OLD.cantidad)
         WHERE id = NEW.celular_id;
    END IF;
END¬¬

-- T17. BEFORE INSERT marcas
CREATE TRIGGER trg_marca_before_insert
BEFORE INSERT ON marcas
FOR EACH ROW
BEGIN
    IF CHAR_LENGTH(TRIM(NEW.nombre)) < 2 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El nombre de la marca debe tener al menos 2 caracteres';
    END IF;
    SET NEW.nombre = TRIM(NEW.nombre);
END¬¬

-- T18. BEFORE INSERT sistemas_operativos
CREATE TRIGGER trg_so_before_insert
BEFORE INSERT ON sistemas_operativos
FOR EACH ROW
BEGIN
    IF CHAR_LENGTH(TRIM(NEW.nombre)) < 2 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El nombre del SO debe tener al menos 2 caracteres';
    END IF;
    SET NEW.nombre = TRIM(NEW.nombre);
END¬¬

-- T19. BEFORE INSERT gamas
CREATE TRIGGER trg_gama_before_insert
BEFORE INSERT ON gamas
FOR EACH ROW
BEGIN
    IF NEW.nombre NOT IN ('Alta','Media','Baja') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La gama debe ser Alta, Media o Baja';
    END IF;
END¬¬

DELIMITER ;