package ${package};

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.math.essentials.layout.AbstractMathAnswer;
import edu.vserver.exercises.math.essentials.layout.MathExerciseView;
import edu.vserver.exercises.math.essentials.layout.MathLayoutController;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.ui.IntegerField;

public class ${VilleJavaClassPrefix}View extends VerticalLayout implements
        MathExerciseView<${VilleJavaClassPrefix}Problem> {

    private static final long serialVersionUID = 4938331703711987006L;

    private final ${VilleJavaClassPrefix}Data data;
    private final Localizer localizer;

    private IntegerField userAnswer;

    public ${VilleJavaClassPrefix}View(${VilleJavaClassPrefix}Data data,
            Localizer localizer) {
        this.data = data == null ? new ${VilleJavaClassPrefix}Data(5, new int[]{5,5}) : data;
        this.localizer = localizer;

    }

    @Override
    public void drawProblem(${VilleJavaClassPrefix}Problem problem) {
        this.removeAllComponents();
        this.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Label question = new Label(problem.getQuestion(localizer));

        userAnswer = new IntegerField(
                localizer.getUIText(${VilleJavaClassPrefix}UiConstants.ANSWER));

        this.addComponents(question, userAnswer);
    }

    @Override
    public void showSolution(${VilleJavaClassPrefix}Problem problem) {
        Label solution = new Label(problem.getCorrectAnswer());
        this.addComponent(solution);
    }

    @Override
    public AbstractMathAnswer getAnswer() {
        int answer = userAnswer.getInteger();
        if (data.giveAnswersAlwaysAsNegative()) {
            answer = -Math.abs(answer);
        }

        return new ${VilleJavaClassPrefix}Answer(answer + "");

    }

    @Override
    public void lockControls() {
        // This is called when there are no more questions left
    }

    @Override
    public void clearFields() {
        this.removeAllComponents();

    }

    @Override
    public void setLayoutController(MathLayoutController cont) {
        //MathLayoutController gives access to the check button
    }

}
