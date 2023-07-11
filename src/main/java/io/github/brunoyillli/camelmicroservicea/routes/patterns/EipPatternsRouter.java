package io.github.brunoyillli.camelmicroservicea.routes.patterns;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EipPatternsRouter  extends RouteBuilder{

	@Override
	public void configure() throws Exception {
//		from("timer:multicas?period=10000")
//		.multicast()
//		.to("log:something1", "log:something2");
		
//		from("file:files/csv")
//		.unmarshal().csv()
//		.split(body())
//		.to("log:split-files");
	
//		from("activemq:split-queue")
//		.to("log:received-message-from-active-mq");
		
		from("file:files/csv")
		.unmarshal().csv()
		.split(body())
		.to("activemq:split-queue");
	}
	

}
