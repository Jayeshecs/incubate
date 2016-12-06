package functions.dataset;

import java.util.Map;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import utils.dataset.DataSetUtil;

class StringToDataSetFn implements Function<String, DataSet> {
    
    private final Broadcast<Map<Character, Integer>> ctiBroadcast;

    private StringToDataSetFn(Broadcast<Map<Character, Integer>> characterIntegerMap) {
        this.ctiBroadcast = characterIntegerMap;
    }

    @Override
    public DataSet call(String s) throws Exception {
        //Here: take a String, and map the characters to a one-hot representation
        Map<Character, Integer> cti = ctiBroadcast.getValue();
        int length = s.length();
        INDArray features = Nd4j.zeros(1, DataSetUtil.N_CHARS, length - 1);
        INDArray labels = Nd4j.zeros(1, DataSetUtil.N_CHARS, length - 1);
        char[] chars = s.toCharArray();
        int[] f = new int[3];
        int[] l = new int[3];
        for (int i = 0; i < chars.length - 2; i++) {
            f[1] = cti.get(chars[i]);
            f[2] = i;
            l[1] = cti.get(chars[i + 1]);   //Predict the next character given past and current characters
            l[2] = i;

            features.putScalar(f, 1.0);
            labels.putScalar(l, 1.0);
        }
        return new DataSet(features, labels);
    }
}