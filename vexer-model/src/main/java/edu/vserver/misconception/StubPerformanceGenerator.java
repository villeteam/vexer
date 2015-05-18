package edu.vserver.misconception;

public class StubPerformanceGenerator implements PerformanceDataGenerator{

	@Override
	public MisconceptionPerformanceData getPerformanceData(
			MisconceptionPerformanceSubject misconceptionPerformanceSubject) {
		return new NullMisconceptionPerformanceData();
	}

}
