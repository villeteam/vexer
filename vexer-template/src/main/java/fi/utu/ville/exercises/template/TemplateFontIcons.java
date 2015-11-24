package fi.utu.ville.exercises.template;

import fi.utu.ville.standardutils.StandardIcon;
import fi.utu.ville.standardutils.StandardIcon.RawIcon;

public enum TemplateFontIcons {
	COGS(StandardIcon.RawIcon.cogs),
	OK(StandardIcon.RawIcon.check),
	DELETE(StandardIcon.RawIcon.times),
	UPLOAD(StandardIcon.RawIcon.upload);
	
	private final RawIcon rawIcon;
	
	private TemplateFontIcons(RawIcon icon) {
		rawIcon = icon;
	}
	
	public RawIcon getIcon() {
		return rawIcon;
	}
	
}
