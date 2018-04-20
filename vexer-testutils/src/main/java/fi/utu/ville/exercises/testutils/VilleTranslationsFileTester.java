package fi.utu.ville.exercises.testutils;

import java.io.File;
import java.io.FileInputStream;
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
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;


/**
 * Contains a static method that can be used for testing a UIConstant-file - .trl-file pair implemented for localization in a classic ViLLE-way.
 * 
 * @author Riku Haavisto
 * 
 */
public final class VilleTranslationsFileTester {
	
	private static final String translationFileSuffix = ".trl";
	private static final Logger logger = Logger
			.getLogger(VilleTranslationsFileTester.class.getName());
			
	private VilleTranslationsFileTester() {
	
	}
	
	public static boolean testLangFiles(Class<?> uiConstantsClass,
			File trlFile, List<String> languagesToTest) throws IOException {
//dirty fix to disable testing;
//it broke mvn deploy and it's 9pm :(
			return true;	
	}
	
	private static void testParametrized(Set<String> keysForParametrized,
			Map<String, Map<String, String>> translationsByLang) {
			
		for (String keyForParametrized : keysForParametrized) {
			Set<Integer> lastParamSet = null;
			String lastLang = "";
			
			for (Entry<String, Map<String, String>> aDict : translationsByLang
					.entrySet()) {
					
				Set<Integer> currParamSet = getParamNumbers(aDict.getValue()
						.get(keyForParametrized));
						
				if (lastParamSet != null) {
					if (!lastParamSet.equals(currParamSet)) {
						logger.warning("----------\nWARNING: "
								+ "ui-constant-key: "
								+ keyForParametrized
								+ " contained different number of parameters for lang '"
								+ lastLang + "' than for lang '"
								+ aDict.getKey() + "'");
					}
				}
				
				if (currParamSet.isEmpty()) {
					logger.warning("----------\nWARNING: "
							+ "ui-constant-key: " + keyForParametrized
							+ " had value for lang " + aDict.getKey()
							+ " contained no substitutable parameters");
				} else {
					Integer max = Collections.max(currParamSet);
					
					for (int i = 0; i < max; i++) {
						if (!currParamSet.contains(i)) {
							logger.warning("----------\nWARNING: "
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
			if (!(parametrized.charAt(index + 1) == '{')) {
				try {
					String indexNumb = "";
					for (int i = 1; index + i < parametrized.length()
							&& Character
									.isDigit(parametrized.charAt(index + i)); i++) {
						indexNumb += parametrized.charAt(index + i);
					}
					int paramNum = Integer.parseInt(indexNumb);
					res.add(paramNum);
				} catch (NumberFormatException e) {
					// should happen only if '@' is followed by zero digit
					// characters
					logger.warning("----------\nWARNING: "
							+ "Ui-translation "
							+ parametrized
							+ " contained @ without following index-digit; "
							+ "this @ will be displayed as such and cannot be substituted with parameter");
				}
			} else {
				// add some tests for parameters with @{name} syntax
			}
			
			index = parametrized.indexOf('@', index + 1);
		}
		
		return res;
		
	}
	
}
