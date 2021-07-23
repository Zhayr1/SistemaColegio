-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 23-11-2020 a las 12:25:45
-- Versión del servidor: 10.4.14-MariaDB
-- Versión de PHP: 7.4.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `colegio`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alumnos`
--

CREATE TABLE `alumnos` (
  `id_alumno` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `representante_cedula` int(11) NOT NULL,
  `aulas_id_aula` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `alumnos`
--

INSERT INTO `alumnos` (`id_alumno`, `nombre`, `apellido`, `fecha_nacimiento`, `representante_cedula`, `aulas_id_aula`) VALUES
(13, 'Luis', 'Lopez', '2010-12-16', 123, 32),
(14, 'Albanis', 'Simanca', '2008-12-04', 321, 11),
(15, 'Daniela', 'Salazar', '2006-12-06', 23654789, 12),
(16, 'Alvarito', 'Gutierrez', '2012-12-14', 98765432, 22),
(17, 'Emily', 'Zabala', '2005-12-14', 98765432, 22);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `aulas`
--

CREATE TABLE `aulas` (
  `id_aula` int(11) NOT NULL,
  `grado` enum('primero','segundo','tercero','cuarto','quinto','sexto') NOT NULL,
  `seccion` varchar(45) NOT NULL,
  `capacidad_alumnos` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `aulas`
--

INSERT INTO `aulas` (`id_aula`, `grado`, `seccion`, `capacidad_alumnos`) VALUES
(1, 'tercero', '3C', 30),
(10, 'primero', '1B', 20),
(11, 'tercero', '3A', 15),
(12, 'cuarto', '4A', 20),
(22, 'sexto', '6A', 15),
(23, 'quinto', '5A', 20),
(32, 'primero', '1A', 15);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `docentes`
--

CREATE TABLE `docentes` (
  `cedula` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `direccion` varchar(45) NOT NULL,
  `telefono` varchar(45) NOT NULL,
  `aulas_id_aula` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `docentes`
--

INSERT INTO `docentes` (`cedula`, `nombre`, `apellido`, `fecha_nacimiento`, `direccion`, `telefono`, `aulas_id_aula`) VALUES
(12345678, 'Maria', 'Paz', '1994-12-23', 'Av 5 de julio, calle 100, Dpto #5', '02617548659', 32),
(32165478, 'Dariana', 'Jimenez', '1994-12-21', 'La coromoto', '04125487565', 22),
(87654123, 'Marco', 'Perez', '1989-12-15', 'Maracaibo', '04145872541', 12),
(87654321, 'Ana', 'Rojas', '1996-12-18', 'San francisco', '04146589745', 11);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `representantes`
--

CREATE TABLE `representantes` (
  `cedula` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `telefono` varchar(45) NOT NULL,
  `direccion` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `representantes`
--

INSERT INTO `representantes` (`cedula`, `nombre`, `apellido`, `telefono`, `direccion`) VALUES
(123, 'Ana', 'Lopez', '0421654876', 'Cabimas'),
(321, 'Noemi', 'Garcia', '02617689584', 'San francisco'),
(23654789, 'Alba', 'Rosa', '0421654879', 'San felix'),
(98765432, 'Alvaro', 'Gutierrez', '04215469874', 'San francisco');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alumnos`
--
ALTER TABLE `alumnos`
  ADD PRIMARY KEY (`id_alumno`,`representante_cedula`),
  ADD UNIQUE KEY `id_alumno_UNIQUE` (`id_alumno`),
  ADD KEY `fk_alumno_representante_idx` (`representante_cedula`),
  ADD KEY `fk_alumnos_aulas1_idx` (`aulas_id_aula`);

--
-- Indices de la tabla `aulas`
--
ALTER TABLE `aulas`
  ADD PRIMARY KEY (`id_aula`),
  ADD UNIQUE KEY `id_aula_UNIQUE` (`id_aula`),
  ADD UNIQUE KEY `seccion_UNIQUE` (`seccion`);

--
-- Indices de la tabla `docentes`
--
ALTER TABLE `docentes`
  ADD PRIMARY KEY (`cedula`),
  ADD UNIQUE KEY `cedula_UNIQUE` (`cedula`),
  ADD KEY `fk_docentes_aulas1_idx` (`aulas_id_aula`);

--
-- Indices de la tabla `representantes`
--
ALTER TABLE `representantes`
  ADD PRIMARY KEY (`cedula`),
  ADD UNIQUE KEY `cedula_UNIQUE` (`cedula`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `alumnos`
--
ALTER TABLE `alumnos`
  MODIFY `id_alumno` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `aulas`
--
ALTER TABLE `aulas`
  MODIFY `id_aula` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `alumnos`
--
ALTER TABLE `alumnos`
  ADD CONSTRAINT `fk_alumno_representante` FOREIGN KEY (`representante_cedula`) REFERENCES `representantes` (`cedula`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_alumnos_aulas1` FOREIGN KEY (`aulas_id_aula`) REFERENCES `aulas` (`id_aula`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `docentes`
--
ALTER TABLE `docentes`
  ADD CONSTRAINT `fk_docentes_aulas1` FOREIGN KEY (`aulas_id_aula`) REFERENCES `aulas` (`id_aula`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
