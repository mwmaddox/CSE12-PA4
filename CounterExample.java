import java.util.Arrays;

class CounterExample {
	String[] before, after;
	int low, high, pivot;
	String reason;

	public CounterExample(String[] before, int low, int high, int pivot, String[] after, String reason) {
		this.before = before;
		this.after = after;
		this.pivot = pivot;
		this.reason = reason;
		this.low = low;
		this.high = high;
	}

	public String toString() {
		return this.reason + ": " + Arrays.toString(this.before) + " " + this.low + " to " + this.high +
				" -> " + this.pivot + " in " + Arrays.toString(after);
	}
}