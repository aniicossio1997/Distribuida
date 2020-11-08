trap "kill 0" EXIT

#se inicia el servidor
java -jar app.jar &
#espera unos cuantos segundos a que inicie
sleep 2

echo "#Starting Clients"

#inicia el clientes
echo "#El cliente lee el archivo server-tp3.pdf del servidor"
java -cp ./app.jar pdytr.example.grpc.client.Client read server-tp3.pdf &

wait $!

echo "#El cliente lee el archivo server-planet.mp4 del servidor"
java -cp ./app.jar pdytr.example.grpc.client.Client read server-planet.mp4 &

wait $!

echo "#El cliente escribe el archivo client-explicacion-tp3.pdf en el servidor"
java -cp ./app.jar pdytr.example.grpc.client.Client write client-explicacion-tp3.pdf &

wait $!

echo "#El cliente escribe el archivo client-nature.mp4 en el servidor"
java -cp ./app.jar pdytr.example.grpc.client.Client write client-nature.mp4 &

wait $!



