## Environment setting
```
install git
install docker
install docker-compose
```

## Installing
Select the directory for the project and clone the project from github:
```
$ git clone https://github.com/kkarpesh/dress-rental.git
```

## Build project
Run command in project directory:
```
$ gradle build
```

## Create config files
default.conf
```
server {
  listen 8989;
  server_name localhost;

  location / {
    proxy_pass       http://web:8888;
    proxy_set_header Host      $host:$server_port;
    proxy_set_header X-real-IP $remote_addr;
  }
}
```
application.properties
```
host=rest
```
and put them into config directory. For example:
```
/home/carpeat/development/config
```
## Run project
Run command in project directory:
```
$ docker-compose up
```
application should be available at:
```
http://localhost:8989
```

If you want to add more instance of app, run:
```
docker-compose up --scale web=NUMBER_OF_INSTANCE
```


