package fi.utu.ville.exercises.stub;

import fi.utu.ville.exercises.model.DialogWithConfirm;
import fi.utu.ville.exercises.model.VilleContent;
import fi.utu.ville.exercises.model.VilleUI;

public class StubUI implements VilleUI {
	
	@Override
	public boolean isOkToExit() {
		return false;
	}
	
	@Override
	public boolean changeContent(VilleContent content) {
		return false;
	}
	
	@Override
	public void pushCurrentViewToStack() {
	}
	
	@Override
	public VilleContent restorePreviousViewFromStack() {
		return null;
	}
	
	@Override
	public void doLayout() {
	
	}
	
	@Override
	public void clearPageStack() {
	
	}
	
	@Override
	public void setHelpPage(String page) {
	
	}
	
	@Override
	public DialogWithConfirm getConfirmExitDialog() {
		return new DialogWithConfirm() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isOk() {
				return true;
			}
		};
	}
	
	@Override
	public boolean changeContent(VilleContent content, boolean forceChange) {
		return false;
	}
	
	@Override
	public String getPageStack() {
		return "";
	}
	@Override
	public void setHeaderBarEnabled(boolean enabled) {

	}
	@Override
	public void setBackButtonVisible(boolean visible) {

	}
}
