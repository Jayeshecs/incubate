/**
 * 
 */
package data.integration.eip.error;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

/**
 * @author Prajapati
 *
 */
public class EtlRedeliveryProcessor implements Processor {
	
	private static Logger LOG = Logger.getLogger(EtlRedeliveryProcessor.class);

	/* (non-Javadoc)
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		LOG.info("EtlPrepareFailureProcessor - exchange: " + exchange.toString());
		LOG.error("EtlPrepareFailureProcessor - exception: " + exchange.getException(), exchange.getException());
	}

}
