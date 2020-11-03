trap "kill 0" EXIT

cd ./servidor

java Servidor &
sleep 1
cd ../cliente

java Cliente "Cliente 1" & 
java Cliente "Cliente 2" & 
java Cliente "Cliente 3" & 

wait
