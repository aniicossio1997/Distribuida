trap "kill 0" EXIT

cd ./servidor

java Servidor &
sleep 1
cd ../cliente

java Cliente & 

wait
