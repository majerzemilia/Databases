CREATE OR REPLACE PROCEDURE
przelicz AS
wolne_miejsca INT;
BEGIN
 FOR rekord IN (select * from WYCIECZKI)
 LOOP
 SELECT rekord.liczba_miejsc - COUNT(*) INTO wolne_miejsca FROM REZERWACJE
r WHERE r.ID_WYCIECZKI = rekord.ID_WYCIECZKI
 and r.STATUS != 'A';
 UPDATE WYCIECZKI w
 SET w.liczba_wolnych_miejsc = wolne_miejsca
 WHERE w.ID_WYCIECZKI = rekord.id_wycieczki;
 end loop;
end;
