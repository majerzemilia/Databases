CREATE OR REPLACE TYPE rezerwacje_osoby_rekord as object (
 ID_WYCIECZKI NUMBER,
 NAZWA VARCHAR2(100),
 KRAJ VARCHAR2(50),
 "DATA" DATE,
 IMIĘ VARCHAR2(50),
 NAZWISKO VARCHAR2(50),
 STATUS_REZERWACJI CHAR(1)
);