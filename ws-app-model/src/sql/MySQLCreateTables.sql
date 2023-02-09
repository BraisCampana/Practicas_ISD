DROP TABLE Reserva;
DROP TABLE Excursion;

CREATE TABLE Excursion ( excursionId BIGINT NOT NULL AUTO_INCREMENT,
                     ciudad VARCHAR(255) NOT NULL,
                     descripcion VARCHAR(255) NOT NULL,
                     fechaCelebracion DATETIME NOT NULL,
                     precio FLOAT NOT NULL,
                     numMaxPersonas SMALLINT NOT NULL,
                     numPersonas SMALLINT NOT NULL,
                     fechaCreacion DATETIME NOT NULL,
                     CONSTRAINT ExcursionPK PRIMARY KEY(excursionId) ) ENGINE = InnoDB;

CREATE TABLE Reserva(reserveID BIGINT NOT NULL AUTO_INCREMENT,
                    email VARCHAR(255) NOT NULL,
                    excursionID BIGINT NOT NULL,
                    numSeats SMALLINT NOT NULL,
                    creditCard VARCHAR(16) NOT NULL,
                    reserveDate DATETIME NOT NULL,
                    cancellationDate DATETIME,
                    precioCompra FLOAT NOT NULL,
                    CONSTRAINT ReservaPK PRIMARY KEY (reserveID),
                    CONSTRAINT Reserva_Excursion_FK FOREIGN KEY (excursionID) REFERENCES Excursion(excursionId)
)
