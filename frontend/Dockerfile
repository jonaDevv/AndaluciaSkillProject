# Usa una imagen base de Node.js para construir la aplicación Angular
FROM node:20-alpine AS builder

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia package.json y package-lock.json (si existe)
COPY package*.json ./

# Instala las dependencias de la aplicación
RUN npm install

# Copia el resto de los archivos de la aplicación
COPY . .

# Construye la aplicación Angular para producción
RUN npm run build --configuration production

# Usa una imagen base de Nginx para servir la aplicación
FROM nginx:alpine

# Copia los archivos construidos desde la etapa de "builder" al directorio de archivos estáticos de Nginx
COPY --from=builder /app/dist/competicion /usr/share/nginx/html

# Expone el puerto 80 para acceder a la aplicación desde el exterior
EXPOSE 80

# Comando para iniciar Nginx (Nginx ya se inicia por defecto)
CMD ["nginx", "-g", "daemon off;"]