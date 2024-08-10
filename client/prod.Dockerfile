FROM node:20-slim as build

WORKDIR /app
COPY package*.json ./
COPY . .
RUN npm install

ENV PORT=5173
RUN npm run build

CMD [ "node", "./build" ]