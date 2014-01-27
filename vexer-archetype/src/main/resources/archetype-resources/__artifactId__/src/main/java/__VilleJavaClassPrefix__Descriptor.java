package ${package};

import com.vaadin.server.Resource;

import edu.vserver.exercises.helpers.GsonPersistenceHandler;
import edu.vserver.exercises.model.ExerciseTypeDescriptor;
import edu.vserver.exercises.model.PersistenceHandler;
import edu.vserver.exercises.model.SubmissionStatisticsGiver;
import edu.vserver.exercises.model.SubmissionVisualizer;
import edu.vserver.standardutils.Localizer;

public class ${VilleJavaClassPrefix}Descriptor implements
		ExerciseTypeDescriptor<${VilleJavaClassPrefix}ExerciseData, ${VilleJavaClassPrefix}SubmissionInfo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5743225101617556960L;

	public static final ${VilleJavaClassPrefix}Descriptor INSTANCE = new ${VilleJavaClassPrefix}Descriptor();

	private ${VilleJavaClassPrefix}Descriptor() {

	}

	@Override
	public PersistenceHandler<${VilleJavaClassPrefix}ExerciseData, ${VilleJavaClassPrefix}SubmissionInfo> newExerciseXML() {
		// You can also implement your own PersistenceHandler if you want (see JavaDoc for more info)
		return new GsonPersistenceHandler<${VilleJavaClassPrefix}ExerciseData, ${VilleJavaClassPrefix}SubmissionInfo>(
				getTypeDataClass(), getSubDataClass());
	}

	@Override
	public ${VilleJavaClassPrefix}Executor newExerciseExecutor() {
		return new ${VilleJavaClassPrefix}Executor();
	}

	@Override
	public ${VilleJavaClassPrefix}Editor newExerciseEditor() {
		return new ${VilleJavaClassPrefix}Editor();
	}

	@Override
	public Class<${VilleJavaClassPrefix}ExerciseData> getTypeDataClass() {
		return ${VilleJavaClassPrefix}ExerciseData.class;
	}

	@Override
	public Class<${VilleJavaClassPrefix}SubmissionInfo> getSubDataClass() {
		return ${VilleJavaClassPrefix}SubmissionInfo.class;
	}

	@Override
	public SubmissionStatisticsGiver<${VilleJavaClassPrefix}ExerciseData, ${VilleJavaClassPrefix}SubmissionInfo> newStatisticsGiver() {
		return new ${VilleJavaClassPrefix}StatisticsGiver();
	}

	@Override
	public SubmissionVisualizer<${VilleJavaClassPrefix}ExerciseData, ${VilleJavaClassPrefix}SubmissionInfo> newSubmissionVisualizer() {
		return new ${VilleJavaClassPrefix}SubmissionViewer();
	}

	@Override
	public String getTypeName(Localizer localizer) {
		return localizer.getUIText(${VilleJavaClassPrefix}UiConstants.NAME);
	}

	@Override
	public String getTypeDescription(Localizer localizer) {
		return localizer.getUIText(${VilleJavaClassPrefix}UiConstants.DESC);
	}

	@Override
	public Resource getSmallTypeIcon() {
		return ${VilleJavaClassPrefix}Icon.SMALL_TYPE_ICON.getIcon();
	}

	@Override
	public Resource getMediumTypeIcon() {
		return ${VilleJavaClassPrefix}Icon.SMALL_TYPE_ICON.getIcon();
	}

	@Override
	public Resource getLargeTypeIcon() {
		return ${VilleJavaClassPrefix}Icon.SMALL_TYPE_ICON.getIcon();
	}
	
	@Override
	public boolean isManuallyGraded() {
		return false;
	}

}