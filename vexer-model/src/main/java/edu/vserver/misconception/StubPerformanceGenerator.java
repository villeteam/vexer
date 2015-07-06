package edu.vserver.misconception;

public class StubPerformanceGenerator implements PerformanceDataGenerator{

	@Override
	public MisconceptionPerformanceData getPerformanceData(
			MisconceptionPerformanceSubject misconceptionPerformanceSubject) {
		return new NullMisconceptionPerformanceData();
	}

	@Override
	public MisconceptionData getMisconceptionData(
			MisconceptionPerformanceSubject misconceptionPerformanceSubject) {
		return new NullMisconceptionPerformanceData();
	}

	@Override
	public MisconceptionTypeData getMisconceptionTypeData(
			MisconceptionPerformanceSubject misconceptionPerformanceSubject) {
		return new NullMisconceptionPerformanceData();
	}

	@Override
	public MisconceptionTypeData getMisconceptionTypeData(
			MisconceptionPerformanceSubject misconceptionPerformanceSubject,
			Misconception misconception) {
		return new NullMisconceptionPerformanceData();
	}

}
