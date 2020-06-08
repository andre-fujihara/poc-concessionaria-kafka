package br.com.zup.poc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import br.com.zup.poc.entity.Venda;

@Configuration
public class KafkaConfig {

	@Value("${kafka.group.id}")
	private String groupId;

	@Value("${kafka.reply.topic}")
	private String replyTopic;

	@Bean
	public ReplyingKafkaTemplate<String, Venda, Venda> replyingKafkaTemplate(ProducerFactory<String, Venda> pf,
			ConcurrentKafkaListenerContainerFactory<String, Venda> factory) {
		ConcurrentMessageListenerContainer<String, Venda> replyContainer = factory.createContainer(replyTopic);
		replyContainer.getContainerProperties().setMissingTopicsFatal(false);
		replyContainer.getContainerProperties().setGroupId(groupId);
		return new ReplyingKafkaTemplate<>(pf, replyContainer);
	}

	@Bean
	public KafkaTemplate<String, Venda> replyTemplate(ProducerFactory<String, Venda> pf,
			ConcurrentKafkaListenerContainerFactory<String, Venda> factory) {
		KafkaTemplate<String, Venda> kafkaTemplate = new KafkaTemplate<>(pf);
		factory.getContainerProperties().setMissingTopicsFatal(false);
		factory.setReplyTemplate(kafkaTemplate);
		return kafkaTemplate;
	}
}