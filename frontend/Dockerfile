# --- Build ---
FROM node:22-alpine AS build
WORKDIR /build
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
# Im Container ruft das FE das BE über denselben Host auf, nginx leitet /api weiter
ENV VITE_API_URL=/api
RUN npm run build

# --- Runtime ---
FROM nginx:1.29-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /build/dist /usr/share/nginx/html
EXPOSE 80
