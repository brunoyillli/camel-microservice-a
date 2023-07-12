package io.github.brunoyillli.camelmicroservicea.routes.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.brunoyillli.camelmicroservicea.CurrencyExchange;

@Component
public class EipPatternsRouter  extends RouteBuilder{



	public class ArrayListAggregationStrategy implements AggregationStrategy {

		@Autowired
	    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
	        Object newBody = newExchange.getIn().getBody();
	        ArrayList<Object> list = null;
	        if (oldExchange == null) {
	            list = new ArrayList<Object>();
	            list.add(newBody);
	            newExchange.getIn().setBody(list);
	            return newExchange;
	        } else {
	            list = oldExchange.getIn().getBody(ArrayList.class);
	            list.add(newBody);
	            return oldExchange;
	        }
	    }

	}

	@Autowired
	private SplitterComponent splitterComponent;
	
	@Autowired
	DynamicRouterBean dynamicRouterBean;
	
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
		
//		from("file:files/csv")
//		.unmarshal().csv()
//		.split(body())
//		.to("activemq:split-queue");
		
//		from("file:files/csv")
//		.convertBodyTo(String.class)
//		.split(body(),",")
//		.to("activemq:split-queue");
		
//		from("file:files/csv")
//		.convertBodyTo(String.class)
//		.split(method(splitterComponent))
//		.to("activemq:split-queue");
		
		
		//agregando mensagem pela tag "to" do json ate 3
		from("file:files/aggregation-json")
		.unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
		.aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
		.completionSize(3)
//		.completionTimeout(HIGHEST);
		.to("log:aggregate-json");
		
		String routinSlip = "direct:endpoint1, direct:endpoint2";
		
//		from("timer:multicas?period=10000")
//		.transform().constant("testando varias rotas")
//		.routingSlip(simple(routinSlip));

		from("timer:multicas?period=10000")
		.transform().constant("testando varias rotas")
		.dynamicRouter(method(dynamicRouterBean));
	}
	
 
}

@Component
class SplitterComponent{
	public List<String> splitInput(String body){
		return List.of("ABC","DEF", "GHT");
	}
}

@Component
class DynamicRouterBean {
	
	Logger logger = LoggerFactory.getLogger(DynamicRouterBean.class);
	
	int invocations;
	
	public String decideTheNextEndpoint(
			@ExchangeProperties Map<String, String> properties, 
			@Headers Map<String, String> headers,
			@Body String body) {
		logger.info("{} {} {}", properties, headers, body);
		
		invocations++;
		
		if(invocations%3==0) {
			return "direct:endpoint1"; 
		}
		if(invocations%3==1) {
			return "direct:endpoint1,direct:endpoint2";
		}
		return null;
	}
}
