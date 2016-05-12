package fi.utu.ville.exercises.helpers;

/**
 * Concrete implementation for reporting errors through {@link VilleErrorReporterStrategy}. Only "reports" the errors to console.</br>
 * VServer has another implementation for this that really reports the error by mail.
 * 
 * @author tosata
 *		
 */
public class StubErrorHandler implements VilleErrorReporterStrategy {
	
	@Override
	public void reportByMail(String description) {
		reportErrorToVilleTeam(description, "SuppressedError", null, null);
	}
	
	@Override
	public void reportByMail(String description, Throwable exception) {
		reportErrorToVilleTeam(description, "SuppressedError", exception, null);
	}
	
	@Override
	public void reportByMail(String description, String title, Throwable exception) {
		reportErrorToVilleTeam(description, title, exception, null);
	}
	
	@Override
	public void reportByMail(String description, String title, Throwable exception, VilleErrorMsgInterface type) {
		reportErrorToVilleTeam(description, title, exception, type);
	}
	
	@Override
	public void sendFeedbackToVilleTeam(String description, String title, Throwable exception, String... attachments) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void logError(int messageType, String message, Object obj) {
		// TODO Auto-generated method stub
		
	}
	
	private void reportErrorToVilleTeam(String description, String title, Throwable exception, VilleErrorMsgInterface type) {
		// TODO Auto-generated method stub
		System.out.println("Error message: " + description + title + exception.getMessage());
		
	}
	
}
