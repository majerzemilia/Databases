create table REZERWACJE
(
    NR_REZERWACJI NUMBER generated as identity
        constraint REZERWACJE_PK
            primary key,
    ID_WYCIECZKI  NUMBER
        constraint REZERWACJE_FK2
            references WYCIECZKI,
    ID_OSOBY      NUMBER
        constraint REZERWACJE_FK1
            references OSOBY,
    STATUS        CHAR
        constraint REZERWACJE_CHK1
            check (status IN ('N', 'P', 'Z', 'A'))
)