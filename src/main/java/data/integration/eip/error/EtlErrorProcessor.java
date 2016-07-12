/**
 * 
 */
package data.integration.eip.error;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import org.beanio.BeanIOException;

/**
 * @author Prajapati
 *
 */
public class EtlErrorProcessor implements Processor {
	
	private static Logger LOG = Logger.getLogger(EtlErrorProcessor.class);

	/* (non-Javadoc)
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		Exception exception = exchange.getException();
		if(exception instanceof BeanIOException) {
			LOG.error("EtlErrorProcessor - BeanIOException", exception);
			// TODO: Add error handling for exceptions related to BeanIO
		} else {
			LOG.error("EtlErrorProcessor - exception", exception);
		}

	}

}
