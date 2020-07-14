# MiniCRM 
[![Build status](https://circleci.com/gh/MiniCRM-Group/MiniCRM.svg?style=svg)](https://circleci.com/gh/MiniCRM-Group/MiniCRM)

This is the repository for our miniCRM website!


## Getting Started

Install the following tools:
- Java 8
- [npm](https://nodejs.org/en/)

To run our project locally, you should run the frontend and backend separately,

To run the frontend, you want to do the following
```
# Install dependencies
npm install

# Run frontend
npm run start
```
To run the backend, you want to do the following
```
# Run backend (works only with Java 8)
mvn package appengine:run
```


To deploy our project onto GCP, you should run the production build of our frontend (which builds inside of the webapp folder)
and then run the production build of our backend (which also deploys). You will need to specify a GCP project id in `backend/pom.xml`.

```
# First, run prod build of frontend
npm run build
# Then, run prod build of backend
mvn package appengine:deploy
```


From the contributors: Alex, Amanuel, Roddy, Rodrigo, Anthoney
