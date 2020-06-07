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

import com.google.common.collect.Lists;

import br.com.zup.poc.entity.Carro;
import br.com.zup.poc.jpa.CarroRepository;
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


}
