package ${package};

import java.util.Random;

import edu.vserver.exercises.math.essentials.layout.AbstractMathState;
import fi.utu.ville.standardutils.Localizer;

public class ${VilleJavaClassPrefix}State extends
        AbstractMathState<${VilleJavaClassPrefix}Data, ${VilleJavaClassPrefix}Problem> {

    private static final long serialVersionUID = -8617477584787810586L;

    private ${VilleJavaClassPrefix}Data data;

    public ${VilleJavaClassPrefix}State(${VilleJavaClassPrefix}Data data, Localizer localizer) {
        super(data, localizer);
    }

    @Override
    protected int loadDataAndGetAmount(${VilleJavaClassPrefix}Data data) {
        this.data = data;	
        return data.getAmount();
    }

    @Override
    protected ${VilleJavaClassPrefix}Problem createProblem() {

        Random generator = new Random();

        int[] numbers = new int[]{generator.nextInt(data.getLimit(1)),generator.nextInt(data.getLimit(2))};
        int answer = numbers[0]+numbers[1];

        if(data.giveAnswersAlwaysAsNegative()) {
            answer = -Math.abs(answer);
        }

		${VilleJavaClassPrefix}Answer correctAnswer = new ${VilleJavaClassPrefix}Answer(answer+"");
		${VilleJavaClassPrefix}Problem problem = new ${VilleJavaClassPrefix}Problem(numbers[0]+"+"+numbers[1]);
        problem.setCorrectAnswer(correctAnswer);
	
        return problem;
    }

}
