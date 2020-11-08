#!/bin/bash
trap "kill 0" EXIT

echo "#Starting Server"
java -jar app.jar &

sleep 3

echo "#Starting Client"

java -cp ./app.jar pdytr.example.grpc.client.Client &

wait
