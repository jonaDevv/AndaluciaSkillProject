# Usa una imagen base de Node.js para construir la aplicación Angular
FROM node:20-alpine AS builder

WORKDIR /app

COPY package*.json ./

# Instala dependencias de forma limpia usando el lock file
RUN npm ci

COPY . .

RUN npm run build --configuration production

FROM nginx:alpine
COPY --from=builder /app/dist/competicion /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
