package fi.utu.ville.exercises.model;

import com.vaadin.ui.Window;

public abstract class DialogWithConfirm extends Window {
	
	private static final long serialVersionUID = 6566752641350945779L;
	
	public abstract boolean isOk();
}
