package io.github.brunoyillli.camelmicroservicea.routes.b;

import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyFileRouter extends RouteBuilder {

	@Autowired
	private DeciderBean deciderBean;
	
	@Override
	public void configure() throws Exception {
		from("file:files/input")
		.routeId("Files-Input-Route")
		.transform().body(String.class)
		.choice()
			.when(simple("${file:ext} == 'xml'")) // ou poderia ser 'ends with'
					.log("XML FILE")
			.when(simple("${body} contains 'USD'"))
				.log("not an XML FILE BUT contains USD")
			.when(method(deciderBean))
			.otherwise()
				.log("NOT AN XML FILE")
		.end()
		.to("direct://log-file-values")
		.to("file:files/output");
		
		from("direct:log-file-values")
		.log("${messageHistory} ${file:absolute.path}"); //https://camel.apache.org/components/3.20.x/languages/simple-language.html

	}

}


@Component
class DeciderBean{
	
	Logger logger = LoggerFactory.getLogger(DeciderBean.class);
	
	public boolean isThisConditionMet(@Body String body,
			@Headers Map<String, String> headers,
			@ExchangeProperties Map<String, String> exchangeProperties) {
		
		logger.info("DeciderBean {} {} {}",body,headers,exchangeProperties);
		
		return true;
	}
}