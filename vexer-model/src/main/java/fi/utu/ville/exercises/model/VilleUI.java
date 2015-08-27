package fi.utu.ville.exercises.model;

public interface VilleUI {

	/**
	 * Determines whether it is allowed to move away from the current view.
	 * @return True if there are changes that would be lost if the view is changed. 
	 * 		False if no changes will be lost or any changes made should not be saved
	 */
	public boolean isOkToExit();

	/**
	 * Change the current content. The user will be queried whether to discard any possible changed.
	 * @param content the content to change to
	 * @return true if the content change was successful, false otherwise.
	 */
	public boolean changeContent(VilleContent content);
	
	/**
	 * Changes the current content. 
	 * @param content The new content.
	 * @param forceChange if true, the user will not be prompted to save any changes made. If false, acts the same as the changeContent(VilleContent content) method. 
	 * @return true if the content change was successful, false otherwise.
	 */
	public boolean changeContent(VilleContent content, boolean forceChange);

	/**
	 * Saves the current view in the view history stack.
	 */
	public void pushCurrentViewToStack();

	/**
	 * Restores the previous view from the history stack.
	 * @return The previous view.
	 */
	public VilleContent restorePreviousViewFromStack();

	/**
	 * Draws the UI.
	 */
	public void doLayout();

	/**
	 * Clears all view history.
	 */
	public void clearPageStack();

	/**
	 * @return A String representation of the current page stack + the current page.
	 */
	public String getPageStack();
	
	/**
	 * Sets the help page to be shown in the help menu.
	 * @param page A valid url for the help page.
	 */
	public void setHelpPage(String page);
	
	/**
	 * Get the confirm dialog which is shown when the user attempts to leave the view but has changes that should be saved.
	 * @return A confirm dialog.
	 */
	public DialogWithConfirm getConfirmExitDialog();
}
