# FamilyTree API

An API Service written in Java with Springboot and Maven. Springboot was chosen because of its robustness, readability and popularity in the java development community. Maven was chosen over Gradle due to personal preference.

# Build&Run Instructions
To build and run the project on a UNIX-based system, simply run the following commands:
> mvn clean install
> mvn exec:java -Dexec.mainClass="com.example.familytreeapi.FamilyTreeApiApplication"

For windows, the above command needs to be changed to:
> mvn exec:java -D"exec.mainClass=com.example.familytreeapi.FamilyTreeApiApplication"

To run all the tests you have to run:
> mvn test

# Testing the API with Postman
A postman collection was provided on the root path of the project:
>./FamilyTree API.postman_collection.json

This can be imported into postman and used to test the various endpoints.