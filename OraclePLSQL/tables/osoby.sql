create table OSOBY
(
    ID_OSOBY NUMBER generated as identity
        constraint OSOBY_PK
            primary key,
    IMIE     VARCHAR2(50),
    NAZWISKO VARCHAR2(50),
    PESEL    VARCHAR2(11),
    KONTAKT  VARCHAR2(100)
)