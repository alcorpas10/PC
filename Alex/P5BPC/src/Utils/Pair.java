package Utils;

public class Pair<T,P> {

	private T first;
	private P second;
	
	public Pair(T first, P second) {
		this.first = first;
		this.second = second;
	}

	public T getFirst() {
		return first;
	}

	public P getSecond() {
		return second;
	}
}
