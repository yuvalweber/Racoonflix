FROM node:latest
WORKDIR /usr/src/app
COPY . .
RUN npm install
EXPOSE 12345
ENTRYPOINT ["node","app.js"]