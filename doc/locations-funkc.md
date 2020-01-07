# Locations alkalmazás

A Locations alkalmazás feladata kedvenc helyek nyilvántartása. Minden hely rendelkezik egy
névvel és egy koordinátával.
Lehet kedvenc helyet törölni, felvenni, módosítani és listázni. A név megadása felvétel és
módosítás esetén is kötelező. A koordinátákat vesszővel kell elválasztani, a tizedeshatároló
karakter a pont. A szélesség értéke -90 és 90 közötti, a hosszúság értéke -180 és 180 közötti.

## Felhasználói felülettel kapcsolatos elvárások

A kedvenc helyeket táblázatban kell megjeleníteni, az azonosítót, a nevet és a koordinátákat.
A táblázatban lapozni lehessen, egyszerre maximum 10 kedvenc helyet lehet megjeleníteni. A
lapozás elején már nem lehet előrébb lépni, a végén nem lehet tovább lépni (nem jelennek meg a gombok).
A lapozásnál kiírásra kerül az első megjelenített elem indexe (0-tól kezdve).

Újat felvenni a `Create location` gombra klikkelve lehet, akkor megjelenik egy űrlap, ahol az adatokat
lehet megadni. Hibás adatok esetén hibaüzenet jelenik meg. Amennyiben a szerkesztés űrlap
meg volt nyitva, az bezáródik. A `Cancel` gombra kattintva eltűnik az űrlap. Sikeres felvitelről üzenet jelenik meg. Amennyiben el volt lapozva az
első oldalról, újra az első oldal jelenik meg, és az űrlap eltűnik. Az űrlapot újra megnyitva, az
üres beviteli mezőkkel jelenik meg. A táblázatban, ha az első oldalra kerülne az új elem,
ott megjelenik.

Szerkeszteni az `Edit` gombra klikkelve lehet, akkor megjelenik egy űrlap, ahol az adatokat
lehet megadni. Az űrlap fel van töltve a szerkeszteni kívánt hely adataival.
Hibás adatok esetén hibaüzenet jelenik meg. Amennyiben az új felvétel űrlap
meg volt nyitva, az bezáródik. Sikeres felvitelről üzenet jelenik meg. Azon az oldalon marad,
amelyiken a szerkesztés kezdetekor volt, de a táblázatban már az új adatok jelennek meg.

Törölni a `Delete` gombbal lehet. Törlés gombra nyomva, ha éppen új felvétel, vagy
szerkesztés volt folyamatban, az az űrlap eltűnik. A törlés előtt rákérdez.
A törlés tényéről üzenet jelenik meg.
Amennyiben nem az első oldal került megjelenítésre, a törlés után visszaugrik arra.

## Alkalmazás telepítése

Az alkalmazás futtatásához szükséges egy MariaDB a localhostra telepítve, `locations`
sémával, és ugyanezzel a felhasználóval és jelszóval.

Töltsd le a `locations-app.jar` állományt. Az alkalmazás 12-es Java-t követel meg.
Telepítsd fel a 12-es Java-t!
Ekkor a `C:\Program Files (x86)\Java` könyvtárba kerül feltelepítésre.

Az alkalmazás futtatása:

```
java -jar locations-app.jar
```

(Amennyiben a java nincs a PATH-ban, teljes elérési útvonalat kell megadni.)

Az alkalmazás indítás után elérhető böngészőből a `http://localhost:8080` címen.