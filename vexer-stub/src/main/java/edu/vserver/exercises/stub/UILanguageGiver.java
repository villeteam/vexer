package edu.vserver.exercises.stub;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
		private static final String keyConstant = "@key:";
		// private static final String splitter = ":";
		private static final String prefixSplitter = ".";

		private final Map<String, Map<String, String>> dictsByLang

		= new HashMap<String, Map<String, String>>();;

		private String currKey = "";
		private String currOpenTranslationLang = "";
		private String currOpenTranslation = "";
		private String currPrefix = "";

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
					currPrefix = fname.substring(0,
							fname.length() - translationFileSuffix.length())
							.toUpperCase();

					currKey = "";
					currOpenTranslationLang = "";
					currOpenTranslation = "";

					parseFile(readFile(VaadinServlet.getCurrent()
							.getServletContext().getResourceAsStream(aMatch)));
				} else {
					logger.warning("Non-trl resource-file ( " + aMatch
							+ " ) contained in translation file path!");
				}
			}

		}

		private void parseFile(List<String> lines) {

			// Process the file: remove comments and empty lines
			for (int i = 0; i < lines.size(); i++) {
				String currLine = lines.get(i);
				logger.fine("line: " + currLine);
				logger.fine(currKey + "; " + currOpenTranslationLang + "; "
						+ currOpenTranslation);
				if (!checkLineFormat(currLine)) {
					throw new IllegalArgumentException(
							"Language file has illegal format");
				}

				if (currLine.startsWith("//")) {
					continue; // comment line
				}
				// breaks the current key
				else if (currLine.startsWith(keyConstant)) {
					addCurrOpenTranslation();
					// String[] parts = currLine.split(splitter);
					String keyPart = currLine.substring(keyConstant.length());

					currKey = keyPart.trim();
					// currKey = parts[1].trim();
					currOpenTranslationLang = "";
					currOpenTranslation = "";
				}
				// breaks the current translation
				// allow starting a translation with just the lang-tag
				else if (currLine.matches("^@\\w+:.*$")) {
					addCurrOpenTranslation();
					// String[] parts = currLine.split(splitter);
					String langPart = currLine.substring(1,
							currLine.indexOf(':'));

					String transPart = currLine
							.substring(currLine.indexOf(':') + 1);

					// currOpenTranslationLang = parts[0].trim().substring(1);
					// currOpenTranslation = parts[1];
					currOpenTranslationLang = langPart.trim();
					currOpenTranslation = transPart;
				}
				// appends to the current translation
				else {
					currOpenTranslation += currLine;
				}

			}
			// add the final hanging translation
			addCurrOpenTranslation();

		}

		private void addCurrOpenTranslation() {

			if (!"".equals(currOpenTranslationLang) && !"".equals(currKey)
					&& !"".equals(currOpenTranslation)) {

				if (Arrays.binarySearch(Locale.getISOLanguages(),
						currOpenTranslationLang) < 0) {
					throw new IllegalStateException(
							"Not a valid ISO-language-code: "
									+ currOpenTranslationLang);
				}

				if (!dictsByLang.containsKey(currOpenTranslationLang)) {
					if (!dictsByLang.containsKey(currOpenTranslationLang)) {
						dictsByLang.put(currOpenTranslationLang,
								new HashMap<String, String>());
					}
					// throw new IllegalStateException(
					// "Trying to add translation for language that was "
					// + "not specified in the languages list");
				}
				String oldValue = null;
				if (null != (oldValue = dictsByLang
						.get(currOpenTranslationLang).put(
								currPrefix + prefixSplitter + currKey,
								currOpenTranslation))) {
					logger.warning("Overrid entry for key: " + currPrefix
							+ prefixSplitter + currKey + " and language: "
							+ currOpenTranslationLang + "\nOld value: "
							+ oldValue + "\nNew value: " + currOpenTranslation);
				} else {
					logger.fine("Added for key: " + currPrefix + prefixSplitter
							+ currKey + " and language: "
							+ currOpenTranslationLang + "\nValue: "
							+ currOpenTranslation);
				}

			}
		}

		private static boolean checkLineFormat(String line) {
			return true;
		}

		private List<String> readFile(InputStream toRead) throws IOException {
			List<String> res = IOUtils.readLines(toRead);
			return res;
		}

	}
}
