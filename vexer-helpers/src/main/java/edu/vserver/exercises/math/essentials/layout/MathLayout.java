package edu.vserver.exercises.math.essentials.layout;

import org.vaadin.jouni.animator.AnimatorProxy;
import org.vaadin.jouni.animator.AnimatorProxy.Animation;
import org.vaadin.jouni.animator.AnimatorProxy.AnimationEvent;
import org.vaadin.jouni.animator.AnimatorProxy.AnimationListener;
import org.vaadin.jouni.animator.shared.AnimType;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import fi.utu.ville.exercises.helpers.StandardSubmissionType;
import fi.utu.ville.exercises.model.ExecutionSettings;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon.Icon;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.UIConstants;

public class MathLayout<P extends Problem> extends VerticalLayout
		implements MathLayoutController {
		
	private static final long serialVersionUID = 2062189376088801532L;
	
	private HorizontalLayout hLayout;
	
	private ExecutionSettings execSettings;
	private AbstractMathExecutor<?, ?, P> exerExecutor;
	private final HorizontalLayout buttonBar;
	private final VerticalLayout innerLayout;
	private final Button checkBtn, nextBtn, prevBtn;
	private final MathAssignmentProgressBar progress;
	private final AnimatorProxy proxy;
	public Animation aPrev, aNext, aCheck;
	public static final int shiftSize = 2000;
	private final TimeStampHandler timeStampHandler;
	
	public MathLayout(final Localizer localizer,
			final MathExerciseView<P> exView,
			final MathExerciseState<P> exState,
			TimeStampHandler timeStampHandler,
			final ExecutionSettings execSettings) {
			
		this.execSettings = execSettings;
		exView.setLayoutController(this);
		
		checkBtn = StandardUIFactory.getButton(
				localizer.getUIText(UIConstants.MATH_CHECK), Icon.MATH_CHECK);
				
		nextBtn = StandardUIFactory.getButton(
				localizer.getUIText(UIConstants.MATH_NEXT), Icon.MATH_NEXT);
				
		prevBtn = StandardUIFactory.getButton(
				localizer.getUIText(UIConstants.MATH_PREV), Icon.MATH_PREV);
				
		setFocusAndShortcut(checkBtn);
		
		// innerLayout = new VerticalLayout();
		innerLayout = new VerticalLayout();
		innerLayout.setStyleName("math-layout-margin");
		
		this.timeStampHandler = timeStampHandler;
		
		proxy = new AnimatorProxy();
		
		proxy.addListener(new AnimationListener() {
			@Override
			public void onAnimation(AnimationEvent event) {
				if (event.getAnimation() == aNext) {
					
					exView.clearFields();
					
					prevBtn.setEnabled(false); // Change if browsing is
												// possible.
					nextBtn.setEnabled(false);
					
					progress.nextStep(exState.getCurrentProblem().isCorrect());
					
					HorizontalLayout old = hLayout;
					hLayout = new HorizontalLayout();
					// hLayout.setStyleName("base");
					
					// Math navigation bar
					// - previous button
					// - check button
					// - next button
					
					hLayout.addComponent(innerLayout);
					hLayout.setComponentAlignment(innerLayout,
							Alignment.MIDDLE_CENTER);
					hLayout.setStyleName("makeItTransparent");
					replaceComponent(old, hLayout);
					setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
					setComponentAlignment(buttonBar, Alignment.MIDDLE_LEFT);
					
					proxy.animate(hLayout, AnimType.FADE_IN).setDuration(500)
							.setDelay(20);
					checkBtn.setEnabled(true);
					setFocusAndShortcut(checkBtn);
					exView.drawProblem(exState.nextProblem());
					
					exerExecutor
							.startMathPerformance(exState.getCurrentProblem());
							
				} else if (event.getAnimation() == aPrev) {
					
					exView.clearFields();
					prevBtn.setEnabled(false); // Change if browsing is
												// possible.
					nextBtn.setEnabled(false);
					// TODO Fetch previous solution / math exercise.
					HorizontalLayout old = hLayout;
					hLayout = new HorizontalLayout();
					hLayout.setStyleName("base");
					
					// buttonBar.addComponent(prevBtn);
					hLayout.addComponent(innerLayout);
					hLayout.setComponentAlignment(innerLayout,
							Alignment.MIDDLE_CENTER);
					hLayout.setStyleName("makeItTransparent");
					
					replaceComponent(old, hLayout);
					setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
					
					proxy.animate(hLayout, AnimType.FADE_IN).setDuration(500)
							.setDelay(20);
							
				} else if (event.getAnimation() == aCheck) {
					exState.tryAnswer(exView.getAnswer(), execSettings.isExam());
					exerExecutor.setUnsubmittedChangesFlag(true);
					
					if (!execSettings.isNoCorrAnswers()) {
						exView.showSolution(exState.getCurrentProblem());
					}
					exerExecutor
							.finishMathPerformance(exState.getCurrentProblem());
							
					MathLayout.this.timeStampHandler
							.add("Question" + TimeStampHandler.separator
									+ exState.getCurrentProblem().getQuestion(
											localizer)
									+ TimeStampHandler.separator + "UserAnswer"
									+ TimeStampHandler.separator
									+ exState.getCurrentProblem().getUserAnswer()
									+ TimeStampHandler.separator + "CorrectAnswer"
									+ TimeStampHandler.separator
									+ exState.getCurrentProblem().getCorrectAnswer()
									+ TimeStampHandler.separator + "isCorrect"
									+ TimeStampHandler.separator
									+ exState.getCurrentProblem().isCorrect());
									
					checkBtn.setEnabled(false);
					
					if (exState.hasNextProblem()) {
						nextBtn.setEnabled(true);
						setFocusAndShortcut(nextBtn);
					} else {
						exView.lockControls();
						progress.nextStep(
								exState.getCurrentProblem().isCorrect());
								
						final Window confirm = new Window(localizer
								.getUIText(UIConstants.SUBMIT_QUESTION));
						confirm.setModal(true);
						confirm.setWidth("600px");
						confirm.setHeight("330px");
						confirm.setCaption("");
						confirm.setStyleName("opaque");
						confirm.addStyleName("unclosable-window");
						confirm.setClosable(false);
						confirm.addStyleName("submit-window");
						
						VerticalLayout content = new VerticalLayout();
						content.setSizeFull();
						content.setMargin(true);
						Label question = new Label(localizer
								.getUIText(UIConstants.SUBMIT_QUESTION));
						question.setContentMode(ContentMode.HTML);
						question.addStyleName("big-text-white");
						question.setSizeUndefined();
						content.addComponent(question);
						content.setComponentAlignment(question,
								Alignment.TOP_CENTER);
						// content.addComponent(StandardUIFactory
						// .getInformationPanel(localizer.getUIText(
						// UIConstants.SUBMIT_QUESTION_DESCR)));
						confirm.setContent(content);
						
						// Button holder (back to round, replay, next)
						HorizontalLayout buttons = new HorizontalLayout();
						buttons.addStyleName("submit-buttons-container");
						buttons.setWidth("100%");
						buttons.setSpacing(true);
						
						content.addComponent(buttons);
						content.setComponentAlignment(buttons,
								Alignment.TOP_CENTER);
								
						Button submit = StandardUIFactory
								.getRoundButton(Icon.SUBMIT);
						submit.setDescription(
								localizer.getUIText(UIConstants.SUBMIT));
						Button cancel = StandardUIFactory
								.getRoundButton(Icon.CLOSE);
						cancel.setDescription(
								localizer.getUIText(UIConstants.CLOSE));
								
						buttons.addComponents(submit, cancel);
						buttons.setComponentAlignment(submit,
								Alignment.MIDDLE_CENTER);
						buttons.setComponentAlignment(cancel,
								Alignment.MIDDLE_CENTER);
								
						submit.addClickListener(new Button.ClickListener() {
							
							private static final long serialVersionUID = -1372389156569716860L;
							
							@Override
							public void buttonClick(ClickEvent event) {
								
								exerExecutor.askSubmit(
										StandardSubmissionType.NORMAL);
										
								// listenerHelper.informOnlySubmit(checkCorrectness(),
								// parseSubmissionInfo(),
								// StandardSubmissionType.NORMAL,
								// null);
								confirm.close();
							}
						});
						
						cancel.addClickListener(new Button.ClickListener() {
							
							/**
							 * 
							 */
							private static final long serialVersionUID = 6537215144432359150L;
							
							@Override
							public void buttonClick(ClickEvent event) {
								confirm.close();
							}
						});
						
						UI.getCurrent().addWindow(confirm);
					}
					
					proxy.animate(exView, AnimType.FADE_IN).setDuration(250)
							.setDelay(50);
							
				}
			}
		});
		
		// Constructors.
		progress = new MathAssignmentProgressBar(exState.getProblemCount(),
				!execSettings.isNoCorrAnswers());
				
		hLayout = new HorizontalLayout();
		buttonBar = StandardUIFactory.getButtonPanelForMathControls(checkBtn,
				nextBtn, progress);
				
		prevBtn.setEnabled(false);
		nextBtn.setEnabled(false);
		
		// Component hierarchy.
		innerLayout.addComponent(exView);
		addComponent(proxy);
		// hLayout.setStyleName("base");
		
		// hLayout.addComponent(prevBtn);
		addComponent(buttonBar);
		hLayout.addComponent(innerLayout);
		// hLayout.addComponent(nextBtn);
		addComponent(hLayout);
		
		// Alignment properties.
		innerLayout.setComponentAlignment(exView, Alignment.MIDDLE_CENTER);
		hLayout.setComponentAlignment(innerLayout, Alignment.MIDDLE_CENTER);
		setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
	}
	
	/**
	 * Removes all ClickShortcuts for the check and next buttons. Adds Enter as the ClickShortcut for the given button.
	 * 
	 * @param b
	 *            the button to add Enter as a ClickShortcut. f null, all ClickShortcuts are removed
	 */
	private void setFocusAndShortcut(Button b) {
		checkBtn.removeClickShortcut();
		nextBtn.removeClickShortcut();
		if (b != null) {
			b.setClickShortcut(KeyCode.ENTER);
			b.focus();
		}
	}
	
	public MathAssignmentProgressBar getProgressBar() {
		return progress;
	}
	
	public void attachExecutor(AbstractMathExecutor<?, ?, P> exerExecutor) {
		this.exerExecutor = exerExecutor;
	}
	
	public void attachControllers(PrevButtonListener prevController,
			CheckButtonListener checkController,
			NextButtonListener nextController) {
		prevBtn.addClickListener(prevController);
		checkBtn.addClickListener(checkController);
		nextBtn.addClickListener(nextController);
		// opt2.addListener(controller);
	}
	
	public void attachExecSettings(ExecutionSettings execSettings) {
		this.execSettings = execSettings;
	}
	
	public Layout getInnerLayout() {
		return innerLayout;
	}
	
	public Layout getHLayout() {
		return hLayout;
	}
	
	public AnimatorProxy getProxy() {
		return proxy;
	}
	
	public Button getPrevBtn() {
		return prevBtn;
	}
	
	public Button getNextBtn() {
		return nextBtn;
	}
	
	@Override
	public Button getCheckBtn() {
		return checkBtn;
	}
	
	public void reset() {
		prevBtn.setEnabled(false);
		checkBtn.setEnabled(true);
		nextBtn.setEnabled(false);
		progress.reset();
	}
	
	@Override
	public void setCheckButtonEnabled(boolean enabled) {
		checkBtn.setEnabled(enabled);
	}
}
