#!/bin/bash



## Install Locust
pip install locust

## Start locust
python locustfile.py

## Start WebServer
mvn spring-boot:run

## Load Locations
python hash-locations.py