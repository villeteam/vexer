package edu.vserver.exercises.stub;

import edu.vserver.exercises.model.GeneralExerciseInfo;

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
