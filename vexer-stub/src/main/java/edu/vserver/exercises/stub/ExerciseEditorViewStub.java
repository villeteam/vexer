package edu.vserver.exercises.stub;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.model.Editor;
import edu.vserver.exercises.model.ExerciseData;
import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.ExerciseSaveListener;
import edu.vserver.exercises.model.ExerciseTypeDescriptor;
import edu.vserver.exercises.model.GeneralExerciseInfo;
import edu.vserver.exercises.model.PersistenceHandler;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.TestTempFilesManager;

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
class ExerciseEditorViewStub<E extends ExerciseData> extends VerticalLayout {

	private static final Logger logger = Logger
			.getLogger(ExerciseEditorViewStub.class.getName());

	private static final long serialVersionUID = 6333118110488402015L;

	private final TestTempFilesManager tempManager;

	private final EditorHelperStubImpl<E> genInfoEditor;

	public ExerciseEditorViewStub(final ExerciseTypeDescriptor<E, ?> toLoad,
			GeneralExerciseInfo info, final Localizer localizer) {

		setWidth("100%");
		setMargin(true);

		String exerName = info.getName();

		tempManager = new TestTempFilesManager(StubSessionData.getInstance()
				.getStubExerMaterialsTempDir());

		genInfoEditor = new EditorHelperStubImpl<E>(info, localizer, toLoad,
				tempManager);

		Editor<E> generalEditor = toLoad.newExerciseEditor();

		try {
			generalEditor.initialize(localizer, StubDataFilesHandler
					.loadExerDataForEditor(toLoad.getTypeDataClass(),
							toLoad.newExerciseXML(), toLoad, exerName,
							tempManager), genInfoEditor);
		} catch (ExerciseException e) {
			// TODO FIXME this should be shown to user probably somehow ...
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

	}

	private static <E extends ExerciseData> ExerciseEditorViewStub<E> getView(
			ExerciseTypeDescriptor<E, ?> controller, GeneralExerciseInfo info,
			Localizer localizer) {
		return new ExerciseEditorViewStub<E>(controller, info, localizer);
	}

	public static ExerciseEditorViewStub<?> getViewFor(
			ExerciseTypeDescriptor<?, ?> toLoad, GeneralExerciseInfo info,
			Localizer localizer) {
		return getView(toLoad, info, localizer);
	}

	@Override
	public void detach() {
		super.detach();
		tempManager.shutdown();
	}
}
