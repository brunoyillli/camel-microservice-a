package io.github.brunoyillli.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqSenderRouter extends RouteBuilder{

	@Override
	public void configure() throws Exception {
//		enviar mensagem para o activemq a cada 10 segundos
//		from("timer:active-mq-timer?period=10000")
//		.transform().constant("My message for Active MQ")
//		.log("${body}")
//		.to("activemq:my-activemq-queue");
		
//		enviando arquivos json para o activemq
//		from("file:files/json")
//		.log("${body}")
//		.to("activemq:my-activemq-queue");
		
		from("file:files/xml")
		.log("${body}")
		.to("activemq:my-activemq-xml-queue");
	}

}
