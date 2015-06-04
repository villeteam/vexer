package ${package};

import java.util.ArrayList;

import edu.vserver.exercises.math.essentials.layout.AbstractMathExecutor;
import edu.vserver.misconception.MisconceptionTypeData;
import fi.utu.ville.standardutils.Localizer;

public class ${VilleJavaClassPrefix}Executor
        extends
        AbstractMathExecutor<${VilleJavaClassPrefix}Data, ${VilleJavaClassPrefix}SubmissionInfo, ${VilleJavaClassPrefix}Problem> {

    private static final long serialVersionUID = -6563707901988219539L;
    private ${VilleJavaClassPrefix}State exState;
    private ${VilleJavaClassPrefix}View view;

    @Override
    protected ${VilleJavaClassPrefix}State getMathState() {
        return exState;
    }

    @Override
    protected ${VilleJavaClassPrefix}View getMathView() {
        return view;
    }

    @Override
    protected ${VilleJavaClassPrefix}SubmissionInfo newSubmissionInfo() {
        return new ${VilleJavaClassPrefix}SubmissionInfo("USER ANSWER FROM EXECUTOR");
    }

    @Override
    protected void initStateAndView(${VilleJavaClassPrefix}Data data,
            Localizer localizer) {
        view = new ${VilleJavaClassPrefix}View(data, localizer);

        exState = new ${VilleJavaClassPrefix}State(data, localizer);
    }

    @Override
    protected ArrayList<MisconceptionTypeData> getMisconceptionTypeData(
            ${VilleJavaClassPrefix}Problem problem) {
        return null;
    }

}
