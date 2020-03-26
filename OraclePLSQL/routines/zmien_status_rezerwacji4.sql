CREATE OR REPLACE PROCEDURE
zmien_status_rezerwacji4(id_rezerwacji NUMBER, ob_status CHAR) AS
czy_istnieje INT;
st_status CHAR(1);
zmiana_wolne_miejsca INT;
id_wycieczki INT;
BEGIN
 SELECT COUNT(*) INTO czy_istnieje FROM REZERWACJE r WHERE r.NR_REZERWACJI =
id_rezerwacji;
 IF czy_istnieje = 0 THEN
 raise_application_error(-20001, 'Brak rezerwacji o podanym ID');
 end if;
 SELECT COUNT(*) INTO czy_istnieje from WYCIECZKI_PRZYSZLE wp JOIN REZERWACJE r on
wp.ID_WYCIECZKI = r.ID_WYCIECZKI
 WHERE r.NR_REZERWACJI = id_rezerwacji;
 IF czy_istnieje = 0 THEN
 raise_application_error(-20004, 'Status rezerwacji mozna zmieniac tylko dla
przyszlych wycieczek');
 end if;
 SELECT r.status INTO st_status from REZERWACJE r where r.NR_REZERWACJI =
id_rezerwacji;
 IF st_status = ob_status THEN
 raise_application_error(-20006, 'Próba zmiany statusu rezerwacji na ten sam');
 end if;
 CASE
 WHEN ob_status = 'N' and st_status != 'A'
 THEN raise_application_error(-20002, 'Nie można zmieniać statusu potwierdzonej/i
zapłaconej wycieczki na nową');
 WHEN st_status = 'A'
 THEN SELECT COUNT(*) INTO czy_istnieje from DOSTEPNE_WYCIECZKI dw JOIN
REZERWACJE r on r.ID_WYCIECZKI = dw.ID_WYCIECZKI
 WHERE r.NR_REZERWACJI = id_rezerwacji;
 IF czy_istnieje = 0 THEN
 raise_application_error(-20003, 'Brak dostępnych miejsc na wybraną
wycieczkę');
 end if;
 ELSE null;
 END CASE;
 SELECT w.ID_WYCIECZKI into ZMIEN_STATUS_REZERWACJI4.id_wycieczki FROM WYCIECZKI w
join REZERWACJE r
 on w.ID_WYCIECZKI = r.ID_WYCIECZKI WHERE r.NR_REZERWACJI = id_rezerwacji;
 CASE
 WHEN st_status = 'A' and ob_status != 'A' THEN
 zmiana_wolne_miejsca := -1;
 WHEN st_status != 'A' and ob_status = 'A' THEN
 zmiana_wolne_miejsca := 1;
 ELSE
 zmiana_wolne_miejsca := 0;
 end case;
 UPDATE REZERWACJE
 SET STATUS = ob_status
 WHERE NR_REZERWACJI = id_rezerwacji;
 UPDATE WYCIECZKI w
 SET w.liczba_wolnych_miejsc = w.liczba_wolnych_miejsc + zmiana_wolne_miejsca
 WHERE w.ID_WYCIECZKI = ZMIEN_STATUS_REZERWACJI4.id_wycieczki;
 COMMIT;
end;