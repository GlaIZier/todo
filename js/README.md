# Getting Started

## Dependencies

The following must be installed for the development / build:

* [Node.js](http://nodejs.org/) - used to build and run application
* Webpack:
 ```
 sudo npm i -g webpack 
 ```


### To verify the install:

```
node -v
```

```
npm -v
```
```
webpack --version
```

### Work configuration
Work checked with these versions:

* node v8.9.1
* npm v5.5.1
* webpack v3.8.1

## Install project dependencies

To begin running the environment UI development (from a fresh clone) go to js/ folder and:

Install dependencies:

```sh
npm install
```

## Start the application

There are different possibilities to start this application

1. To start application without Java backend using Node Express server and mock responses from backend. From js/ folder run
```bash
npm run start:local
```
and go to http://localhost:3000/todo/spa or to https://localhost:3443/todo/spa

2. To start application without Java backend using Node Express server and responses from backend. From js/ folder run
```bash
npm run start:dev
```
and go to http://localhost:3000/todo/spa or to https://localhost:3443/todo/spa

3. To start application with Java backend and responses from the server in dev mode from js/ folder run
```bash
npm run build:dev
```
Start Java class src/main/java/com/wiley/contentclassifier/ContentClassifierApplication and go to localhost:8080

4. To start application with Java backend in prod mode from the project folder run
```bash
gradle clean build && java -jar build/libs/ContentClassifier-0.0.1-SNAPSHOT.jar
```

## Additional info
All frontend build information is described in scripts section of js/package.json