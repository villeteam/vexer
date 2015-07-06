package edu.vserver.misconception;

public interface PerformanceDataGenerator {

	MisconceptionPerformanceData getPerformanceData(MisconceptionPerformanceSubject misconceptionPerformanceSubject);

	MisconceptionData getMisconceptionData(
			MisconceptionPerformanceSubject misconceptionPerformanceSubject);

	MisconceptionTypeData getMisconceptionTypeData(
			MisconceptionPerformanceSubject misconceptionPerformanceSubject);
	
	MisconceptionTypeData getMisconceptionTypeData(
			MisconceptionPerformanceSubject misconceptionPerformanceSubject, Misconception misconception);
}
