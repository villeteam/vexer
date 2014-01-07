package edu.vserver.exercises.stub;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.vserver.exercises.stub.UILanguageGiver.UILanguageLoaderStub;

/**
 * Contains a static method that can be used for testing a UIConstant-file -
 * .trl-file pair implemented for localization in a classic ViLLE-way.
 * 
 * @author Riku Haavisto
 * 
 */
public final class StubUIConstantsFileTester {

	private StubUIConstantsFileTester() {

	}

	public static boolean testLangFiles(List<Class<?>> uiconstantsClasses,
			String stubLangBaseDir, List<String> languagesToTest)
			throws IOException {

		HashMap<String, String> uiconstUsedValueToName

		= new HashMap<String, String>();

		HashMap<String, Boolean> loadedKeysUsed

		= new HashMap<String, Boolean>();

		boolean res = true;

		Set<String> keysThatHadParametersInValues = new HashSet<String>();

		UILanguageLoaderStub loader = new UILanguageLoaderStub();

		loader.readFiles();

		for (Map<String, String> aDict : loader.getLoadedBareLangMaps()
				.values()) {
			for (String aKey : aDict.keySet()) {
				loadedKeysUsed.put(aKey, Boolean.FALSE);
			}
		}

		for (Class<?> uiConstantsClass : uiconstantsClasses) {
			for (Field uiConstField : uiConstantsClass.getDeclaredFields()) {

				// interested in public static String fields
				if ((Modifier.isStatic(uiConstField.getModifiers())
						&& Modifier.isPublic(uiConstField.getModifiers()) && uiConstField
						.getType().equals(String.class))) {

					try {

						// should be static and final
						if (!Modifier.isFinal(uiConstField.getModifiers())) {
							System.out
									.println("FAIL: Constant field not declared final: "
											+ uiConstField.getName()
											+ "(="
											+ ((String) uiConstField.get(null))
											+ ")");
							res = false;
						}

						String uiConstValue = (String) uiConstField.get(null);

						String alreadyUsedByField = null;
						if (null != (alreadyUsedByField = uiconstUsedValueToName
								.put(uiConstValue, uiConstField.getName()))) {
							System.out.println("----------\nFAIL: "
									+ "the same UIConstant-value ("
									+ uiConstValue + ") is declared in two"
									+ " fields:\n" + uiConstField.getName()
									+ " and " + alreadyUsedByField);
							res = false;
						}

						loadedKeysUsed.put(uiConstValue, Boolean.TRUE);

						// should have a translation in all required languages
						for (String lang : languagesToTest) {

							// the key has no translation for this language
							if (!loader.getLoadedBareLangMaps().containsKey(
									lang)
									||

									!loader.getLoadedBareLangMaps().get(lang)
											.containsKey(uiConstValue)) {

								System.out.println("----------\nFAIL: "
										+ uiConstValue + " not found for lang "
										+ lang);

								res = false;
							}

							else {
								System.out.println("Matched: "
										+ uiConstValue
										+ " = \""
										+ loader.getLoadedBareLangMaps()
												.get(lang).get(uiConstValue)
										+ " \" in lang " + lang);

								if (loader.getLoadedBareLangMaps().get(lang)
										.get(uiConstValue).contains("@")) {
									keysThatHadParametersInValues
											.add(uiConstValue);
								}

							}

						}

					} catch (IllegalArgumentException e) {
						// Should not happen
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// Should not happen
						e.printStackTrace();
					}

				}

			}
		}

		for (Entry<String, Boolean> keyUsed : loadedKeysUsed.entrySet()) {

			if (!keyUsed.getValue()) {
				System.out
						.println("----------\nWARNING: a translation-key "
								+ keyUsed.getKey()
								+ " contained in a language file identified by the prefix was "
								+ "not used by any of the tested constant-files");
			}

		}

		testParametrized(keysThatHadParametersInValues, loader);

		return res;

	}

	private static void testParametrized(Set<String> keysForParametrized,
			UILanguageLoaderStub loadedLangStub) {

		for (String keyForParametrized : keysForParametrized) {
			Set<Integer> lastParamSet = null;
			String lastLang = "";

			for (Entry<String, Map<String, String>> aDict : loadedLangStub
					.getLoadedBareLangMaps().entrySet()) {

				Set<Integer> currParamSet = getParamNumbers(aDict.getValue()
						.get(keyForParametrized));

				if (lastParamSet != null) {
					if (!lastParamSet.equals(currParamSet)) {
						System.out
								.println("----------\nWARNING: "
										+ "ui-constant-key: "
										+ keyForParametrized
										+ " contained different number of parameters for lang '"
										+ lastLang + "' than for lang '"
										+ aDict.getKey() + "'");
					}
				}

				if (currParamSet.isEmpty()) {
					System.out.println("----------\nWARNING: "
							+ "ui-constant-key: " + keyForParametrized
							+ " had value for lang " + aDict.getKey()
							+ " contained no substitutable parameters");
				} else {
					Integer max = Collections.max(currParamSet);

					for (int i = 0; i < max; i++) {
						if (!currParamSet.contains(i)) {
							System.out
									.println("----------\nWARNING: "
											+ "ui-constant-key: "
											+ keyForParametrized
											+ " had value for lang '"
											+ aDict.getKey()
											+ "' that contained a parameter-index (max) "
											+ max
											+ " but did not contain parameter-index "
											+ i);
						}
					}
				}
				lastParamSet = currParamSet;
				lastLang = aDict.getKey();
			}

		}

	}

	private static Set<Integer> getParamNumbers(String parametrized) {
		if (parametrized == null) {
			return new HashSet<Integer>();
		}

		int index = parametrized.indexOf('@', 0);
		Set<Integer> res = new HashSet<Integer>();

		while (index >= 0) {
			try {
				String indexNumb = "";
				for (int i = 1; index + i < parametrized.length()
						&& Character.isDigit(parametrized.charAt(index + i)); i++) {
					indexNumb += parametrized.charAt(index + i);
				}
				int paramNum = Integer.parseInt(indexNumb);
				res.add(paramNum);
			} catch (NumberFormatException e) {
				// should happen only if '@' is followed by zero digit
				// characters
				System.out
						.println("----------\nWARNING: "
								+ "Ui-translation "
								+ parametrized
								+ " contained @ without following index-digit; "
								+ "this @ will be displayed as such and cannot be substituted with parameter");
			}

			index = parametrized.indexOf('@', index + 1);
		}

		return res;

	}

}
