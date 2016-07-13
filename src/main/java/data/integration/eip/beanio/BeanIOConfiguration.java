/**
 * 
 */
package data.integration.eip.beanio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
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
import org.apache.log4j.Logger;
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
	
	private static Logger LOG = Logger.getLogger(BeanIOConfiguration.class);

	@Autowired
	BeanIOProperties properties;

	@Autowired
	CamelProperties camelProperties;
	
	@Autowired
	CamelContext camelContext;
	
	@Bean
	CamelContextConfiguration contextConfiguration() {
		return this;
	}
	
	@Override
	public void beforeApplicationStart(CamelContext camelContext) {
		LOG.info("beforeApplicationStart - setting ErrorHandlerBuilder to camel context ...");
		// register error handler builder
		camelContext.setErrorHandlerBuilder(
				new DefaultErrorHandlerBuilder()
					.onExceptionOccurred(new EtlErrorProcessor())
					.onRedelivery(new EtlRedeliveryProcessor())
					.onPrepareFailure(new EtlPrepareFailureProcessor()));
		LOG.info("beforeApplicationStart - adding beanio mappings and streams as data format defintion to camel context ...");
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
		LOG.info("afterApplicationStart - adding route to reload routes from configured routes location ...");
		try {
			camelContext.addRoutes(new RouteBuilder() {
				
				@Override
				public void configure() throws Exception {
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
			LOG.error("Exception occurred while adding reload route", e);
		}
	}
	
	public void reloadRoutes() {
		try {
			// TODO: see http://camel.apache.org/loading-routes-from-xml-files.html
			// JIRA for reload of beanio mappings -> https://issues.apache.org/jira/browse/CAMEL-10138
			File routesDirectory = new File(camelProperties.getRoutesLocation());
			String[] routeFiles = routesDirectory.list(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".xml"); // all xml files
				}
			});
			for(String routeFile : routeFiles) {
				reloadRoutesXml(routesDirectory, routeFile);
			}
		} catch (FileNotFoundException e) {
			LOG.error("reloadRoutes - File not found exception", e);
		} catch (Exception e) {
			LOG.error("reloadRoutes - Exception occurred while reloading routes from xml files", e);
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
