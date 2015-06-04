package ${package};

import edu.vserver.exercises.math.essentials.layout.MathExerciseView;
import edu.vserver.exercises.math.essentials.level.LevelMathDataWrapper;
import edu.vserver.math.AbstractMathTableSubmissionViewer;

public class ${VilleJavaClassPrefix}SubmissionVisualizer
        extends
        AbstractMathTableSubmissionViewer<LevelMathDataWrapper<${VilleJavaClassPrefix}Data>, ${VilleJavaClassPrefix}SubmissionInfo, ${VilleJavaClassPrefix}Problem> {

    private static final long serialVersionUID = 2187881034704842330L;

    @Override
    protected MathExerciseView<${VilleJavaClassPrefix}Problem> getExerView(
            LevelMathDataWrapper<${VilleJavaClassPrefix}Data> exerData,
            ${VilleJavaClassPrefix}SubmissionInfo submInfo) {
        return new ${VilleJavaClassPrefix}View(exerData.getForLevel(submInfo
                .getDiffLevel()), getLocalizer());
    }

}