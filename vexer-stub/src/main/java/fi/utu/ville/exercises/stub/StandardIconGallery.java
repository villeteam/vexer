package fi.utu.ville.exercises.stub;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

import fi.utu.ville.standardutils.StandardIcon;

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

		addComponent(new Label("We use Vaadin's font-awesome extension"));
		// for (StandardIcon icon : StandardIcon.values()) {
		// addComponent(new Embedded(icon.name(), icon.getIcon()));
		// }

	}
}
