trap "kill 0" EXIT

#se inicia el servidor
java -jar app.jar &
#espera unos cuantos segundos a que inicie
sleep 2

echo "3Starting Clients"

#inicia los clientes
echo "#Clientes leen el archivo server-tp3.pdf del servidor"
java -cp ./app.jar pdytr.example.grpc.client.Client read server-tp3.pdf 1 &
pidCliente1=$!
java -cp ./app.jar pdytr.example.grpc.client.Client read server-tp3.pdf 2 &
pidCliente2=$!
java -cp ./app.jar pdytr.example.grpc.client.Client read server-tp3.pdf 3 &
pidCliente3=$!

echo "=========================================================="
#espera que terminen los clientes
wait $pidCliente1
wait $pidCliente2
wait $pidCliente3

echo "#Clientes escriben el archivo client-explicacion-tp3.pdf en el servidor"
java -cp ./app.jar pdytr.example.grpc.client.Client write client-explicacion-tp3.pdf 1 &
pidCliente1=$!
java -cp ./app.jar pdytr.example.grpc.client.Client write client-explicacion-tp3.pdf 2 &
pidCliente2=$!
java -cp ./app.jar pdytr.example.grpc.client.Client write client-explicacion-tp3.pdf 3 &
pidCliente3=$!

#espera que terminen los clientes
wait $pidCliente1
wait $pidCliente2
wait $pidCliente3


