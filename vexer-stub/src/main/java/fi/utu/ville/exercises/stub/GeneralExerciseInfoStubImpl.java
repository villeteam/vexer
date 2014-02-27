package fi.utu.ville.exercises.stub;

import fi.utu.ville.exercises.model.GeneralExerciseInfo;

/**
 * A really quick-and-dirty implemention of {@link GeneralExerciseInfo} for
 * testing.
 * 
 * @author Riku Haavisto
 */
class GeneralExerciseInfoStubImpl implements GeneralExerciseInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7260910247079079566L;

	private final String name;
	private final String desc;

	/**
	 * Construct a new {@link GeneralExerciseInfoStubImpl} with given name and
	 * description
	 * 
	 * @param name
	 *            name for the exercise
	 * @param desc
	 *            description for the exercise
	 */
	public GeneralExerciseInfoStubImpl(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return desc;
	}

}
