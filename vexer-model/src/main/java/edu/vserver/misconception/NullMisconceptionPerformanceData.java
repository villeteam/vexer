package edu.vserver.misconception;

import java.sql.ResultSet;
import java.util.ArrayList;

public class NullMisconceptionPerformanceData implements
		MisconceptionPerformanceData, MisconceptionData, MisconceptionTypeData {

	private static final long serialVersionUID = 1L;

	@Override
	public void setData(ResultSet rs) {
	}

	@Override
	public void save() {
	}

	@Override
	public void update() {
	}

	@Override
	public void delete() {
	}

	@Override
	public void loadMisconceptionData() {
	}

	@Override
	public void addMisconceptionData(MisconceptionData data) {
	}

	@Override
	public void saveMisconceptions() {
	}

	@Override
	public void updateMisconception() {
	}

	@Override
	public void deleteMisconception() {

	}

	@Override
	public void setMathPerformanceIdToMisconception() {

	}

	@Override
	public int getAssigId() {
		return -1;
	}

	@Override
	public void setAssigId(int assigId) {

	}

	@Override
	public String getUserId() {
		return "";
	}

	@Override
	public void setUserId(String userId) {

	}

	@Override
	public boolean isSubmitted() {
		return false;
	}

	@Override
	public void setSubmitted(boolean isSubmitted) {

	}

	@Override
	public ArrayList<MisconceptionData> getMmd() {
		return new ArrayList<MisconceptionData>();
	}

	@Override
	public void setMmd(ArrayList<MisconceptionData> mmd) {

	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public void setId(int id) {
	}

	@Override
	public void setStartTime() {
	}

	@Override
	public void setEndTime(){
	}

	@Override
	public long getUsedTime() {
		return 0;
	}

	@Override
	public ArrayList<MisconceptionTypeData> toList() {
		return null;
	}

	@Override
	public int getMisconceptionId() {
		return 0;
	}

	@Override
	public void setMisconceptionId(int misconceptionId) {
		
	}

	@Override
	public Misconception getType() {
		return Misconception.NONE;
	}

	@Override
	public void setType(Misconception type) {		
	}

	@Override
	public String typeToString() {
		return "";
	}

	@Override
	public void setTypeFromString(String str) {
		
	}

	@Override
	public void loadMisconceptionTypeData() {
		
	}

	@Override
	public void setMisconceptionIdToMisconceptionTypeData(int id) {
		
	}

	@Override
	public void saveMisconceptionTypeData() {
		
	}

	@Override
	public void updateMisconceptionTypeData() {		
	}

	@Override
	public void deleteMisconceptionTypeData() {		
	}

	@Override
	public void addMisconceptionTypes(ArrayList<MisconceptionTypeData> newTypes) {		
	}

	@Override
	public void addMIsconceptionType(MisconceptionTypeData type) {		
	}

	@Override
	public ArrayList<MisconceptionTypeData> getTypes() {
		return new ArrayList<>();
	}

	@Override
	public void setTypes(ArrayList<MisconceptionTypeData> types) {
		
	}

	@Override
	public boolean isCorrect() {
		return false;
	}

	@Override
	public void setCorrect(boolean isCorrect) {
		
	}

	@Override
	public String getProblem() {
		return "";
	}

	@Override
	public void setProblem(String problem) {
		
	}

	@Override
	public String getAnswer() {
		return "";
	}

	@Override
	public void setAnswer(String answer) {
		
	}

	@Override
	public int getPerformance_id() {
		return 0;
	}

	@Override
	public void setPerformance_id(int performance_id) {
		
	}

	@Override
	public String getCorrectAnswer() {
		return "";
	}

	@Override
	public void setCorrectAnswer(String correctAnswer) {
		
	}

	@Override
	public long getTime() {
		return 0;
	}

	@Override
	public void setTime(long time) {
		
	}

}
