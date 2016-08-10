package edu.vserver.exercises.math.essentials.layout;

import org.vaadin.jouni.animator.AnimatorProxy;
import org.vaadin.jouni.animator.AnimatorProxy.AnimationListener;
import org.vaadin.jouni.animator.shared.AnimType;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import fi.utu.ville.exercises.helpers.StandardSubmissionType;
import fi.utu.ville.exercises.layout.AssignmentProgressBar;
import fi.utu.ville.exercises.layout.PhasedAssignmentNavigation;
import fi.utu.ville.exercises.model.ExecutionSettings;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon.Icon;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.UIConstants;

public class MathLayout<P extends Problem> extends CssLayout
		implements MathLayoutController {
	
	private static final long serialVersionUID = 2062189376088801532L;
	
	private ExecutionSettings execSettings;
	private AbstractMathExecutor<?, ?, P> exerExecutor;
	public org.vaadin.jouni.animator.AnimatorProxy.Animation aPrev, aNext, aCheck;
	private CssLayout exWrapper;
	private CssLayout animationWrap;
	private final MathAssignmentProgressBar progress;
	private final PhasedAssignmentNavigation navigation;
	private final AnimatorProxy proxy;
	public static final int shiftSize = 2000;
	private final TimeStampHandler timeStampHandler;
	
	public MathLayout(final Localizer localizer,
			final MathExerciseView<P> exView,
			final MathExerciseState<P> exState,
			TimeStampHandler timeStampHandler,
			final ExecutionSettings execSettings) {
		
		this.execSettings = execSettings;
		exView.setLayoutController(this);
		
		navigation = new PhasedAssignmentNavigation(localizer);
		
		exWrapper = new CssLayout();
		exWrapper.setStyleName("math-layout-exer-wrapper");
		initAnimationWrap(exView);
		
		this.timeStampHandler = timeStampHandler;
		
		proxy = new AnimatorProxy();
		
		proxy.addListener(new AnimationListener() {
			@Override
			public void onAnimation(org.vaadin.jouni.animator.AnimatorProxy.AnimationEvent event) {
				if (event.getAnimation() == aNext) {
					exView.clearFields();
					
					navigation.setPrevButtonEnabled(false);
					navigation.setNextButtonEnabled(false);
					
					progress.setCurrentStepCorrect(exState.getCurrentProblem().isCorrect());
					progress.nextStep();
					
					initAnimationWrap(exView);
					
					proxy.animate(animationWrap, AnimType.FADE_IN)
							.setDuration(500)
							.setDelay(20);
					navigation.setCheckButtonEnabled(true);
					navigation.focusCheck();
					
					exView.drawProblem(exState.nextProblem());
					exerExecutor.startMathPerformance(exState.getCurrentProblem());
					
				} else if (event.getAnimation() == aPrev) {
					exView.clearFields();
					
					navigation.setPrevButtonEnabled(false);
					navigation.setNextButtonEnabled(false);
					
					progress.previousStep();
					
					initAnimationWrap(exView);
					
					proxy.animate(animationWrap, AnimType.FADE_IN)
							.setDuration(500)
							.setDelay(20);
					
				} else if (event.getAnimation() == aCheck) {
					exState.tryAnswer(exView.getAnswer(), execSettings.isExam());
					exerExecutor.setUnsubmittedChangesFlag(true);
					
					if (!execSettings.isNoCorrAnswers()) {
						exView.showSolution(exState.getCurrentProblem());
					}
					exerExecutor.finishMathPerformance(exState.getCurrentProblem());
					
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
					
					navigation.setCheckButtonEnabled(false);
					
					if (exState.hasNextProblem()) {
						navigation.setNextButtonEnabled(true);
						navigation.focusNext();
					} else {
						exView.lockControls();
						progress.nextStep(exState.getCurrentProblem().isCorrect());
						
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
						
						submit.addClickListener(e -> {
							exerExecutor.askSubmit(StandardSubmissionType.NORMAL);
							confirm.close();
						});
						
						cancel.addClickListener(e -> confirm.close());
						
						UI.getCurrent().addWindow(confirm);
					}
					
					proxy.animate(animationWrap, AnimType.FADE_IN).setDuration(250)
							.setDelay(50);
					
				}
			}
		});
		
		// Constructors.
		progress = new MathAssignmentProgressBar(exState.getProblemCount(),
				!execSettings.isNoCorrAnswers());
		
		navigation.setNextButtonEnabled(false);
		navigation.focusCheck();
		
		addComponents(progress, navigation, exWrapper, proxy);
		setStyleName("math-layout");
		setSizeFull();
	}
	
	private void initAnimationWrap(MathExerciseView<P> exView) {
		animationWrap = new CssLayout();
		animationWrap.setSizeFull();
		animationWrap.addComponent(exView);
		exWrapper.removeAllComponents();
		exWrapper.addComponent(animationWrap);
	}
	
	public CssLayout getAnimationWrapper() {
		return animationWrap;
	}
	
	public AssignmentProgressBar getProgressBar() {
		return progress;
	}
	
	public void attachExecutor(AbstractMathExecutor<?, ?, P> exerExecutor) {
		this.exerExecutor = exerExecutor;
	}
	
	public void attachControllers(ClickListener prevController,
			ClickListener checkController,
			ClickListener nextController) {
		navigation.addPrevButtonListener(prevController);
		navigation.addCheckButtonListener(checkController);
		navigation.addNextButtonListener(nextController);
	}
	
	public void attachExecSettings(ExecutionSettings execSettings) {
		this.execSettings = execSettings;
	}
	
	public AnimatorProxy getProxy() {
		return proxy;
	}
	
	@Override
	public Button getCheckBtn() {
		return navigation.getCheckButton();
	}
	
	public void reset() {
		navigation.setPrevButtonEnabled(false);
		navigation.setCheckButtonEnabled(true);
		navigation.setNextButtonEnabled(false);
		progress.reset();
	}
	
	@Override
	public void setCheckButtonEnabled(boolean enabled) {
		navigation.setCheckButtonEnabled(enabled);
	}
}
