package utils;

import java.io.IOException;
import java.io.InputStream;

import org.nd4j.linalg.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import functions.CallbackFunction;

public class Utils {

    private static Logger LOG = LoggerFactory.getLogger(Utils.class);

	public static InputStream classpathResourceToInputStream(String resourcePath) {
		try {
			return new ClassPathResource(resourcePath).getInputStream();
		} catch (IOException e) {
			LOG.error("Unable to obtain input stream from given resource path - {}", resourcePath, e);
			throw new IllegalArgumentException("Unable to obtain input stream from given resource path - " + resourcePath, e);
		}
	}

	public static void foreach(String record, CallbackFunction<String> callback) {
		String[] split = record.split(",");
		int index = 0;
		for(String field : split) {
			callback.call(index++, field);
		}
	}
	
}