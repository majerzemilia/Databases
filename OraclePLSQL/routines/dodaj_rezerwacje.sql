CREATE OR REPLACE PROCEDURE
dodaj_rezerwacje(id_wycieczki NUMBER, id_osoby NUMBER) AS
czy_istnieje INT;
BEGIN
 SELECT COUNT(*) INTO czy_istnieje FROM OSOBY o where o.ID_OSOBY =
dodaj_rezerwacje.id_osoby;
 IF czy_istnieje = 0 THEN
 raise_application_error(-20001, 'Brak osoby o podanym ID');
 end if;
 SELECT COUNT(*) INTO czy_istnieje FROM DOSTEPNE_WYCIECZKI w where w.ID_WYCIECZKI =
dodaj_rezerwacje.id_wycieczki;
 IF czy_istnieje = 0 THEN
 raise_application_error(-20002, 'Brak dostępnej wycieczki o podanym ID');
 end if;
 SELECT COUNT(*) INTO czy_istnieje FROM REZERWACJE r WHERE r.ID_OSOBY =
dodaj_rezerwacje.id_osoby
 and r.ID_WYCIECZKI =
dodaj_rezerwacje.id_wycieczki;
 IF czy_istnieje > 0 THEN
 raise_application_error(-20003, 'Rezerwacja już istnieje');
 end if;
 INSERT INTO REZERWACJE (id_wycieczki, id_osoby, STATUS)
 VALUES (dodaj_rezerwacje.id_wycieczki, dodaj_rezerwacje.id_osoby, 'N');
 COMMIT;
end;
