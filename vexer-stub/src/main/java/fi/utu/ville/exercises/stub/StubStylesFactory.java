package fi.utu.ville.exercises.stub;

import fi.utu.ville.standardutils.ui.DynamicStyles.DynamicStylesEditor;
import fi.utu.ville.standardutils.ui.DynamicStyles.DynamicStylesFactory;
import fi.utu.ville.standardutils.ui.DynamicStyles.StyleSettings;

/**
 * <p>
 * Stub-implementor for {@link DynamicStylesFactory}.
 * </p>
 * <p>
 * The dynamic-styles implementors provided by this factory class differ from the real ViLLE-implementors only in that it does not provide saving and loading of
 * user specific style-settings templates. Any code written against the stub-implementors should work in real ViLLE with no modifications.
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
