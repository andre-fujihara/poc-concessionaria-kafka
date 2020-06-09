//package br.com.zup.poc.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.concurrent.ExecutionException;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.SettableListenableFuture;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import br.com.zup.poc.controller.VendasController;
//import br.com.zup.poc.entity.Venda;
//
//@ExtendWith(MockitoExtension.class)
//public class KafkaServiceUnitTest {
//
//	@Mock
//	KafkaTemplate<Integer,String> kafkaTemplate;
//	
//	@Mock
//	VendasController vendasController;
//
//	@Spy
//	ObjectMapper objectMapper = new ObjectMapper();
//
//	@InjectMocks
//	KafkaService kafkaService;
//
//	@Test
//	void sendToKafka_error() throws JsonProcessingException {
//
//		Venda venda = new Venda();
//		String value = objectMapper.writeValueAsString(venda);
//
//		SettableListenableFuture<SendResult<Integer,String>> future = new SettableListenableFuture<>();
//
//		future.setException(new RuntimeException("Erro com kafka"));
//		when(kafkaTemplate.sendDefault(eq(value))).thenReturn(future);
//
//		Exception except = assertThrows(Exception.class, ()->kafkaService.sendToKafka(venda).get());
//		assertEquals("java.lang.RuntimeException: Erro com kafka", except.getMessage());
//		verify(vendasController,times(1)).validate(eq(venda));
//	}
//
//	@Test
//	void sendToKafka_success() throws JsonProcessingException, ExecutionException, InterruptedException {
//
//		Venda venda = new Venda();
//		String value = objectMapper.writeValueAsString(venda);
//
//		SettableListenableFuture<SendResult<Integer,String>> future = new SettableListenableFuture<>();
//        SendResult<Integer, String> sendResultExpected = new SendResult<Integer, String>(null,null);
//
//		future.set(sendResultExpected);
//		when(kafkaTemplate.sendDefault(eq(value))).thenReturn(future);
//
//		ListenableFuture<SendResult<Integer,String>> listenableFuture =  kafkaService.sendToKafka(venda);
//
//		SendResult<Integer,String> sendResult = listenableFuture.get();
//		assertEquals(sendResultExpected, sendResult);
//		verify(vendasController,times(1)).validate(eq(venda));
//
//	}
//}
