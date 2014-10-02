package fi.utu.ville.exercises.template;

import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.standardutils.AbstractFile;

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
