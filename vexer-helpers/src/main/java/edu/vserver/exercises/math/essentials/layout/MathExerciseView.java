package edu.vserver.exercises.math.essentials.layout;

import com.vaadin.ui.Layout;

public interface MathExerciseView<P extends Problem> extends Layout {

	public void drawProblem(P p);

	public AbstractMathAnswer getAnswer();

	public void lockControls();

	public void clearFields();

	public void showSolution(P currentProblem);

	public void setLayoutController(MathLayoutController cont);

}
