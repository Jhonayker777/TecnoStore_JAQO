
DROP DATABASE IF EXISTS TecnoStore_JAQO;
CREATE DATABASE TecnoStore_JAQO

USE TecnoStore_JAQO;



CREATE TABLE marcas (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE sistemas_operativos (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE gamas (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE personas (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    identificacion VARCHAR(30)  NOT NULL UNIQUE,
    correo         VARCHAR(120) NOT NULL UNIQUE,
    telefono       VARCHAR(20),
    tipo           ENUM('CLIENTE','EMPLEADO') NOT NULL,
    creado_en      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE clientes (
    persona_id     INT PRIMARY KEY,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cliente_persona FOREIGN KEY (persona_id)
        REFERENCES personas(id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE empleados (
    persona_id     INT PRIMARY KEY,
    cargo          ENUM('Vendedor','Admin','Bodega','Gerente') NOT NULL,
    salario        DECIMAL(12,2) NOT NULL,
    moneda         CHAR(3) NOT NULL DEFAULT 'COP',
    fecha_ingreso  DATE NOT NULL,
    activo         BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_empleado_persona FOREIGN KEY (persona_id)
        REFERENCES personas(id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE celulares (
    
    id       INT AUTO_INCREMENT PRIMARY KEY,
    marca_id INT NOT NULL,
    modelo   VARCHAR(80) NOT NULL,
    so_id    INT NOT NULL, --Sistema operativo
    gama_id  INT NOT NULL,
    precio   DECIMAL(12,2) NOT NULL,
    moneda   CHAR(3) NOT NULL DEFAULT 'COP',
    stock    INT NOT NULL DEFAULT 0,

    CONSTRAINT fk_cel_marca FOREIGN KEY (marca_id) REFERENCES marcas(id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_cel_so    FOREIGN KEY (so_id)    REFERENCES sistemas_operativos(id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_cel_gama  FOREIGN KEY (gama_id)  REFERENCES gamas(id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT uq_celular   UNIQUE (marca_id, modelo)
) ENGINE=InnoDB;

CREATE TABLE ventas (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id  INT NOT NULL,
    empleado_id INT NOT NULL,
    fecha       DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_venta_cliente FOREIGN KEY (cliente_id)
        REFERENCES clientes(persona_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_venta_empleado FOREIGN KEY (empleado_id)
        REFERENCES empleados(persona_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE detalle_ventas (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    venta_id        INT NOT NULL,
    celular_id      INT NOT NULL,
    cantidad        INT NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    moneda          CHAR(3) NOT NULL DEFAULT 'COP',

    CONSTRAINT fk_det_venta FOREIGN KEY (venta_id)
        REFERENCES ventas(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_det_celular FOREIGN KEY (celular_id)
        REFERENCES celulares(id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE auditoria_celulares (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    celular_id    INT NOT NULL,
    campo         VARCHAR(30) NOT NULL,
    valor_antes   VARCHAR(50),
    valor_despues VARCHAR(50),
    usuario_bd    VARCHAR(100),
    fecha         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_celular FOREIGN KEY (celular_id)
        REFERENCES celulares(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE historico_ventas_mensuales (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    anio         INT NOT NULL,
    mes          INT NOT NULL,
    total_ventas DECIMAL(12,2) NOT NULL,
    num_ventas   INT NOT NULL,
    registrado   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_periodo (anio, mes)
) ENGINE=InnoDB;

CREATE TABLE empleados_inactivos_log (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    persona_id  INT NOT NULL,
    detectado   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE INDEX idx_personas_tipo   ON personas(tipo);
CREATE INDEX idx_ventas_fecha    ON ventas(fecha);
CREATE INDEX idx_ventas_cliente  ON ventas(cliente_id);
CREATE INDEX idx_ventas_empleado ON ventas(empleado_id);
CREATE INDEX idx_detalle_venta   ON detalle_ventas(venta_id);
CREATE INDEX idx_detalle_celular ON detalle_ventas(celular_id);
CREATE INDEX idx_celulares_stock ON celulares(stock);
CREATE INDEX idx_celulares_gama  ON celulares(gama_id);


