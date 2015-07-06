package ${package};

import com.vaadin.server.Resource;

import edu.vserver.exercises.math.essentials.level.LevelMathDataWrapper;
import edu.vserver.exercises.math.essentials.level.LevelMathExecutorWrapper;
import edu.vserver.exercises.math.essentials.level.LevelMathPersistenceWrapper;
import fi.utu.ville.exercises.model.Editor;
import fi.utu.ville.exercises.model.Executor;
import fi.utu.ville.exercises.model.ExerciseTypeDescriptor;
import fi.utu.ville.exercises.model.PersistenceHandler;
import fi.utu.ville.exercises.model.SubmissionStatisticsGiver;
import fi.utu.ville.exercises.model.SubmissionVisualizer;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon;
import fi.utu.ville.standardutils.StandardIcon.IconVariant;

public class ${VilleJavaClassPrefix}Descriptor
        implements
        ExerciseTypeDescriptor<LevelMathDataWrapper<${VilleJavaClassPrefix}Data>, ${VilleJavaClassPrefix}SubmissionInfo> {

    private static final long serialVersionUID = -2550301084361603798L;
    public static ${VilleJavaClassPrefix}Descriptor INSTANCE = new ${VilleJavaClassPrefix}Descriptor();

    @Override
    public PersistenceHandler<LevelMathDataWrapper<${VilleJavaClassPrefix}Data>, ${VilleJavaClassPrefix}SubmissionInfo> newExerciseXML() {
        return new LevelMathPersistenceWrapper<${VilleJavaClassPrefix}Data, ${VilleJavaClassPrefix}SubmissionInfo>(
                ${VilleJavaClassPrefix}Data.class, getSubDataClass());
    }

    @Override
    public Executor<LevelMathDataWrapper<${VilleJavaClassPrefix}Data>, ${VilleJavaClassPrefix}SubmissionInfo> newExerciseExecutor() {
        return new LevelMathExecutorWrapper<${VilleJavaClassPrefix}Data, ${VilleJavaClassPrefix}SubmissionInfo>(
                new ${VilleJavaClassPrefix}Executor());
    }

    @Override
    public Editor<LevelMathDataWrapper<${VilleJavaClassPrefix}Data>> newExerciseEditor() {
        return new ${VilleJavaClassPrefix}TabbedEditor();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<LevelMathDataWrapper<${VilleJavaClassPrefix}Data>> getTypeDataClass() {
        return (Class<LevelMathDataWrapper<${VilleJavaClassPrefix}Data>>) (Class<?>) LevelMathDataWrapper.class;
    }

    @Override
    public Class<${VilleJavaClassPrefix}SubmissionInfo> getSubDataClass() {
        return ${VilleJavaClassPrefix}SubmissionInfo.class;
    }

    @Override
    public SubmissionStatisticsGiver<LevelMathDataWrapper<${VilleJavaClassPrefix}Data>, ${VilleJavaClassPrefix}SubmissionInfo> newStatisticsGiver() {
        return new ${VilleJavaClassPrefix}StatisticsGiver();
    }

    @Override
    public SubmissionVisualizer<LevelMathDataWrapper<${VilleJavaClassPrefix}Data>, ${VilleJavaClassPrefix}SubmissionInfo> newSubmissionVisualizer() {
        return new ${VilleJavaClassPrefix}SubmissionVisualizer();
    }

    @Override
    public String getTypeName(Localizer localizer) {
        return "${VilleJavaClassPrefix}";
    }

    @Override
    public String getTypeDescription(Localizer localizer) {
        return "A currently generic exercise built on the ViLLE stub.";
    }

    /**
     * These type icons are for legacy support and shown in very few places.
     * Feel free to make icons of your own. Do bear in mind that these icons are
     * shown in very few places anymore.
     * 
     * @return An image for this exercise type.
     */
    @Override
    public Resource getSmallTypeIcon() {
        return null;
    }

    @Override
    public Resource getMediumTypeIcon() {
        return null;
    }

    @Override
    public Resource getLargeTypeIcon() {
        return null;
    }

    @Override
    public boolean isManuallyGraded() {
        return false;
    }

	/**
	 * This icon is shown in the exercise lists everywhere.
	 */
    @Override
    public String getHTMLIcon() {
        return StandardIcon.RawIcon.dribbble.variant(IconVariant.ORANGE);
    }

}
