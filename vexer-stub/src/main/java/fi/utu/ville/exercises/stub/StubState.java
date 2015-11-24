package fi.utu.ville.exercises.stub;

import java.io.Serializable;
import java.util.Locale;

import com.vaadin.server.VaadinSession;

import fi.utu.ville.exercises.model.ExecutionSettings;
import fi.utu.ville.exercises.model.ExerciseTypeDescriptor;
import fi.utu.ville.standardutils.Localizer;

/**
 * A class storing the current stub-settings (which {@link ExerciseTypeDescriptor} to test etc.) in {@link VaadinSession}.
 * 
 * @author Riku Haavisto
 * 
 */
final class StubState implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6217519318193530725L;
	private Localizer currResourceGiver;
	private ExerciseTypeDescriptor<?, ?> currDesc;
	private ExecutionSettings currExecSettings;
	private String currExerName = "";
	
	// instances should be fetched through static-methods
	private StubState() {
	}
	
	/**
	 * @return {@link StubState}-object storing the current settings for the stub
	 */
	public static StubState getCurrent() {
		return VaadinSession.getCurrent().getAttribute(StubState.class);
	}
	
	/**
	 * Initiate a new {@link StubState} and store it to {@link VaadinSession} if there is no state-object already stored to the session.
	 * 
	 * @param initLocale
	 *            {@link Locale} to use initially
	 * @param initDesc
	 *            {@link ExerciseTypeDescriptor} to use initially
	 * @param initExecSettings
	 *            {@link ExecutionSettings} to use initially
	 */
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
	
	/**
	 * @return current {@link Localizer} that uses current {@link Locale}
	 */
	public Localizer getCurrResourceGiver() {
		return currResourceGiver;
	}
	
	/**
	 * Set current {@link Localizer}.
	 * 
	 * @param currResourceGiver
	 *            new {@link Localizer}
	 */
	public void setCurrResourceGiver(Localizer currResourceGiver) {
		this.currResourceGiver = currResourceGiver;
	}
	
	/**
	 * @return {@link ExerciseTypeDescriptor} for the current exercise-type
	 */
	public ExerciseTypeDescriptor<?, ?> getCurrDesc() {
		return currDesc;
	}
	
	/**
	 * Sets the exercise-type to be currently selected exercise-type.
	 * 
	 * @param currDesc
	 *            {@link ExerciseTypeDescriptor} to use
	 */
	public void setCurrDesc(ExerciseTypeDescriptor<?, ?> currDesc) {
		this.currDesc = currDesc;
	}
	
	/**
	 * @return name of the currently selected exercise-instance
	 */
	public String getCurrExerName() {
		return currExerName;
	}
	
	/**
	 * Sets the currently selected exercise-instance name.
	 * 
	 * @param currName
	 *            new exercise-name (or id) to use
	 */
	public void setCurrExerName(String currName) {
		currExerName = currName;
	}
	
	/**
	 * @return current {@link ExecutionSettings}
	 */
	public ExecutionSettings getCurrExecSettings() {
		return currExecSettings;
	}
	
	/**
	 * Sets new {@link ExecutionSettings} to be used.
	 * 
	 * @param currExecSettings
	 *            new {@link ExecutionSettings}
	 */
	public void setCurrExecSettings(ExecutionSettings currExecSettings) {
		this.currExecSettings = currExecSettings;
	}
	
}
