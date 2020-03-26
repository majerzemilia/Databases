create table REZERWACJE_LOG
(
    ID            NUMBER generated as identity
        constraint REZERWACJE_LOG_PK
            primary key,
    ID_REZERWACJI NUMBER,
    DATA          DATE,
    STATUS        CHAR
        constraint REZERWACJE_LOG_CHK1
            check (status IN ('N', 'P', 'Z', 'A'))
)