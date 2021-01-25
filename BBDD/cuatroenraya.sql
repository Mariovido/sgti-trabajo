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
-- Table `cuatroenraya`.`UsuarioPartidas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cuatroenraya`.`UsuarioPartidas` (
  `IdPartida` INT NOT NULL,
  `IdUsuario` INT NOT NULL,
  PRIMARY KEY (`IdPartida`, `IdUsuario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cuatroenraya`.`Usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cuatroenraya`.`Usuarios` (
  `IdUsuarios` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Nick` VARCHAR(30) NOT NULL,
  `Correo` VARCHAR(320) NOT NULL,
  `Contraseña` VARCHAR(40) NOT NULL,
  `Metodo` ENUM('texto', 'md5', 'sha1') NOT NULL,
  `Nombre` VARCHAR(64) NULL,
  `Conectado` BIT(1) NULL,
  `UsuarioPartidas_IdPartida` INT NOT NULL,
  `UsuarioPartidas_IdUsuario` INT NOT NULL,
  PRIMARY KEY (`IdUsuarios`),
  INDEX `fk_Usuarios_UsuarioPartidas_idx` (`UsuarioPartidas_IdPartida` ASC, `UsuarioPartidas_IdUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Usuarios_UsuarioPartidas`
    FOREIGN KEY (`UsuarioPartidas_IdPartida` , `UsuarioPartidas_IdUsuario`)
    REFERENCES `cuatroenraya`.`UsuarioPartidas` (`IdPartida` , `IdUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
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
  `UsuarioPartidas_IdPartida` INT NOT NULL,
  `UsuarioPartidas_IdUsuario` INT NOT NULL,
  PRIMARY KEY (`IdPartida`),
  INDEX `fk_Partidas_UsuarioPartidas1_idx` (`UsuarioPartidas_IdPartida` ASC, `UsuarioPartidas_IdUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_Partidas_UsuarioPartidas1`
    FOREIGN KEY (`UsuarioPartidas_IdPartida` , `UsuarioPartidas_IdUsuario`)
    REFERENCES `cuatroenraya`.`UsuarioPartidas` (`IdPartida` , `IdUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
