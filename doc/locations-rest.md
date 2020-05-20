# REST API leírás

## Lekérdezés

A kedvenc helyek lekérdezhetők a `api/locations` végponton. Két paraméter adható meg a lapozáshoz.
A `page` az oldal (nullától számozva), a `size`, az oldal mérete, hogy mennyi találat legyen egy oldalon.
Alapértelmezett paraméterezése: `/api/locations?page=0&size=10`.  

A válasz státuszkódja `200`, visszaadott eredmény a következő JSON. Az eredmény utolsó részében a lapozással és rendezéssel kapcsolatos 
információk szerepelnek.


```json
{
    "content": [
        {
            "id": 1,
            "name": "Budapest",
            "lat": 47.497912,
            "lon": 19.040235,
            "interestingAt": "2019-01-01T05:00:00",
            "tags": [
                "capital",
                "favourite"
            ]
        },
        {
            "id": 2,
            "name": "Debrecen",
            "lat": 47.5316049,
            "lon": 21.6273124,
            "tags": []
        },
        {
            "id": 5,
            "name": "Győr",
            "lat": 47.6874569,
            "lon": 17.6503974,
            "tags": []
        },
        {
            "id": 3,
            "name": "Miskolc",
            "lat": 48.1034775,
            "lon": 20.7784384,
            "tags": []
        },
        {
            "id": 4,
            "name": "Veszprém",
            "lat": 47.1028087,
            "lon": 17.9093019,
            "tags": []
        }
    ],
    "first": true,
    "last": true,
    "totalPages": 1,
    "totalElements": 5,
    "numberOfElements": 5,
    "size": 10,
    "number": 0,
    "sort": [
        {
            "property": "name",
            "direction": "ASC",
            "ignoreCase": false,
            "nullHandling": "NATIVE"
        }
    ]
}
```

## Egy kedvenc hely lekérése

Lekérdezhető a `locations/{id}` végponton, ahol az `id`
a kedvenc hely egyedi azonosítója.

A válasz státuszkódja `200`, a visszaadott eredmény:

```json
{
    "id": 1,
    "name": "Budapest",
    "lat": 47.497912,
    "lon": 19.040235,
    "interestingAt": "2019-01-01T05:00:00",
    "tags": [
        "capital",
        "favourite"
    ]
}
```

Amennyiben a megadott azonosítóval nincs kedvenc hely, `404` sátuszkóddal
tér vissza, és az eredmény:

```json
{
  "type": "locations/not-found",
  "title": "Not found",
  "status": 404,
  "detail": "Not found"
}
```

## Kedvenc hely felvétele

A `api/locations` végponton `POST` metódussal kell a következő
JSON-t felküldeni. Az `interestingAt` és a `tags` megadása opcionális.

```json
{
    "name":"Budapest",
    "coords":"47.497912,19.040235", 
    "interestingAt": "2019-01-01T05:00:00", 
    "tags": "capital,favourite"
}
```

A státuszkód `201`, a visszaadott eredmény:

```json
{
    "id": 7,
    "name": "Budapest",
    "lat": 47.497912,
    "lon": 19.040235,
    "interestingAt": "2019-01-01T05:00:00",
    "tags": [
        "capital",
        "favourite"
    ]
}
```

Ellenőrzések:

* Nem lehet üres a név. `Name can not be empty!`
* A koordináták nem megfelelő formátumban vannak megadva. `Invalid coordinates format!`
* A szélességi fok értéke -90 és 90 között kell legyen. `Latitude must be between -90 and 90`
* A hosszúsági fok értéke -180 és 180 között kell legyen. `Longitude must be between -180 and 180`
* A dátum formátuma nem megfelelő. `Invalid Interesting at format!`

Amennyiben helytelenek az adatok, `400` státuszkóddal tér vissza. Hiba esetén a válasz JSON:

```json
{
  "type": "locations/validation-error",
  "title": "Validation error",
  "status": 400,
  "detail": "Validation error",
  "validationErrors": [
    {
      "field": "name",
      "message": "Name must not be blank"
    }
  ]
}
```

## Kedvenc hely módosítása

A `locations/{id}` végpontra kell a következő JSON-t küldeni:

```json
{
    "name":"Budapest",
    "coords":"47.497912,19.040235", 
    "interestingAt": "2019-01-01T05:00:00", 
    "tags": "capital,favourite"
}
```

A visszaadott eredmény:

```json
{
    "id": 1,
    "name": "Budapest",
    "lat": 47.497912,
    "lon": 19.040235,
    "interestingAt": "2019-01-01T05:00:00",
    "tags": [
        "capital",
        "favourite"
    ]
}
```

Amennyiben a megadott azonosítóval nincs kedvenc hely, `404` státuszkóddal
tér vissza. További ellenőrzések, mint a _Kedvenc hely felvitele_
funkciónál.

## Kedvenc hely törlése

A `locations/{id}` végponton `DELETE` metódussal.

Üres válasszal tér vissza, `204` státuszkóddal.

Amennyiben a megadott azonosítóval nincs kedvenc hely, `404` státuszkóddal
tér vissza.

## Összes hely törlése

A `locations` végponton `DELETE` metódussal.

Üres válasszal tér vissza, `204` státuszkóddal.