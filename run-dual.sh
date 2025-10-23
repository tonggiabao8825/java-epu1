#!/bin/bash

echo "======================================"
echo "Starting Two Banking Instances"
echo "======================================"
echo ""

cd /home/barodev/java_course/exam/banking-system

# Check if JAR exists
if [ ! -f "target/banking-system-1.0.0-jar-with-dependencies.jar" ]; then
    echo "Building project first..."
    mvn clean package -q
fi

echo "Starting Instance 1 in background..."
java -jar target/banking-system-1.0.0-jar-with-dependencies.jar &
PID1=$!
echo "Instance 1 PID: $PID1"

sleep 2

echo "Starting Instance 2 in background..."
java -jar target/banking-system-1.0.0-jar-with-dependencies.jar &
PID2=$!
echo "Instance 2 PID: $PID2"

echo ""
echo "Both instances are running!"
echo "Press Ctrl+C to stop both instances"
echo ""

# Wait for both processes
wait $PID1 $PID2
