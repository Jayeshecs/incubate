/**
 * 
 */
package data.integration.eip.beanio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.DefaultErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.apache.camel.model.dataformat.BeanioDataFormat;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import data.integration.eip.error.EtlErrorProcessor;
import data.integration.eip.error.EtlPrepareFailureProcessor;
import data.integration.eip.error.EtlRedeliveryProcessor;

/**
 * @author Prajapati
 */
@Configuration
public class BeanIOConfiguration implements CamelContextConfiguration {

	@Autowired
	BeanIOProperties properties;
	
	@Autowired
	CamelContext camelContext;
	
	@Bean
	CamelContextConfiguration contextConfiguration() {
		return this;
	}
	
	@Override
	public void beforeApplicationStart(CamelContext camelContext) {
		// register error handler builder
		camelContext.setErrorHandlerBuilder(
				new DefaultErrorHandlerBuilder()
					.onExceptionOccurred(new EtlErrorProcessor())
					.onRedelivery(new EtlRedeliveryProcessor())
					.onPrepareFailure(new EtlPrepareFailureProcessor()));
		Map<String, DataFormatDefinition> dataFormats = camelContext.getDataFormats();
		for(String stream : properties.getStreams().split(",")) {
			BeanioDataFormat dataFormatDefinition = new BeanioDataFormat();
			dataFormatDefinition.setMapping(properties.getMappings());
			dataFormatDefinition.setStreamName(stream);
			dataFormats.put(stream, dataFormatDefinition);
		}
	}
	
	private boolean reloadRouteAdded = false;
	@Override
	public void afterApplicationStart(CamelContext camelContext) {
		if(reloadRouteAdded) {
			return ;
		}
		// DO NOTHING
		System.out.println("=================>>> afterApplicationStart");
		try {
			camelContext.addRoutes(new RouteBuilder() {
				
				@Override
				public void configure() throws Exception {
					// TODO Auto-generated method stub
					from("file:files/reload").process(new Processor() {
						
						@Override
						public void process(Exchange exchange) throws Exception {
							reloadRoutes();
						}
					});
				}
			});
			reloadRouteAdded = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reloadRoutes() {
		try {
			// TODO: see http://camel.apache.org/loading-routes-from-xml-files.html
			File routesDirectory = new File("routes");
			String[] routeFiles = routesDirectory.list(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".xml");
				}
			});
			for(String routeFile : routeFiles) {
				reloadRoutesXml(routesDirectory, routeFile);
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void reloadRoutesXml(File routesDirectory, String routeFile)
			throws FileNotFoundException, Exception {
		// load route from XML and add them to the existing camel context
		InputStream is = new FileInputStream(new File(routesDirectory, routeFile));
		RoutesDefinition routes = camelContext.loadRoutesDefinition(is);
		camelContext.addRouteDefinitions(routes.getRoutes());
	}
}
