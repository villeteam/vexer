package fi.utu.ville.exercises.helpers;

public interface VilleErrorReporterStrategy {
	
	/**
	 * Reports an error only to ViLLE team via email to villeteam@utu.fi
	 * 
	 * @param description
	 *            any helpful debugging information.
	 */
	public void reportByMail(String description);
	
	/**
	 * Reports an error only to ViLLE team via email to villeteam@utu.fi
	 * 
	 * @param description
	 *            any helpful debugging information.
	 * @param exception
	 *            the exception that caused this report
	 */
	public void reportByMail(String description, Throwable exception);
	
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
	public void reportByMail(String description, String title, Throwable exception);
	
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
	public void reportByMail(String description, String title, Throwable exception, VilleErrorMsgInterface type);
	
	public void sendFeedbackToVilleTeam(String description, String title, Throwable exception, String... attachments);
	
	public void logError(int messageType, String message, Object obj);
}
