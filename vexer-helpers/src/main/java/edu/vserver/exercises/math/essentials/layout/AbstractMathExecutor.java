package edu.vserver.exercises.math.essentials.layout;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;

import edu.vserver.misconception.MisconceptionData;
import edu.vserver.misconception.MisconceptionPerformanceData;
import edu.vserver.misconception.MisconceptionPerformanceSubject;
import edu.vserver.misconception.MisconceptionTypeData;
import edu.vserver.misconception.NullMisconceptionPerformanceData;
import fi.utu.ville.exercises.helpers.ExerciseExecutionHelper;
import fi.utu.ville.exercises.model.ExecutionSettings;
import fi.utu.ville.exercises.model.ExecutionState;
import fi.utu.ville.exercises.model.ExecutionStateChangeListener;
import fi.utu.ville.exercises.model.Executor;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.SubmissionListener;
import fi.utu.ville.exercises.model.SubmissionType;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.TempFilesManager;

public abstract class AbstractMathExecutor<E extends ExerciseData, F extends MathSubInfo<P>, P extends Problem>
		implements Executor<E, F> {

	private static final long serialVersionUID = 2682119786422750060L;

	protected MathLayout<P> exLayout;
	private TimeStampHandler timeStampHandler;
	private MisconceptionPerformanceSubject misconceptionSubject = MisconceptionPerformanceSubject.NONE;
	private MisconceptionPerformanceData mpdInternal;
	protected Localizer localizer;
	private long lastProblemStartTime;
	private E data;
	
	// private Localizer main;
	private final ExerciseExecutionHelper<F> listenerHelper 
	               = new ExerciseExecutionHelper<F>();

	protected TimeStampHandler getTimeStampHandler() {
		return timeStampHandler;
	}

	protected abstract MathExerciseState<P> getMathState();

	protected abstract MathExerciseView<P> getMathView();

	protected abstract F newSubmissionInfo();

	protected abstract void initStateAndView(E data, Localizer localizer);

	protected F parseSubmissionInfo() {
		ArrayList<P> problems = new ArrayList<P>();
		for (int i = 0; i < getMathState().getProblemCount(); i++) {
			problems.add(getMathState().getProblem(i));
		}

		F subData = newSubmissionInfo();
		subData.setTimeStamps(getTimeStampHandler());
		subData.setUserProblemsAndAnswers(problems);

		return subData;
	}

	@Override
	public final void initialize(Localizer localizer, E data, F oldData,
			TempFilesManager tempMan, ExecutionSettings execSettings)
			throws ExerciseException {
		// MathExercise exer = MathPersistenceHandler.load(in);

		// You must rename these two to match your filenames.

		this.data = data;
		this.localizer = localizer;

		UI.getCurrent().addStyleName(getBgStyleName());

		initStateAndView(data, localizer);

		MisconceptionPerformanceData mpd = getMathPerformanceData();

		timeStampHandler = new TimeStampHandler();
		timeStampHandler.add(TimeStampHandler.startExercise);

		exLayout = new MathLayout<P>(localizer, getMathView(), getMathState(),
				timeStampHandler, execSettings);
		// exLayout.attachExecSettings(execSettings);

		PrevButtonListener prevListener = new PrevButtonListener(exLayout,
				timeStampHandler);
		CheckButtonListener checkListener = new CheckButtonListener(
				getMathView(), exLayout, timeStampHandler);
		NextButtonListener nextListener = new NextButtonListener(exLayout,
				timeStampHandler);

		// Adds executor to MathLayout so that askSubmit can be called
		// when last calculation has been checked.
		exLayout.attachExecutor(this);
		exLayout.attachControllers(prevListener, checkListener, nextListener);
		// ExecSettings are used to check if immediate feedback should be shown
		// or not

		getMathView().drawProblem(getMathState().nextProblem());
		startMathPerformance(getMathState().getCurrentProblem());
		mpd.setStartTime();
	}

	private double checkCorrectness() {
		return getMathState().getCorrectness();
	}

	@Override
	public final void registerSubmitListener(
			SubmissionListener<F> submitListener) {
		listenerHelper.registerSubmitListener(submitListener);

	}

	@Override
	public final Layout getView() {
		return exLayout;
	}

	@Override
	public final void shutdown() {
		UI.getCurrent().removeStyleName(getBgStyleName());
	}

	protected String getBgStyleName() {
		return "math-default-bg";
	}

	@Override
	public final void askReset() {

		MisconceptionPerformanceData mpd = getMathPerformanceData();
		mpd.setSubmitted(false);
		// if you try this as a student first and then test it in teacher view,
		// mpd will get saved
		if (mpd.getAssigId() > 0) {
			mpd.save();
		}
		getView().setEnabled(true);
		getMathState().reset();
		exLayout.reset();
		getMathView().clearFields();
		getMathView().drawProblem(getMathState().nextProblem());
		listenerHelper.informResetDefault();
		// startTime = System.currentTimeMillis();

		if(timeStampHandler == null)
			timeStampHandler = new TimeStampHandler();
		
		timeStampHandler.add(TimeStampHandler.resetExercise);
	}

	@Override
	public final void askSubmit(SubmissionType submType) {

		MisconceptionPerformanceData mpd = getMathPerformanceData();

		mpd.setEndTime();
		mpd.setSubmitted(true);
		// if you try this as a student first and then test it in teacher view,
		// mpd will get saved
		if (mpd.getAssigId() > 0) {
			mpd.save();
		}

		if(timeStampHandler == null)
			timeStampHandler = new TimeStampHandler();
		
		timeStampHandler.add(TimeStampHandler.submitExercise);

		listenerHelper.informSubmitDefault(checkCorrectness(),
				parseSubmissionInfo(), submType, null);

		setUnsubmittedChangesFlag(false);

		getView().setEnabled(false);

	}

	@Override
	public final void registerExecutionStateChangeListener(
			ExecutionStateChangeListener execStateListener) {
		listenerHelper
				.registerExerciseExecutionStateListener(execStateListener);
	}

	@Override
	public final ExecutionState getCurrentExecutionState() {
		return listenerHelper.getState();
	}

	public void setUnsubmittedChangesFlag(boolean value) {
		if (value) {
			listenerHelper.getState().setUnSubmChangesFlag();
		} else {
			listenerHelper.getState().clearUnSubmChangesFlag();
		}
	}
	
	/**
	 * Get the MathPerformanceData object, which is used to save misconception
	 * related information.
	 * 
	 * @return MathPerformanceData object for handling misconception information
	 */
	private MisconceptionPerformanceData getMathPerformanceData() {
		mpdInternal = misconceptionSubject.getPerformanceData();
		if(mpdInternal == null)
			mpdInternal = new NullMisconceptionPerformanceData();
		return mpdInternal;
	}

	/**
	 * 
	 * @return can misconception data be collected from this exercise.
	 */
	public boolean canCollectMisconceptionData(){
		return misconceptionSubject != MisconceptionPerformanceSubject.NONE;
	}
	
	/** Abstract method for concrete implementations to implement **/

	/**
	 * This method may be overridden if derived class wants to specify its own
	 * misconception performance-data. If this returns null, this exercise can 
	 * not be used to collect misconception performance data<br><br>
	 * MisconceptionPerformanceData is a collection of MisconceptionData for a single assignment 
	 * 
	 * @return the performance data-object used to calculate misconceptions 
	 * or null if no data collection is available.
	 */
	protected MisconceptionPerformanceData getMisconceptionPerformanceData(){
		return null;
	}
	
	/**
	 * This method may be overridden if derived class wants to specify its own
	 * misconception-data.<br><br>
	 * MisconceptionData contains data from a user for a single attempt at solving a problem
	 * 
	 * @param problem the problem to collect data from
	 * @return The misconceptionData for that problem instance
	 */
	protected MisconceptionData getMisconceptionData(P problem){
		return null;
	}

	/**
	 * This method may be overridden if derived class wants to specify any
	 * MathMisconceptionTypeData. MisconceptionTypeData is the type of 
	 * misconception involved in this exercise. The possibilities are found in the Misconception-enum
	 * 
	 * @param problem the problem to collect data from
	 * @return The different misconceptions collected for this problem instance
	 */
	protected List<MisconceptionTypeData> getMisconceptionTypeData(
			P problem) {
		return null;
	}

	protected void startMathPerformance(P problem) {
		lastProblemStartTime = System.currentTimeMillis();
	}

	protected void finishMathPerformance(P problem) {
		MisconceptionPerformanceData mpd = getMathPerformanceData();
		MisconceptionData mmd = getMisconceptionData(problem);
		if(mmd == null)
			return;
		
		List<MisconceptionTypeData> misconceptions = getMisconceptionTypeData(problem);
		if (misconceptions != null) {
			mmd.addMisconceptionTypes(misconceptions);
		}
		mmd.setTime(System.currentTimeMillis() - lastProblemStartTime);
		mpd.addMisconceptionData(mmd);
		
		mpd.save();
	}

	@Override
	public final void setMisconceptionSubject(
			MisconceptionPerformanceSubject misconceptionSubject) {
		this.misconceptionSubject = misconceptionSubject;
	}

	@Override
	public final MisconceptionPerformanceSubject getMisconceptionSubject() {
		return misconceptionSubject;
	}

	
}