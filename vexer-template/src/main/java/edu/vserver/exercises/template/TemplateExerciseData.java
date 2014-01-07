package edu.vserver.exercises.template;

import edu.vserver.exercises.model.ExerciseData;
import edu.vserver.standardutils.AbstractFile;

public class TemplateExerciseData implements ExerciseData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -716445297446246493L;

	private final String question;
	private final AbstractFile imgFile;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QuestionExercise [question=" + question + "]";
	}

	public TemplateExerciseData(String question, AbstractFile imgFile) {
		this.question = question;
		this.imgFile = imgFile;
	}

	public String getQuestion() {
		return question;
	}

	public AbstractFile getImgFile() {
		return imgFile;
	}

}
