# Locations alkalmazás

## Célok

Ez az alkalmazás a Training360 különböző tanfolyamain használható.

* [Bevezetés a szoftverarchitektúrákba](https://www.training360.com/bevezetes-a-szoftverarchitekturakba-tanfolyam-arch-bgn)
* [Tesztautomatizálás](https://www.training360.com/tesztautomatizalas-tanfolyam-atm-teszt1)
* [Tesztautomatizálás Selenium WebDriverrel Javaban](https://www.training360.com/tesztautomatizalas-selenium-webdriverrel-javaban-tanfolyam-swd-java)
* [Tesztautomatizálás Selenium WebDriverrel Pythonban](https://www.training360.com/tesztautomatizalas-selenium-webdriverrel-pythonban-tanfolyam-swd-python)

## Alkalmazás működésének a leírása

Az alkalmazás [funkcionális leírása megtalálható itt](doc/locations-rest.md).

## Alkalmazás indítása

Az alkalmazás legegyszerűbben Docker Compose használatával indítható el.

Az alkalmazás futtatásához nem kell az buildelni, ugyanis a kész Docker image megtalálható a Docker Hubon a [https://hub.docker.com/r/training360/locations](https://hub.docker.com/r/training360/locations) címen.

Ehhez egy telepített és futó Docker Desktop kell, valamint a `8080`-as és a `3306`-os portnak szabadnak kell lennie.

```shell
git clone https://github.com/Training360/locations-app
cd locations-app\locations
docker compose up -d
```

Az alkalmazás ezután elérhető lesz a `http://localhost:8080` címen. Ez egy Java backend és HTML/CSS/Vanilla JavaScript frontend alkalmazás, köztük REST kommunikációval.

A `http://localhost:8080/server` címen a Java backend állítja elő a HTTP tartalmat, itt nincsenek AJAX hívások.

A Swagger elérhető a `http://localhost:8080/swagger-ui/index.html` címen.

Példa kérések találhatóak a [src/test/locations.http](src/test/locations.http) fájlban. REST API leírása a [doc/locations-rest.md](doc/locations-rest.md) fájlban is megtalálható.

A SOAP webszolgáltatások leírása, valamint linkek a WSDL-ekre elérhető a `http://localhost:8080/services` címen.

Az adatbázis elérhető a következő belépési paraméterekkel:

* RDBMS: MariaDB
* Host: `localhost`
* Port: `3306`
* Database: `locations`
* Username: `locations`
* Password: `locations`

## Alkalmazás buildelése lokális számítógépen

A JAR állomány előállítható az `mvnw package` parancs kiadásával, ehhez egy JAva 17-nek kell
telepítve lennie a számítógépen. Utána egy layered Docker image előállítható
a `docker build -t locations .` parancs kiadásával

## CI/CD

A projektre a GitHub Actions is be van állítva (`.github/workflows/locations.yml`), mely automatikusan lefuttatja a buildet, a Docker image előállítását és a 
push-olást a Docker Hub-ra, de egyelőre ki van kapcsolva.

## Kubernetes

Az alkalmazás a `deployment` könyvtárban lévő leírókkal Kubernetes környezetre is telepíthető.