package edu.vserver.exercises.stub;

import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;

import edu.vserver.standardutils.StandardIcon;

/**
 * A simple "gallery" of all the icons currently in {@link StandardIcon} that
 * are guaranteed to be present in the real ViLLE.
 * 
 * @author Riku Haavisto
 * 
 */
class StandardIconGallery extends GridLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5355264666283422058L;

	public StandardIconGallery() {
		super(4, 4);
		setMargin(true);
		setSpacing(true);
		doLayout();
	}

	private void doLayout() {

		for (StandardIcon icon : StandardIcon.values()) {
			addComponent(new Embedded(icon.name(), icon.getIcon()));
		}

	}
}
