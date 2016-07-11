package edu.vserver.exercises.math.essentials.level;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;

import edu.vserver.misconception.MisconceptionPerformanceSubject;
import fi.utu.ville.exercises.helpers.ExerciseExecutionHelper;
import fi.utu.ville.exercises.helpers.VilleErrorReporter;
import fi.utu.ville.exercises.model.ExecutionSettings;
import fi.utu.ville.exercises.model.ExecutionState;
import fi.utu.ville.exercises.model.ExecutionStateChangeListener;
import fi.utu.ville.exercises.model.Executor;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.ResetListener;
import fi.utu.ville.exercises.model.SubmissionListener;
import fi.utu.ville.exercises.model.SubmissionType;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.MathIcons;
import fi.utu.ville.standardutils.MathUIFactory;
import fi.utu.ville.standardutils.StandardUIConstants;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.TempFilesManager;
import fi.utu.ville.standardutils.UIConstants;

public class LevelMathExecutorWrapper<E extends ExerciseData, S extends LevelSubmissionInfo>
		extends CssLayout implements Executor<LevelMathDataWrapper<E>, S> {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -7363354799686763767L;
	
	private final Executor<E, S> realExecutor;
	
	private LevelMathDataWrapper<E> allLevels;
	
	private S submInfo;
	
	private boolean initialized = false;
	
	private boolean levelSelected = false;
	
	private int difficultyIndicator = 0;
	
	private final ExerciseExecutionHelper<S> realListeners = new ExerciseExecutionHelper<>();
	
	public LevelMathExecutorWrapper(Executor<E, S> realExecutor) {
		super();
		this.realExecutor = realExecutor;
	}
	
	private Button easy;
	private Button normal;
	private Button hard;
	
	private Localizer localizer;
	
	private ExecutionSettings execSettings;
	
	private final ClickListener cl = new ClickListener() {
		
		private static final long serialVersionUID = 4026684080223114316L;
		
		@Override
		public void buttonClick(ClickEvent event) {
			final DiffLevel usedLevel;
			levelSelected = true;
			
			if (event.getButton().equals(easy)) {
				usedLevel = DiffLevel.EASY;
				difficultyIndicator = 1;
			} else if (event.getButton().equals(normal)) {
				usedLevel = DiffLevel.NORMAL;
				difficultyIndicator = 2;
			} else {
				usedLevel = DiffLevel.HARD;
				difficultyIndicator = 3;
			}
			
			loadRealExercise(usedLevel);
			
			realExecutor.registerResetListener(() -> {
				realListeners.informResetDefault();
				
			});
			
			realExecutor.registerSubmitListener(submission -> {
				submission.getSubmissionInfo().setDiffLevel(usedLevel);
				realListeners.informOnlySubmit(submission);
			});
			
		}
	};
	
	@Override
	public void initialize(Localizer localizer, LevelMathDataWrapper<E> data,
			S submInfo, TempFilesManager tempMan, ExecutionSettings execSettings) {
		this.execSettings = execSettings;
		this.localizer = localizer;
		this.submInfo = submInfo;
		allLevels = data;
		
		easy = MathUIFactory.getStarButton(
				localizer.getUIText(UIConstants.LEVEL_EASY), localizer);
		easy.setIcon(MathIcons.getIcon(MathIcons.STAR_EASY));
		normal = MathUIFactory.getStarButton(
				localizer.getUIText(UIConstants.LEVEL_NORMAL), localizer);
		normal.setIcon(MathIcons.getIcon(MathIcons.STAR_NORMAL));
		hard = MathUIFactory.getStarButton(
				localizer.getUIText(UIConstants.LEVEL_HARD), localizer);
		hard.setIcon(MathIcons.getIcon(MathIcons.STAR_HARD));
		
		List<Button> buttonList = new ArrayList<>();
		buttonList.add(easy);
		buttonList.add(normal);
		buttonList.add(hard);
		
		CssLayout buttons = new CssLayout();
		buttons.setStyleName("math-levelbutton-wrapper");
		
		buttonList.forEach(b -> {
			b.addClickListener(cl);
			b.addStyleName("math-levelbutton");
			buttons.addComponent(b);
		});
		
		Label header = new Label(localizer.getUIText(UIConstants.CHOOSE_LEVEL));
		header.addStyleName("math-h1");
		header.setSizeUndefined();
		
		addComponents(header, buttons);
	}
	
	@Override
	public void attach() {
		super.attach();
		addStyleName("level-math-executor-wrapper");
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
			realListeners.getState().setCanReset(false);
			realListeners.getState().setCanSubmit(false);
			realListeners.informStateListeners();
			VilleErrorReporter.reportByMail("", e);
			addComponent(StandardUIFactory.getWarningPanel(localizer
					.getUIText(StandardUIConstants.EXERCISE_LOAD_ERROR)));
		}
		
	}
	
	@Override
	public void registerSubmitListener(SubmissionListener<S> submitListener) {
		realListeners.registerSubmitListener(submitListener);
	}
	
	@Override
	public void registerResetListener(ResetListener resetListener) {
		realListeners.registerResetListener(resetListener);
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
	
	/**
	 * 
	 * @return 1 = easy, 2 = medium, 3 = hard
	 */
	public int getDifficulty() {
		return difficultyIndicator;
	}
	
}
