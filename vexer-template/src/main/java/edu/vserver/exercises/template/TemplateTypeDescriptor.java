package edu.vserver.exercises.template;

import com.vaadin.server.Resource;

import edu.vserver.exercises.helpers.GsonPersistenceHandler;
import edu.vserver.exercises.model.ExerciseTypeDescriptor;
import edu.vserver.exercises.model.PersistenceHandler;
import edu.vserver.exercises.model.SubmissionStatisticsGiver;
import edu.vserver.exercises.model.SubmissionVisualizer;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.StandardIcon;

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

	@Override
	public boolean isManuallyGraded() {
		return false;
	}

}
