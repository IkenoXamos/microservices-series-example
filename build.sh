#!/bin/bash
# Author: Matthew Oberlies

# Move to build directory
cd /d/revature/201026java/demos/microservices

# Overhead
services=('config' 'quiz' 'flashcard' 'quiz-composite' 'gateway');
pids=();
outputs=();

# Function to compile and build the image for a single service
build() {
    mvn -f "$1-service/pom.xml" clean package;
    docker build -t $1:latest $1-service;
}

# Function that will build in parallel and record the output
process() {
    service=${services[$1]};
    echo "Building $service-service..."
    build "$service" > $service.output &

    # Track the pids and output files to be referenced later
    pids+=($!)
    outputs+=($service.output)
}

# For loop to process each service
for i in ${!services[@]}; do
    process "$i"
done;

# join this process on all of the child processes and display the overall success/failure
wait "${pids[@]}" && echo "Build Success" || echo "Build Failure"

echo "Run 'cat microservices/quiz.output' or similar to see the output for any service"
echo "You can see the files listed below"
ls | grep output
