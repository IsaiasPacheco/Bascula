@echo off 

javac -cp .;Conexion.jar Principal.java Dial.java Reloj.java

echo "Todo bien..." 

java -cp .;Conexion.jar Principal 