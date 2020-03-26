CREATE OR REPLACE PROCEDURE
zmien_liczbe_miejsc(id_wycieczki NUMBER, liczba_miejsc NUMBER) AS
zajete_miejsca NUMBER;
czy_istnieje INT;
BEGIN
 SELECT COUNT(*) INTO czy_istnieje FROM WYCIECZKI w where w.ID_WYCIECZKI =
zmien_liczbe_miejsc.id_wycieczki;
 IF czy_istnieje = 0 THEN
 raise_application_error(-20002, 'Wycieczka o podanym ID nie istnieje');
 end if;
 SELECT wm.LICZBA_MIEJSC - wm.WOLNE_MIEJSCA INTO zajete_miejsca FROM
WYCIECZKI_MIEJSCA wm
 WHERE wm.ID_WYCIECZKI = zmien_liczbe_miejsc.id_wycieczki;
 IF zajete_miejsca > liczba_miejsc or liczba_miejsc < 0 THEN
 raise_application_error(-20001, 'Podano zbyt małą liczbę miejsc');
 end if;
 UPDATE WYCIECZKI
 SET LICZBA_MIEJSC = zmien_liczbe_miejsc.liczba_miejsc
 WHERE ID_WYCIECZKI = zmien_liczbe_miejsc.id_wycieczki;
 COMMIT;
end;
