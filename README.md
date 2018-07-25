# podwozka-srv
Podwózka (podvoozca) is an mobile application connecting drivers with passengers looking for transport.

# Build
Before building an app please create a database 'podwoozka' using mysql server.

    ./mvnw clean install
## Using Docker to simplify development (optional)

You can use Docker to improve development experience. 

First you have to build a .war file. Then you can build/update an image of Podwoozka App.

    docker build -t podwoozka-srv -f docker/Dockerfile .

Before next step please ensure that all ports defined in [docker/docker-compose.yml](docker/docker-compose.yml)
file are disabled e.g. stop mysql server:

    sudo service mysql stop

Now you can run the app:

    docker-compose -f docker/docker-compose.yml  up

Link to the app: [http://localhost:8080/podwozka-srv-0.0.1-SNAPSHOT/](http://localhost:8080/podwozka-srv-0.0.1-SNAPSHOT/).
