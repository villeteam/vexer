package ${package};

import edu.vserver.exercises.math.essentials.layout.AbstractMathAnswer;

public class ${VilleJavaClassPrefix}Answer extends AbstractMathAnswer {

    private static final long serialVersionUID = 1L;

    private final String answer;
    
    public ${VilleJavaClassPrefix}Answer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

}
