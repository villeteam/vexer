package edu.vserver.exercises.stub;

import java.util.Locale;
import java.util.Map;

import edu.vserver.standardutils.Localizer;

/**
 * Stub-version implementor of {@link Localizer}. Translations found in
 * .trl-files in {@link VilleExerStubUI #getStubResourceBaseDir()}/lang/ will be
 * available.
 * 
 * @author Riku Haavisto
 * 
 */
final class StubResourceGiverImpl implements Localizer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9062818693540069962L;

	private final Locale currLocale;

	public StubResourceGiverImpl(Locale currLocale) {
		this.currLocale = currLocale;
	}

	@Override
	public String getUIText(String constant) {
		String res = UILanguageGiver.getUILanguage(currLocale).getTranslation(
				constant);

		if (res == null) {
			res = UILanguageGiver.getUILanguage(Locale.ENGLISH).getTranslation(
					constant);
		}
		if (res == null) {
			res = constant;
		}

		return res;

	}

	@Override
	public String getUIText(String key, String... params) {
		return UILanguageStub.constructLine(getUIText(key), params);
	}

	@Override
	public Locale getCurrentLocale() {
		return currLocale;
	}

	@Override
	public String getUIText(String key, Map<String, String> params) {
		return UILanguageStub.constructLine(getUIText(key), params);
	}

}
