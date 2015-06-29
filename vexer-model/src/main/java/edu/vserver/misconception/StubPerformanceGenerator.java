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
		// TODO Auto-generated method stub
		return new NullMisconceptionPerformanceData();
	}

	@Override
	public MisconceptionTypeData getMisconceptionTypeData(
			MisconceptionPerformanceSubject misconceptionPerformanceSubject) {
		// TODO Auto-generated method stub
		return new NullMisconceptionPerformanceData();
	}

}
