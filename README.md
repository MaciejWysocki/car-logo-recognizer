# Car Logo Recognizer

Recognition of car logos by neural network.

![Car Logo Recognizer screenshot](https://raw.githubusercontent.com/MaciejWysocki/car-logo-recognizer/master/carlogorecognizer.png)

# How to run
```
mvn install
java -jar target/car-logo-recognizer-1.0-SNAPSHOT.jar
```
And then just go visit [http://localhost:8080/](http://localhost:8080/)

# Status

Current version uses Neuroph library, can distinguish between 4 predefined car logos.

UI allows user to draw the picture which is given to the neural network for judgement.

Learning is written in TrainingTool class which is run as a separate program on demand.

# Possible improvements

- More car brands supported
- More learning samples
- Code quality refactoring
