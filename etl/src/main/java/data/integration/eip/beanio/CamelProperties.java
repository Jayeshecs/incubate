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
@ConfigurationProperties(prefix="camel.springboot")
public class CamelProperties {
	
	private String routesLocation;

	/**
	 * @return the routesLocation
	 */
	public String getRoutesLocation() {
		return routesLocation;
	}

	/**
	 * @param routesLocation the routesLocation to set
	 */
	public void setRoutesLocation(String routesLocation) {
		this.routesLocation = routesLocation;
	}
	
}
