package ${package};

import fi.utu.ville.exercises.model.ExerciseData;

public class ${VilleJavaClassPrefix}Data implements ExerciseData {

    private static final long serialVersionUID = 7859129767103173914L;

    private int numberOfQuestions = 1;
    private int[] limits;

    public ${VilleJavaClassPrefix}Data(int questions, int[] limits) {
        numberOfQuestions = questions;
        this.limits = limits;
    }

    public int getAmount() {
        return numberOfQuestions;
    }

    public int getLimit(int termNumber) {
        return limits[termNumber-1];
    }

    public boolean giveAnswersAlwaysAsNegative(){
        return false;
    }
}
