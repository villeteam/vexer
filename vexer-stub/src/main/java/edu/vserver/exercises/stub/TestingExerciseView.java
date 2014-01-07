package edu.vserver.exercises.stub;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.helpers.StandardSubmissionType;
import edu.vserver.exercises.model.ExecutionSettings;
import edu.vserver.exercises.model.Executor;
import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.ExerciseTypeDescriptor;
import edu.vserver.exercises.model.GeneralExerciseInfo;
import edu.vserver.exercises.model.StatisticalSubmissionInfo;
import edu.vserver.exercises.model.SubmissionResult;
import edu.vserver.exercises.model.SubmissionInfo;
import edu.vserver.exercises.model.SubmissionListener;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.StandardUIFactory;
import edu.vserver.standardutils.TempFilesManager;

/**
 * A quick-and-dirty class for loading {@link Executor} -implementor for testing
 * and storing {@link SubmissionInfo}-objects from it.
 * 
 * @author Riku Haavisto
 * 
 * @param <S>
 *            tested {@link SubmissionInfo}-implementor
 */
class TestingExerciseView<S extends SubmissionInfo> extends VerticalLayout {

	private static final Logger logger = Logger
			.getLogger(TestingExerciseView.class.getName());

	private static final String testingExerViewId = "testingexerview";

	/**
	 * 
	 */
	private static final long serialVersionUID = 6423843365009117711L;

	public static <S extends SubmissionInfo> TestingExerciseView<S> getViewForStudentTesting(
			ExerciseTypeDescriptor<?, S> toLoad, Localizer localizer,
			GeneralExerciseInfo exerInfo, ExecutionSettings execSettings,
			TempFilesManager tempMan) {

		Executor<?, S> execToUse = null;
		String exerName = exerInfo.getName();
		try {
			execToUse = StubExertypeClassLoader.loadExecutor(toLoad, exerName,
					localizer, execSettings, tempMan);
		} catch (ExerciseException e) {
			// informing the user is handled by detecting the possible null
			// executor
			logger.log(Level.WARNING, "", e);
		}
		return getViewFor(execToUse, exerInfo, false, localizer, toLoad,
				tempMan);
	}

	public static TestingExerciseView<?> getViewForTestingInEditor(
			Executor<?, ? extends SubmissionInfo> execToUse,
			GeneralExerciseInfo exerInfo, Localizer localizer,
			TempFilesManager tempMan) {

		// descriptor is not needed here as it is only used to get correct
		// path for saving submission-infos
		return getViewFor(execToUse, exerInfo, true, localizer, null, tempMan);
	}

	private static <S extends SubmissionInfo> TestingExerciseView<S> getViewFor(
			Executor<?, S> execToUse, GeneralExerciseInfo exerInfo,
			boolean editorTest, Localizer localizer,
			ExerciseTypeDescriptor<?, S> toLoad, TempFilesManager tempMan) {
		return new TestingExerciseView<S>(execToUse, exerInfo, editorTest,
				localizer, toLoad, tempMan);
	}

	private final Executor<?, S> currentExecutor;
	private final GeneralExerciseInfo exerInfo;
	private final boolean editorTest;
	private final Localizer localizer;
	private final ExerciseTypeDescriptor<?, S> stubUsed;
	private final TempFilesManager tempMan;

	private TestingExerciseView(Executor<?, S> execToUse,
			GeneralExerciseInfo exerInfo, boolean editorTest,
			Localizer localizer, ExerciseTypeDescriptor<?, S> desc,
			TempFilesManager tempMan) {
		this.tempMan = tempMan;
		this.localizer = localizer;
		currentExecutor = execToUse;
		this.exerInfo = exerInfo;
		this.editorTest = editorTest;
		this.stubUsed = desc;
		setId(testingExerViewId);
		doLayout();

	}

	private void doLayout() {

		removeAllComponents();
		setMargin(true);
		setSpacing(true);
		setWidth("100%");
		if (!editorTest) {

			this.addComponent(new StubViewHeaderBar(localizer, localizer
					.getUIText(StubUiConstants.EXECUTOR_TEST_INFO)));
		}
		if (currentExecutor == null) {
			this.addComponent(StandardUIFactory
					.getInformationPanel(StubUiConstants.EXECUTOR_LOAD_ERROR));

		} else {

			addComponent(new TestingExerciseInfoView(localizer, exerInfo,
					stubUsed));

			addComponent(new ResetSubmitControlView(localizer, currentExecutor));

			Component exerLayout = currentExecutor.getView();
			addComponent(exerLayout);
			setComponentAlignment(exerLayout, Alignment.TOP_CENTER);
			setExpandRatio(exerLayout, 1.0f);

			currentExecutor.registerSubmitListener(new SubmissionListener<S>() {

				/**
				 * 
				 */
				private static final long serialVersionUID = -8757213787542086588L;

				@Override
				public void submitted(SubmissionResult<S> submission) {
					if (Arrays.binarySearch(StandardSubmissionType.values(),
							submission.getSubmissionType()) < 0) {
						throw new IllegalArgumentException(
								"For correct behavior the SubmissionType from "
										+ "askSubmit must be passed as such to "
										+ "the corresponding SubmissionFeedback");
					}

					if (!editorTest) {

						StatisticalSubmissionInfo<S> toWrite

						= new StatisticalSubmissionInfo<S>(submission
								.getTimeOnTask(), submission.getCorrectness(),
								System.currentTimeMillis(), submission
										.getSubmissionInfo());

						StubDataFilesHandler.writeSubmToDisk(stubUsed,
								exerInfo.getName(), toWrite, tempMan);

					}

					SubmissionPopup sp = new SubmissionPopup(localizer,
							submission);

					getUI().addWindow(sp);
				}
			});
		}

	}

	@Override
	public void detach() {
		if (currentExecutor != null) {
			currentExecutor.shutdown();
		}
		super.detach();
	}

}
