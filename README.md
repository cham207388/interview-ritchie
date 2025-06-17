# Equipment Management

This is a simple RESTful service for managing equipment

- Store equipment
- Retrieve equipments (all or by id)
- Remove an equipment

## Steps the run the project

### Option 1

build the application

```bash
./gradlew build
```
run the application 

```bash
java -jar build/libs/interview-ritchie-0.0.1-SNAPSHOT.jar
```

### Option 2

build docker image

```bash
docker image build -t ritchie:v1.0 .
```
run docker container

```bash
docker container run -p 8080:8080 ritchie:v1.0
```

access the swagger ui
[swagger](http://localhost:8080/swagger-ui/index.html)

sample request to add equipment

```json
{"type":  "Truck"}
```

## Running test

building the project with `./gradlew build executes the 'test' task`
To run only the test, please execute

```bash
./gradlew test
```