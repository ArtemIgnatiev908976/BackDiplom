CREATE TABLE `Task` (
	`ID` INT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255) NOT NULL,
	`priceAmount` DECIMAL NOT NULL,
	`statusFlag` VARCHAR(255) NOT NULL DEFAULT 'OPENED',
	`category` INT NOT NULL,
	`isDraft` BOOLEAN NOT NULL DEFAULT true,
	`customer` INT NOT NULL,
	`executorId` INT,
	`isRegular` BOOLEAN,
	PRIMARY KEY (`ID`)
);

CREATE TABLE `Category` (
	`ID` INT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`ID`)
);

CREATE TABLE `Executor` (
	`ID` INT NOT NULL AUTO_INCREMENT,
	`person` INT NOT NULL,
	`minPriceForTask` DECIMAL,
	`category` INT,
	`description` VARCHAR(255) NOT NULL DEFAULT '""',
	`isCompany` BOOLEAN NOT NULL,
	PRIMARY KEY (`ID`)
);

CREATE TABLE `Person` (
	`ID` INT NOT NULL AUTO_INCREMENT,
	`FirstName` VARCHAR(255) NOT NULL,
	`SecondName` VARCHAR(255),
	`ThirdName` VARCHAR(255),
	`Age` INT,
	`Sex` VARCHAR(255) NOT NULL,
	`City` INT,
	PRIMARY KEY (`ID`)
);

CREATE TABLE `Sex` (
	`type` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`type`)
);

CREATE TABLE `Cities` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `Customer` (
	`ID` INT NOT NULL AUTO_INCREMENT,
	`Person` INT NOT NULL,
	`Rating` INT,
	PRIMARY KEY (`ID`)
);

CREATE TABLE `Rating` (
	`ID` INT NOT NULL AUTO_INCREMENT,
	`PositiveReviews` INT NOT NULL,
	`NegativeReviews` INT NOT NULL,
	`SummaryScore` INT NOT NULL,
	PRIMARY KEY (`ID`)
);

CREATE TABLE `ContactDetails` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`person` INT NOT NULL,
	`phone` VARCHAR(255) NOT NULL,
	`email` VARCHAR(255),
	PRIMARY KEY (`id`)
);

ALTER TABLE `Task` ADD CONSTRAINT `TASK_FK_CATEGORY` FOREIGN KEY (`category`) REFERENCES `Category`(`ID`);

ALTER TABLE `Task` ADD CONSTRAINT `TASK_FK_CUSTOMER` FOREIGN KEY (`customer`) REFERENCES `Customer`(`ID`);

ALTER TABLE `Task` ADD CONSTRAINT `TASK_FK_EXECUTOR` FOREIGN KEY (`executorId`) REFERENCES `Executor`(`ID`);

ALTER TABLE `Executor` ADD CONSTRAINT `EXECUTOR_FK_PERSON` FOREIGN KEY (`person`) REFERENCES `Person`(`ID`);

ALTER TABLE `Executor` ADD CONSTRAINT `EXECUTOR_FK_CATEGORY` FOREIGN KEY (`category`) REFERENCES `Category`(`ID`);

ALTER TABLE `Person` ADD CONSTRAINT `PERSON_FK_SEX` FOREIGN KEY (`Sex`) REFERENCES `Sex`(`type`);

ALTER TABLE `Person` ADD CONSTRAINT `PERSON_FK_CITY` FOREIGN KEY (`City`) REFERENCES `Cities`(`id`);

ALTER TABLE `Customer` ADD CONSTRAINT `CUSTOMER_FK_PERSON` FOREIGN KEY (`Person`) REFERENCES `Person`(`ID`);

ALTER TABLE `Customer` ADD CONSTRAINT `CUSTOMER_FK_RATING` FOREIGN KEY (`Rating`) REFERENCES `Rating`(`ID`);

ALTER TABLE `ContactDetails` ADD CONSTRAINT `CONTACTDETAILS_FK_PERSON` FOREIGN KEY (`person`) REFERENCES `Person`(`ID`);

CREATE TABLE `hibernate_sequence` (
  `idhibernate_sequence` int(11) NOT NULL,
  `next_val` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idhibernate_sequence`)
);

insert into hibernate_sequence values (1, 1);

insert into sex values ("MALE");
insert into sex values ("FEMALE");

insert into cities (name) values ("SAMARA");

