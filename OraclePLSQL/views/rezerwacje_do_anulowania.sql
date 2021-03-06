CREATE VIEW rezerwacje_do_anulowania
AS
SELECT r.NR_REZERWACJI, r.ID_WYCIECZKI, r.ID_OSOBY, r.STATUS, w.DATA
from REZERWACJE r
join WYCIECZKI w on r.ID_WYCIECZKI = w.ID_WYCIECZKI
where R.STATUS = 'N' and W.DATA - TRUNC(CURRENT_DATE, 'dd') = 7;