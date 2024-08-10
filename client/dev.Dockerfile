FROM node:20-slim

WORKDIR /app
COPY package*.json ./
COPY . .
RUN npm i

EXPOSE 5173

CMD ["npm", "run", "dev"]