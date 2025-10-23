#!/bin/bash

echo "======================================"
echo "Banking Management System - Runner"
echo "======================================"
echo ""

# Check if MongoDB is running
echo "Checking MongoDB connection..."
if mongosh --eval "db.runCommand({ ping: 1 })" > /dev/null 2>&1; then
    echo "✓ MongoDB is running"
else
    echo "✗ MongoDB is not running. Starting MongoDB..."
    sudo systemctl start mongodb
    sleep 2
fi

echo ""
echo "Building project..."
cd /home/barodev/java_course/exam/banking-system
mvn clean package -q

if [ $? -eq 0 ]; then
    echo "✓ Build successful"
    echo ""
    echo "Starting Banking GUI..."
    echo "======================================"
    java -jar target/banking-system-1.0.0-jar-with-dependencies.jar
else
    echo "✗ Build failed. Please check errors above."
fi
