package edu.vserver.exercises.stub;

import java.io.Serializable;
import java.util.Locale;

import com.vaadin.server.VaadinSession;

import edu.vserver.exercises.model.ExecutionSettings;
import edu.vserver.exercises.model.ExerciseTypeDescriptor;
import edu.vserver.standardutils.Localizer;

/**
 * A class storing the current stub-settings (which
 * {@link ExerciseTypeDescriptor} to test etc.) in {@link VaadinSession}.
 * 
 * @author Riku Haavisto
 * 
 */
class StubState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6217519318193530725L;
	private Localizer currResourceGiver;
	private ExerciseTypeDescriptor<?, ?> currDesc;
	private ExecutionSettings currExecSettings;
	private String currExerName = "";

	public static StubState getCurrent() {
		return VaadinSession.getCurrent().getAttribute(StubState.class);
	}

	static void initIfNeeded(Locale initLocale,
			ExerciseTypeDescriptor<?, ?> initDesc,
			ExecutionSettings initExecSettings) {
		if (StubState.getCurrent() == null) {
			StubState newState = new StubState();

			newState.setCurrResourceGiver(new StubResourceGiverImpl(initLocale));
			newState.setCurrDesc(initDesc);
			newState.setCurrExecSettings(initExecSettings);
			VaadinSession.getCurrent().setAttribute(StubState.class, newState);
		}
	}

	public Localizer getCurrResourceGiver() {
		return currResourceGiver;
	}

	public void setCurrResourceGiver(Localizer currResourceGiver) {
		this.currResourceGiver = currResourceGiver;
	}

	public ExerciseTypeDescriptor<?, ?> getCurrDesc() {
		return currDesc;
	}

	public void setCurrDesc(ExerciseTypeDescriptor<?, ?> currDesc) {
		this.currDesc = currDesc;
	}

	public String getCurrExerName() {
		return currExerName;
	}

	public void setCurrExerName(String currName) {
		this.currExerName = currName;
	}

	public ExecutionSettings getCurrExecSettings() {
		return currExecSettings;
	}

	public void setCurrExecSettings(ExecutionSettings currExecSettings) {
		this.currExecSettings = currExecSettings;
	}

}
