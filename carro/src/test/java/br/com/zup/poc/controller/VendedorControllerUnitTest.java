//package br.com.zup.poc.controller;
//
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
//import br.com.zup.poc.entity.Vendedor;
//import br.com.zup.poc.jpa.VendedorRepository;
//
//
//@WebMvcTest(VendedorController.class)
//@AutoConfigureMockMvc
//public class VendedorControllerUnitTest {
//
//
//    @Autowired
//    MockMvc mockMvc;
//    
//    @MockBean
//	VendedorRepository vendedorRepository;
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    
//    @Test
//    void postCreateSeller() throws Exception {
//    	
//    	Vendedor vendedor = Vendedor.builder()
//    			.nome("Teste")
//    			.build();
//
//        String json = objectMapper.writeValueAsString(vendedor);
//
//        mockMvc.perform(post("/v0/vendedor")
//        .content(json)
//        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//		verify(vendedorRepository,times(1)).save(eq(vendedor));
//    }
//    
//    @Test
//    void postCreateSeller_4xx_null() throws Exception {
//    	
//        String json = objectMapper.writeValueAsString(null);
//
//        mockMvc.perform(post("/v0/vendedor")
//        .content(json)
//        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is4xxClientError());
//    }
//    
//    @Test
//    void postCreateSeller_4xx() throws Exception {
//    	
//    	Vendedor vendedor = Vendedor.builder()
//    			.build();
//
//        String json = objectMapper.writeValueAsString(vendedor);
//
//        String errorMsg = "O nome do vendedor não foi informado";
//        
//        mockMvc.perform(post("/v0/vendedor")
//        .content(json)
//        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is4xxClientError())
//                .andExpect(content().string(errorMsg));
//    }
//    
//    @Test
//    void getListSellers() throws Exception {
//    	Vendedor vendedor1 = Vendedor.builder()
//       			.id(1)
//       			.nome("Vendedor1")
//    			.build();
//       	Vendedor vendedor2 = Vendedor.builder()
//       			.id(2)
//       			.nome("Vendedor2")
//    			.build();
//       	
//       	List<Vendedor> listVendedor = Arrays.asList(vendedor1, vendedor2);
//       	String result = objectMapper.writeValueAsString(listVendedor);
//		when(vendedorRepository.findAll()).thenReturn(listVendedor);
//    	
//        mockMvc.perform(get("/v0/vendedor/all")
//        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string(result));
//        
//		verify(vendedorRepository,times(1)).findAll();
//    }
//}
