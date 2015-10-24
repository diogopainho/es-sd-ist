# Projecto de Sistemas Distribuídos #

## Primeira entrega ##

Grupo de SD 11

Diogo Painho 73245 diogopainho@tecnico.ulisboa.pt

Natalino Cordeiro 74117 natalinoc0001@gmail.com

André Caldeira 75580 andre.francisco@tecnico.ulisboa.pt


Repositório:
[tecnico-softeng-distsys-2015/A_27_10_11-project](https://github.com/tecnico-softeng-distsys-2015/A_27_10_11-project/)


-------------------------------------------------------------------------------

## Serviço SD-STORE 

### Instruções de instalação 

[0] Iniciar sistema operativo 
Linux


[1] Iniciar servidores de apoio

JUDDI:
> startup.sh

[2] Criar pasta temporária

> cd Desktop
> mkdir grupo11-store
> cd grupo11-store

[3] Obter versão entregue

> git clone -b SD-STORE_R_1 https://github.com/tecnico-softeng-distsys-2015/A_27_10_11-project/


(o caminho para as diretorias do projecto nao pode ter espacos.)

[4] Construir e executar sd-store

> cd A_27_10_11-project/sd-store/
> mvn clean package 
> mvn install
> mvn exec:java


[5] Construir sd-store-cli

(O servidor tem que estar a correr antes de se poder correr o cliente. Isto porque o cliente obtem o wsdl a partir do servidor.)

> cd A_27_10_11-project/sd-store-cli/
> mvn clean package
> mvn install
> mvn exec:java


-------------------------------------------------------------------------------

### Instruções de teste: ###



[1] Executar SDStoreClientTest.java ...

> cd A_27_10_11-project/sd-store-cli/
> mvn test


[2] Executar SDStoreTest.java ...

> cd A_27_10_11-project/sd-store/
> mvn test

-------------------------------------------------------------------------------
**FIM**