package fi.utu.ville.exercises.template;

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

import fi.utu.ville.exercises.helpers.StatSubmInfoFilterEditor;
import fi.utu.ville.exercises.helpers.StatSubmInfoFilterEditor.FilterEditor;
import fi.utu.ville.exercises.helpers.StatSubmInfoFilterEditor.FilterEditorFactory;
import fi.utu.ville.exercises.helpers.StatSubmInfoFilterTable;
import fi.utu.ville.exercises.helpers.StatSubmInfoFilterTable.ShowSubmissionColGenerator;
import fi.utu.ville.exercises.helpers.StatSubmInfoFilterTable.SubmInfoColumnGenerator;
import fi.utu.ville.exercises.helpers.StatsGiverHelper.BySubmMatcher;
import fi.utu.ville.exercises.helpers.StatsGiverHelper.SubmMatcher;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.StatisticalSubmissionInfo;
import fi.utu.ville.exercises.model.StatisticsInfoColumn;
import fi.utu.ville.exercises.model.SubmissionStatisticsGiver;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardUIConstants;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.TempFilesManager;

public class TemplateSubmissionStatisticsGiver extends VerticalLayout implements
		SubmissionStatisticsGiver<TemplateExerciseData, TemplateSubmissionInfo> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -1410253605264134011L;
	
	private List<StatisticalSubmissionInfo<TemplateSubmissionInfo>> data;
	private TemplateExerciseData exer;
	
	private StatSubmInfoFilterTable<TemplateSubmissionInfo> genTable;
	private StatSubmInfoFilterEditor<TemplateSubmissionInfo> genTableEditor;
	
	private final VerticalLayout answFreqView = new VerticalLayout();
	
	private Localizer localizer;
	
	@Override
	public void initialize(
			TemplateExerciseData exercise,
			List<StatisticalSubmissionInfo<TemplateSubmissionInfo>> dataObjects,
			Localizer localizer, TempFilesManager tempManager)
					throws ExerciseException {
		exer = exercise;
		data = dataObjects;
		this.localizer = localizer;
		
		System.out.println("data: " + data);
		
		initAllSubmissionsTable();
		
		doLayout();
	}
	
	private static class AnswerColGen implements
			SubmInfoColumnGenerator<String, TemplateSubmissionInfo> {
			
		@Override
		public String getColumnHeader(Localizer localizer) {
			return localizer.getUIText(StandardUIConstants.ANSWER);
		}
		
		@Override
		public String getColumnDescription(Localizer localizer) {
			return localizer.getUIText(TemplateUiConstants.ANSWER_COL_DESC);
		}
		
		@Override
		public String getDefaultValue() {
			return null;
		}
		
		@Override
		public Class<String> getColumnDataType() {
			return String.class;
		}
		
		@Override
		public String getColValueFor(
				StatisticalSubmissionInfo<TemplateSubmissionInfo> statSubmInfo,
				Localizer localizer) {
			return statSubmInfo.getSubmissionData().getAnswer();
		}
		
		@Override
		public boolean isExportable() {
			return true;
		}
		
	}
	
	private void initAllSubmissionsTable() {
		
		List<SubmInfoColumnGenerator<?, TemplateSubmissionInfo>> extraCols = new ArrayList<SubmInfoColumnGenerator<?, TemplateSubmissionInfo>>();
		
		extraCols.add(new AnswerColGen());
		// using null for temp-files-manager is safe here as it is known
		// that TemplateSubmissionViewer does not need TempFilesManager;
		// though it would not hurt to pass the real TempFilesManager anyway
		extraCols
				.add(new ShowSubmissionColGenerator<TemplateExerciseData, TemplateSubmissionInfo>(
						TemplateTypeDescriptor.INSTANCE, exer, null));
						
		genTable = new StatSubmInfoFilterTable<TemplateSubmissionInfo>(
				localizer, data, extraCols);
				
		List<FilterEditorFactory<TemplateSubmissionInfo>> extras = new ArrayList<FilterEditorFactory<TemplateSubmissionInfo>>();
		
		extras.add(new AnswerLengthFilterEditor(localizer));
		
		genTableEditor = new StatSubmInfoFilterEditor<TemplateSubmissionInfo>(
				genTable, localizer, extras);
				
	}
	
	private Table getAnswerLengthFreqTable() {
		Table res = new Table("Answer-length frequencies");
		
		res.addContainerProperty("Length", Integer.class, null);
		res.addContainerProperty("Frequency", Integer.class, null);
		
		for (Entry<Integer, Integer> freq : getAnswerLengthFrequencies()
				.entrySet()) {
				
			System.out.println("Adding to ans-freq table: " + freq.getKey()
					+ freq.getValue());
					
			res.addItem(new Object[] { freq.getKey(), freq.getValue() }, null);
		}
		
		return res;
	}
	
	private Map<Integer, Integer> getAnswerLengthFrequencies() {
		Map<Integer, Integer> res = new HashMap<Integer, Integer>();
		
		for (StatisticalSubmissionInfo<TemplateSubmissionInfo> statSubmInf : genTable
				.getSelectedInfos()) {
			int answerLength = statSubmInf.getSubmissionData().getAnswer()
					.length();
			Integer currValue = res.get(answerLength);
			
			int newValue;
			if (currValue != null) {
				newValue = currValue + 1;
			} else {
				newValue = 1;
			}
			
			res.put(answerLength, newValue);
		}
		
		return res;
	}
	
	private void doLayout() {
		this.setWidth("100%");
		
		VerticalLayout centeredLayout = new VerticalLayout();
		centeredLayout.setWidth("800px");
		
		Label questionLabel = new Label("Question: " + exer.getQuestion());
		questionLabel.addStyleName("template-exercise-title");
		centeredLayout.addComponent(questionLabel);
		centeredLayout.addComponent(genTableEditor.getView());
		centeredLayout.addComponent(genTable.getStatInfoTableView());
		
		Button drawAnswLengthTableBtn = StandardUIFactory.getButton(
				"Draw freq-table", null);
				
		drawAnswLengthTableBtn.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -1254314606936710857L;
			
			@Override
			public void buttonClick(ClickEvent event) {
				answFreqView.removeAllComponents();
				answFreqView.addComponent(getAnswerLengthFreqTable());
				
			}
			
		});
		centeredLayout.addComponent(drawAnswLengthTableBtn);
		
		centeredLayout.addComponent(answFreqView);
		
		this.addComponent(centeredLayout);
		setComponentAlignment(centeredLayout, Alignment.TOP_CENTER);
		
	}
	
	@Override
	public Component getView() {
		return this;
	}
	
	private static class AnswerLengthFilter implements
			SubmMatcher<TemplateSubmissionInfo> {
			
		private final int minLength;
		private final int maxLength;
		
		public AnswerLengthFilter(int minLength, int maxLength) {
			this.maxLength = maxLength;
			this.minLength = minLength;
		}
		
		@Override
		public boolean matches(TemplateSubmissionInfo toTest) {
			int answLength = toTest.getAnswer().length();
			return answLength <= maxLength && answLength >= minLength;
		}
		
	}
	
	private static class AnswerLengthFilterEditor implements
			FilterEditor<TemplateSubmissionInfo>,
			FilterEditorFactory<TemplateSubmissionInfo> {
			
		private final TextField min;
		private final TextField max;
		
		private final Localizer localizer;
		
		public AnswerLengthFilterEditor(Localizer localizer) {
			this.localizer = localizer;
			min = new TextField();
			max = new TextField();
			
		}
		
		@Override
		public AnswerLengthFilterEditor newEditorInstance(Localizer localizer) {
			return new AnswerLengthFilterEditor(localizer);
		}
		
		@Override
		public String getFilterDesc(Localizer localizer) {
			return "answer-length-range-editor";
		}
		
		@Override
		public String getFilterName(Localizer localizer) {
			return "answer-length-range-editor";
		}
		
		@Override
		public Resource getFilterIcon() {
			return null;
			// return StandardIcon.CALCULATOR_MEDIUM.getIcon();
		}
		
		@Override
		public BySubmMatcher<TemplateSubmissionInfo> getFilter() {
			return new BySubmMatcher<TemplateSubmissionInfo>(
					new AnswerLengthFilter(getMin(), getMax()));
		}
		
		private int getMin() {
			String val = min.getValue();
			if (val.length() == 0) {
				return 0;
			} else {
				return Integer.parseInt(val);
			}
		}
		
		private int getMax() {
			String val = max.getValue();
			if (val.length() == 0) {
				return 0;
			} else {
				return Integer.parseInt(val);
			}
		}
		
		@Override
		public Component getFilterEditView() {
			VerticalLayout res = new VerticalLayout();
			res.addComponent(new Label("min"));
			res.addComponent(min);
			res.addComponent(new Label("max"));
			res.addComponent(max);
			
			return res;
		}
		
		@Override
		public Component getMinifiedView() {
			Image img = new Image();
			img.setSource(getFilterIcon());
			img.setDescription(getFilterStateDesc());
			
			return img;
		}
		
		@Override
		public AnswerLengthFilterEditor getCopy() {
			
			AnswerLengthFilterEditor res = new AnswerLengthFilterEditor(
					localizer);
			res.min.setValue(getMin() + "");
			res.max.setValue(getMax() + "");
			
			return res;
		}
		
		@Override
		public String getFilterStateDesc() {
			return getFilterName(localizer) + ": from " + getMin() + " to "
					+ getMax();
		}
		
		@Override
		public boolean checkAndNotify() {
			if (getMin() <= getMax()) {
				return true;
			} else {
				Notification.show("Max must be at least equal to min!");
				return false;
			}
		}
	}
	
	@Override
	public List<StatisticsInfoColumn<?>> getAsTabularData() {
		return genTable.exportByColGenTitle(Collections.singletonList(localizer
				.getUIText(StandardUIConstants.ANSWER)));
	}
	
}
