package ${package};

import edu.vserver.exercises.model.ExerciseData;
import edu.vserver.standardutils.AbstractFile;

public class ${VilleJavaClassPrefix}ExerciseData implements ExerciseData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -716445297446246493L;

	private final String question;
	private final AbstractFile imgFile;

	public ${VilleJavaClassPrefix}ExerciseData(String question, AbstractFile imgFile) {
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
