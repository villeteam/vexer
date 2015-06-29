package edu.vserver.misconception;

import java.sql.ResultSet;
import java.util.ArrayList;

public class NullMisconceptionPerformanceData implements
		MisconceptionPerformanceData, MisconceptionData, MisconceptionTypeData {

	private static final long serialVersionUID = 1L;

	@Override
	public void setData(ResultSet rs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadMisconceptionData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMisconceptionData(MisconceptionData data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveMisconceptions() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateMisconception() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteMisconception() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMathPerformanceIdToMisconception() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getAssigId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAssigId(int assigId) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setUserId(String userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSubmitted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSubmitted(boolean isSubmitted) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<MisconceptionData> getMmd() {
		// TODO Auto-generated method stub
		return new ArrayList<MisconceptionData>();
	}

	@Override
	public void setMmd(ArrayList<MisconceptionData> mmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setId(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStartTime() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEndTime() {
		// TODO Auto-generated method stub

	}

	@Override
	public long getUsedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<MisconceptionTypeData> toList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMisconceptionId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMisconceptionId(int misconceptionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Misconception getType() {
		// TODO Auto-generated method stub
		return Misconception.NONE;
	}

	@Override
	public void setType(Misconception type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String typeToString() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setTypeFromString(String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadMisconceptionTypeData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMisconceptionIdToMisconceptionTypeData(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveMisconceptionTypeData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMisconceptionTypeData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMisconceptionTypeData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMisconceptionTypes(ArrayList<MisconceptionTypeData> newTypes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMIsconceptionType(MisconceptionTypeData type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<MisconceptionTypeData> getTypes() {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public void setTypes(ArrayList<MisconceptionTypeData> types) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCorrect(boolean isCorrect) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getProblem() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setProblem(String problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAnswer() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setAnswer(String answer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPerformance_id() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPerformance_id(int performance_id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCorrectAnswer() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setCorrectAnswer(String correctAnswer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTime(long time) {
		// TODO Auto-generated method stub
		
	}

}
