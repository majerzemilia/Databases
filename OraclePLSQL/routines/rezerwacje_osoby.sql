CREATE OR REPLACE FUNCTION
rezerwacje_osoby(id NUMBER)
RETURN rezerwacje_osoby_tabela as
 v_ret rezerwacje_osoby_tabela;
 czy_istnieje INT;
BEGIN
 SELECT COUNT(*) INTO czy_istnieje from OSOBY o where o.ID_OSOBY = id;
 IF czy_istnieje = 0 THEN
 raise_application_error(-20001,'Osoba o podanym ID nie istnieje');
 end if;
 SELECT rezerwacje_osoby_rekord(w.ID_WYCIECZKI, w.NAZWA, w.KRAJ, w.DATA, o.IMIE,
o.NAZWISKO, r.STATUS)
 bulk collect into v_ret
 from WYCIECZKI w join REZERWACJE r on w.ID_WYCIECZKI = r.ID_WYCIECZKI
 join osoby o on o.ID_OSOBY = r.ID_OSOBY
 where o.ID_OSOBY = id;
 return v_ret;
end;
