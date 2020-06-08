package br.com.zup.poc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import br.com.zup.poc.entity.Venda;
import br.com.zup.poc.jpa.CarroRepository;
import br.com.zup.poc.jpa.VendaRepository;
import br.com.zup.poc.jpa.VendedorRepository;
import br.com.zup.poc.service.KafkaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(path = "/v0/vendas", produces = MediaType.APPLICATION_JSON_VALUE)
public class VendasController {

	@Autowired
	KafkaService service;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	VendaRepository vendaRepository;

	@Autowired
	CarroRepository carroRepository;

	@Autowired
	VendedorRepository vendedorRepository;

	@ApiOperation(value = "Realiza a operação de venda de um carro utilizando kafka")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Venda enviada com sucesso!")
	})
	@PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createSale(@RequestBody Venda venda) throws JsonProcessingException {
		log.info("createSale -> Venda recebida {}", venda);
		this.validate(venda);
		service.sendToKafka(venda);
		return ResponseEntity.status(HttpStatus.CREATED).body("Venda salva com sucesso!");
	}

	@ApiOperation(value = "Realiza a listagem das vendas utilizando kafka")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista com as vendas realizadas")
	})
	@GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Venda>> listSales() {
		log.info("listSales");
		Iterable<Venda> vendas = vendaRepository.findAll();
		log.info("listSales -> Vendas encontradas {}", vendas);	
		return new ResponseEntity<List<Venda>>(Lists.newArrayList(vendas), HttpStatus.OK);
	}
	

	public void validate(Venda venda) {
		if(venda==null){
			throw new IllegalArgumentException(
					"A venda não foi informada");
		}
		
		if(venda.getCarro()==null || venda.getCarro().getId() ==null){
			throw new IllegalArgumentException(
					"O carro não foi informado");
		}

		if(venda.getVendedor()==null || venda.getVendedor().getId() ==null){
			throw new IllegalArgumentException(
					"O vendedor não foi informado");
		}

		if(this.carroRepository.findById(venda.getCarro().getId()).isEmpty()) {
			throw new IllegalArgumentException(
					"O carro informado não foi encontrado com id -> " + 
					venda.getCarro().getId());
		}
		
		if(this.vendedorRepository.findById(venda.getVendedor().getId()).isEmpty()) {
			throw new IllegalArgumentException(
					"O vendedor informado não foi encontrado com id -> " + 
					venda.getVendedor().getId());
		}
	}

}
