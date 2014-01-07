package edu.vserver.exercises.stub;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A simple UILanguage dictionary containing keys mapped to their translations.
 * 
 * @author Riku Haavisto
 * 
 */
class UILanguageStub implements Serializable {

	private static final long serialVersionUID = 3846615275324908529L;

	// Thread-safety: instances of this class are immutable (no synchronization
	// needed)
	// as they only expose String-values from the backing map, never the map
	// itself, because Strings are immutable, and because the backing
	// is a private copy of the initial dictionary

	private final Map<String, String> dict;

	public UILanguageStub(Map<String, String> dictionary) {
		dict = new HashMap<String, String>(dictionary);
	}

	public String getTranslation(String key) {

		return dict.get(key);

	}

	/**
	 * Constructs an line by attaching components in correct places into given
	 * line
	 * 
	 * @param explanation
	 *            the explanation string from syntax file
	 * @param components
	 *            components in order
	 * @return new line as a string.
	 */
	public static String constructLine(String explanation, String[] components) {
		for (int i = 0; i < components.length; i++) {
			String toRepl = "@" + i;
			explanation = explanation.replace(toRepl, components[i]);
		}
		return explanation;
	}

	/**
	 * Constructs an line by attaching components in correct places into given
	 * line
	 * 
	 * @param explanation
	 *            the explanation string from syntax file
	 * @param components
	 *            components in order
	 * @return new line as a string.
	 */
	public static String constructLine(String explanation,
			Map<String, String> params) {
		for (Entry<String, String> aParam : params.entrySet()) {
			String toRepl = "@{" + aParam.getKey() + "}";
			explanation = explanation.replace(toRepl, aParam.getValue());
		}
		return explanation;
	}

}
