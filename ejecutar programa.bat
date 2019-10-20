@echo off 

javac -cp .;Conexion.jar Principal.java Dial.java Bascula.java

echo "Todo bien..." 

java -cp .;Conexion.jar Principal 