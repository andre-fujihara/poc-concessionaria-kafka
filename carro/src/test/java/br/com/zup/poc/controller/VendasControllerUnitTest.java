//package br.com.zup.poc.controller;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import br.com.zup.poc.entity.Carro;
//import br.com.zup.poc.entity.Venda;
//import br.com.zup.poc.entity.Vendedor;
//import br.com.zup.poc.jpa.CarroRepository;
//import br.com.zup.poc.jpa.VendaRepository;
//import br.com.zup.poc.jpa.VendedorRepository;
//import br.com.zup.poc.service.KafkaService;
//
//@WebMvcTest(VendasController.class)
//@AutoConfigureMockMvc
//public class VendasControllerUnitTest {
//
//    @Autowired
//    MockMvc mockMvc;
//    
//    @MockBean
//    VendaRepository vendaRepository;
//    
//    @MockBean
//	CarroRepository carroRepository;
//
//    @MockBean
//	VendedorRepository vendedorRepository;
//    
//    @MockBean
//	KafkaService service;
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    
//    @Test
//    void postCreateSale() throws Exception {
//    	
//    	Carro carro = Carro.builder().id(1).build();
//    	Vendedor vendedor = Vendedor.builder().id(1).build();
//    	
//    	Venda venda = Venda.builder()
//    			.carro(carro)
//    			.vendedor(vendedor)
//    			.build();
//
//        String json = objectMapper.writeValueAsString(venda);
//        when(carroRepository.findById((any(Integer.class)))).thenReturn(Optional.of(carro));
//        when(vendedorRepository.findById((any(Integer.class)))).thenReturn(Optional.of(vendedor));
//
//        mockMvc.perform(post("/v0/vendas")
//        .content(json)
//        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//
//		verify(carroRepository,times(1)).findById(eq(carro.getId()));
//		verify(vendedorRepository,times(1)).findById(eq(vendedor.getId()));
//		verify(service,times(1)).sendToKafka(eq(venda));
//
//    }
//    
//    @Test
//    void postCreateCar_4xx_null() throws Exception {
//    	
//        String json = objectMapper.writeValueAsString(null);
//
//        mockMvc.perform(post("/v0/vendas")
//        .content(json)
//        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is4xxClientError());
//    }
//    
//    @Test
//    void postCreateSale_4xx_noCar() throws Exception {
//    	
//    	Venda venda = Venda.builder()
//    			.build();
//
//        String json = objectMapper.writeValueAsString(venda);
//
//        String errorMsg = "O carro não foi informado";
//        
//        mockMvc.perform(post("/v0/vendas")
//        .content(json)
//        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is4xxClientError())
//                .andExpect(content().string(errorMsg));
//    }
//    
//    @Test
//    void postCreateSale_4xx_noSeller() throws Exception {
//
//    	Carro carro = Carro.builder().id(1).build();
//    	Venda venda = Venda.builder()
//    			.carro(carro)
//    			.build();
//
//        String json = objectMapper.writeValueAsString(venda);
//
//        String errorMsg = "O vendedor não foi informado";
//        
//        mockMvc.perform(post("/v0/vendas")
//        .content(json)
//        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is4xxClientError())
//                .andExpect(content().string(errorMsg));
//    }
//    
//    @Test
//    void getListSales() throws Exception {
//    	Venda venda1 = Venda.builder()
//       			.id(1)
//    			.build();
//    	Venda venda2 = Venda.builder()
//       			.id(2)
//    			.build();
//       	
//       	List<Venda> listVendas = Arrays.asList(venda1, venda2);
//       	String result = objectMapper.writeValueAsString(listVendas);
//		when(vendaRepository.findAll()).thenReturn(listVendas);
//    	
//        mockMvc.perform(get("/v0/vendas/all")
//        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string(result));
//        
//		verify(vendaRepository,times(1)).findAll();
//
//    }
//}
