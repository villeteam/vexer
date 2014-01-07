package edu.vserver.exercises.stub;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.model.ExerciseData;
import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.ExerciseTypeDescriptor;
import edu.vserver.exercises.model.SubmissionInfo;
import edu.vserver.exercises.model.SubmissionVisualizer;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.StandardUIFactory;
import edu.vserver.standardutils.TempFilesManager;

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

	private static <E extends ExerciseData, S extends SubmissionInfo> SubmissionVisualizerTestingView<E, S> getViewFor(
			SubmissionVisualizer<E, S> visualizer, Localizer localizer) {

		return new SubmissionVisualizerTestingView<E, S>(localizer, visualizer);
	}

	private final SubmissionVisualizer<E, S> visualizer;
	private final Localizer localizer;

	private SubmissionVisualizerTestingView(Localizer main,
			SubmissionVisualizer<E, S> visualizer) {

		setId(submVisualizerTestViewId);

		this.localizer = main;
		this.visualizer = visualizer;

		doLayout();
	}

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

			addComponent(StandardUIFactory.getHr());

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
			this.addComponent(StandardUIFactory.getErrorPanel(localizer
					.getUIText(StubUiConstants.NO_SUBMISSIONS_TO_VISUALIZE)));
		}

	}

}
