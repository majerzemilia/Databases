CREATE VIEW dostepne_wycieczki2
AS
SELECT nazwa, kraj, data, liczba_miejsc, liczba_wolnych_miejsc
from WYCIECZKI
where DATA > CURRENT_DATE and LICZBA_WOLNYCH_MIEJSC > 0;