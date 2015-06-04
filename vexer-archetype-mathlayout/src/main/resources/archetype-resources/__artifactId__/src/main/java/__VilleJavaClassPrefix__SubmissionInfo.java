package ${package};

import edu.vserver.exercises.math.AbstractMathSubmissionInfo;

public class ${VilleJavaClassPrefix}SubmissionInfo extends
        AbstractMathSubmissionInfo<${VilleJavaClassPrefix}Problem> {

    private static final long serialVersionUID = 8702870727095225372L;

    private final String answer;

    public ${VilleJavaClassPrefix}SubmissionInfo(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

}
