
package github.rainbowmori.rainbowapi.object.command.wrappers;

public class IntegerRange {

	private final int low;
	private final int high;

	public IntegerRange(int low, int high) {
		this.low = low;
		this.high = high;
	}

	public static IntegerRange integerRangeGreaterThanOrEq(int min) {
		return new IntegerRange(min, Integer.MAX_VALUE);
	}
	

	public static IntegerRange integerRangeLessThanOrEq(int max) {
		return new IntegerRange(Integer.MIN_VALUE, max);
	}

	public int getLowerBound() {
		return this.low;
	}
	

	public int getUpperBound() {
		return this.high;
	}
	

	public boolean isInRange(int i) {
		return i >= low && i <= high;
	}

	@Override
	public String toString() {
		if(this.high == Integer.MAX_VALUE) {
			return this.low + "..";
		} else if(this.low == Integer.MIN_VALUE) {
			return ".." + this.high;
		} else {
			return this.low + ".." + this.high;
		}
	}
	
}
