package fi.utu.ville.exercises.template;

import com.vaadin.server.Resource;

import fi.utu.ville.exercises.helpers.GsonPersistenceHandler;
import fi.utu.ville.exercises.model.ExerciseTypeDescriptor;
import fi.utu.ville.exercises.model.PersistenceHandler;
import fi.utu.ville.exercises.model.SubmissionStatisticsGiver;
import fi.utu.ville.exercises.model.SubmissionVisualizer;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon.IconVariant;

public class TemplateTypeDescriptor implements
		ExerciseTypeDescriptor<TemplateExerciseData, TemplateSubmissionInfo> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 5743225101617556960L;
	
	public static final TemplateTypeDescriptor INSTANCE = new TemplateTypeDescriptor();
	
	private TemplateTypeDescriptor() {
	
	}
	
	@Override
	public PersistenceHandler<TemplateExerciseData, TemplateSubmissionInfo> newExerciseXML() {
		// return TemplateXMLHandler.INSTANCE;
		return new GsonPersistenceHandler<TemplateExerciseData, TemplateSubmissionInfo>(
				getTypeDataClass(), getSubDataClass());
	}
	
	@Override
	public TemplateExecutor newExerciseExecutor() {
		return new TemplateExecutor();
	}
	
	@Override
	public TemplateEditor newExerciseEditor() {
		return new TemplateEditor();
	}
	
	@Override
	public Class<TemplateExerciseData> getTypeDataClass() {
		return TemplateExerciseData.class;
	}
	
	@Override
	public Class<TemplateSubmissionInfo> getSubDataClass() {
		return TemplateSubmissionInfo.class;
	}
	
	@Override
	public SubmissionStatisticsGiver<TemplateExerciseData, TemplateSubmissionInfo> newStatisticsGiver() {
		return new TemplateSubmissionStatisticsGiver();
	}
	
	@Override
	public SubmissionVisualizer<TemplateExerciseData, TemplateSubmissionInfo> newSubmissionVisualizer() {
		return new TemplateSubmissionViewer();
	}
	
	@Override
	public String getTypeName(Localizer localizer) {
		return localizer.getUIText(TemplateUiConstants.NAME);
	}
	
	@Override
	public String getTypeDescription(Localizer localizer) {
		return "Template-desciption";
	}
	
	@Override
	public Resource getSmallTypeIcon() {
		return null;
		// return StandardIcon.EDIT_ICON_SMALL.getIcon();
	}
	
	@Override
	public Resource getMediumTypeIcon() {
		return null;
		// return StandardIcon.EDIT_ICON_SMALL.getIcon();
	}
	
	@Override
	public Resource getLargeTypeIcon() {
		return null;
		// return StandardIcon.EDIT_ICON_SMALL.getIcon();
	}
	
	@Override
	public boolean isManuallyGraded() {
		return false;
	}
	
	@Override
	public String getHTMLIcon() {
		return TemplateFontIcons.COGS.getIcon().variant(IconVariant.BLACK);
	}
	
}
