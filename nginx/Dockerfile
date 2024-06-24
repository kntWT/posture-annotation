FROM nginx

COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf
COPY ./nginx/images /root/public/images

RUN mkdir /root/logs
RUN chmod 755 -R /root

CMD ["nginx", "-g", "daemon off;"]
