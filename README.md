# TextilFlow Backend

Guia corta para prender el backend local con Docker Compose.

## Requisitos

- Docker Desktop

No hace falta instalar MySQL local ni configurar Java manualmente para el flujo recomendado.

## Primer arranque

Opcional: crea un `.env` basado en `.env.example` si quieres cambiar secretos o integrar correo, Cloudinary o Stripe.

Luego corre:

```powershell
docker compose up -d --build
```

Valida que la API este arriba:

```powershell
Invoke-WebRequest -UseBasicParsing http://127.0.0.1:8080/v3/api-docs
```

## Puertos

- API Spring Boot: `8080`
- MySQL: `3307`

## Comandos utiles

Levantar o reconstruir:

```powershell
docker compose up -d --build
```

Ver logs del backend:

```powershell
docker logs --tail 200 textilflow-spring
```

Parar contenedores:

```powershell
docker compose down
```

Resetear base de datos local:

```powershell
docker compose down -v
docker compose up -d --build
```

## Variables de entorno

La app puede arrancar para desarrollo local sin configurar todas las integraciones externas.

Estas variables son las mas relevantes:

- `JWT_SECRET`
- `CLOUDINARY_CLOUD_NAME`
- `CLOUDINARY_API_KEY`
- `CLOUDINARY_API_SECRET`
- `EMAIL_USERNAME`
- `EMAIL_PASSWORD`
- `STRIPE_SECRET_KEY`
- `STRIPE_PUBLISHABLE_KEY`
- `STRIPE_WEBHOOK_SECRET`

Para desarrollo local:

- JWT conviene definirlo
- Cloudinary, email y Stripe pueden quedarse vacios si no vas a probar esas funciones
