CREATE OR REPLACE FUNCTION
dostepne_wycieczki_fun(kraj VARCHAR2, data_od DATE, data_do DATE)
RETURN wycieczki_t as
 v_ret wycieczki_t;
 czy_istnieje_kraj INT;
BEGIN
 IF data_od > data_do THEN
 raise_application_error(-20001,'Nieprawidlowy zakres dat');
 end if;
 SELECT COUNT(*) INTO czy_istnieje_kraj FROM WYCIECZKI w where w.KRAJ =
dostepne_wycieczki_fun.kraj;
 IF czy_istnieje_kraj = 0 THEN
 raise_application_error(-20002,'Brak wycieczek realizowanych w podanym
kraju');
 end if;
 SELECT wycieczki_r(w.id_wycieczki, w.nazwa, w.kraj, w.data, w.opis,
w.liczba_miejsc,
 w.LICZBA_MIEJSC - (SELECT COUNT(*) FROM REZERWACJE r where r.ID_WYCIECZKI =
w.ID_WYCIECZKI and r.STATUS != 'A'))
 BULK COLLECT INTO v_ret
 FROM WYCIECZKI w
 WHERE w.KRAJ = dostepne_wycieczki_fun.kraj and w.DATA >= data_od and w.DATA <=
data_do and
 w.LICZBA_MIEJSC - (SELECT COUNT(*) FROM REZERWACJE r where r.ID_WYCIECZKI =
w.ID_WYCIECZKI and r.STATUS != 'A') > 0;
 return v_ret;
end dostepne_wycieczki_fun;