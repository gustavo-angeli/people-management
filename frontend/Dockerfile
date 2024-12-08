FROM node:18 AS builder

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

FROM node:18

WORKDIR /app

COPY package*.json ./

RUN npm install --production

COPY --from=builder /app/dist ./dist

RUN npm install -g vite

EXPOSE 4173

CMD ["vite", "preview", "--host", "0.0.0.0"]