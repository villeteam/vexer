package ${package};

import edu.vserver.exercises.math.essentials.layout.AbstractMathAnswer;
import edu.vserver.exercises.math.essentials.layout.Problem;
import fi.utu.ville.standardutils.Localizer;

public class ${VilleJavaClassPrefix}Problem implements Problem {

    private static final long serialVersionUID = 1132903398317834085L;
    private ${VilleJavaClassPrefix}Answer userAnswer;
    private ${VilleJavaClassPrefix}Answer correct;

    private boolean correctness;
    private String expression;

    public ${VilleJavaClassPrefix}Problem(String expression) {
        this.expression = expression;
    }

    @Override
    public boolean tryAnswer(AbstractMathAnswer answer) {

        userAnswer = (${VilleJavaClassPrefix}Answer) answer;
        correctness = userAnswer.getAnswer().equals(correct.getAnswer());
        
        return correctness;
    }

    @Override
    public boolean isCorrect() {
        return correctness;
    }

    @Override
    public String getQuestion(Localizer localizer) {
        return expression;
    }

    @Override
    public String getCorrectAnswer() {
        return correct.getAnswer();
    }

    public void setCorrectAnswer(${VilleJavaClassPrefix}Answer correctAnswer) {
        correct = correctAnswer;
    }

    @Override
    public String getUserAnswer() {
        return userAnswer == null ? "" : userAnswer.getAnswer();
    }
}
