package edu.vserver.math;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnResizeEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.ChameleonTheme;

import edu.vserver.exercises.math.MathSubmissionUIFactory;
import edu.vserver.exercises.math.essentials.layout.MathExerciseView;
import edu.vserver.exercises.math.essentials.layout.Problem;
import edu.vserver.exercises.math.essentials.layout.TimeStamp;
import edu.vserver.exercises.math.essentials.layout.TimeStampHandler;
import edu.vserver.exercises.math.essentials.level.DiffLevel;
import edu.vserver.exercises.math.essentials.level.LevelMathSubmissionInfo;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.SubmissionVisualizer;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon.Icon;
import fi.utu.ville.standardutils.StandardIcon.IconVariant;
import fi.utu.ville.standardutils.TempFilesManager;
import fi.utu.ville.standardutils.UIConstants;

public abstract class AbstractMathTableSubmissionViewer<E extends ExerciseData, F extends LevelMathSubmissionInfo<G>, G extends Problem>
		extends VerticalLayout implements SubmissionVisualizer<E, F> {
		
	/**
	* 
	*/
	private static final long serialVersionUID = -1228022609075470958L;
	
	private Table table;
	private E exercise;
	private F submInfo;
	private Localizer localizer;
	private DecimalFormat format;
	
	private final String assignmentName;
	private final String exerciseName;
	
	public AbstractMathTableSubmissionViewer(String assignmentName,String exerciseName) {
		this.assignmentName = assignmentName;
		this.exerciseName = exerciseName;
	}
	
	// Abstract method for getting executor for each type of exercise.
	protected abstract MathExerciseView<G> getExerView(E exerData, F submInfo);
	
	@Override
	public void initialize(E exercise, F dataObject, Localizer localizer,
			TempFilesManager tempFilesManager) throws ExerciseException {
		this.exercise = exercise;
		this.submInfo = dataObject;
		this.localizer = localizer;
		format = new DecimalFormat("#.##");
		doLayout();
		
	}
	
	@Override
	public Layout getView() {
		return this;
	}
	
	@Override
	public String exportSubmissionDataAsText() {
		return AbstractMathSubmissionViewer.getSimpleTextExport(exercise,
				submInfo, localizer, "");
	}
	
	protected Embedded getDiffLevel() {
		ThemeResource pic = new ThemeResource(
				"../vexer-math/icons/star-gold-64.png");
				
		if (submInfo.getDiffLevel() == DiffLevel.EASY) {
			pic = new ThemeResource("../vexer-math/icons/star-bronze-64.png");
		} else if (submInfo.getDiffLevel() == DiffLevel.NORMAL) {
			pic = new ThemeResource("../vexer-math/icons/star-silver-64.png");
		}
		
		Embedded diffLevel = new Embedded(submInfo.getDiffLevel().name(), pic);
		
		return diffLevel;
	}
	
	protected void doLayout() {
		setSpacing(true);
		setMargin(true);
		setWidth("100%");
		
		table = new Table();
		table.addColumnResizeListener(new Table.ColumnResizeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3535283813634437776L;
			
			@Override
			public void columnResize(ColumnResizeEvent event) {
				// Get the new width of the resized column
				int width = event.getCurrentWidth();
				
				// Get the property ID of the resized column
				String column = (String) event.getPropertyId();
				
				// Do something with the information
				table.setColumnFooter(column, String.valueOf(width) + "px");
			}
		});
		
		// Must be immediate to send the resize events immediately
		table.setImmediate(true);
		
		// Common knowledge to all math problems:
		// - Question
		// - isCorrect
		// - Correct answer
		// - User's anwer
		// - Correctness
		// - Time on task
		// - Time on answer
		
		table.addStyleName(ChameleonTheme.TABLE_STRIPED);
		
		table.addContainerProperty("Question", String.class, null);
		table.addContainerProperty("Given answer", String.class, null);
		table.addContainerProperty("Correct answer", String.class, null);
		table.addContainerProperty("Time", String.class, null);
		table.addContainerProperty("Correctness", Label.class, null);
		
//		table.addContainerProperty("Timeline", Button.class, null);
//		table.addContainerProperty("Show exercise", Button.class, null);
		
		table.setColumnAlignment("Correctness", Table.Align.CENTER);
		
		table.setCellStyleGenerator(new Table.CellStyleGenerator() {
			
			private static final long serialVersionUID = -2373086824079035962L;

			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				if(submInfo.getProblems().get((int)itemId).isCorrect()) {
					return "background-green";
				}
				return "background-red";
			}
		});
		
		int correctAnswers = 0;
		double average = 0;
		int amount = 0;
		
		// Divided handlers
		final ArrayList<TimeStampHandler> handlers = divideTimeStamps(submInfo
				.getTimeStamps());
				
		//int i = 0;
		Double[] timesPerTask = new Double[handlers.size()];
		for (int i = 0; i < handlers.size() && i < submInfo.getProblems().size(); i++) {
			final G problem = submInfo.getProblems().get(i);
			String time = timePerTask(handlers, i);
			
			double t = 0;
			try {
				t = Double.parseDouble(time);
			} catch (Exception e) {
				//				e.printStackTrace();
			}
			timesPerTask[i] = t;
			
			Button timeline = MathSubmissionUIFactory.getTimelineButton(handlers.get(i));
			
			Button showExercise = new Button("Show exercise");
			showExercise.addStyleName(BaseTheme.BUTTON_LINK);
			showExercise.addClickListener(new Button.ClickListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 524295315804770796L;
				
				@Override
				public void buttonClick(ClickEvent event) {
					Window res = new Window("Show exercise");
					
					VerticalLayout content = new VerticalLayout();
					content.setSizeUndefined();
					content.setSpacing(true);
					
					MathExerciseView<G> view = getExerView(exercise, submInfo);
					view.drawProblem(problem);
					
					content.addComponent(view);
					
					res.setContent(content);
					
					UI.getCurrent().addWindow(res);
				}
			});
			
//			ThemeResource icon = new ThemeResource(
//					"../vexer-math/icons/false.png");
//			Embedded correctness = new Embedded(null, icon);
//			
//			try {
//				// The amount of correct answers
//				if (problem.isCorrect()) {
//					correctAnswers++;
//					
//					icon = new ThemeResource("../vexer-math/icons/correct.png");
//					correctness = new Embedded(null, icon);
//				}
//			} catch (Exception e) {
//			
//			}
			
			if(problem.isCorrect()) {
				correctAnswers++;
			}
			
			table.addItem(new Object[] { problem.getQuestion(localizer),
					problem.getUserAnswer(),
					problem.getCorrectAnswer(),
					time,
					new Label(problem.isCorrect() ? Icon.OK.getIcon().variant(IconVariant.BLACK)
							: Icon.CANCEL.getIcon().variant(IconVariant.BLACK),ContentMode.HTML)},
					new Integer(i));
					
			// Average time answering exercises
			if (t > 0) {
				average += t;
				amount++;
			}
			
		}
		
		HorizontalLayout horStats = new HorizontalLayout();
		horStats.setSizeUndefined();
		horStats.setSpacing(true);
		
		Label stats = new Label("<span>" + correctAnswers + "</span>"
				+ "<span>/</span>" + "<span style='font-size:26px'>" + submInfo.getProblems().size()
				+ "</span>");
		stats.setContentMode(ContentMode.HTML);
		stats.addStyleName("lastSubmissionInfo");
		VerticalLayout statsContainer = new VerticalLayout();
		Label pointLabel = new Label(localizer.getUIText(UIConstants.POINTS));
		pointLabel.addStyleName("lastSubmissionInfo");
		statsContainer.setSpacing(true);
		statsContainer.addComponents(stats,pointLabel);
		statsContainer.setComponentAlignment(stats, Alignment.MIDDLE_CENTER);
		statsContainer.setComponentAlignment(pointLabel, Alignment.MIDDLE_CENTER);
		horStats.addComponent(statsContainer);
		
//		double avg;
//		if (amount > 0) {
//			avg = average / amount;
//		} else {
//			avg = 0;
//		}
		Label averageTime = new Label("<span>"
				+ format.format((average)) + "s" + "</span>");
		averageTime.setContentMode(ContentMode.HTML);
		averageTime.addStyleName("lastSubmissionInfo");
		VerticalLayout timeContainer = new VerticalLayout();
		Label timeLabel = new Label(localizer.getUIText(UIConstants.TIME));
		timeLabel.addStyleName("lastSubmissionInfo");
		timeContainer.setSpacing(true);
		timeContainer.addComponents(averageTime,timeLabel);
		timeContainer.setComponentAlignment(averageTime, Alignment.MIDDLE_CENTER);
		timeContainer.setComponentAlignment(timeLabel, Alignment.MIDDLE_CENTER);
		horStats.addComponent(timeContainer);
		
		Label doneExers = new Label("<span>" + amount + "</span>");
		doneExers.setContentMode(ContentMode.HTML);
		doneExers.addStyleName("lastSubmissionInfo");
		VerticalLayout doneExersContainer = new VerticalLayout();
		Label doneExersLabel = new Label(localizer.getUIText(UIConstants.QUESTIONS));
		doneExersLabel.addStyleName("lastSubmissionInfo");
		doneExersContainer.setSpacing(true);
		doneExersContainer.addComponents(doneExers,doneExersLabel);
		doneExersContainer.setComponentAlignment(doneExers, Alignment.MIDDLE_CENTER);
		doneExersContainer.setComponentAlignment(doneExersLabel, Alignment.MIDDLE_CENTER);
		horStats.addComponent(doneExersContainer);
		
		//HorizontalLayout chartContainer = MathSubmissionUIFactory.getChartContainer(handlers, timesPerTask, correctAnswers, (handlers.size() - correctAnswers));//new HorizontalLayout();
		
		//Embedded diffLevel = getDiffLevel();
		
		//addComponent(diffLevel);
		addComponent(horStats);
		//addComponent(chartContainer);
		
		VerticalLayout tableContainer = new VerticalLayout();
		tableContainer.setSizeUndefined();
		tableContainer.addStyleName("tableBox");
		tableContainer.addComponent(table);
		addComponent(tableContainer);
		
		//setComponentAlignment(diffLevel, Alignment.MIDDLE_CENTER);
		//setComponentAlignment(chartContainer, Alignment.MIDDLE_CENTER);
		setComponentAlignment(horStats, Alignment.MIDDLE_CENTER);
		
		setComponentAlignment(tableContainer, Alignment.MIDDLE_CENTER);
		
	}
	
	/*
	 * divides Submission info Time stamp handler into sub exercise handlers.
	 */
	protected ArrayList<TimeStampHandler> divideTimeStamps(TimeStampHandler stamps) {
		ArrayList<TimeStampHandler> handlers = new ArrayList<TimeStampHandler>();
		int i = 3;
		while (i < stamps.size()) {
			TimeStampHandler handler = new TimeStampHandler();
			
			int j = i - 3;
			
			while (j <= i) {
				handler.add(stamps.get(j));
				j++;
			}
			
			handlers.add(handler);
			
			i = i + 3;
			
		}
		
		return handlers;
	}
	
	protected String timePerTask(ArrayList<TimeStampHandler> stamp, int i) {
		
		TimeStampHandler subProblem = stamp.get(i);
		TimeStamp firstEvent = subProblem.get(0);
		TimeStamp lastEvent = subProblem.get(subProblem.size() - 1);
		
		if (firstEvent.getEventStr().equals("startExercise")
				|| firstEvent.getEventStr().equals(
						"NextButtonListener.buttonClick()")
				|| lastEvent.getEventStr().equals("submitExercise")
				|| lastEvent.getEventStr().equals(
						"NextButtonListener.buttonClick()")) {
			long start = firstEvent.getTime();
			long end = lastEvent.getTime();
			
			double time = (end - start) / (double) 1000;
			
			return "" + time;
		} else {
			return "N/A";
		}
	}
	
	protected Layout doAdditionalSubProbLayout(final G subProblem) {
		return null;
	}
	
	protected Localizer getLocalizer() {
		return localizer;
	}
	
}
