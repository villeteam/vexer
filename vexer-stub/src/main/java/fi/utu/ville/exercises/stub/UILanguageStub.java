package fi.utu.ville.exercises.stub;

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
	// itself, because Strings are immutable, and because the backing map
	// is a private copy of the initial dictionary

	// thread-safety is quite irrelevant when used in stub

	private final Map<String, String> dict;

	/**
	 * Constructs a new {@link UILanguageStub} containing translations from
	 * given dictionary.
	 * 
	 * @param dictionary
	 *            translations to use in this {@link UILanguageStub}
	 */
	public UILanguageStub(Map<String, String> dictionary) {
		dict = new HashMap<String, String>(dictionary);
	}

	/**
	 * Return a translation for given key
	 * 
	 * @param key
	 *            key representing some UI-constant
	 * @return translation for the given key in current dictionary, or null if
	 *         no translation exists
	 */
	public String getTranslation(String key) {
		return dict.get(key);
	}

	/**
	 * Constructs an line by attaching components in correct places into given
	 * line
	 * 
	 * @param baseString
	 *            the base translation with parameter place-holders
	 * @param parameters
	 *            parameters in order
	 * @return basestring from which the place-holders are replaced with
	 *         parameters
	 */
	public static String constructLine(String baseString, String[] parameters) {
		for (int i = 0; i < parameters.length; i++) {
			String toRepl = "@" + i;
			baseString = baseString.replace(toRepl, parameters[i]);
		}
		return baseString;
	}

	/**
	 * Constructs an line by substituting
	 * 
	 * @param baseString
	 *            the explanation string from syntax file
	 * @param parameters
	 *            map of parameters to substitute for place-holders
	 * @return basestring from which the place-holders are replaced with
	 *         parameters
	 */
	public static String constructLine(String baseString,
			Map<String, String> params) {
		for (Entry<String, String> aParam : params.entrySet()) {
			String toRepl = "@{" + aParam.getKey() + "}";
			baseString = baseString.replace(toRepl, aParam.getValue());
		}
		return baseString;
	}

}
