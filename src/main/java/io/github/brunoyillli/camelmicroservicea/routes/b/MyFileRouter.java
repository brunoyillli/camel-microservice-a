package io.github.brunoyillli.camelmicroservicea.routes.b;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyFileRouter extends RouteBuilder {

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
			.otherwise()
				.log("NOT AN XML FILE")
		.end()
		.log("${messageHistory} ${file:absolute.path}") //https://camel.apache.org/components/3.20.x/languages/simple-language.html
		.to("file:files/output");
	}

}
