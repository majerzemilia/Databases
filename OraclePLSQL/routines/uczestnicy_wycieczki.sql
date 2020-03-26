CREATE OR REPLACE FUNCTION
uczestnicy_wycieczki
(
 id NUMBER
)
RETURN uczestnicy_wycieczki_tabela as
 v_ret uczestnicy_wycieczki_tabela;
 czy_istnieje INT;
 BEGIN
 SELECT COUNT(*) INTO czy_istnieje FROM wycieczki w where w.id_wycieczki = id;
 IF czy_istnieje = 0 THEN
 raise_application_error(-20001,'Wycieczka o podanym ID nie istnieje');
 end if;
 select uczestnicy_wycieczki_rekord(w.kraj, w.data, w.nazwa, o.imie, o.nazwisko,
r.status)
 bulk collect into v_ret
 from WYCIECZKI w join REZERWACJE r on w.ID_WYCIECZKI = r.ID_WYCIECZKI
 join OSOBY o on o.ID_OSOBY = r.ID_OSOBY
 where w.ID_WYCIECZKI = id;
 return v_ret;
 end uczestnicy_wycieczki;
