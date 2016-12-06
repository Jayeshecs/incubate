package functions;

public interface CallbackFunction<T> {
	
	void call(int index, T data);
}