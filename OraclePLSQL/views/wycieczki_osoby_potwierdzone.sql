CREATE VIEW wycieczki_osoby_potwierdzone
AS
SELECT *
FROM WYCIECZKI_OSOBY w
WHERE w.STATUS = 'P';