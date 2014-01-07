package edu.vserver.exercises.template;

import java.io.File;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.model.Editor;
import edu.vserver.exercises.model.EditorHelper;
import edu.vserver.exercises.model.EditorHelper.EditedExerciseGiver;
import edu.vserver.standardutils.AFFile;
import edu.vserver.standardutils.AbstractFile;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.SimpleFileUploader;
import edu.vserver.standardutils.SimpleFileUploader.UploaderListener;

public class TemplateEditor extends HorizontalLayout implements
		Editor<TemplateExerciseData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4600841604409240872L;

    public static final int MAX_FILE_SIZE_KB = 1024;
	public static final String IMAGE_MIME_FILTER = "^image/.*$";

	private EditorHelper<TemplateExerciseData> editorHelper;

	private TextField questionText;

	private AbstractFile currImgFile;

	private Localizer localizer;

	public TemplateEditor() {
	}

	@Override
	public Layout getView() {
		return this;
	}

	@Override
	public void initialize(Localizer localizer, TemplateExerciseData oldData,
			EditorHelper<TemplateExerciseData> editorHelper) {

		this.localizer = localizer;

		this.editorHelper = editorHelper;

		editorHelper.getTempManager().initialize();

		doLayout(oldData);
	}

	private TemplateExerciseData getCurrentExercise() {
		return new TemplateExerciseData(questionText.getValue(), currImgFile);
	}

	private void doLayout(TemplateExerciseData oldData) {

		this.setMargin(true);
		this.setSpacing(true);
		this.setWidth("100%");

		String oldQuestion;
		if (oldData != null) {
			oldQuestion = oldData.getQuestion();
			currImgFile = oldData.getImgFile();
		} else {
			oldQuestion = "";
			currImgFile = null;
		}

		VerticalLayout controlsLayout = new VerticalLayout();
		controlsLayout.setWidth("400px");

		controlsLayout.addComponent(editorHelper.getInfoEditorView());

		controlsLayout.addComponent(editorHelper
				.getControlbar(new EditedExerciseGiver<TemplateExerciseData>() {

					@Override
					public TemplateExerciseData getCurrExerData(
							boolean forSaving) {
						return getCurrentExercise();
					}
				}));

		this.addComponent(controlsLayout);

		VerticalLayout editlayout = new VerticalLayout();

		Label questionTextCapt = new Label(
				localizer.getUIText(TemplateUiConstants.QUESTION_TEXT));
		questionTextCapt.addStyleName("template-exercise-title");
		questionText = new TextField(null, oldQuestion);

		SimpleFileUploader uploader = new SimpleFileUploader(localizer,
				editorHelper.getTempManager(), MAX_FILE_SIZE_KB,
				IMAGE_MIME_FILTER);

		uploader.registerUploaderListener(new UploaderListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 8266397773350713952L;

			@Override
			public void fileUploadSucceeded(File tempFile, String fileName,
					String mimeType) {
				currImgFile = new AFFile(tempFile);
			}

			@Override
			public void uploadedFileDeleted(File tempFile) {
				currImgFile = null;
			}

		});

		if (currImgFile != null) {
			uploader.setAbstractUploadedFile(currImgFile);
		}
		editlayout.addComponent(questionTextCapt);
		editlayout.addComponent(questionText);
		editlayout.addComponent(uploader);

		this.addComponent(editlayout);

	}
}
