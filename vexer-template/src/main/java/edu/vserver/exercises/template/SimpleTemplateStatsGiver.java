package edu.vserver.exercises.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.ui.Component;

import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.StatisticalSubmissionInfo;
import edu.vserver.exercises.model.StatisticsInfoColumn;
import edu.vserver.exercises.model.SubmissionStatisticsGiver;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.TempFilesManager;

public class SimpleTemplateStatsGiver implements
		SubmissionStatisticsGiver<TemplateExerciseData, TemplateSubmissionInfo> {

	/**
* 
*/
	private static final long serialVersionUID = -1410253605264134011L;

	private Localizer localizer;

	private List<StatisticsInfoColumn<?>> statCols;

	@Override
	public void initialize(
			TemplateExerciseData exercise,
			List<StatisticalSubmissionInfo<TemplateSubmissionInfo>> dataObjects,
			Localizer localizer, TempFilesManager tempManager)
			throws ExerciseException {
		this.localizer = localizer;

		initStatsCol(dataObjects, exercise);

	}

	private void initStatsCol(
			List<StatisticalSubmissionInfo<TemplateSubmissionInfo>> data,
			TemplateExerciseData exer) {

		List<String> answers = new ArrayList<String>();

		for (int i = 0, n = data.size(); i < n; i++) {
			answers.add(data.get(i).getSubmissionData().getAnswer());
		}

		StatisticsInfoColumn<String> answersCol = new StatisticsInfoColumn<String>(
				localizer.getUIText(TemplateUiConstants.ANSWER),
				localizer.getUIText(TemplateUiConstants.ANSWER_COL_DESC,
						exer.getQuestion()), String.class, answers, true);

		statCols = Collections
				.<StatisticsInfoColumn<?>> singletonList(answersCol);
	}

	@Override
	public Component getView() {
		// no extra statistics functionality, just export the data as
		// stat-column
		return null;
	}

	@Override
	public List<StatisticsInfoColumn<?>> getAsTabularData() {
		return statCols;
	}

}