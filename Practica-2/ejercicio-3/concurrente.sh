#!/bin/bash
trap "kill 0" EXIT

cd ./servidor
java Servidor &
sleep 1
cd ../cliente
java Cliente tp2.pdf & 
java Cliente tp2.pdf & 
java Cliente tp2.pdf & 
wait
