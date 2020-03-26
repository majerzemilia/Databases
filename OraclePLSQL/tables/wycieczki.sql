create table WYCIECZKI
(
    ID_WYCIECZKI          NUMBER generated as identity
        constraint WYCIECZKI_PK
            primary key,
    NAZWA                 VARCHAR2(100),
    KRAJ                  VARCHAR2(50),
    DATA                  DATE,
    OPIS                  VARCHAR2(200),
    LICZBA_MIEJSC         NUMBER,
    LICZBA_WOLNYCH_MIEJSC NUMBER
)