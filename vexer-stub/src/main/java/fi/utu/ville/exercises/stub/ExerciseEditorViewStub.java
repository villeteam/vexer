package fi.utu.ville.exercises.stub;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import fi.utu.ville.exercises.model.Editor;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.ExerciseSaveListener;
import fi.utu.ville.exercises.model.ExerciseTypeDescriptor;
import fi.utu.ville.exercises.model.GeneralExerciseInfo;
import fi.utu.ville.exercises.model.PersistenceHandler;
import fi.utu.ville.exercises.model.VilleContent;
import fi.utu.ville.exercises.model.VilleUI;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.TestTempFilesManager;

/**
 * A quick and dirty implementation of a view presenting implementors of
 * {@link Editor} and storing new {@link ExerciseData}-instances as XML-files
 * (as transformed by given {@link PersistenceHandler}.
 * 
 * @author Riku Haavisto
 * 
 * @param <E>
 *            {@link ExerciseData}-implementor to be edited
 */
class ExerciseEditorViewStub<E extends ExerciseData> extends VilleContent {

	private static final Logger logger = Logger
			.getLogger(ExerciseEditorViewStub.class.getName());

	private static final long serialVersionUID = 6333118110488402015L;

	private final TestTempFilesManager tempManager;

	private final EditorHelperStubImpl<E> genInfoEditor;
	
	private final String exerName;

	/**
	 * Constructs a new {@link ExerciseEditorViewStub}.
	 * 
	 * @param toLoad
	 *            {@link ExerciseTypeDescriptor} for the exercise for which the
	 *            load the editor
	 * @param info
	 *            {@link GeneralExerciseInfo} name and description of the
	 *            exercise
	 * @param localizer
	 *            {@link Localizer} for localizing the UI
	 */
	public ExerciseEditorViewStub(VilleUI ui,
			final ExerciseTypeDescriptor<E, ?> toLoad,
			GeneralExerciseInfo info, final Localizer localizer) {
		super(ui);
		setWidth("100%");
		setHeight("100%");
		setMargin(true);

		exerName = info.getName();

		tempManager = new TestTempFilesManager(StubSessionData.getInstance()
				.getStubExerMaterialsTempDir());

		genInfoEditor = new EditorHelperStubImpl<E>(info, localizer, toLoad,
				tempManager);

		Editor<E> generalEditor = toLoad.newExerciseEditor();

		try {
			// TODO: quick fix passes null instead of an actual VilleUI
			generalEditor.initialize(ui, localizer, StubDataFilesHandler
					.loadExerDataForEditor(toLoad.getTypeDataClass(),
							toLoad.newExerciseXML(), toLoad, exerName,
							tempManager), genInfoEditor);
		} catch (ExerciseException e) {
			// this should maybe be shown directly to user of the stub
			logger.log(Level.SEVERE, "Failed loading editor-view", e);
		}

		genInfoEditor.registerExerSaveListener(new ExerciseSaveListener<E>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actOnExerciseTypeSave(E dataToSave) {
				if ("".equals(genInfoEditor.getExerciseInfo().getName())) {

					if (!genInfoEditor.wasShown()) {
						Notification.show(
								localizer
										.getUIText(StubUiConstants.INFO_EDITOR_NOT_SHOWN),
								Notification.Type.ERROR_MESSAGE);
					} else {
						Notification.show(localizer
								.getUIText(StubUiConstants.EXER_NAME_MISSING),
								Notification.Type.ERROR_MESSAGE);
					}

				} else {
					try {
						StubDataFilesHandler.saveExerStream(toLoad,
								genInfoEditor.getExerciseInfo(),

								dataToSave, tempManager);
						Notification.show(localizer
								.getUIText(StubUiConstants.EXERCISE_SAVED),
								Notification.Type.HUMANIZED_MESSAGE);
					} catch (ExerciseException e) {
						Notification.show(localizer
								.getUIText(StubUiConstants.EXER_SAVE_FAILED),
								Notification.Type.ERROR_MESSAGE);
						logger.log(Level.SEVERE,
								"Parsing exercise to xml failed", e);
					}

					UI.getCurrent().setContent(new StubStartView());

				}

			}

		});

		addComponent(new StubViewHeaderBar(localizer,
				localizer.getUIText(StubUiConstants.EDITOR_TEST_INFO)));

		addComponent(generalEditor.getView());
		setExpandRatio(generalEditor.getView(), 1);

	}

	private static <E extends ExerciseData> ExerciseEditorViewStub<E> getView(
			VilleUI ui, ExerciseTypeDescriptor<E, ?> controller,
			GeneralExerciseInfo info, Localizer localizer) {
		return new ExerciseEditorViewStub<E>(ui, controller, info, localizer);
	}

	/**
	 * Constructs and returns a new {@link ExerciseEditorViewStub} with given
	 * parameters.
	 * 
	 * @param toLoad
	 *            {@link ExerciseTypeDescriptor} for the exercise for which the
	 *            load the editor
	 * 
	 * @param info
	 *            {@link GeneralExerciseInfo} name and description of the
	 *            exercise
	 * @param localizer
	 *            {@link Localizer} for localizing the UI
	 * @return newly constructed {@link ExerciseEditorViewStub}
	 */
	public static ExerciseEditorViewStub<?> getViewFor(VilleUI ui,
			ExerciseTypeDescriptor<?, ?> toLoad, GeneralExerciseInfo info,
			Localizer localizer) {
		return getView(ui, toLoad, info, localizer);
	}

	@Override
	public void detach() {
		super.detach();
		tempManager.shutdown();
	}

	@Override
	public void doLayout() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOkToExit() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getViewName() {
		return exerName;
	}
}
