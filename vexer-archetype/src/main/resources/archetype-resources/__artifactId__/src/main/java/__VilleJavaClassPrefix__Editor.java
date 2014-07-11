package ${package};

import java.io.File;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fi.utu.ville.exercises.model.Editor;
import fi.utu.ville.exercises.model.EditorHelper;
import fi.utu.ville.exercises.model.EditorHelper.EditedExerciseGiver;
import fi.utu.ville.standardutils.AFFile;
import fi.utu.ville.standardutils.AbstractFile;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.SimpleFileUploader;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.SimpleFileUploader.UploaderListener;
import fi.utu.ville.standardutils.ui.AbstractEditorLayout;
import fi.utu.ville.exercises.model.VilleContent;
import fi.utu.ville.exercises.model.VilleUI;

public class ${VilleJavaClassPrefix}Editor extends VilleContent implements
		Editor<${VilleJavaClassPrefix}ExerciseData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4600841604409240872L;

	private static final int MAX_FILE_SIZE_KB = 1024;
	
	private static final String MIME_FILTER = "^image/.*$";
	
	private EditorHelper<${VilleJavaClassPrefix}ExerciseData> editorHelper;

	private TextField questionText;

	private AbstractFile currImgFile;

	private Localizer localizer;
	
	private AbstractEditorLayout layout;


	public ${VilleJavaClassPrefix}Editor() {
		super(null;)
	}

	@Override
	public Layout getView() {
		return this;
	}

	@Override
	public void initialize(VilleUI ui, Localizer localizer, ${VilleJavaClassPrefix}ExerciseData oldData,
			EditorHelper<${VilleJavaClassPrefix}ExerciseData> editorHelper) {
		this.init(ui);
		this.localizer = localizer;

		this.editorHelper = editorHelper;

		editorHelper.getTempManager().initialize();

		doLayout(oldData);
	}

	private ${VilleJavaClassPrefix}ExerciseData getCurrentExercise() {
		return new ${VilleJavaClassPrefix}ExerciseData(questionText.getValue(), currImgFile);
	}

	@Override
	public boolean isOkToExit() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void doLayout(${VilleJavaClassPrefix}ExerciseData oldData) {

		this.setMargin(false);
		this.setSpacing(false);
		this.setWidth("100%");

		layout = StandardUIFactory.getTwoColumnView();
		addComponent(layout);
		
		layout.setTitle("Editor");
		
		String oldQuestion;
		if (oldData != null) {
			oldQuestion = oldData.getQuestion();
			currImgFile = oldData.getImgFile();
		} else {
			oldQuestion = "";
			currImgFile = null;
		}


		layout.addToLeft(editorHelper.getInfoEditorView());

		layout.addToTop(editorHelper
				.getControlbar(new EditedExerciseGiver<${VilleJavaClassPrefix}ExerciseData>() {

					@Override
					public ${VilleJavaClassPrefix}ExerciseData getCurrExerData(
							boolean forSaving) {
						return getCurrentExercise();
					}
				}));


		VerticalLayout editlayout = new VerticalLayout();

		Label questionTextCapt = new Label(
				localizer.getUIText(${VilleJavaClassPrefix}UiConstants.QUESTION));
		questionTextCapt.addStyleName(${VilleJavaClassPrefix}ThemeConsts.TITLE_STYLE);
		questionText = new TextField(null, oldQuestion);

		SimpleFileUploader uploader = new SimpleFileUploader(localizer,
				editorHelper.getTempManager(), MAX_FILE_SIZE_KB,
				MIME_FILTER);

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
}
