package fi.utu.ville.exercises.helpers;

public class VilleErrorReporter {
	
	private static VilleErrorReporterStrategy strategy = new StubErrorHandler();
	
	/**
	 * Set the ErrorRepoerterStrategy you want to use. If not set, this uses {@link StubErrorHandler}.
	 * 
	 * @param strategyToSet
	 */
	public static void setVilleErrorReporterStrategy(VilleErrorReporterStrategy strategyToSet) {
		strategy = strategyToSet;
	}
	
	/**
	 * Reports an error only to ViLLE team via email to villeteam@utu.fi
	 * 
	 * @param description
	 *            any helpful debugging information.
	 */
	public static void reportByMail(String description) {
		strategy.reportByMail(description);
	}
	
	/**
	 * Reports an error only to ViLLE team via email to villeteam@utu.fi
	 * 
	 * @param description
	 *            any helpful debugging information.
	 * @param exception
	 *            the exception that caused this report
	 */
	public static void reportByMail(String description, Throwable exception) {
		strategy.reportByMail(description, exception);
	}
	
	/**
	 * Reports an error only to ViLLE team via email to villeteam@utu.fi
	 * 
	 * @param description
	 *            any helpful debugging information.
	 * @param title
	 *            the title for the email
	 * @param exception
	 *            the exception that caused this report
	 */
	public static void reportByMail(String description, String title, Throwable exception) {
		strategy.reportByMail(description, title, exception);
	}
	
	/**
	 * Reports an error only to ViLLE team via email to villeteam@utu.fi
	 * 
	 * @param description
	 *            any helpful debugging information.
	 * @param title
	 *            the title for the email
	 * @param exception
	 *            the exception that caused this report
	 */
	public static void reportByMail(String description, String title, Throwable exception, VilleErrorMsgInterface type) {
		strategy.reportByMail(description, title, exception, type);
	}
	
	public static void sendFeedbackToVilleTeam(String description, String title, Throwable exception, String... attachments) {
		strategy.sendFeedbackToVilleTeam(description, title, exception, attachments);
	}
	
	public void logError(int messageType, String message, Object obj) {
		strategy.logError(messageType, message, obj);
	}
}
