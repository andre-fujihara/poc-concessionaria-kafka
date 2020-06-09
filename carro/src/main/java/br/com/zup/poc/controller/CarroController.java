package br.com.zup.poc.controller;

import java.util.List;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import br.com.zup.poc.entity.Carro;
import br.com.zup.poc.entity.KafkaTopics;
import br.com.zup.poc.entity.Venda;
import br.com.zup.poc.jpa.CarroRepository;
import br.com.zup.poc.jpa.VendaRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(path = "/v0/carro", produces = MediaType.APPLICATION_JSON_VALUE)
public class CarroController {

	@Autowired
	CarroRepository carroRepository;

	@Autowired
	VendaRepository vendaRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired(required = false)
	KafkaTemplate<Integer,String> kafkaTemplate;

	@ApiOperation(value = "Realiza o cadastro de um carro")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Carro cadastrado com sucesso!")
	})
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Carro> createCar(@RequestBody Carro carro) {
		log.info("createCar -> Carro recebido {}", carro);	
		this.validate(carro);
		carro = carroRepository.save(carro);
		return ResponseEntity.status(HttpStatus.CREATED).body(carro);
	}

	@ApiOperation(value = "Recupera todos os carros cadastrados")
	@ApiResponses(value = @ApiResponse(code = 200, message = "Retorna todos os carros cadastrados"))
	@GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Carro>> listCars() {
		log.info("getAllCars");	
		Iterable<Carro> carros = carroRepository.findAll();
		log.info("getAllCars -> Carros encontrados {}", carros);	
		return new ResponseEntity<List<Carro>>(Lists.newArrayList(carros), HttpStatus.OK);
	}

	private void validate(Carro carro) {
		if(carro==null){
			throw new IllegalArgumentException(
					"O carro não foi informado");
		}

		if(carro.getNome()==null){
			throw new IllegalArgumentException(
					"O nome do carro não foi informado");
		}
	}

	@ApiOperation(value = "Realiza o registro da venda")
	@ApiResponses(value = @ApiResponse(code = 200, message = "Venda registrada com sucesso!"))
	@PostMapping(path = "/vendas", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createSale(@RequestBody Venda venda) throws JsonProcessingException {
		log.info("createSale -> {}", venda);
		this.validate(venda);
		String value = objectMapper.writeValueAsString(venda);
		kafkaTemplate.send(KafkaTopics.CARROS_VENDEDOR, value);

		// Caso espere uma resposta sincrona
//		KafkaConsumer<String, String> consumer = this.getConsumer();
//		String result = "";
//		int retry = 0;
//		while(retry < 5) {
//			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
//			retry++;
//			if(records.count() >0) {
//				ConsumerRecord<String, String> next = records.iterator().next();
//				result =next.value();
//				break;
//			}
//		}
//		
//		if(StringUtils.isEmpty(result)) {
//			throw new IllegalArgumentException(
//					"Ocorreu um erro ao recuperar o vendedor com id : " + venda.getIdVendedor());
//		}
//		venda = objectMapper.readValue(result, Venda.class);
//		venda = vendaRepository.save(venda);

		return ResponseEntity.status(HttpStatus.CREATED).body("Venda registrada no kafka!");
	}

	//	private KafkaConsumer<String, String>  getConsumer() {
//		Properties properties = new Properties();
//		properties.put("bootstrap.servers", "host.docker.internal:9092");
//		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//		properties.put("group.id", "vendas");
//		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
//		consumer.subscribe(Collections.singletonList(KafkaTopics.VENDEDOR_CARROS));
//
//		return consumer;
//	}
	
	@KafkaListener(topics = KafkaTopics.VENDEDOR_CARROS)
	public void onMessage(ConsumerRecord<Integer,String> vendaJson) throws JsonProcessingException {
		log.info("onMessage CARROS_VENDEDOR -> {} ", vendaJson );
		Venda venda = objectMapper.readValue(vendaJson.value(), Venda.class);
		this.validate(venda);
		venda = vendaRepository.save(venda);
	}
	
	private void validate(Venda venda) {
		if(venda==null){
			throw new IllegalArgumentException(
					"A venda não foi informada");
		}
		
		if(venda.getCarro()==null || venda.getCarro().getId() ==null){
			throw new IllegalArgumentException(
					"O carro não foi informado");
		}

		if(venda.getIdVendedor()==null){
			throw new IllegalArgumentException(
					"O vendedor não foi informado");
		}

		Optional<Carro> carro = this.carroRepository.findById(venda.getCarro().getId());
		if(!carro.isPresent()) {
			throw new IllegalArgumentException(
					"O carro informado não foi encontrado com id -> " + 
					venda.getCarro().getId());
		}
		venda.setCarro(carro.get());
	}

	@ApiOperation(value = "Realiza a listagem das vendas utilizando kafka")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista com as vendas realizadas")
	})
	@GetMapping(path = "/vendas/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Venda>> listSales() {
		log.info("listSales");
		Iterable<Venda> vendas = vendaRepository.findAll();
		log.info("listSales -> Vendas encontradas {}", vendas);	
		return new ResponseEntity<List<Venda>>(Lists.newArrayList(vendas), HttpStatus.OK);
	}

}
