package edu.vserver.exercises.stub;

import java.io.Serializable;

import com.vaadin.server.Resource;

import edu.vserver.standardutils.StandardIcon;

/**
 * A stub-class containing some info purely for testing purposes.
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

	public String getDescription() {
		return "Description";
	}

	public Resource getIcon() {
		return StandardIcon.EDIT_ICON_SMALL.getIcon();
	}

	public String getName() {
		return "Name";

	}

}
