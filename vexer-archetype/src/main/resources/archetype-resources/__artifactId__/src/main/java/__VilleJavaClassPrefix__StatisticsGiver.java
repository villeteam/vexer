package ${package};

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.helpers.StatSubmInfoFilterEditor;
import edu.vserver.exercises.helpers.StatSubmInfoFilterEditor.FilterEditor;
import edu.vserver.exercises.helpers.StatSubmInfoFilterEditor.FilterEditorFactory;
import edu.vserver.exercises.helpers.StatSubmInfoFilterTable;
import edu.vserver.exercises.helpers.StatSubmInfoFilterTable.ShowSubmissionColGenerator;
import edu.vserver.exercises.helpers.StatSubmInfoFilterTable.SubmInfoColumnGenerator;
import edu.vserver.exercises.helpers.StatsGiverHelper.BySubmMatcher;
import edu.vserver.exercises.helpers.StatsGiverHelper.SubmMatcher;
import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.StatisticalSubmissionInfo;
import edu.vserver.exercises.model.StatisticsInfoColumn;
import edu.vserver.exercises.model.SubmissionStatisticsGiver;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.StandardIcon;
import edu.vserver.standardutils.StandardUIConstants;
import edu.vserver.standardutils.StandardUIFactory;
import edu.vserver.standardutils.TempFilesManager;

public class ${VilleJavaClassPrefix}StatisticsGiver implements
	SubmissionStatisticsGiver<${VilleJavaClassPrefix}ExerciseData, ${VilleJavaClassPrefix}SubmissionInfo> {

	/**
	* 
	*/
	private static final long serialVersionUID = -1410253605264134011L;
	
	private Localizer localizer;
	
	private List<StatisticsInfoColumn<?>> statCols;
	
	@Override
	public void initialize(
		${VilleJavaClassPrefix}ExerciseData exercise,
		List<StatisticalSubmissionInfo<${VilleJavaClassPrefix}SubmissionInfo>> dataObjects,
		Localizer localizer, TempFilesManager tempManager)
		throws ExerciseException {
			this.localizer = localizer;
			
			initStatsCol(dataObjects, exercise);
	
	}
	
	private void initStatsCol(
		List<StatisticalSubmissionInfo<${VilleJavaClassPrefix}SubmissionInfo>> data,
		${VilleJavaClassPrefix}ExerciseData exer) {
	
		List<String> answers = new ArrayList<String>();
		
		for (int i = 0, n = data.size(); i < n; i++) {
			answers.add(data.get(i).getSubmissionData().getAnswer());
		}
		
		StatisticsInfoColumn<String> answersCol = new StatisticsInfoColumn<String>(
				localizer.getUIText(${VilleJavaClassPrefix}UiConstants.ANSWER),
				localizer.getUIText(${VilleJavaClassPrefix}UiConstants.ANSWER_COL_DESC,
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
