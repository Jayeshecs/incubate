/**
 * 
 */
package data.integration.eip.aggregator;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * @author Prajapati
 *
 */
public class StringBodyAggregationStrategy implements AggregationStrategy {

	/* (non-Javadoc)
	 * @see org.apache.camel.processor.aggregate.AggregationStrategy#aggregate(org.apache.camel.Exchange, org.apache.camel.Exchange)
	 */
	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if(oldExchange == null) {
			return newExchange;
		}
		if(newExchange == null) {
			return oldExchange;
		}
		Message newIn = newExchange.getIn();
		String oldBody = oldExchange.getIn().getBody(String.class);
		String newBody = newIn.getBody(String.class);
		newIn.setBody(oldBody + newBody);
		return newExchange;
	}
}
