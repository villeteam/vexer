package edu.vserver.math;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import edu.vserver.exercises.math.essentials.layout.Problem;
import edu.vserver.exercises.math.essentials.level.DiffLevel;
import edu.vserver.exercises.math.essentials.level.LevelMathSubmissionInfo;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.StatisticalSubmissionInfo;
import fi.utu.ville.exercises.model.StatisticsInfoColumn;
import fi.utu.ville.exercises.model.SubmissionStatisticsGiver;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.TempFilesManager;

public class AbstractMathSubmissionStatisticsGiver<E extends ExerciseData, F extends LevelMathSubmissionInfo<G>, G extends Problem>
		extends VerticalLayout implements SubmissionStatisticsGiver<E, F> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -1410253605264134011L;
	
	private List<StatisticalSubmissionInfo<F>> data;
	// not used
	// private E exer;
	private DecimalFormat f;
	
	@Override
	public void initialize(E exercise,
			List<StatisticalSubmissionInfo<F>> dataObjects,
			Localizer localizer, TempFilesManager tempFilesManager)
					throws ExerciseException {
		// exer = exercise;
		data = dataObjects;
		f = new DecimalFormat("#.##");
		doLayout();
	}
	
	protected Table getAllSubmissionsTable() {
		Table res = new Table();
		res.addStyleName(ChameleonTheme.TABLE_STRIPED);
		
		res.addContainerProperty("Level", Label.class, null);
		res.addContainerProperty("Done time", Date.class, null);
		res.addContainerProperty("Time on task (s)", Integer.class, null);
		res.addContainerProperty("Correctness", String.class, null);
		res.addContainerProperty("Answer", String.class, null);
		
		for (StatisticalSubmissionInfo<F> statSubmInf : data) {
			ThemeResource ico = new ThemeResource(
					"../vexer-math/icons/star-gold-64.png");
					
			if (statSubmInf.getSubmissionData().getDiffLevel() == DiffLevel.EASY) {
				ico = new ThemeResource(
						"../vexer-math/icons/star-bronze-64.png");
			} else if (statSubmInf.getSubmissionData().getDiffLevel() == DiffLevel.NORMAL) {
				ico = new ThemeResource(
						"../vexer-math/icons/star-silver-64.png");
			}
			
			Label level = new Label(statSubmInf.getSubmissionData()
					.getDiffLevel().name());
			level.setIcon(ico);
			level.setSizeUndefined();
			
			res.addItem(new Object[] { level, new Date(
					
					statSubmInf.getDoneTime()),
					
					statSubmInf.getTimeOnTask(),
					
					f.format(statSubmInf.getEvalution() * 100) + "%",
					
					null }, null);
		}
		
		return res;
	}
	
	protected void doLayout() {
		this.setWidth("100%");
		
		// this.addComponent(new Label("Question: " + exer.getQuestion()));
		HorizontalLayout tableWrapper = new HorizontalLayout();
		// tableWrapper.setSizeUndefined();
		tableWrapper.addStyleName("tableBox");
		
		Table table = getAllSubmissionsTable();
		tableWrapper.addComponent(table);
		
		HorizontalLayout horStats = getHorInfo();
		
		HorizontalLayout statistics = new HorizontalLayout();
		statistics.setSpacing(true);
		// statistics.setSizeUndefined();
		statistics.setMargin(true);
		
		this.addComponent(horStats);
		this.addComponent(statistics);
		this.addComponent(tableWrapper);
		setComponentAlignment(tableWrapper, Alignment.MIDDLE_CENTER);
		setComponentAlignment(statistics, Alignment.MIDDLE_CENTER);
		setComponentAlignment(horStats, Alignment.MIDDLE_CENTER);
		
	}
	
	@Override
	public Component getView() {
		return this;
	}
	
	protected HorizontalLayout getHorInfo() {
		
		int amount = 0;
		double timeOnTask = 0;
		double correctness = 0;
		ArrayList<Double> time = new ArrayList<Double>();
		ArrayList<Double> correct = new ArrayList<Double>();
		
		for (StatisticalSubmissionInfo<F> statSubmInf : data) {
			timeOnTask = timeOnTask + statSubmInf.getTimeOnTask();
			correctness = correctness + statSubmInf.getEvalution();
			
			time.add((double) statSubmInf.getTimeOnTask());
			correct.add(statSubmInf.getEvalution() * 100);
			amount++;
		}
		
		double avg = (timeOnTask / amount);
		double avgPercentCorrect = (correctness / amount) * 100;
		
		HorizontalLayout horStats = new HorizontalLayout();
		horStats.setSizeUndefined();
		horStats.setSpacing(true);
		horStats.addStyleName("statBox");
		
		Label stats = new Label("Average correctness: <span>"
				+ f.format(avgPercentCorrect) + "%"
				+ "</span><p>Std. devation: "
				+ "<p>Best submission: " + f.format(getMax(correct)) + " %</p>");
		stats.setContentMode(ContentMode.HTML);
		stats.addStyleName("submissionInfo");
		horStats.addComponent(stats);
		
		Label averageTime = new Label("Average time: " + "<span>"
				+ f.format((avg)) + " s" + "</span>  <p>Std. deviation: "
				+ "<p>Best submission: " + f.format(getMin(time)) + " s</p>");
		averageTime.setContentMode(ContentMode.HTML);
		averageTime.addStyleName("submissionInfo");
		horStats.addComponent(averageTime);
		
		Label doneExers = new Label("Submissions: " + "<span>" + amount
				+ "</span>");
		doneExers.setContentMode(ContentMode.HTML);
		doneExers.addStyleName("lastSubmissionInfo");
		horStats.addComponent(doneExers);
		
		return horStats;
	}
	
	protected double getMax(ArrayList<Double> list) {
		double max = 0;
		Collections.sort(list, Collections.reverseOrder());
		max = list.get(0);
		return max;
	}
	
	protected double getMin(ArrayList<Double> list) {
		double min = 0;
		Collections.sort(list);
		min = list.get(0);
		return min;
	}
	
	@Override
	public List<StatisticsInfoColumn<?>> getAsTabularData() {
		// TODO Auto-generated method stub
		return null;
	}
}
