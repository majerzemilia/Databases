CREATE OR REPLACE TYPE uczestnicy_wycieczki_rekord as object (
 KRAJ VARCHAR2(50),
 "DATA" DATE,
 NAZWA VARCHAR2(100),
 IMIĘ VARCHAR2(50),
 NAZWISKO VARCHAR2(50),
 STATUS_REZERWACJI CHAR(1)
);