package ${package};

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

public class ${VilleJavaClassPrefix}Editor extends HorizontalLayout implements
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

	public ${VilleJavaClassPrefix}Editor() {
	}

	@Override
	public Layout getView() {
		return this;
	}

	@Override
	public void initialize(Localizer localizer, ${VilleJavaClassPrefix}ExerciseData oldData,
			EditorHelper<${VilleJavaClassPrefix}ExerciseData> editorHelper) {

		this.localizer = localizer;

		this.editorHelper = editorHelper;

		editorHelper.getTempManager().initialize();

		doLayout(oldData);
	}

	private ${VilleJavaClassPrefix}ExerciseData getCurrentExercise() {
		return new ${VilleJavaClassPrefix}ExerciseData(questionText.getValue(), currImgFile);
	}

	private void doLayout(${VilleJavaClassPrefix}ExerciseData oldData) {

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
				.getControlbar(new EditedExerciseGiver<${VilleJavaClassPrefix}ExerciseData>() {

					@Override
					public ${VilleJavaClassPrefix}ExerciseData getCurrExerData(
							boolean forSaving) {
						return getCurrentExercise();
					}
				}));

		this.addComponent(controlsLayout);

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

		this.addComponent(editlayout);

	}
}
