package fi.utu.ville.exercises.model;

public interface VilleUI {

	public boolean isOkToExit();

	public boolean changeContent(VilleContent content);

	public void pushCurrentViewToStack();

	public VilleContent restorePreviousViewFromStack();

	public void doLayout();

	public void clearPageStack();

	public void setHelpPage(String page);
	
	public DialogWithConfirm getConfirmExitDialog();
}
