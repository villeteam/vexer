package fi.utu.ville.exercises.stub;

import java.io.Serializable;

import com.vaadin.server.Resource;

/**
 * A stub-class containing some mock values to be used when testing exercises
 * without having the actual info.
 * 
 * @author Riku Haavisto
 * 
 */
class TestStubDescription implements Serializable {

	/**
			 * 
			 */
	private static final long serialVersionUID = 3954568372850480549L;

	public final static TestStubDescription INSTANCE = new TestStubDescription();

	private TestStubDescription() {
	}

	/**
	 * @return string to use as test-description
	 */
	public String getDescription() {
		return "Description";
	}

	/**
	 * @return icon to use as test-icon
	 */
	public Resource getIcon() {
		return null;
		// return StandardIcon.EDIT_ICON_SMALL.getIcon();
	}

	/**
	 * @return string to use as test-name
	 */
	public String getName() {
		return "Name";
	}

}
