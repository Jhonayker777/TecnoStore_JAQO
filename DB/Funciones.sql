

USE TecnoStore_JAQO;

DELIMITER ¬¬
-- F1. Total con IVA
CREATE FUNCTION fn_total_venta(p_venta_id INT)
RETURNS double
DETERMINISTIC READS SQL DATA
BEGIN
    DECLARE v_total double;
    SELECT ROUND(SUM(cantidad * precio_unitario) * 1.19, 2)
      INTO v_total
      FROM detalle_ventas
     WHERE venta_id = p_venta_id;
    RETURN IFNULL(v_total, 0);
END¬¬

-- F2. Subtotal sin IVA
CREATE FUNCTION fn_subtotal_venta(p_venta_id INT)
RETURNS double
DETERMINISTIC READS SQL DATA
BEGIN
    DECLARE v_sub double;
    SELECT ROUND(SUM(cantidad * precio_unitario), 2)
      INTO v_sub
      FROM detalle_ventas
     WHERE venta_id = p_venta_id;
    RETURN IFNULL(v_sub, 0);
END¬¬

-- F3. Correo válido
CREATE FUNCTION fn_correo_valido(p_correo VARCHAR(120))
RETURNS BOOLEAN
DETERMINISTIC NO SQL
BEGIN
    RETURN p_correo REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$';
END¬¬

-- F4. Antigüedad empleado
CREATE FUNCTION fn_antiguedad_empleado(p_persona_id INT)
RETURNS INT
DETERMINISTIC READS SQL DATA
BEGIN
    DECLARE v_fecha DATE;
    SELECT fecha_ingreso INTO v_fecha
      FROM empleados WHERE persona_id = p_persona_id;
    RETURN IFNULL(TIMESTAMPDIFF(YEAR, v_fecha, CURDATE()), 0);
END¬¬

-- F5. Hay stock suficiente
CREATE FUNCTION fn_hay_stock(p_celular_id INT, p_cantidad INT)
RETURNS BOOLEAN
DETERMINISTIC READS SQL DATA
BEGIN
    DECLARE v_stock INT;
    SELECT stock INTO v_stock FROM celulares WHERE id = p_celular_id;
    RETURN IFNULL(v_stock, 0) >= p_cantidad;
END¬¬

-- F6. Descuento por gama
CREATE FUNCTION fn_descuento_gama(p_gama_id INT)
RETURNS DECIMAL(5,2)
DETERMINISTIC NO SQL
BEGIN
    RETURN CASE p_gama_id
        WHEN 1 THEN 10.00
        WHEN 2 THEN  5.00
        ELSE 0.00
    END;
END¬¬

-- F7. Identificación válida
CREATE FUNCTION fn_identificacion_valida(p_ident VARCHAR(30))
RETURNS BOOLEAN
DETERMINISTIC NO SQL
BEGIN
    RETURN p_ident REGEXP '^[0-9]{6,15}$';
END¬¬

-- F8. Teléfono válido
CREATE FUNCTION fn_telefono_valido(p_tel VARCHAR(20))
RETURNS BOOLEAN
DETERMINISTIC NO SQL
BEGIN
    IF p_tel IS NULL OR p_tel = '' THEN
        RETURN TRUE;
    END IF;
    RETURN p_tel REGEXP '^[0-9+\\- ]{7,20}$';
END¬¬

-- F9. Nombre válido
CREATE FUNCTION fn_nombre_valido(p_nombre VARCHAR(100))
RETURNS BOOLEAN
DETERMINISTIC NO SQL
BEGIN
    RETURN CHAR_LENGTH(TRIM(p_nombre)) >= 3
           AND p_nombre REGEXP '^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$';
END¬¬

-- F10. contraseña válida

    CREATE FUNCTION fn_validar_contraseña(p_contraseña VARCHAR(20))
    RETURNS BOOLEAN
    DETERMINISTIC NO SQL
    BEGIN
        IF p_contraseña IS NULL OR p_contraseña = '' THEN
            RETURN FALSE;
        END IF;

        IF CHAR_LENGTH(p_contraseña) < 6 THEN
            RETURN FALSE;
        END IF;

        IF p_contraseña NOT REGEXP '[A-Z]' THEN
            RETURN FALSE;
        END IF;

        IF p_contraseña NOT REGEXP '[a-z]' THEN
            RETURN FALSE;
        END IF;

        IF p_contraseña NOT REGEXP '[0-9]' THEN
            RETURN FALSE;
        END IF;

        RETURN TRUE;
    END¬¬

DELIMITER ;