package br.com.zup.poc.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.poc.controller.VendasController;
import br.com.zup.poc.entity.Venda;
import br.com.zup.poc.jpa.VendaRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaService {

	@Autowired
	KafkaTemplate<Integer,String> kafkaTemplate;
	
	@Autowired
	VendasController vendasController;

	@Autowired
	VendaRepository vendaRepository;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@KafkaListener(topics = "${spring.kafka.template.default-topic}")
	public void onMessageNew(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
		log.info("Consumer msg NEW -> {} ", consumerRecord );
		Venda venda = objectMapper.readValue(consumerRecord.value(), Venda.class);
		vendasController.validate(venda);
		vendaRepository.save(venda);
	}

	public ListenableFuture<SendResult<Integer,String>> sendToKafka(Venda venda) throws JsonProcessingException  {

		vendasController.validate(venda);
		
		Integer id = venda.getId();
		String value = objectMapper.writeValueAsString(venda);

		ListenableFuture<SendResult<Integer,String>> listenableFuture = kafkaTemplate.sendDefault(value);

		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
			@Override
			public void onFailure(Throwable ex) {
				handleFailure(id, value, ex);
			}

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccess(id, value, result);
			}
		});

		return listenableFuture;
	}

	private void handleFailure(Integer key, String value, Throwable ex) {
		log.error("handleFailure -> Erro no envio da mensagem : {}", ex.getMessage());
		try {
			throw ex;
		} catch (Throwable throwable) {
			log.error("handleFailure -> Erro : {}", throwable.getMessage());
		}


	}

	private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
		log.info("Mensagem enviada, key : {} value : {} , partition : {}", key, value, result.getRecordMetadata().partition());
	}
}
