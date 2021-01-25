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
-- Table `cuatroenraya`.`UsuariosPartidas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cuatroenraya`.`UsuariosPartidas` (
  `IdUsuario` INT NOT NULL,
  `IdPartida` INT NOT NULL,
  PRIMARY KEY (`IdUsuario`, `IdPartida`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cuatroenraya`.`UsuarioStats`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cuatroenraya`.`UsuarioStats` (
  `IdUsuario` INT NOT NULL,
  `Jugadas` INT NOT NULL,
  `Ganadas` INT NOT NULL,
  `Perdidas` INT NOT NULL,
  `Empates` INT NOT NULL,
  `MediaTurnos` FLOAT NOT NULL,
  PRIMARY KEY (`IdUsuario`))
ENGINE = InnoDB;


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
  `UsuariosPartidas_IdUsuario` INT NOT NULL,
  `UsuariosPartidas_IdPartida` INT NOT NULL,
  `UsuarioStats_IdUsuario` INT NOT NULL,
  PRIMARY KEY (`IdUsuario`, `UsuariosPartidas_IdUsuario`, `UsuariosPartidas_IdPartida`, `UsuarioStats_IdUsuario`),
  INDEX `fk_Usuarios_UsuariosPartidas_idx` (`UsuariosPartidas_IdUsuario` ASC, `UsuariosPartidas_IdPartida` ASC) VISIBLE,
  INDEX `fk_Usuarios_UsuarioStats1_idx` (`UsuarioStats_IdUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Usuarios_UsuariosPartidas`
    FOREIGN KEY (`UsuariosPartidas_IdUsuario` , `UsuariosPartidas_IdPartida`)
    REFERENCES `cuatroenraya`.`UsuariosPartidas` (`IdUsuario` , `IdPartida`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Usuarios_UsuarioStats1`
    FOREIGN KEY (`UsuarioStats_IdUsuario`)
    REFERENCES `cuatroenraya`.`UsuarioStats` (`IdUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cuatroenraya`.`PartidaStats`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cuatroenraya`.`PartidaStats` (
  `IdPartida` INT NOT NULL,
  `TurnosJugados` INT NOT NULL,
  `Ganador` INT NULL,
  PRIMARY KEY (`IdPartida`))
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
  `UsuariosPartidas_IdUsuario` INT NOT NULL,
  `UsuariosPartidas_IdPartida` INT NOT NULL,
  `PartidaStats_IdPartida` INT NOT NULL,
  PRIMARY KEY (`IdPartida`, `UsuariosPartidas_IdUsuario`, `UsuariosPartidas_IdPartida`, `PartidaStats_IdPartida`),
  INDEX `fk_Partidas_UsuariosPartidas1_idx` (`UsuariosPartidas_IdUsuario` ASC, `UsuariosPartidas_IdPartida` ASC) VISIBLE,
  INDEX `fk_Partidas_PartidaStats1_idx` (`PartidaStats_IdPartida` ASC) VISIBLE,
  CONSTRAINT `fk_Partidas_UsuariosPartidas1`
    FOREIGN KEY (`UsuariosPartidas_IdUsuario` , `UsuariosPartidas_IdPartida`)
    REFERENCES `cuatroenraya`.`UsuariosPartidas` (`IdUsuario` , `IdPartida`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Partidas_PartidaStats1`
    FOREIGN KEY (`PartidaStats_IdPartida`)
    REFERENCES `cuatroenraya`.`PartidaStats` (`IdPartida`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
