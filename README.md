# Car Logo Recognizer

Recognition of car logos by neural network.

![Car Logo Recognizer screenshot](https://raw.githubusercontent.com/MaciejWysocki/car-logo-recognizer/master/carlogorecognizer.png)

# How to run
```
mvn install -Dmaven.test.skip
java -jar target/car-logo-recognizer-1.0-SNAPSHOT.jar
```
And then just go visit [http://localhost:8080/](http://localhost:8080/)

Sometimes requires couple runs as learning process uses random weights and not always is able to learn properly.

# Status

Current version uses Neuroph library, can learn itself to distinguish between 4 predefined car logos.

UI allows user to draw the picture which is given to the neural network for judgement.

# Todo

Prepare more learning examples as with small amount of data it is very random in many picture configurations.

As learning takes time it should be extracted from the main program and best learned weights should be added as a resource.
