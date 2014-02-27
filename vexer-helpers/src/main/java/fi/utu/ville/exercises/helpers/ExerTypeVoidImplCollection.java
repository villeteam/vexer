package fi.utu.ville.exercises.helpers;

import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import fi.utu.ville.exercises.model.Editor;
import fi.utu.ville.exercises.model.EditorHelper;
import fi.utu.ville.exercises.model.ExecutionSettings;
import fi.utu.ville.exercises.model.ExecutionState;
import fi.utu.ville.exercises.model.ExecutionStateChangeListener;
import fi.utu.ville.exercises.model.Executor;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.ExerciseTypeDescriptor;
import fi.utu.ville.exercises.model.PersistenceHandler;
import fi.utu.ville.exercises.model.StatisticalSubmissionInfo;
import fi.utu.ville.exercises.model.StatisticsInfoColumn;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.exercises.model.SubmissionListener;
import fi.utu.ville.exercises.model.SubmissionStatisticsGiver;
import fi.utu.ville.exercises.model.SubmissionType;
import fi.utu.ville.exercises.model.SubmissionVisualizer;
import fi.utu.ville.exercises.model.ExerciseException.ErrorType;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon;
import fi.utu.ville.standardutils.StandardUIConstants;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.TempFilesManager;
import fi.utu.ville.standardutils.XMLHelper;

/**
 * A collection of classes that can be used to give implementations that inform
 * the user that certain aspect of the exercise-type system is not really
 * implemented for certain exercise-type. These classes should of course only be
 * used temporarily.
 * 
 * @author Riku Haavisto
 * 
 */
public final class ExerTypeVoidImplCollection {

	private ExerTypeVoidImplCollection() {
		// this class is just to collect some empty-implementations together
	}

	/**
	 * An abstract class that can be extended to get a very simple
	 * {@link ExerciseTypeDescriptor}-implementor returning more or less
	 * void-values for everything else than {@link Executor}. <b>Only usable for
	 * starting a new exercise-type-development by first developing the
	 * {@link Executor} with some fixed test data-set.</b>
	 * 
	 * @author Riku Haavisto
	 * 
	 */
	public static abstract class NoDataExerciseDescriptor implements
			ExerciseTypeDescriptor<VoidExerciseData, VoidSubmissionData> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6388065029097594861L;

		@Override
		public PersistenceHandler<VoidExerciseData, VoidSubmissionData> newExerciseXML() {
			return VoidExerciseXMLHandler.INSTANCE;
		}

		@Override
		public Editor<VoidExerciseData> newExerciseEditor() {
			return new VoidExerciseEditor();
		}

		@Override
		public Class<VoidExerciseData> getTypeDataClass() {
			return VoidExerciseData.class;
		}

		@Override
		public Class<VoidSubmissionData> getSubDataClass() {
			return VoidSubmissionData.class;
		}

		@Override
		public SubmissionStatisticsGiver<VoidExerciseData, VoidSubmissionData> newStatisticsGiver() {
			return new VoidSubmissionDataStatsGiver<VoidExerciseData, VoidSubmissionData>();
		}

		@Override
		public SubmissionVisualizer<VoidExerciseData, VoidSubmissionData> newSubmissionVisualizer() {
			return new VoidSubmissionVisualizer<VoidExerciseData, VoidSubmissionData>();
		}

		@Override
		public String getTypeDescription(Localizer localizer) {
			return "Void-description";
		}

		@Override
		public Resource getSmallTypeIcon() {
			return StandardIcon.EDIT_ICON_SMALL.getIcon();
		}

		@Override
		public Resource getMediumTypeIcon() {
			return StandardIcon.EDIT_ICON_SMALL.getIcon();
		}

		@Override
		public Resource getLargeTypeIcon() {
			return StandardIcon.EDIT_ICON_SMALL.getIcon();
		}

	}

	/**
	 * An abstract class that can be extended to get a very simple
	 * {@link Executor}-implementor that does not care about
	 * {@link ExerciseData}-object it receives. <b>Only usable for starting a
	 * new exercise-type-development by first developing the {@link Executor}
	 * with some fixed test data-set.</b>
	 * 
	 * @author Riku Haavisto
	 * 
	 */
	public abstract static class RealSimpleExerciseExecutor extends
			VerticalLayout implements
			Executor<VoidExerciseData, VoidSubmissionData> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1683026008965901979L;

		private final ExerciseExecutionHelper<VoidSubmissionData> execHelper =

		new ExerciseExecutionHelper<VoidSubmissionData>();

		private Localizer localizer;

		/**
		 * @return current {@link Localizer}
		 */
		protected Localizer getLocalizer() {
			return localizer;
		}

		/**
		 * Creates the layout for the class extending
		 * {@link RealSimpleExerciseExecutor}. The layout should be added to
		 * implementor itself as it extends {@link VerticalLayout}.
		 */
		protected abstract void doLayout();

		/**
		 * @return the correctness of the current state of the {@link Executor}
		 *         as double in range 0.0 - 1.0
		 */
		protected abstract double getCorrectness();

		@Override
		public void initialize(Localizer localizer,
				VoidExerciseData exerciseData, VoidSubmissionData oldSubm,
				TempFilesManager materials, ExecutionSettings execSettings)
				throws ExerciseException {
			this.localizer = localizer;
			doLayout();
		}

		@Override
		public Component getView() {
			return this;
		}

		@Override
		public void askSubmit(SubmissionType askedSubmType) {
			execHelper.informOnlySubmit(getCorrectness(), null, askedSubmType,
					null);
		}

		@Override
		public void registerSubmitListener(
				SubmissionListener<VoidSubmissionData> submitListener) {
			execHelper.registerSubmitListener(submitListener);
		}

		@Override
		public void shutdown() {
			// nothing to do
		}

		@Override
		public void askReset() {
			// nothing to do
		}

		@Override
		public void registerExecutionStateChangeListener(
				ExecutionStateChangeListener execStateListener) {
			execHelper
					.registerExerciseExecutionStateListener(execStateListener);

		}

		@Override
		public ExecutionState getCurrentExecutionState() {
			return execHelper.getState();
		}

	}

	/**
	 * An {@link Editor}-implementor that only has a button for saving an
	 * empty-xml-file. Only usable in conjunction with
	 * {@link NoDataExerciseDescriptor}. The stub-ViLLE requires some file to be
	 * saved before the {@link Executor} of an exercise-type can be loaded; the
	 * empty file returned by this class can act as such if the {@link Executor}
	 * does not yet use any real {@link ExerciseData}-object.
	 * 
	 * @author Riku Haavisto
	 */
	public static final class VoidExerciseEditor extends VerticalLayout
			implements Editor<VoidExerciseData> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3825284271956719565L;

		private void doLayout(
				final EditorHelper<VoidExerciseData> genExerInfoEditor) {
			Button saveButton = new Button("save");

			saveButton.addClickListener(new ClickListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 7007598145996138576L;

				@Override
				public void buttonClick(ClickEvent event) {
					genExerInfoEditor
							.informSaveListeners(VoidExerciseData.INSTANCE);
				}

			});
			this.addComponent(saveButton);

		}

		@Override
		public Component getView() {
			return this;
		}

		@Override
		public void initialize(Localizer localizer, VoidExerciseData oldData,
				EditorHelper<VoidExerciseData> genExerInfoEditor)
				throws ExerciseException {
			doLayout(genExerInfoEditor);
		}

	}

	/**
	 * Empty implementation for the {@link ExerciseData} interface.
	 * 
	 * @author Riku Haavisto
	 * 
	 */
	public static final class VoidExerciseData implements ExerciseData {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5033069436051595412L;

		public static final VoidExerciseData INSTANCE = new VoidExerciseData();

		private VoidExerciseData() {

		}

	}

	/**
	 * Empty implementation for the {@link PersistenceHandler}. Creates empty
	 * XML-files and returns {@link VoidExerciseData #INSTANCE} or
	 * {@link VoidSubmissionData} from its load-methods regardless of the file
	 * really used.
	 * 
	 * @author Riku Haavisto
	 * 
	 */
	public static final class VoidExerciseXMLHandler implements
			PersistenceHandler<VoidExerciseData, VoidSubmissionData> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5033069436051595412L;

		public static final VoidExerciseXMLHandler INSTANCE = new VoidExerciseXMLHandler();

		private VoidExerciseXMLHandler() {

		}

		@Override
		public byte[] saveExerData(VoidExerciseData etd,
				TempFilesManager tempFiles, ByRefSaver matSaver)
				throws ExerciseException {
			try {
				return XMLHelper.xmlToBytes(XMLHelper.createEmptyDocument());
			} catch (TransformerConfigurationException e) {
				throw new ExerciseException(ErrorType.EXER_WRITE_ERROR, "", e);
			} catch (TransformerException e) {
				throw new ExerciseException(ErrorType.EXER_WRITE_ERROR, "", e);
			} catch (TransformerFactoryConfigurationError e) {
				throw new ExerciseException(ErrorType.EXER_WRITE_ERROR, "", e);
			} catch (ParserConfigurationException e) {
				throw new ExerciseException(ErrorType.EXER_WRITE_ERROR, "", e);
			}
		}

		@Override
		public VoidExerciseData loadExerData(byte[] inStream,
				TempFilesManager tempFiles, ByRefLoader matLoader)
				throws ExerciseException {
			return VoidExerciseData.INSTANCE;
		}

		@Override
		public byte[] saveSubmission(VoidSubmissionData subm,
				TempFilesManager tempManager) throws ExerciseException {
			try {
				return XMLHelper.xmlToBytes(XMLHelper.createEmptyDocument());
			} catch (TransformerConfigurationException e) {
				throw new ExerciseException(ErrorType.EXER_WRITE_ERROR, "", e);
			} catch (TransformerException e) {
				throw new ExerciseException(ErrorType.EXER_WRITE_ERROR, "", e);
			} catch (TransformerFactoryConfigurationError e) {
				throw new ExerciseException(ErrorType.EXER_WRITE_ERROR, "", e);
			} catch (ParserConfigurationException e) {
				throw new ExerciseException(ErrorType.EXER_WRITE_ERROR, "", e);
			}
		}

		@Override
		public VoidSubmissionData loadSubmission(byte[] inStream,
				boolean forStatGiver, TempFilesManager tempManager)
				throws ExerciseException {
			return VoidSubmissionData.INSTANCE;
		}

	}

	/**
	 * Empty implementation for the {@link SubmissionInfo} interface.
	 * 
	 * @author Riku Haavisto
	 * 
	 */
	public static final class VoidSubmissionData implements SubmissionInfo {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5033069436051595412L;

		public static final VoidSubmissionData INSTANCE = new VoidSubmissionData();

		private VoidSubmissionData() {

		}

	}

	/**
	 * <p>
	 * Empty implementation for the {@link SubmissionStatisticsGiver} that can
	 * be used with any implementor of the {@link SubmissionInfo}-interface.
	 * </p>
	 * <p>
	 * Only displays a localized message stating that
	 * "Submission statistics viewer is not implemented for the exercise type".
	 * </p>
	 * <p>
	 * In principle {@link SubmissionStatisticsGiver} can be implemented later
	 * than the rest of an exercise-type if the {@link SubmissionInfo} for that
	 * exercise-type is well thought out (so that it contains enough data of
	 * done submissions).
	 * </p>
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <S>
	 *            implementor of the {@link SubmissionInfo}-interface.
	 */
	public static final class VoidSubmissionDataStatsGiver<E extends ExerciseData, S extends SubmissionInfo>
			implements SubmissionStatisticsGiver<E, S> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2796141876732642688L;
		private Localizer localizer;

		@Override
		public void initialize(E exercise,
				List<StatisticalSubmissionInfo<S>> dataObjects,
				Localizer localizer, TempFilesManager tempFiles) {
			this.localizer = localizer;
			// Void method

		}

		@Override
		public Layout getView() {
			Label info = StandardUIFactory
					.getInformationPanel(localizer
							.getUIText(StandardUIConstants.TYPE_SUBMISSION_STATISTICS_GIVER_NOT_IMPL));
			Layout res = new VerticalLayout();
			res.addComponent(info);
			return res;
		}

		@Override
		public List<StatisticsInfoColumn<?>> getAsTabularData() {
			return Collections.emptyList();
		}

	}

	/**
	 * <p>
	 * Empty implementation for the {@link SubmissionVisualizer} that can be
	 * used with any implementor of the {@link SubmissionInfo}-interface.
	 * </p>
	 * <p>
	 * Only displays a localized message stating that
	 * "Submission viewer / export is not implemented for the exercise type".
	 * </p>
	 * <p>
	 * In principle {@link SubmissionVisualizer} can be implemented later than
	 * the rest of an exercise-type if the {@link SubmissionInfo} for that
	 * exercise-type is well thought out (so that it contains enough data of
	 * done submissions).
	 * </p>
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <S>
	 *            implementor of the {@link SubmissionInfo}-interface.
	 */
	public static final class VoidSubmissionVisualizer<E extends ExerciseData, S extends SubmissionInfo>
			implements SubmissionVisualizer<E, S> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7703972393720883138L;
		private Localizer localizer;

		@Override
		public Layout getView() {
			Label info = StandardUIFactory
					.getInformationPanel(localizer
							.getUIText(StandardUIConstants.TYPE_SUBMISSION_VIEWER_NOT_IMPLEMENTED));
			Layout res = new VerticalLayout();
			res.addComponent(info);
			return res;
		}

		@Override
		public String exportSubmissionDataAsText() {
			return localizer
					.getUIText(StandardUIConstants.TYPE_SUBMISSION_EXPORT_NOT_IMPLEMENTED);
		}

		@Override
		public void initialize(E exercise, S dataObject, Localizer localizer,
				TempFilesManager tempManager) {
			this.localizer = localizer;
		}

	}

}
