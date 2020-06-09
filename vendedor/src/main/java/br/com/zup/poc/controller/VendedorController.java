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

import br.com.zup.poc.dto.KafkaTopics;
import br.com.zup.poc.dto.VendaDTO;
import br.com.zup.poc.entity.Vendedor;
import br.com.zup.poc.jpa.VendedorRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(path = "/v0/vendedor", produces = MediaType.APPLICATION_JSON_VALUE)
public class VendedorController {

	@Autowired
	VendedorRepository vendedorRepository;
	
	@Autowired
	ObjectMapper objectMapper;

	@Autowired(required = false)
	KafkaTemplate<Integer,String> kafkaTemplate;

	@ApiOperation(value = "Realiza o cadastro de um vendedor")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Vendedor cadastrado com sucesso!")
	})
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Vendedor> createSeller(@RequestBody Vendedor vendedor) {
		log.info("createVendedor -> Vendedor recebido {}", vendedor);
		this.validate(vendedor);
		vendedor = vendedorRepository.save(vendedor);
		return ResponseEntity.status(HttpStatus.CREATED).body(vendedor);
	}

	@ApiOperation(value = "Recupera todos os vendedores cadastrados")
	@ApiResponses(value = @ApiResponse(code = 200, message = "Retorna todos os vendedores cadastrados"))
	@GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Vendedor>> listSellers() {
		log.info("getAllVendedores");	
		Iterable<Vendedor> vendedores = vendedorRepository.findAll();
		log.info("getVendedor -> Vendedores encontrados {}", vendedores);	
		return new ResponseEntity<List<Vendedor>>(Lists.newArrayList(vendedores), HttpStatus.OK);

	}

	private void validate(Vendedor vendedor) {
		if(vendedor==null){
			throw new IllegalArgumentException(
					"O vendedor não foi informado");
		}

		if(vendedor.getNome()==null){
			throw new IllegalArgumentException(
					"O nome do vendedor não foi informado");
		}
	}
	
	
	@KafkaListener(topics = KafkaTopics.CARROS_VENDEDOR)
	public void onMessage(ConsumerRecord<Integer,String> vendaJson) throws JsonProcessingException {
		log.info("onMessage CARROS_VENDEDOR -> {} ", vendaJson );
		VendaDTO venda = objectMapper.readValue(vendaJson.value(), VendaDTO.class);
		Optional<Vendedor> vendedor = this.vendedorRepository.findById(venda.getIdVendedor());
		
		if(vendedor.isPresent() && vendedor.get() != null) {
			throw new IllegalArgumentException(
						"O vendedor não foi encontrado para o id : " + venda.getIdVendedor());
		}
		
		venda.setNomeVendedor(vendedor.get().getNome());
		String value = objectMapper.writeValueAsString(venda);
		kafkaTemplate.send(KafkaTopics.VENDEDOR_CARROS, value);

	}


}
