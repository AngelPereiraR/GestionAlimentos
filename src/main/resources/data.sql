-- Almacen "Alacena"
INSERT INTO almacen (nombre, temp)
VALUES ('Alacena', '20°C');
-- Almacen "Nevera"
INSERT INTO almacen (nombre, temp)
VALUES ('Nevera', '4°C');
-- Almacen "Congelador"
INSERT INTO almacen (nombre, temp)
VALUES ('Congelador', '-18°C');

-- Insertar Secciones
-- Sección "Alacena" con 6 huecos (3 arriba y 3 abajo)
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Hueco Superior 1', 1, 4, 1);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Hueco Superior 2', 1, 4, 1);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Hueco Superior 3', 1, 3, 1);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Hueco Inferior 1', 1, 3, 1);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Hueco Inferior 2', 1, 3, 1);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Hueco Inferior 3', 1, 2, 1);

-- Sección "Nevera" con 4 baldas, 3 estantes de puerta y 2 cajones
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Baldas 1', 1, 4, 2);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Baldas 2', 1, 4, 2);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Baldas 3', 1, 3, 2);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Baldas 4', 1, 3, 2);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Estante Puerta 1', 1, 5, 2);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Estante Puerta 2', 1, 4, 2);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Estante Puerta 3', 1, 3, 2);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Cajón Grande', 1, 2, 2);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Cajón Pequeño', 1, 1, 2);

-- Sección "Congelador" similar a Nevera
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Baldas 1', 1, 4, 3);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Baldas 2', 1, 4, 3);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Baldas 3', 1, 3, 3);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Baldas 4', 1, 3, 3);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Estante Puerta 1', 1, 5, 3);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Estante Puerta 2', 1, 4, 3);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Estante Puerta 3', 1, 3, 3);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Cajón Grande', 1, 2, 3);
INSERT INTO seccion (nombre, limite, accesibilidad, id_almacen)
VALUES ('Cajón Pequeño', 1, 1, 3);

-- Insertar Recipientes
-- Recipiente en "Alacena"
INSERT INTO recipiente (tamanyo, id_seccion)
VALUES (15, 1);
INSERT INTO recipiente (tamanyo, id_seccion)
VALUES (20, 4);

-- Recipientes en "Nevera"
INSERT INTO recipiente (tamanyo, id_seccion)
VALUES (10, 6);
INSERT INTO recipiente (tamanyo, id_seccion)
VALUES (15, 9);

-- Recipientes en "Congelador"
INSERT INTO recipiente (tamanyo, id_seccion)
VALUES (12, 6);
INSERT INTO recipiente (tamanyo, id_seccion)
VALUES (20, 8);

-- Insertar Alimentos
-- Alimentos para la "Alacena"
INSERT INTO alimento (nombre, perecedero, abierto, tamano, fecha_caducidad, numero_de_usos, id_seccion, id_recipiente,
                      categoria, estado)
VALUES ('Galletas', false, true, 5, '2024-12-01', 3, 1, 1, 'APERITIVOS', 'ABIERTO'),
       ('Lentejas', false, false, 10, '2025-06-30', 6, 4, 2, 'VEGETAL', 'NUEVO'),
       ('Arroz', false, false, 15, '2025-05-30', 8, 1, 1, 'VEGETAL', 'NUEVO'),
       ('Tomates en conserva', false, false, 12, '2025-07-20', 5, 2, 1, 'CONSERVAS', 'NUEVO'),
       ('Aceite de oliva', false, true, 1, '2025-09-15', 12, 3, 2, 'ADEREZO_SALSAS', 'ABIERTO'),
       ('Harina', false, false, 20, '2025-12-10', 10, 5, 1, 'VEGETAL', 'NUEVO'),
       ('Pasta', false, false, 10, '2025-04-22', 6, 6, 2, 'VEGETAL', 'NUEVO'),
       ('Lentejas', false, false, 18, '2025-11-15', 10, 1, 1, 'VEGETAL', 'NUEVO'),
       ('Sopa de sobre', false, false, 8, '2025-03-10', 15, 1, 2, 'CONSERVAS', 'NUEVO'),
       ('Galletas', false, true, 5, '2025-09-20', 7, 4, 1, 'APERITIVOS', 'ABIERTO'),
       ('Sal', false, false, 5, '2025-12-30', 20, 5, 2, 'ESPECIAS', 'NUEVO'),
       ('Café', false, false, 12, '2025-04-12', 5, 3, 1, 'ESPECIAS', 'NUEVO'),
       ('Té', false, false, 10, '2025-08-01', 8, 6, 2, 'ESPECIAS', 'NUEVO');

-- Alimentos para la "Nevera"
INSERT INTO alimento (nombre, perecedero, abierto, tamano, fecha_caducidad, numero_de_usos, id_seccion, id_recipiente,
                      categoria, estado)
VALUES ('Leche', true, false, 1, '2024-01-15', 10, 2, 1, 'LACTEO', 'NUEVO'),
       ('Mantequilla', true, true, 1, '2023-12-01', 2, 4, 2, 'LACTEO', 'ABIERTO'),
       ('Yogur', true, false, 1, '2024-12-10', 15, 2, 2, 'LACTEO', 'NUEVO'),
       ('Jamon York', true, true, 1, '2024-12-05', 4, 3, 1, 'CARNE_PESCADO', 'ABIERTO'),
       ('Queso', true, true, 1, '2024-12-15', 5, 4, 2, 'LACTEO', 'ABIERTO'),
       ('Pechuga de Pollo', true, false, 12, '2024-12-08', 6, 2, 1, 'CARNE_PESCADO', 'NUEVO'),
       ('Frambuesas', true, false, 2, '2024-12-01', 8, 3, 1, 'VEGETAL', 'NUEVO'),
       ('Leche', true, true, 10, '2024-12-15', 6, 2, 2, 'LACTEO', 'ABIERTO'),
       ('Mantequilla', true, false, 1, '2024-12-10', 9, 3, 1, 'LACTEO', 'NUEVO'),
       ('Pechuga de Pavo', true, false, 12, '2024-12-12', 4, 1, 2, 'CARNE_PESCADO', 'NUEVO'),
       ('Huevos', true, false, 12, '2024-12-20', 15, 2, 2, 'LACTEO', 'NUEVO'),
       ('Tomates frescos', true, false, 3, '2024-12-07', 10, 4, 1, 'VEGETAL', 'NUEVO'),
       ('Zanahorias', true, true, 6, '2024-12-05', 5, 4, 2, 'VEGETAL', 'ABIERTO');

-- Alimentos para el "Congelador"
INSERT INTO alimento (nombre, perecedero, abierto, tamano, fecha_caducidad, numero_de_usos, id_seccion, id_recipiente,
                      categoria, estado)
VALUES ('Pechuga de Pollo Congelada', true, false, 20, '2024-01-30', 7, 7, 1, 'CARNE_PESCADO', 'CONGELADO'),
       ('Pescado Congelado', true, false, 15, '2024-02-10', 5, 9, 2, 'CARNE_PESCADO', 'CONGELADO'),
       ('Pizza Congelada', true, false, 25, '2025-01-20', 10, 6, 1, 'APERITIVOS', 'CONGELADO'),
       ('Verduras Congeladas', true, true, 15, '2024-12-15', 7, 7, 2, 'VEGETAL', 'CONGELADO'),
       ('Helado de Chocolate', true, false, 10, '2025-06-30', 3, 9, 2, 'ADEREZO_SALSAS', 'CONGELADO'),
       ('Salmón Congelado', true, false, 20, '2024-12-10', 5, 8, 1, 'CARNE_PESCADO', 'CONGELADO'),
       ('Hamburguesas Congeladas', true, false, 25, '2025-02-05', 12, 9, 1, 'CARNE_PESCADO', 'CONGELADO'),
       ('Pollo Congelado', true, false, 20, '2025-03-01', 8, 6, 2, 'CARNE_PESCADO', 'CONGELADO'),
       ('Brocoli Congelado', true, true, 15, '2025-02-18', 6, 7, 1, 'VEGETAL', 'CONGELADO'),
       ('Comida para perros', false, false, 25, '2025-06-15', 25, 9, 1, 'CONSERVAS', 'NUEVO'),
       ('Pechuga de Pavo Congelada', true, false, 12, '2025-01-10', 4, 8, 2, 'CARNE_PESCADO', 'CONGELADO'),
       ('Patatas fritas congeladas', false, false, 30, '2025-04-15', 10, 9, 1, 'APERITIVOS', 'CONGELADO'),
       ('Verduras mixtas congeladas', true, true, 10, '2025-02-28', 6, 8, 2, 'VEGETAL', 'CONGELADO');

-- Usuario "admin" con rol ADMIN
-- Contraseña: password
INSERT INTO usuario (nombre, email, password, rol)
VALUES ('Administrador', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRpXolM4OlY99X/0YlH9XXmrbqXwS', 'ADMIN');

-- Usuario "usuario" con rol USUARIO
-- Contraseña: password
INSERT INTO usuario (nombre, email, password, rol)
VALUES ('Juan Pérez', 'juan@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZRpXolM4OlY99X/0YlH9XXmrbqXwS', 'USUARIO');
