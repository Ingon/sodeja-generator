package org.sodeja.generator.uml;

public enum UmlMultiplicityRange {
	ONE_ONE(false),
	ZERO_ONE(false),
	ZERO_MANY(true),
	ONE_MANY(true);
	
	private boolean multy;
	
	private UmlMultiplicityRange(boolean multy) {
		this.multy = multy;
	}

	public boolean isMulty() {
		return multy;
	}
}
