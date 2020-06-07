# poc-concessionaria-kafka
Poc para exercitar uma fila de vendas de carros por kafka

## Instalar e configurar o kafka

Zookeeper
- url de acesso do zookeeper: localhost:2181

Brokers:
Utilizei as portas dos brokers abaixo, tem que configurar pelo menos um
- url dos brokers: localhost:9092,localhost:9093,localhost:9094

## Clonar o projeto:
git clone https://github.com/ndrfjhr-zup/poc-concessionaria-kafka.git 

## Gerar os sources
mvn package -DskipTests

## Gerar a imagem e executá-la
docker-compose up --build

# URLs para testes e validação

## SWAGGER
Link: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## H2 Database
Link: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: jdbc:h2:mem:testdb
- User Name: sa
- Password:


