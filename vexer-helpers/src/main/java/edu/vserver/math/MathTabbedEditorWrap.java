package edu.vserver.math;

import java.io.Serializable;

import com.vaadin.ui.Layout;

import fi.utu.ville.exercises.model.ExerciseData;

public interface MathTabbedEditorWrap<E extends ExerciseData> extends
		Serializable {

	public Layout drawEditorLayout();

	public Layout drawSettings();

	public E getCurrData();

	public Boolean validateData();

	public String setTitleText();
}
