package github.rainbowmori.rainbowapi.object.command.wrappers;

public class FloatRange {

	private final float low;
	private final float high;

	public FloatRange(float low, float high) {
		this.low = low;
		this.high = high;
	}
	public static FloatRange floatRangeGreaterThanOrEq(float min) {
		return new FloatRange(min, Float.MAX_VALUE);
	}

	public static FloatRange floatRangeLessThanOrEq(float max) {
		return new FloatRange(-Float.MAX_VALUE, max);
	}

	public float getLowerBound() {
		return this.low;
	}

	public float getUpperBound() {
		return this.high;
	}

	public boolean isInRange(float f) {
		return f >= low && f <= high;
	}
	@Override
	public String toString() {
		if(this.high == Float.MAX_VALUE) {
			return this.low + "..";
		} else if(this.low == -Float.MAX_VALUE) {
			return ".." + this.high;
		} else {
			return this.low + ".." + this.high;
		}
	}
	
}
