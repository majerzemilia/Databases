CREATE OR REPLACE PROCEDURE
dodaj_rezerwacje3(id_wycieczki NUMBER, id_osoby NUMBER) AS
czy_istnieje INT;
id_rezerwacji INT;
BEGIN
SELECT COUNT(*) INTO czy_istnieje FROM OSOBY o where o.ID_OSOBY =
dodaj_rezerwacje3.id_osoby;
 IF czy_istnieje = 0 THEN
 raise_application_error(-20001, 'Brak osoby o podanym ID');
end if;
SELECT w.LICZBA_WOLNYCH_MIEJSC INTO czy_istnieje FROM WYCIECZKI w WHERE
w.ID_WYCIECZKI = dodaj_rezerwacje3.id_wycieczki;
 IF czy_istnieje = 0 THEN
 raise_application_error(-20002, 'Brak dostępnej wycieczki o podanym ID');
end if;
SELECT COUNT(*) INTO czy_istnieje FROM REZERWACJE r WHERE r.ID_OSOBY =
dodaj_rezerwacje3.id_osoby and r.ID_WYCIECZKI = dodaj_rezerwacje3.id_wycieczki;
 IF czy_istnieje > 0 THEN
 raise_application_error(-20003, 'Rezerwacja już istnieje');
end if;
INSERT INTO REZERWACJE (id_wycieczki, id_osoby, STATUS)
VALUES (dodaj_rezerwacje3.id_wycieczki, dodaj_rezerwacje3.id_osoby, 'N');
SELECT r.NR_REZERWACJI INTO id_rezerwacji from REZERWACJE r WHERE r.ID_WYCIECZKI
= dodaj_rezerwacje3.id_wycieczki
 and r.ID_OSOBY = dodaj_rezerwacje3.id_osoby and r.STATUS = 'N';
INSERT INTO REZERWACJE_LOG(id_rezerwacji, data, status)
VALUES (dodaj_rezerwacje3.id_rezerwacji, CURRENT_DATE, 'N');
UPDATE WYCIECZKI
SET WYCIECZKI.liczba_wolnych_miejsc = WYCIECZKI.liczba_wolnych_miejsc - 1
WHERE WYCIECZKI.ID_WYCIECZKI = dodaj_rezerwacje3.id_wycieczki;
COMMIT;
end;
