package br.com.zup.poc.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("br.com.zup.poc.controller"))
				.paths(PathSelectors.any())
				.build()        
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.POST, responseMessage())
				.globalResponseMessage(RequestMethod.GET, responseMessage())
				.apiInfo(apiInfo());
	}

	@SuppressWarnings("serial")
	private List<ResponseMessage> responseMessage()
	{
		return new ArrayList<ResponseMessage>() {{
			add(new ResponseMessageBuilder()
					.code(415)
					.message("Formato incorreto da mensagem")
					.build());
		}};
	}


	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Poc para vendas de carros")
				.description("Exemplo de aplicação com kafka")
				.version("1.0.0")
				.license("Apache License Version 2.0")
				.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
				.contact(new Contact("André Fujihara", "", "andre.fujihara@zup.com.br"))
				.build();
	}
}
