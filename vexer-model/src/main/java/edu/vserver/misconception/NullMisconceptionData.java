package edu.vserver.misconception;

import java.util.ArrayList;

public class NullMisconceptionData implements MisconceptionData{

	private static final long serialVersionUID = 1L;

	@Override
	public void save() {}

	@Override
	public void update() {}

	@Override
	public void delete() {}

	@Override
	public void loadMisconceptionTypeData() {}

	@Override
	public void setMisconceptionIdToMisconceptionTypeData(int id) {}

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
	public void addMisconceptionTypes(
			ArrayList<MisconceptionTypeData> newTypes) {
	}

	@Override
	public void addMIsconceptionType(MisconceptionTypeData type) {
	}

	@Override
	public ArrayList<MisconceptionTypeData> getTypes() {
		return new ArrayList<MisconceptionTypeData>();
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
	public int getId() {
		return 0;
	}

	@Override
	public void setId(int id) {
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
