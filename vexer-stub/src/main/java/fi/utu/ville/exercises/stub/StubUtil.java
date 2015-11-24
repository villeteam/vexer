package fi.utu.ville.exercises.stub;

import java.io.File;
import java.io.Serializable;

/**
 * A collection of static utility methods needed in various stages of ViLLE. The main focus is in formatting of dates and numbers. No instances of this class
 * are needed, as all methods are static.
 * 
 * @author Riku Haavisto
 * 
 */
class StubUtil implements Serializable {
	
	private static final long serialVersionUID = 7942926019637649010L;
	
	/**
	 * Not to be used: Use static methods only
	 */
	private StubUtil() {
	
	}
	
	/**
	 * Deletes files contained in the directory and possibly the directory itself.
	 * 
	 * NOTE: this implementation would very likely fail with sym-links.
	 * 
	 * @param path
	 *            path of the directory to delete
	 */
	public static void deleteDirectory(File path, boolean deleteBaseDirectory) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					deleteDirectory(file, true);
				} else {
					file.delete();
				}
			}
		}
		// this is true in all the recursive calls, so only the real
		// base-directory
		// might remain undeleted
		if (deleteBaseDirectory) {
			path.delete();
		}
	}
	
}
