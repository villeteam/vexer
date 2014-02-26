package edu.vserver.exercises.stub;

import edu.vserver.standardutils.ui.DynamicStyles.DynamicStylesEditor;
import edu.vserver.standardutils.ui.DynamicStyles.DynamicStylesFactory;
import edu.vserver.standardutils.ui.DynamicStyles.StyleSettings;

/**
 * <p>
 * Stub-implementor for {@link DynamicStylesFactory}.
 * </p>
 * <p>
 * The dynamic-styles implementors provided by this factory class differ from
 * the real ViLLE-implementors only in that it does not provide saving and
 * loading of user specific style-settings templates. Any code written against
 * the stub-implementors should work in real ViLLE with no modifications.
 * </p>
 * 
 * @author Riku Haavisto
 * 
 */
class StubStylesFactory implements DynamicStylesFactory {

	@Override
	public DynamicStylesEditor newDynStyles() {
		return new StubStyleSettingsEditor(StubState.getCurrent()
				.getCurrResourceGiver());
	}

	@Override
	public StyleSettings newStyleSettings() {
		return new StubStyleSettings();
	}

}
