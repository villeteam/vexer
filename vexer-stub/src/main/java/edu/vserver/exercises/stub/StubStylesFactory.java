package edu.vserver.exercises.stub;

import edu.vserver.standardutils.ui.DynamicStyles.DynamicStylesEditor;
import edu.vserver.standardutils.ui.DynamicStyles.DynamicStylesFactory;
import edu.vserver.standardutils.ui.DynamicStyles.StyleSettings;

public class StubStylesFactory implements DynamicStylesFactory {

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
