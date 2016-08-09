package fi.utu.ville.exercises.layout;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;

import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon.Icon;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.UIConstants;

public class PhasedAssignmentNavigation extends CssLayout {
	
	private final Button prevButton, checkButton, nextButton;
	
	private final Set<ClickListener> prevButtonListeners = new HashSet<>();
	private final Set<ClickListener> checkButtonListeners = new HashSet<>();
	private final Set<ClickListener> nextButtonListeners = new HashSet<>();
	
	public PhasedAssignmentNavigation(Localizer localizer) {
		this(localizer, false);
	}
	
	public PhasedAssignmentNavigation(Localizer localizer, boolean showPrevButton) {
		if (Page.getCurrent().getWebBrowser().isTouchDevice()) {
			String postfix = "_SHORT";
			prevButton = new Button(localizer.getUIText(UIConstants.MATH_PREV + postfix));
			checkButton = new Button(localizer.getUIText(UIConstants.MATH_CHECK + postfix));
			nextButton = new Button(localizer.getUIText(UIConstants.MATH_NEXT + postfix));
		} else {
			prevButton = StandardUIFactory.getButton(
					localizer.getUIText(UIConstants.MATH_PREV), Icon.MATH_PREV);
			checkButton = StandardUIFactory.getButton(
					localizer.getUIText(UIConstants.MATH_CHECK), Icon.MATH_CHECK);
			nextButton = StandardUIFactory.getButton(
					localizer.getUIText(UIConstants.MATH_NEXT), Icon.MATH_NEXT);
		}
		prevButton.addClickListener(e -> runPrevButtonListeners(e));
		checkButton.addClickListener(e -> runCheckButtonListeners(e));
		nextButton.addClickListener(e -> runNextButtonListeners(e));
		
		setPrevButtonVisible(showPrevButton);
		
		addComponents(prevButton, checkButton, nextButton);
		setStyleName("phased-assig-nav-bar");
		if (Page.getCurrent().getWebBrowser().isTouchDevice()) {
			addStyleName("mobile");
		}
	}
	
	public void setPrevButtonVisible(boolean visible) {
		prevButton.setVisible(visible);
	}
	
	public boolean isPrevButtonVisible() {
		return prevButton.isVisible();
	}
	
	public void setPrevButtonEnabled(boolean enabled) {
		prevButton.setEnabled(enabled);
	}
	
	public void setCheckButtonVisible(boolean visible) {
		checkButton.setVisible(visible);
	}
	
	public boolean isCheckButtonVisible() {
		return checkButton.isVisible();
	}
	
	public void setCheckButtonEnabled(boolean enabled) {
		checkButton.setEnabled(enabled);
	}
	
	public void setNextButtonEnabled(boolean enabled) {
		nextButton.setEnabled(enabled);
	}
	
	public void focusPrev() {
		focusButton(prevButton);
	}
	
	public void focusCheck() {
		focusButton(checkButton);
	}
	
	public void focusNext() {
		focusButton(nextButton);
	}
	
	private void focusButton(Button b) {
		prevButton.removeClickShortcut();
		checkButton.removeClickShortcut();
		nextButton.removeClickShortcut();
		b.setClickShortcut(KeyCode.ENTER);
		b.focus();
	}
	
	public void removeClickShortcuts() {
		prevButton.removeClickShortcut();
		checkButton.removeClickShortcut();
		nextButton.removeClickShortcut();
	}
	
	/**
	 * Returns the check button, needed to keep backwards compatibility with MathLayout
	 *
	 * @return Check button
	 */
	public Button getCheckButton() {
		return checkButton;
	}
	
	public void addPrevButtonListener(ClickListener l) {
		prevButtonListeners.add(l);
	}
	
	public void addCheckButtonListener(ClickListener l) {
		checkButtonListeners.add(l);
	}
	
	public void addNextButtonListener(ClickListener l) {
		nextButtonListeners.add(l);
	}
	
	private void runPrevButtonListeners(ClickEvent e) {
		prevButtonListeners.forEach(l -> l.buttonClick(e));
	}
	
	private void runCheckButtonListeners(ClickEvent e) {
		checkButtonListeners.forEach(l -> l.buttonClick(e));
	}
	
	private void runNextButtonListeners(ClickEvent e) {
		nextButtonListeners.forEach(l -> l.buttonClick(e));
	}
	
}
