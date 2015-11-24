package fi.utu.ville.exercises.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import fi.utu.ville.exercises.helpers.StatsGiverHelper.StatSubmInfoFilter;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.ExerciseTypeDescriptor;
import fi.utu.ville.exercises.model.StatisticalSubmissionInfo;
import fi.utu.ville.exercises.model.StatisticsInfoColumn;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.exercises.model.SubmissionStatisticsGiver;
import fi.utu.ville.exercises.model.SubmissionVisualizer;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardUIConstants;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.TempFilesManager;

/**
 * <p>
 * This class can be used to generate a table of a {@link List} of {@link StatisticalSubmissionInfo}-objects that can be filtered and from which a set of rows
 * can be selected.
 * </p>
 * <p>
 * More columns (also {@link SubmissionInfo}-type specific) can be added by providing a {@link List} of {@link SubmInfoColumnGenerator}s. By default there are
 * three columns: time-on-task, time-done and evaluation. {@link ShowSubmissionColGenerator}-class can be used to generate a column with a button that opens a
 * {@link SubmissionVisualizer} initialized with info from that row in a popup.
 * </p>
 * <p>
 * This class can be used to provide filtering functionalities and to possible also to connect some other functionality (eg. visualizations such as a graph or a
 * heatmap) that can be applied to the filtered set of {@link StatisticalSubmissionInfo}.
 * </p>
 * 
 * @author Riku Haavisto
 * 
 * @param <S>
 *            accepted {@link SubmissionInfo}-type
 * 
 * @see StatsGiverHelper
 * @see StatSubmInfoFilterEditor
 */
public class StatSubmInfoFilterTable<S extends SubmissionInfo> {
	
	private final List<StatisticalSubmissionInfo<S>> allStatInfos;
	private final Table infosTable;
	
	private final List<SubmInfoColumnGenerator<?, S>> colGenerators =
	
	new ArrayList<SubmInfoColumnGenerator<?, S>>();
	
	private StatSubmInfoFilter<S> currFilter = null;
	
	private final List<StatisticalSubmissionInfo<S>> currMatch =
	
	new ArrayList<StatisticalSubmissionInfo<S>>();
	
	private final Localizer localizer;
	
	/**
	 * Initializes a new {@link StatSubmInfoFilterTable} instance. The actual table can be fetched by {@link #getStatInfoTableView()}.
	 * 
	 * @param localizer
	 *            {@link Localizer} used to localize UI
	 * @param allStatInfos
	 *            {@link List} of {@link StatisticalSubmissionInfo}-objects backing the table
	 * @param extraColumns
	 *            {@link List} of {@link SubmInfoColumnGenerator}s for generating extra columns to the table; can be null
	 */
	public StatSubmInfoFilterTable(Localizer localizer,
			List<StatisticalSubmissionInfo<S>> allStatInfos,
			List<SubmInfoColumnGenerator<?, S>> extraColumns) {
		this.localizer = localizer;
		this.allStatInfos = new ArrayList<StatisticalSubmissionInfo<S>>(
				allStatInfos);
				
		infosTable = new Table();
		
		initTable();
		if (extraColumns != null) {
			for (SubmInfoColumnGenerator<?, S> extraGen : extraColumns) {
				addExtraColumn(extraGen);
			}
		}
		
		updateTable();
		
	}
	
	/**
	 * Sets the filter used to select which items to show in the table. You can use the {@link StatSubmInfoFilterEditor}-class to generate and apply filters
	 * through its GUI.
	 * 
	 * @param aFilter
	 *            {@link StatSubmInfoFilter} to use
	 */
	public void setFilter(StatSubmInfoFilter<S> aFilter) {
		this.currFilter = aFilter;
		updateTable();
	}
	
	private void initTable() {
		
		infosTable.setSelectable(true);
		infosTable.setMultiSelect(true);
		
		infosTable.setWidth("100%");
		
		addExtraColGen(new DoneTimeColumnGen<S>());
		addExtraColGen(new TimeOnTaskColumnGen<S>());
		addExtraColGen(new EvalColumnGen<S>());
		
	}
	
	private boolean matchesFilters(StatisticalSubmissionInfo<S> toTest) {
		if (currFilter == null) {
			return true;
		}
		
		return currFilter.matches(toTest);
		
	}
	
	/**
	 * @return all the {@link StatisticalSubmissionInfo}s matching current filter
	 */
	public List<StatisticalSubmissionInfo<S>> getAllMatchingInfos() {
		return currMatch;
	}
	
	/**
	 * This method can be used to allow user to select certain rows that will be used in some further calculations or visualizations. If user has not explicitly
	 * selected any rows, this method defaults to returning all rows matching current filter.
	 * 
	 * @return all the selected {@link StatisticalSubmissionInfo}s in the table, or same as {@link #getAllMatchingInfos()} if no rows are selected
	 */
	public List<StatisticalSubmissionInfo<S>> getSelectedInfos() {
		Set<?> selection = (Set<?>) infosTable.getValue();
		
		if (selection == null || selection.isEmpty()) {
			return getAllMatchingInfos();
		} else {
			List<StatisticalSubmissionInfo<S>> res =
			
			new ArrayList<StatisticalSubmissionInfo<S>>();
			
			for (Object selObj : selection) {
				if (StatisticalSubmissionInfo.class.isAssignableFrom(selObj
						.getClass())) {
						
					// unfortunately there is no info to make this any safer;
					// however we can rest assured that these items are safe
					// (from allStatInfos)
					// unless someone has accessed the infosTable directly
					@SuppressWarnings("unchecked")
					StatisticalSubmissionInfo<S> submInfo = StatisticalSubmissionInfo.class
							.cast(selObj);
					res.add(submInfo);
					
				} else {
					throw new IllegalStateException(
							"Info-table contained items with ids that were not of class StatisticalSubmissionInfo; item was: "
									+ selObj);
				}
			}
			
			return res;
		}
		
	}
	
	private void addExtraColGen(SubmInfoColumnGenerator<?, S> toAdd) {
		
		colGenerators.add(toAdd);
		
		infosTable.addContainerProperty(toAdd, toAdd.getColumnDataType(),
				toAdd.getDefaultValue());
				
		infosTable.setColumnHeader(toAdd, toAdd.getColumnHeader(localizer));
		
	}
	
	/**
	 * Adds a new {@link SubmInfoColumnGenerator} to the table and updates the table.
	 * 
	 * @param toAdd
	 *            {@link SubmInfoColumnGenerator} to use for generating a new column
	 */
	public void addExtraColumn(SubmInfoColumnGenerator<?, S> toAdd) {
		
		addExtraColGen(toAdd);
		
		updateTable();
		
	}
	
	private void updateTable() {
		infosTable.removeAllItems();
		
		currMatch.clear();
		
		for (StatisticalSubmissionInfo<S> statInfo : allStatInfos) {
			if (matchesFilters(statInfo)) {
				Object[] values = new Object[colGenerators.size()];
				for (int i = 0, n = colGenerators.size(); i < n; i++) {
					values[i] = colGenerators.get(i).getColValueFor(statInfo,
							localizer);
				}
				
				currMatch.add(statInfo);
				infosTable.addItem(values, statInfo);
			}
		}
		
	}
	
	/**
	 * @return {@link Component} representing a view to the {@link StatSubmInfoFilterTable}
	 */
	public Component getStatInfoTableView() {
		return infosTable;
	}
	
	/**
	 * Generates a list of {@link StatisticsInfoColumn}s from all the {@link StatisticalSubmissionInfo}-objects adding a column generated by each
	 * {@link SubmInfoColumnGenerator} registered to this instance.
	 * 
	 * @return {@link List} of {@link StatisticsInfoColumn}s generated by registered {@link SubmInfoColumnGenerator}s from unfiltered data
	 */
	public List<StatisticsInfoColumn<?>> exportUnfilteredTabularData() {
		
		List<StatisticsInfoColumn<?>> toExport = new ArrayList<StatisticsInfoColumn<?>>();
		
		for (int i = 0, n = colGenerators.size(); i < n; i++) {
			toExport.add(toStatInfoColumn(colGenerators.get(i)));
		}
		
		return toExport;
	}
	
	/**
	 * <p>
	 * This method does the same as {@link #exportUnfilteredTabularData()} with the exception that it adds {@link StatisticsInfoColumn}s only for certain
	 * columns.
	 * </p>
	 * <p>
	 * Only columns generated by {@link SubmInfoColumnGenerator} for which {@link SubmInfoColumnGenerator #getColumnHeader(Localizer) getColumnHeader()} is in
	 * list of accepted titles are added to the generated list.
	 * </p>
	 * <p>
	 * This method can be useful when certain exercise-type has some uses {@link StatSubmInfoFilterTable} to show a more customized view on submissions made to
	 * it and wants to also return some or all of these custom columns from its {@link SubmissionStatisticsGiver #getAsTabularData()}-method.
	 * </p>
	 * 
	 * @param acceptedTitles
	 *            list of names that identify columns to export
	 * @return list of {@link StatisticsInfoColumn}s generated from accepted columns
	 */
	public List<StatisticsInfoColumn<?>> exportByColGenTitle(
			List<String> acceptedTitles) {
		List<StatisticsInfoColumn<?>> toExport = new ArrayList<StatisticsInfoColumn<?>>();
		
		for (int i = 0, n = colGenerators.size(); i < n; i++) {
			if (acceptedTitles.contains(colGenerators.get(i).getColumnHeader(
					localizer))) {
				toExport.add(toStatInfoColumn(colGenerators.get(i)));
			}
		}
		
		return toExport;
	}
	
	private <X> StatisticsInfoColumn<X> toStatInfoColumn(
			SubmInfoColumnGenerator<X, S> colGen) {
		String header = colGen.getColumnHeader(localizer);
		String desc = colGen.getColumnDescription(localizer);
		Class<X> type = colGen.getColumnDataType();
		List<X> colData = new ArrayList<X>();
		for (int j = 0; j < allStatInfos.size(); j++) {
			colData.add(colGen.getColValueFor(allStatInfos.get(j), localizer));
		}
		return new StatisticsInfoColumn<X>(header, desc, type, colData,
				colGen.isExportable());
	}
	
	/**
	 * Implementor of this interface can generate a column for the {@link StatSubmInfoFilterTable} and correct values for that column from
	 * {@link StatisticalSubmissionInfo}-objects.
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <T>
	 *            type of data shown in the column
	 * @param <A>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public interface SubmInfoColumnGenerator<T, A extends SubmissionInfo> {
		
		/**
		 * This method tells whether it makes sense (or whether it is even possible) to export data generated by this {@link SubmInfoColumnGenerator} outside of
		 * Vaadin and ViLLE. An example of when to return false, is the 'visualize'-column.
		 * 
		 * @return whether this column's data can be exported outside ViLLE
		 */
		boolean isExportable();
		
		/**
		 * @param localizer
		 *            {@link Localizer} for localizing the ui
		 * @return {@link String} to use as header for the generated column
		 */
		String getColumnHeader(Localizer localizer);
		
		/**
		 * @param localizer
		 *            {@link Localizer} for localizing the ui
		 * @return localized text telling what is shown in the column (eg. score)
		 */
		String getColumnDescription(Localizer localizer);
		
		/**
		 * 
		 * @return default value for the column
		 */
		T getDefaultValue();
		
		/**
		 * @return {@link Class}-file of the type used for values of this column
		 */
		Class<T> getColumnDataType();
		
		/**
		 * Return the value for this column for a {@link StatisticalSubmissionInfo}-object in a localized format.
		 * 
		 * @param statSubmInfo
		 *            {@link StatisticalSubmissionInfo}-object for which to generate a value
		 * @param localizer
		 *            {@link Localizer} for localizing UI
		 * @return value for the column for row corresponding to given statSubmInfo
		 */
		T getColValueFor(StatisticalSubmissionInfo<A> statSubmInfo,
				Localizer localizer);
				
	}
	
	/*
	 * 
	 * Default implementations for ColumnGenerators for the general properties
	 * (done-time, evaluation, time-on-task) start here.
	 */
	
	/**
	 * {@link SubmInfoColumnGenerator}-implementor for generating a 'done-time'-column. Added to {@link StatSubmInfoFilterTable} by default.
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <S>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public static class DoneTimeColumnGen<S extends SubmissionInfo> implements
			SubmInfoColumnGenerator<Date, S> {
			
		@Override
		public String getColumnHeader(Localizer localizer) {
			return localizer.getUIText(StandardUIConstants.TIME);
		}
		
		@Override
		public Date getDefaultValue() {
			return null;
		}
		
		@Override
		public Class<Date> getColumnDataType() {
			return Date.class;
		}
		
		@Override
		public Date getColValueFor(StatisticalSubmissionInfo<S> statSubmInfo,
				Localizer localizer) {
			return new Date(statSubmInfo.getDoneTime());
		}
		
		@Override
		public boolean isExportable() {
			return true;
		}
		
		@Override
		public String getColumnDescription(Localizer localizer) {
			return localizer
					.getUIText(StandardUIConstants.STATS_COL_DESC_DONE_TIME);
		}
		
	}
	
	/**
	 * {@link SubmInfoColumnGenerator}-implementor for generating a 'evaluation'-column. Added to {@link StatSubmInfoFilterTable} by default.
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <S>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public static class EvalColumnGen<S extends SubmissionInfo> implements
			SubmInfoColumnGenerator<Double, S> {
			
		@Override
		public String getColumnHeader(Localizer localizer) {
			return localizer.getUIText(StandardUIConstants.SCORE);
		}
		
		@Override
		public Double getDefaultValue() {
			return null;
		}
		
		@Override
		public Class<Double> getColumnDataType() {
			return Double.class;
		}
		
		@Override
		public Double getColValueFor(StatisticalSubmissionInfo<S> statSubmInfo,
				Localizer localizer) {
			return statSubmInfo.getEvalution();
		}
		
		@Override
		public boolean isExportable() {
			return true;
		}
		
		@Override
		public String getColumnDescription(Localizer localizer) {
			return localizer
					.getUIText(StandardUIConstants.STATS_COL_DESC_SCORE);
		}
		
	}
	
	/**
	 * {@link SubmInfoColumnGenerator}-implementor for generating a 'time-on-task'-column. Added to {@link StatSubmInfoFilterTable} by default.
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <S>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public static class TimeOnTaskColumnGen<S extends SubmissionInfo>
			implements SubmInfoColumnGenerator<Integer, S> {
			
		@Override
		public String getColumnHeader(Localizer localizer) {
			return localizer.getUIText(StandardUIConstants.TIME_USED);
		}
		
		@Override
		public Integer getDefaultValue() {
			return null;
		}
		
		@Override
		public Class<Integer> getColumnDataType() {
			return Integer.class;
		}
		
		@Override
		public Integer getColValueFor(
				StatisticalSubmissionInfo<S> statSubmInfo, Localizer localizer) {
			return statSubmInfo.getTimeOnTask();
		}
		
		@Override
		public boolean isExportable() {
			return true;
		}
		
		@Override
		public String getColumnDescription(Localizer localizer) {
			return localizer
					.getUIText(StandardUIConstants.STATS_COL_DESC_TIME_ON_TASK);
		}
		
	}
	
	/**
	 * <p>
	 * Helper method for parsing a {@link List} of {@link SubmInfoColumnGenerator}s usable with {@link StatSubmInfoFilterTable} from a list of
	 * {@link StatisticsInfoColumn}s.
	 * </p>
	 * <p>
	 * This method is analogous to {@link StatSubmInfoFilterTable #exportByColGenTitle(List)} in that it converts columns between the format used in
	 * {@link SubmissionStatisticsGiver #getAsTabularData()} and {@link StatSubmInfoFilterTable}. This time the conversion goes the other way around, meaning
	 * that if you already have implemented generation for some custom {@link StatisticsInfoColumn}s you can get those columns displayed in a
	 * {@link StatSubmInfoFilterTable} by using this method.
	 * </p>
	 * <p>
	 * The returned {@link SubmInfoColumnGenerator}s do not really do any hard work, they just map the already generated data and info from
	 * {@link StatisticsInfoColumn} in such a way that the resulting {@link TabularDataSetColumnGen} can act like a {@link SubmInfoColumnGenerator}.
	 * </p>
	 * 
	 * @param allSubmInfos
	 *            list of all {@link StatisticalSubmissionInfo}s (in correct order)
	 * @param extraInfoCols
	 *            list of {@link StatisticsInfoColumn}s for which to generate wrappers
	 * @return a list of {@link SubmInfoColumnGenerator} wrapping the {@link StatisticsInfoColumn}s
	 */
	public static <R extends SubmissionInfo> List<SubmInfoColumnGenerator<?, R>> parseTabularDataColGens(
			List<StatisticalSubmissionInfo<R>> allSubmInfos,
			List<StatisticsInfoColumn<?>> extraInfoCols) {
		List<SubmInfoColumnGenerator<?, R>> res = new ArrayList<SubmInfoColumnGenerator<?, R>>();
		
		Map<StatisticalSubmissionInfo<R>, Integer> submInfosToInd =
		
		new HashMap<StatisticalSubmissionInfo<R>, Integer>();
		
		for (int i = 0; i < allSubmInfos.size(); i++) {
			submInfosToInd.put(allSubmInfos.get(i), i);
		}
		
		for (StatisticsInfoColumn<?> sic : extraInfoCols) {
			res.add(parseTabularDataSetColGen(sic, submInfosToInd));
		}
		
		return res;
	}
	
	private static <X, S extends SubmissionInfo> TabularDataSetColumnGen<X, S> parseTabularDataSetColGen(
			StatisticsInfoColumn<X> toWrap,
			Map<StatisticalSubmissionInfo<S>, Integer> submInfosToOrigIndex) {
		return new TabularDataSetColumnGen<X, S>(toWrap, submInfosToOrigIndex);
	}
	
	/**
	 * A class for wrapping a {@link StatisticsInfoColumn} so that it can be used like {@link SubmInfoColumnGenerator}
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <A>
	 *            type of data shown in the column
	 * @param <S>
	 *            accepted {@link SubmissionInfo}-type >
	 * 
	 */
	public static class TabularDataSetColumnGen<A extends Object, S extends SubmissionInfo>
			implements SubmInfoColumnGenerator<A, S> {
			
		private final StatisticsInfoColumn<A> toWrap;
		private final Map<StatisticalSubmissionInfo<S>, Integer> submInfosToOrigIndex;
		
		/**
		 * Construct a new {@link TabularDataSetColumnGen} wrapping a {@link StatisticsInfoColumn} to {@link SubmInfoColumnGenerator}.
		 * 
		 * @param toWrap
		 *            {@link StatisticsInfoColumn} to wrap
		 * @param submInfosToOrigIndex
		 *            mapping from {@link StatisticalSubmissionInfo} to index the object in original data-set; used to select correct value from
		 *            {@link StatisticsInfoColumn}s value when "generating" columns to new rows
		 */
		public TabularDataSetColumnGen(StatisticsInfoColumn<A> toWrap,
				Map<StatisticalSubmissionInfo<S>, Integer> submInfosToOrigIndex) {
			this.toWrap = toWrap;
			this.submInfosToOrigIndex = submInfosToOrigIndex;
		}
		
		@Override
		public String getColumnHeader(Localizer localizer) {
			return toWrap.getName();
		}
		
		@Override
		public A getDefaultValue() {
			return null;
		}
		
		@Override
		public Class<A> getColumnDataType() {
			return toWrap.getColDataType();
		}
		
		@Override
		public A getColValueFor(StatisticalSubmissionInfo<S> statSubmInfo,
				Localizer localizer) {
			return toWrap.getDataObjects().get(
					submInfosToOrigIndex.get(statSubmInfo));
		}
		
		@Override
		public boolean isExportable() {
			return toWrap.isExportable();
		}
		
		@Override
		public String getColumnDescription(Localizer localizer) {
			return toWrap.getDescription();
		}
		
	}
	
	/**
	 * <p>
	 * Implements {@link SubmInfoColumnGenerator} in such a way that it generates a column with a button for opening a popup with {@link SubmissionVisualizer}
	 * for visualizing the {@link SubmissionInfo} corresponding to given row.
	 * </p>
	 * <p>
	 * <b>For safely using this method the {@link SubmissionVisualizer} of this type as well as {@link SubmissionStatisticsGiver} must treat the
	 * {@link ExerciseData}-object as immutable, as all the generated submission-viewers will share the same {@link ExerciseData}-instance.</b>
	 * </p>
	 * 
	 * @author rahaav
	 * 
	 * @param <E>
	 *            accepted {@link ExerciseData} implementor
	 * @param <S>
	 *            accepted {@link SubmissionInfo} implementor
	 */
	public static class ShowSubmissionColGenerator<E extends ExerciseData, S extends SubmissionInfo>
			implements SubmInfoColumnGenerator<Button, S> {
			
		private final ExerciseTypeDescriptor<E, S> typeDesc;
		private final E exerData;
		private final TempFilesManager tempMan;
		
		/**
		 * Constructs a {@link ShowSubmissionColGenerator} that can be added to {@link StatSubmInfoFilterTable} to generate a column with a button for opening a
		 * popup with {@link SubmissionVisualizer} for visualizing the {@link SubmissionInfo} corresponding to given row.
		 * 
		 * @param typeDesc
		 *            {@link ExerciseTypeDescriptor} of the exercise-type for which the class is used
		 * @param exerData
		 *            {@link ExerciseData}-object corresponding to the exercise-instance for which statistics are shown
		 * @param tempMan
		 *            {@link TempFilesManager} in use; can be null if it is known that exercise-type does not use it
		 */
		public ShowSubmissionColGenerator(
				ExerciseTypeDescriptor<E, S> typeDesc, E exerData,
				TempFilesManager tempMan) {
			this.typeDesc = typeDesc;
			this.exerData = exerData;
			this.tempMan = tempMan;
		}
		
		@Override
		public String getColumnHeader(Localizer localizer) {
			// TODO FIXME: localize
			return "Show submission";
		}
		
		@Override
		public Button getDefaultValue() {
			return null;
		}
		
		@Override
		public Class<Button> getColumnDataType() {
			return Button.class;
		}
		
		@Override
		public Button getColValueFor(
				final StatisticalSubmissionInfo<S> statSubmInfo,
				final Localizer localizer) {
			// TODO localize
			Button res = StandardUIFactory.getButton("Show", null);
			
			res.addClickListener(new Button.ClickListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 7188121454165685244L;
				
				@Override
				public void buttonClick(ClickEvent event) {
					SubmissionVisualizer<E, S> vis = typeDesc
							.newSubmissionVisualizer();
							
					Window popup = new Window();
					popup.setWidth("80%");
					popup.setHeight("80%");
					popup.setModal(true);
					popup.center();
					
					VerticalLayout layout = new VerticalLayout();
					
					layout.setWidth("100%");
					layout.setMargin(true);
					
					try {
						vis.initialize(exerData,
								statSubmInfo.getSubmissionData(), localizer,
								tempMan);
						layout.addComponent(vis.getView());
					} catch (ExerciseException e) {
						e.printStackTrace();
						layout.addComponent(StandardUIFactory
								.getWarningPanel("Could not load visualization!"));
					}
					
					popup.setContent(layout);
					
					UI.getCurrent().addWindow(popup);
					
				}
			});
			
			return res;
		}
		
		@Override
		public boolean isExportable() {
			// would not make sense to export button-column
			return false;
		}
		
		@Override
		public String getColumnDescription(Localizer localizer) {
			return null;
		}
	}
	
}
