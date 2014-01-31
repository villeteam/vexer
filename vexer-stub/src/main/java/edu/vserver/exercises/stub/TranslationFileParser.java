package edu.vserver.exercises.stub;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public class TranslationFileParser {

	private static final String keyConstant = "@key:";
	private static final String splitter = ":";
	private static final String prefixSplitter = ".";

	public static Map<String, Map<String, String>> parseTranslationFile(
			String prefix, List<String> trlFileLines) {
		TranslationFileParser actParser = new TranslationFileParser();
		return actParser.parseFile(prefix, trlFileLines);
	}

	private static final Logger logger = Logger
			.getLogger(TranslationFileParser.class.getName());

	private final Map<String, Map<String, String>> dictsByLang

	= new HashMap<String, Map<String, String>>();;

	private String currKey = "";
	private String currOpenTranslationLang = "";
	private String currOpenTranslation = "";
	private String currPrefix = "";

	public TranslationFileParser() {

	}

	public void clear() {
		dictsByLang.clear();
		currKey = "";
		currOpenTranslation = "";
		currOpenTranslationLang = "";
		currPrefix = "";
	}

	public Map<String, Map<String, String>> parseFile(String prefix,
			List<String> lines) {

		currPrefix = prefix;
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
				String keyPart = currLine.substring(keyConstant.length());

				currKey = keyPart.trim();
				// currKey = parts[1].trim();
				currOpenTranslationLang = "";
				currOpenTranslation = "";
			}
			// breaks the current translation
			// only way to start a new translation, is to start by lang-tag
			else if (currLine.matches("^@\\w+:.*$")) {
				addCurrOpenTranslation();
				String langPart = currLine.substring(1,
						currLine.indexOf(splitter));

				String transPart = currLine.substring(currLine
						.indexOf(splitter) + 1);

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

		return dictsByLang;

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
			}
			String oldValue = null;
			if (null != (oldValue = dictsByLang.get(currOpenTranslationLang)
					.put(currPrefix + prefixSplitter + currKey,
							currOpenTranslation))) {
				logger.warning("Overrid entry for key: " + currPrefix
						+ prefixSplitter + currKey + " and language: "
						+ currOpenTranslationLang + "\nOld value: " + oldValue
						+ "\nNew value: " + currOpenTranslation);
			} else {
				logger.fine("Added for key: " + currPrefix + prefixSplitter
						+ currKey + " and language: " + currOpenTranslationLang
						+ "\nValue: " + currOpenTranslation);
			}

		}
	}

	private static boolean checkLineFormat(String line) {
		return true;
	}

}
