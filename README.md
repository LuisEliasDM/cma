# CMA (Dockerized)

Aplicación empaquetada como WAR y desplegada en Tomcat sobre contenedor Docker (Java 8).

---

## Build

Construcción de la imagen (incluye compilación del WAR):

```bash
docker-compose build
```

---

## Run

```bash
docker-compose up -d
```

---

## Access

```
http://localhost:8080
```

---

## Stop

```bash
docker-compose down
```

---

## Notes

* La aplicación se construye en modo producción (`grails prod war`)
* No requiere variables de entorno
* El WAR se genera durante el build de la imagen
* Puerto expuesto: `8080`

---

## Rebuild

Forzar reconstrucción completa:

```bash
docker-compose build --no-cache
docker-compose up -d
```

---

## Stack

* Grails 2.4.2
* Java 8
* Apache Tomcat 8.5
* Docker / Docker Compose

---

## Files

* `Dockerfile` → Build + runtime
* `docker-compose.yml` → Servicio de la app
