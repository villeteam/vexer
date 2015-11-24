package edu.vserver.exercises.math.essentials.level;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.misconception.MisconceptionPerformanceSubject;
import fi.utu.ville.exercises.helpers.ExerciseExecutionHelper;
import fi.utu.ville.exercises.model.ExecutionSettings;
import fi.utu.ville.exercises.model.ExecutionState;
import fi.utu.ville.exercises.model.ExecutionStateChangeListener;
import fi.utu.ville.exercises.model.Executor;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.SubmissionListener;
import fi.utu.ville.exercises.model.SubmissionResult;
import fi.utu.ville.exercises.model.SubmissionType;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.MathIcons;
import fi.utu.ville.standardutils.MathUIFactory;
import fi.utu.ville.standardutils.StandardUIConstants;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.TempFilesManager;
import fi.utu.ville.standardutils.UIConstants;

public class LevelMathExecutorWrapper<E extends ExerciseData, S extends LevelSubmissionInfo>
		extends VerticalLayout implements Executor<LevelMathDataWrapper<E>, S> {
		
	/**
	 *
	 */
	private static final long serialVersionUID = -7363354799686763767L;
	
	private final Executor<E, S> realExecutor;
	
	private LevelMathDataWrapper<E> allLevels;
	
	private S submInfo;
	
	private boolean initialized = false;
	
	private boolean levelSelected = false;
	
	private final ExerciseExecutionHelper<S> realListeners = new ExerciseExecutionHelper<S>();
	
	public LevelMathExecutorWrapper(Executor<E, S> realExecutor) {
		super();
		this.realExecutor = realExecutor;
	}
	
	private Button easy;
	private Button normal;
	private Button hard;
	
	private Localizer localizer;
	
	private ExecutionSettings execSettings;
	
	private final ClickListener cl = new Button.ClickListener() {
		
		/**
		 *
		 */
		private static final long serialVersionUID = 4026684080223114316L;
		
		@Override
		public void buttonClick(ClickEvent event) {
			final DiffLevel usedLevel;
			levelSelected = true;
			
			if (event.getButton().equals(easy)) {
				usedLevel = DiffLevel.EASY;
			} else if (event.getButton().equals(normal)) {
				usedLevel = DiffLevel.NORMAL;
			} else {
				usedLevel = DiffLevel.HARD;
			}
			
			loadRealExercise(usedLevel);
			
			realExecutor.registerSubmitListener(new SubmissionListener<S>() {
				
				/**
				 *
				 */
				private static final long serialVersionUID = -1277278707017051715L;
				
				@Override
				public void submitted(SubmissionResult<S> submission) {
					submission.getSubmissionInfo().setDiffLevel(usedLevel);
					realListeners.informOnlySubmit(submission);
				}
				
			});
			
		}
	};
	
	@Override
	public void initialize(Localizer localizer, LevelMathDataWrapper<E> data,
			S submInfo, TempFilesManager tempMan, ExecutionSettings execSettings) {
			
		setMargin(true);
		this.execSettings = execSettings;
		this.localizer = localizer;
		this.submInfo = submInfo;
		
		// addComponent("levelin valitsin");
		easy = MathUIFactory.getStarButton(
				localizer.getUIText(UIConstants.LEVEL_EASY), localizer);
		normal = MathUIFactory.getStarButton(
				localizer.getUIText(UIConstants.LEVEL_NORMAL), localizer);
		hard = MathUIFactory.getStarButton(
				localizer.getUIText(UIConstants.LEVEL_HARD), localizer);
				
		allLevels = data;
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		
		easy.setIcon(MathIcons.getIcon(MathIcons.STAR_EASY));
		normal.setIcon(MathIcons.getIcon(MathIcons.STAR_NORMAL));
		hard.setIcon(MathIcons.getIcon(MathIcons.STAR_HARD));
		
		easy.addClickListener(cl);
		normal.addClickListener(cl);
		hard.addClickListener(cl);
		
		easy.addStyleName("math-levelbutton");
		normal.addStyleName("math-levelbutton");
		hard.addStyleName("math-levelbutton");
		
		buttons.addComponent(easy);
		buttons.addComponent(normal);
		buttons.addComponent(hard);
		buttons.setHeight("256px");
		
		Label header = new Label(
				localizer.getUIText(UIConstants.CHOOSE_LEVEL));
		header.addStyleName("math-h1");
		header.setSizeUndefined();
		
		addComponent(header);
		addComponent(buttons);
		setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
		setComponentAlignment(header, Alignment.MIDDLE_CENTER);
		
		buttons.setComponentAlignment(easy, Alignment.MIDDLE_CENTER);
		buttons.setComponentAlignment(normal, Alignment.MIDDLE_CENTER);
		buttons.setComponentAlignment(hard, Alignment.MIDDLE_CENTER);
		
		// some logic and ui to choose correct level
	}
	
	private void loadRealExercise(DiffLevel corrLevel) {
		removeAllComponents();
		initialized = true;
		realListeners.getState().setCanReset(true);
		realListeners.getState().setCanSubmit(true);
		realListeners.informStateListeners();
		try {
			realExecutor.initialize(localizer,
					allLevels.getForLevel(corrLevel), submInfo, null,
					execSettings);
			addComponent(realExecutor.getView());
		} catch (ExerciseException e) {
			e.printStackTrace();
			addComponent(StandardUIFactory.getWarningPanel(localizer
					.getUIText(StandardUIConstants.EXERCISE_LOAD_ERROR)));
		}
		
	}
	
	@Override
	public void registerSubmitListener(SubmissionListener<S> submitListener) {
		realListeners.registerSubmitListener(submitListener);
	}
	
	@Override
	public Layout getView() {
		return this;
	}
	
	@Override
	public void shutdown() {
		if (initialized) {
			realExecutor.shutdown();
		}
	}
	
	@Override
	public void askReset() {
		if (levelSelected) {
			realExecutor.askReset();
			
		}
	}
	
	@Override
	public void askSubmit(SubmissionType submtype) {
		if (levelSelected) {
			realExecutor.askSubmit(submtype);
		}
	}
	
	@Override
	public void registerExecutionStateChangeListener(
			ExecutionStateChangeListener execStateListener) {
		realListeners.registerExerciseExecutionStateListener(execStateListener);
		realExecutor.registerExecutionStateChangeListener(execStateListener);
		if (!initialized) {
			realListeners.getState().setCanReset(false);
			realListeners.getState().setCanSubmit(false);
			realListeners.informStateListeners();
		}
	}
	
	@Override
	public ExecutionState getCurrentExecutionState() {
		return realExecutor.getCurrentExecutionState();
	}
	
	@Override
	public void setMisconceptionSubject(
			MisconceptionPerformanceSubject misconceptionSubject) {
		realExecutor.setMisconceptionSubject(misconceptionSubject);
	}
	
	@Override
	public MisconceptionPerformanceSubject getMisconceptionSubject() {
		return realExecutor.getMisconceptionSubject();
	}
	
	public void setHardButtonEnabled(boolean value) {
		hard.setEnabled(value);
	}
	
}
