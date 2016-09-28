/**
 * 
 */
package data.integration;

import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Prajapati
 *
 */
@SpringBootApplication
public class EtlFatJarRouter extends FatJarRouter {

	@Override
	public void configure() throws Exception {
		super.configure();
	}
}
