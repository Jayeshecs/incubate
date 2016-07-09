/**
 * 
 */
package data.integration.eip.beanio;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.model.dataformat.BeanioDataFormat;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Prajapati
 */
@Configuration
public class BeanIOConfiguration implements CamelContextConfiguration {

	@Autowired
	BeanIOProperties properties;
	
	@Bean
	CamelContextConfiguration contextConfiguration() {
		return this;
	}
	
	@Override
	public void beforeApplicationStart(CamelContext camelContext) {
		Map<String, DataFormatDefinition> dataFormats = camelContext.getDataFormats();
		for(String stream : properties.getStreams().split(",")) {
			BeanioDataFormat dataFormatDefinition = new BeanioDataFormat();
			dataFormatDefinition.setMapping(properties.getMappings());
			dataFormatDefinition.setStreamName(stream);
			dataFormats.put(stream, dataFormatDefinition);
		}
	}
	
	@Override
	public void afterApplicationStart(CamelContext camelContext) {
		// DO NOTHING
	}
}
