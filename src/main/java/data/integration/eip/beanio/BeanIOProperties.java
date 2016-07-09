/**
 * 
 */
package data.integration.eip.beanio;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Prajapati
 *
 */
@Component
@ConfigurationProperties(prefix="camel.springboot.beanio")
public class BeanIOProperties {
	
	private String mappings;
	
	private String streams;

	/**
	 * @return the mappings
	 */
	public String getMappings() {
		return mappings;
	}

	/**
	 * @param mappings the mappings to set
	 */
	public void setMappings(String mappings) {
		this.mappings = mappings;
	}

	/**
	 * @return the streams
	 */
	public String getStreams() {
		return streams;
	}

	/**
	 * @param streams the streams to set
	 */
	public void setStreams(String streams) {
		this.streams = streams;
	}
	
}
