package edu.vserver.exercises.stub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A collection of static utility methods needed in various stages of ViLLE. The
 * main focus is in formatting of dates and numbers. No instances of this class
 * are usually needed, as all methods are static.
 * 
 * @author Riku Haavisto
 * 
 */
class StubUtil implements Serializable {

	private static final Logger logger = Logger.getLogger(StubUtil.class
			.getName());

	private static final long serialVersionUID = 7942926019637649010L;

	/**
	 * Not to be used: Use static methods only
	 */
	private StubUtil() {

	}

	public static void copyFile(File in, File out) throws IOException {
		FileInputStream inStream = new FileInputStream(in);
		FileOutputStream outStream = new FileOutputStream(out);

		FileChannel inChannel = inStream.getChannel();
		FileChannel outChannel = outStream.getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			throw e;
		} finally {
			inStream.close();
			outStream.close();
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	}

	public static void writeStreamToFile(InputStream toWrite, File file)
			throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);

			byte buf[] = new byte[1024];
			int len;
			while ((len = toWrite.read(buf)) > 0)
				out.write(buf, 0, len);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.log(Level.WARNING, "Error closing stream", e);
				}
			}
			if (toWrite != null) {
				try {
					toWrite.close();
				} catch (IOException e) {
					logger.log(Level.WARNING, "Error closing stream", e);
				}
			}
		}
	}

	/**
	 * Deletes files contained in the directory and possibly directory itself.
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
