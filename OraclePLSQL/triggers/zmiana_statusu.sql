CREATE OR REPLACE TRIGGER zmiana_statusu
 AFTER UPDATE
 ON REZERWACJE
 FOR EACH ROW
DECLARE
 zmiana_wolnych_miejsc INT;
BEGIN
 INSERT INTO REZERWACJE_LOG(ID_REZERWACJI, DATA, STATUS)
 VALUES (:NEW.NR_REZERWACJI, CURRENT_DATE, :NEW.STATUS);
 CASE
 WHEN :OLD.Status != 'A' AND :NEW.STATUS = 'A' THEN
 zmiana_wolnych_miejsc := 1;
 WHEN :OLD.Status = 'A' AND :NEW.STATUS != 'A' THEN
 zmiana_wolnych_miejsc := -1;
 ELSE
 zmiana_wolnych_miejsc := 0;
 END case;
 UPDATE WYCIECZKI w
 SET w.liczba_wolnych_miejsc = w.liczba_wolnych_miejsc + zmiana_wolnych_miejsc
 WHERE w.ID_WYCIECZKI = :NEW.ID_WYCIECZKI;
end;