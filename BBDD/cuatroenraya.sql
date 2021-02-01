-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema cuatroenraya
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema cuatroenraya
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cuatroenraya` DEFAULT CHARACTER SET utf8 ;
USE `cuatroenraya` ;

-- -----------------------------------------------------
-- Table `cuatroenraya`.`Usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cuatroenraya`.`Usuarios` (
  `IdUsuario` INT NOT NULL AUTO_INCREMENT,
  `Nick` VARCHAR(30) NOT NULL,
  `Correo` VARCHAR(320) NOT NULL,
  `Contraseña` VARCHAR(40) NOT NULL,
  `Metodo` ENUM('texto', 'md5', 'sha1') NOT NULL,
  `Nombre` VARCHAR(60) NULL,
  `Conectado` BIT(1) NOT NULL,
  PRIMARY KEY (`IdUsuario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cuatroenraya`.`Partidas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cuatroenraya`.`Partidas` (
  `IdPartida` INT NOT NULL AUTO_INCREMENT,
  `EstadoPartida` VARCHAR(64) NOT NULL,
  `Turno` INT NULL,
  `Finalizada` BIT(1) NOT NULL,
  `TopeJugadores` BIT(1) NOT NULL,
  `JugadorUno` INT NOT NULL,
  `JugadorDos` INT NOT NULL,
  PRIMARY KEY (`IdPartida`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cuatroenraya`.`UsuariosPartidas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cuatroenraya`.`UsuariosPartidas` (
  `IdUsuario` INT NOT NULL,
  `IdPartida` INT NOT NULL,
  PRIMARY KEY (`IdUsuario`, `IdPartida`),
  INDEX `UsuariosPartidas_Partidas_idx` (`IdPartida` ASC) VISIBLE,
  CONSTRAINT `UsuariosPartidas_Usuarios`
    FOREIGN KEY (`IdUsuario`)
    REFERENCES `cuatroenraya`.`Usuarios` (`IdUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `UsuariosPartidas_Partidas`
    FOREIGN KEY (`IdPartida`)
    REFERENCES `cuatroenraya`.`Partidas` (`IdPartida`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cuatroenraya`.`PartidaStats`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cuatroenraya`.`PartidaStats` (
  `IdPartida` INT NOT NULL,
  `TurnosJugados` INT NOT NULL DEFAULT 0,
  `Ganador` INT NULL,
  `PuntosJugadorUno` INT NOT NULL DEFAULT 0,
  `PuntosJugadorDos` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`IdPartida`),
  CONSTRAINT `PartidaStats_Partidas`
    FOREIGN KEY (`IdPartida`)
    REFERENCES `cuatroenraya`.`Partidas` (`IdPartida`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cuatroenraya`.`UsuarioStats`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cuatroenraya`.`UsuarioStats` (
  `IdUsuario` INT NOT NULL,
  `Jugadas` INT NOT NULL DEFAULT 0,
  `Ganadas` INT NOT NULL DEFAULT 0,
  `Perdidas` INT NOT NULL DEFAULT 0,
  `Empates` INT NOT NULL DEFAULT 0,
  `MediaTurnos` FLOAT NOT NULL DEFAULT 0,
  PRIMARY KEY (`IdUsuario`),
  CONSTRAINT `UsuariosStats_Usuarios`
    FOREIGN KEY (`IdUsuario`)
    REFERENCES `cuatroenraya`.`Usuarios` (`IdUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
