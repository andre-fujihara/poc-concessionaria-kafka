# poc-concessionaria-kafka
Poc para exercitar uma fila de vendas de carros por kafka

#### Instalar e configurar o kafka

###### Zookeeper
- url de acesso do zookeeper: localhost:2181

###### Brokers:
Subir um servidor local com os dados
- url dos brokers: localhost:9092

#### Clonar o projeto:
git clone https://github.com/ndrfjhr-zup/poc-concessionaria-kafka.git 

#### Gerar os sources nos sub módulos carro e vendedor
- mvn package -DskipTests

#### Gerar a imagem e executá-la na raiz da aplicação
- docker-compose up --build

# URLs para testes e validação

#### SWAGGER
###### MS carros
- Link: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
###### MS vendedor
- Link: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

#### H2 Database
###### MS carros
[http://localhost:8081/h2-console](http://localhost:8081/h2-console)
- JDBC URL: jdbc:h2:mem:testdb

###### MS vendedor
[http://localhost:8082/h2-console](http://localhost:8082/h2-console)
- JDBC URL: jdbc:h2:mem:testdb

