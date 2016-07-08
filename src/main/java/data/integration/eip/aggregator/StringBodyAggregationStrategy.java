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

	public static void main(String[] args) {
		String tradeRecord = "TC20150112132  072  132  C0795427  2000T972  2000T972                        R07   NG        20150600 00000000000029212015011200000001S   G     0001USD0000000000000000000000  E  C0795427  2000T972   E\r\nUC20150112132  072  132  C0795427  2000T972  NYX  GSAC AYGVF_SVK/AHGB-AYG000661489652ENERGY            N08N 2.921           126791                                                                     X";
		String tradeRecord2 = "TC20150112132  072  132  C0795427  2000T972  2000T972                        R07   NG        20150600 00000000000029212015011200000001S   G     0001USD0000000000000000000000  E  C0795427  2000T972   E\nUC20150112132  072  132  C0795427  2000T972  NYX  GSAC AYGVF_SVK/AHGB-AYG000661489652ENERGY            N08N 2.921           126791                                                                     X";
		System.out.println("trade: " + tradeRecord);
		System.out.println();
		String regex = "^(TC|TE).*(\r\n|\n).*";
		System.out.println("regex '" + regex + "' = " + tradeRecord.matches(regex));
		System.out.println();
		System.out.println();
		System.out.println("trade: " + tradeRecord2);
		System.out.println();
		System.out.println("regex '" + regex + "' = " + tradeRecord2.matches(regex));
		System.out.println(tradeRecord.split("\r\n|\n"));
	}
}
