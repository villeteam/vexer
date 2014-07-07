package fi.utu.ville.exercises.model;

import com.vaadin.ui.VerticalLayout;

public abstract class VilleContent extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private final VilleUI ui;

	public VilleContent(VilleUI ui) {
		this.ui = ui;
	}

	public abstract void doLayout();

	public abstract boolean isOkToExit();

	public VilleUI getVilleUI() {
		return ui;
	}

	protected boolean changeContent(VilleContent content) {
		return ui.changeContent(content);
	}

}