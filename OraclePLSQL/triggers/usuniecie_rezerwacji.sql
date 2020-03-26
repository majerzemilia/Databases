CREATE OR REPLACE TRIGGER usuniecie_rezerwacji
 BEFORE DELETE
 ON REZERWACJE
 FOR EACH ROW
BEGIN
 raise_application_error(-20001, 'Nie można usuwać rezerwacji!');
end;