package edu.vserver.exercises.stub;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.vaadin.server.VaadinServlet;

/**
 * A class for loading a .trl files (if needed) and providing a global access to
 * them.
 */
class UILanguageGiver {

	private static final Logger logger = Logger.getLogger(UILanguageGiver.class
			.getName());

	private static final AtomicReference<Map<String, UILanguageStub>> uiLanguages =

	new AtomicReference<Map<String, UILanguageStub>>(null);

	public static UILanguageStub getUILanguage(Locale locale) {

		if (uiLanguages.get() == null) {
			reloadLanguages();
		}

		UILanguageStub res = uiLanguages.get().get(locale.getLanguage());

		if (res == null) {
			throw new IllegalStateException(
					"No translations were found in the selected language! "
							+ "Add at least some translations in the given language (@"
							+ locale.getLanguage()
							+ ") or choose another language!");
		} else {
			return res;
		}

	}

	public static void reloadLanguages() {

		Map<String, UILanguageStub> oldValue = uiLanguages.get();

		UILanguageLoaderStub loader = new UILanguageLoaderStub();

		try {
			loader.readFiles();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not load language files", e);
		}

		Map<String, UILanguageStub> initiatedLangs = loader.getLoadedLangs();

		uiLanguages.compareAndSet(oldValue, initiatedLangs);

	}

	static class UILanguageLoaderStub {

		private static Logger logger = Logger
				.getLogger(UILanguageLoaderStub.class.getName());

		private static final String translationFileSuffix = ".trl";

		private final Map<String, Map<String, String>> dictsByLang

		= new HashMap<String, Map<String, String>>();;

		public Map<String, UILanguageStub> getLoadedLangs() {
			Map<String, UILanguageStub> res = new HashMap<String, UILanguageStub>();

			for (String langName : dictsByLang.keySet()) {
				res.put(langName, new UILanguageStub(dictsByLang.get(langName)));
			}

			return res;

		}

		Map<String, Map<String, String>> getLoadedBareLangMaps() {
			return dictsByLang;
		}

		public void readFiles() throws IOException {
			dictsByLang.clear();

			Set<String> matches = VaadinServlet.getCurrent()
					.getServletContext()
					.getResourcePaths("/VILLE/language/extensions/");

			for (String aMatch : matches) {
				if (aMatch.endsWith(translationFileSuffix)) {

					// strip path
					String fname = aMatch;
					if (fname.contains("/")) {
						// known to end with 'translationFileSuffix' so should
						// not be out-of-index
						fname = fname.substring(fname.lastIndexOf("/") + 1);
					}
					String currPrefix = fname.substring(0,
							fname.length() - translationFileSuffix.length())
							.toUpperCase();

					Map<String, Map<String, String>> newTranslations = TranslationFileParser
							.parseTranslationFile(currPrefix,
									(readFile(VaadinServlet.getCurrent()
											.getServletContext()
											.getResourceAsStream(aMatch))));

					for (Entry<String, Map<String, String>> addToLang : newTranslations
							.entrySet()) {

						Map<String, String> translationsInLang = dictsByLang
								.get(addToLang.getKey());
						if (translationsInLang == null) {
							translationsInLang = new HashMap<String, String>();
							dictsByLang.put(addToLang.getKey(),
									translationsInLang);
						}

						for (Entry<String, String> newTranslation : addToLang
								.getValue().entrySet()) {
							// this would require having two translation files
							// with same name
							if (translationsInLang.put(newTranslation.getKey(),
									newTranslation.getValue()) != null) {
								logger.warning("Overrid entry for key: "
										+ newTranslation.getKey()
										+ ". This probably means that you have "
										+ "included two .trl files with same namespace (=file-name)");
							}
						}

					}

				} else {
					logger.warning("Non-trl resource-file ( " + aMatch
							+ " ) contained in translation file path!");
				}
			}

		}

		private List<String> readFile(InputStream toRead) throws IOException {
			List<String> res = IOUtils.readLines(toRead, "UTF-8");
			return res;
		}

	}
}
