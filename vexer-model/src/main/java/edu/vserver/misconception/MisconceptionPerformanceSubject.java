package edu.vserver.misconception;

/**
 * An enum for all subjects for which misconceptions are collected. 
 * The main project will handle initializing the correct PerformanceData generator.
 */
public enum MisconceptionPerformanceSubject {
	NONE(0),
	MATH(1),
	FINNISH(2);
	
	private int id;
	private MisconceptionPerformanceSubject(int dbId){
		id = dbId;
	}
	
	public MisconceptionPerformanceData getPerformanceData(){
		return dataGenerator.getPerformanceData(this);
	}
	
	public MisconceptionData getMisconceptionData(){
		return dataGenerator.getMisconceptionData(this);
	}
	
	public MisconceptionTypeData getMisconceptionTypeData(){
		return dataGenerator.getMisconceptionTypeData(this);
	}
	
	public int getDbId(){
		return id;
	}
	
	//statics
	private static PerformanceDataGenerator dataGenerator = new StubPerformanceGenerator(); 
	public static void setPerformanceDataGenerator(PerformanceDataGenerator generator){
		dataGenerator = generator;
	}

	public static MisconceptionPerformanceSubject getSubject(int id) {
		for(MisconceptionPerformanceSubject s : values()){
			if(s.id == id)
				return s;
		}
		return NONE;
	}
}
