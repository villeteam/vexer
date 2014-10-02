package fi.utu.ville.exercises.stub;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.ExerciseTypeDescriptor;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.exercises.model.SubmissionVisualizer;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.TempFilesManager;

/**
 * A quick-and-dirty class for loading {@link SubmissionVisualizer} -implementor
 * for testing.
 * 
 * @author Riku Haavisto
 * 
 * @param <E>
 *            tested {@link ExerciseData}-implementor
 * @param <S>
 *            tested {@link SubmissionInfo}-implementor
 */
class SubmissionVisualizerTestingView<E extends ExerciseData, S extends SubmissionInfo>
		extends VerticalLayout {

	private static final Logger logger = Logger
			.getLogger(SubmissionVisualizerTestingView.class.getName());

	private static final String submVisualizerTestViewId = "submvisualisertest";
	private static final String submVisualizerTestExportLabel = "submvtest.export";

	private static final long serialVersionUID = 2019222167594454557L;

	/**
	 * Constructs a new {@link SubmissionVisualizerTestingView} to test
	 * implementors of {@link SubmissionVisualizer}. When using this
	 * factory-method no explicit type-parameters are needed.
	 * 
	 * @param toLoad
	 *            {@link ExerciseTypeDescriptor} for the exercise-type for which
	 *            to load the {@link SubmissionVisualizer}
	 * @param exerName
	 *            name (or id) of the exercise-instance to load
	 * @param localizer
	 *            {@link Localizer} for localizing the UI
	 * @param tempMan
	 *            {@link TempFilesManager} for managing temporary files
	 * @return newly constructed {@link SubmissionVisualizerTestingView}
	 */
	public static SubmissionVisualizerTestingView<?, ?> getViewFor(
			ExerciseTypeDescriptor<?, ?> toLoad, String exerName,
			Localizer localizer, TempFilesManager tempMan) {

		SubmissionVisualizer<? extends ExerciseData, ? extends SubmissionInfo> toUse = null;
		try {
			toUse = StubExertypeClassLoader.loadVisualizer(toLoad, exerName,
					localizer, tempMan);
		} catch (ExerciseException e) {
			// TODO FIXME show message to user
			logger.log(Level.SEVERE, "Failed loading submission-visualizer", e);
		}

		return getViewFor(toUse, localizer);
	}

	/**
	 * Constructs a new {@link SubmissionVisualizerTestingView} for given
	 * {@link SubmissionVisualizer}
	 * 
	 * @param visualizer
	 *            {@link SubmissionVisualizer} to use
	 * @param localizer
	 *            {@link Localizer} for localizing the UI
	 * @return the loaded {@link SubmissionVisualizerTestingView}
	 */
	private static <E extends ExerciseData, S extends SubmissionInfo> SubmissionVisualizerTestingView<E, S> getViewFor(
			SubmissionVisualizer<E, S> visualizer, Localizer localizer) {

		return new SubmissionVisualizerTestingView<E, S>(localizer, visualizer);
	}

	private final SubmissionVisualizer<E, S> visualizer;
	private final Localizer localizer;

	/**
	 * Constructs a new {@link SubmissionVisualizerTestingView} for the given
	 * {@link SubmissionVisualizer}
	 * 
	 * @param main
	 *            {@link Localizer} for localizing the UI
	 * @param visualizer
	 *            {@link SubmissionVisualizer} to use
	 */
	private SubmissionVisualizerTestingView(Localizer main,
			SubmissionVisualizer<E, S> visualizer) {

		setId(submVisualizerTestViewId);

		this.localizer = main;
		this.visualizer = visualizer;

		doLayout();
	}

	/**
	 * Draws the UI
	 */
	private void doLayout() {
		setMargin(true);
		setSpacing(true);
		setWidth("100%");
		removeAllComponents();
		this.addComponent(new StubViewHeaderBar(localizer, localizer
				.getUIText(StubUiConstants.VIEWER_TEST_INFO)));

		if (visualizer != null) {

			// Add panels to layout
			addComponent(visualizer.getView());

			// addComponent(StandardUIFactory.getHr());

			Label exportTitle = new Label(
					localizer.getUIText(StubUiConstants.EXPORT_OUTPUT));
			addComponent(exportTitle);
			exportTitle.addStyleName("stub-section-title");

			Label exportLabel = new Label(
					visualizer.exportSubmissionDataAsText(),
					ContentMode.PREFORMATTED);

			exportLabel.setId(submVisualizerTestExportLabel);
			addComponent(exportLabel);
		} else {
			this.addComponent(StandardUIFactory.getWarningPanel(localizer
					.getUIText(StubUiConstants.NO_SUBMISSIONS_TO_VISUALIZE)));
		}

	}

}
