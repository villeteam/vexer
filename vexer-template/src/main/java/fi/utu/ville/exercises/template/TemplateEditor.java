package fi.utu.ville.exercises.template;

import java.io.File;

import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fi.utu.ville.exercises.model.Editor;
import fi.utu.ville.exercises.model.EditorHelper;
import fi.utu.ville.exercises.model.EditorHelper.EditedExerciseGiver;
import fi.utu.ville.exercises.model.VilleContent;
import fi.utu.ville.exercises.model.VilleUI;
import fi.utu.ville.standardutils.AFFile;
import fi.utu.ville.standardutils.AbstractFile;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.SimpleFileUploader;
import fi.utu.ville.standardutils.SimpleFileUploader.UploaderListener;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.ui.AbstractEditorLayout;

public class TemplateEditor extends VilleContent implements
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

	private AbstractEditorLayout layout;

	public TemplateEditor() {
		super(null);
	}

	@Override
	public Layout getView() {
		return this;
	}

	@Override
	public void initialize(VilleUI ui, Localizer localizer,
			TemplateExerciseData oldData,
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

		this.setMargin(false);
		this.setSpacing(false);
		this.setWidth("100%");

		layout = StandardUIFactory.getTwoColumnView();
		addComponent(layout);

		layout.addToTop(editorHelper
				.getControlbar(new EditedExerciseGiver<TemplateExerciseData>() {

					@Override
					public TemplateExerciseData getCurrExerData(
							boolean forSaving) {
						return getCurrentExercise();
					}
				}));

		layout.setTitle("Editor");
		layout.addToLeft(editorHelper.getInfoEditorView());

		String oldQuestion;
		if (oldData != null) {
			oldQuestion = oldData.getQuestion();
			currImgFile = oldData.getImgFile();
		} else {
			oldQuestion = "";
			currImgFile = null;
		}
		//
		// VerticalLayout controlsLayout = new VerticalLayout();
		// controlsLayout.setWidth("400px");
		//
		// controlsLayout.addComponent(editorHelper.getInfoEditorView());

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

		layout.addToRight(editlayout);

	}

	@Override
	public void doLayout() {

	}

	@Override
	public boolean isOkToExit() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getViewName() {
		return "template-editor";
	}
}
