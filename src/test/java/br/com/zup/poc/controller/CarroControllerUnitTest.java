package br.com.zup.poc.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.poc.entity.Carro;
import br.com.zup.poc.jpa.CarroRepository;


@WebMvcTest(CarroController.class)
@AutoConfigureMockMvc
public class CarroControllerUnitTest {

    @Autowired
    MockMvc mockMvc;
    
    @MockBean
	CarroRepository carroRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    
    @Test
    void postCreateCar() throws Exception {
    	
    	Carro carro = Carro.builder()
    			.nome("audi")
    			.build();

        String json = objectMapper.writeValueAsString(carro);

        mockMvc.perform(post("/v0/carro")
        .content(json)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
		verify(carroRepository,times(1)).save(eq(carro));
    }
    
    @Test
    void postCreateCar_4xx_null() throws Exception {
    	
        String json = objectMapper.writeValueAsString(null);

        mockMvc.perform(post("/v0/carro")
        .content(json)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
    
    @Test
    void postCreateCar_4xx() throws Exception {
    	
    	Carro carro = Carro.builder()
    			.build();

        String json = objectMapper.writeValueAsString(carro);

        String errorMsg = "O nome do carro não foi informado";
        
        mockMvc.perform(post("/v0/carro")
        .content(json)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(errorMsg));
    }
    
    @Test
    void getListCars() throws Exception {
       	Carro carro1 = Carro.builder()
       			.id(1)
       			.nome("Audi")
    			.build();
       	Carro carro2 = Carro.builder()
       			.id(2)
       			.nome("Astra")
    			.build();
       	
       	List<Carro> listCarros = Arrays.asList(carro1, carro2);
       	String result = objectMapper.writeValueAsString(listCarros);
		when(carroRepository.findAll()).thenReturn(listCarros);
    	
        mockMvc.perform(get("/v0/carro/all")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(result));
        
		verify(carroRepository,times(1)).findAll();

    }
}
