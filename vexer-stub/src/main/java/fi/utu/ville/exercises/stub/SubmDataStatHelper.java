package fi.utu.ville.exercises.stub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.ExerciseTypeDescriptor;
import fi.utu.ville.exercises.model.StatisticalSubmissionInfo;
import fi.utu.ville.exercises.model.StatisticsInfoColumn;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.exercises.model.SubmissionStatisticsGiver;
import fi.utu.ville.exercises.model.SubmissionVisualizer;
import fi.utu.ville.standardutils.FormattingUtils;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardUIConstants;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.TempFilesManager;

/**
 * A helper wrapping {@link SubmissionStatisticsGiver}-instance and giving certain general statistics-info in addition to exercise-type specific
 * statistic-infos.
 * 
 * @author Riku Haavisto
 * 
 * @param <E>
 *            {@link ExerciseData} to use
 * @param <S>
 *            {@link SubmissionInfo} to use
 */
public class SubmDataStatHelper<E extends ExerciseData, S extends SubmissionInfo>
		implements Serializable {
		
	/**
	 * Constructs and returns a new {@link SubmDataStatHelper}. Wraps the other constructor method so that explicit type-parameters are not needed.
	 * 
	 * @param localizer
	 *            {@link Localizer} for localizing the UI
	 * @param tempMan
	 *            {@link TempFilesManager} for managing temporary files
	 * @param exerName
	 *            name (or id) of the exercise-instance to load
	 * @param typeDesc
	 *            {@link ExerciseTypeDescriptor} for the exercise-type to load
	 * @return newly constructed {@link SubmDataStatHelper}
	 * @throws ExerciseException
	 *             if something goes wrong when loading {@link SubmissionStatisticsGiver}
	 */
	public static SubmDataStatHelper<? extends ExerciseData, ? extends SubmissionInfo> loadFor(
			Localizer localizer, TempFilesManager tempMan, String exerName,
			ExerciseTypeDescriptor<?, ?> typeDesc) throws ExerciseException {
			
		return loadWithDesc(localizer, tempMan, exerName, typeDesc);
	}
	
	/**
	 * Constructs and returns a new {@link SubmDataStatHelper}.
	 * 
	 * @param localizer
	 *            {@link Localizer} for localizing the UI
	 * @param tempMan
	 *            {@link TempFilesManager} for managing temporary files
	 * @param exerName
	 *            name (or id) of the exercise-instance to load
	 * @param typeDesc
	 *            {@link ExerciseTypeDescriptor} for the exercise-type to load
	 * @return newly constructed {@link SubmDataStatHelper}
	 * @throws ExerciseException
	 *             if something goes wrong when loading {@link SubmissionStatisticsGiver}
	 */
	public static <F extends ExerciseData, R extends SubmissionInfo> SubmDataStatHelper<F, R> loadWithDesc(
			Localizer localizer, TempFilesManager tempMan, String exerName,
			ExerciseTypeDescriptor<F, R> desc) throws ExerciseException {
			
		List<StatisticalSubmissionInfo<R>> statInfos = StubDataFilesHandler
				.loadAllSubmissions(desc, exerName, desc.getSubDataClass(),
						tempMan);
						
		F e = StubDataFilesHandler.loadExerTypeData(desc.getTypeDataClass(),
				desc.newExerciseXML(), desc, exerName, tempMan);
		SubmissionStatisticsGiver<F, R> toUse = desc.newStatisticsGiver();
		toUse.initialize(e, statInfos, localizer, tempMan);
		
		return new SubmDataStatHelper<F, R>(localizer, statInfos, toUse, desc,
				tempMan, exerName);
	}
	
	/**
	 * An enum-collection of different statistic-column types and their properties. Also contains place-holders for column-types that require special treatment.
	 * 
	 * @author Riku Haavisto
	 * 
	 */
	public enum StatColumnType {
		NAME(StandardUIConstants.NAME, StubUiConstants.STATS_COL_DESC_NAME, String.class, new StatRowParser() {
			@Override
			public Object parse(StatisticalSubmissionInfo<?> parseFrom) {
				return "Name-place-holder";
			}
		}, true),
		
		SCORE(StandardUIConstants.SCORE, StandardUIConstants.STATS_COL_DESC_SCORE, Double.class, new StatRowParser() {
			
			@Override
			public Object parse(StatisticalSubmissionInfo<?> parseFrom) {
				return parseFrom.getEvalution();
			}
			
		}, true),
		
		DONE_TIME(StandardUIConstants.DATE, StandardUIConstants.STATS_COL_DESC_DONE_TIME, Date.class, new StatRowParser() {
			
			@Override
			public Object parse(StatisticalSubmissionInfo<?> parseFrom) {
				return new Date(parseFrom.getDoneTime());
			}
			
		}, true),
		
		TIME_ON_TASK(StandardUIConstants.TIME_USED, StandardUIConstants.STATS_COL_DESC_TIME_ON_TASK, Integer.class, new StatRowParser() {
			
			@Override
			public Object parse(StatisticalSubmissionInfo<?> parseFrom) {
				return parseFrom.getTimeOnTask();
			}
			
		}, true),
		
		// PLACE_HOLDER for all the type-specific columns!
		TYPE_SPEC(null, null, null, null, false),
		
		// This is also easier to implement as a special case...
		VISUALIZE_BTN(null, null, null, null, false);
		
		private final String uiConst;
		private final String descUiConst;
		private final Class<?> colType;
		private final StatRowParser dataParser;
		private final boolean exportable;
		
		private StatColumnType(String uiConst, String descUiConst,
				Class<?> colType, StatRowParser dataParser, boolean exportable) {
			this.uiConst = uiConst;
			this.descUiConst = descUiConst;
			this.colType = colType;
			this.dataParser = dataParser;
			this.exportable = exportable;
		}
		
	}
	
	/**
	 * Implementors of this interface know how to parse an object-value for certain column from certain base data-row ( {@link StatisticalSubmissionInfo}).
	 * 
	 * @author Riku Haavisto
	 */
	private interface StatRowParser {
		Object parse(StatisticalSubmissionInfo<?> parseFrom);
	}
	
	private static final long serialVersionUID = -4073802188668202409L;
	
	private final List<StatisticalSubmissionInfo<S>> answers;
	
	private final ExerciseTypeDescriptor<E, S> exerDesc;
	
	private final SubmissionStatisticsGiver<E, S> currStatsGiver;
	
	private final Localizer localizer;
	
	private final TempFilesManager tempMan;
	
	private final HashMap<StatColumnType, StatisticsInfoColumn<?>> colsByType
	
	= new HashMap<StatColumnType, StatisticsInfoColumn<?>>();
	
	private List<StatisticsInfoColumn<?>> specColumns = null;
	
	private final String exerName;
	
	/**
	 * Constructs a new {@link SubmDataStatHelper}
	 * 
	 * @param localizer
	 *            {@link Localizer}
	 * @param subms
	 *            list of {@link StatisticalSubmissionInfo}-objects (ie. data collected from submissions made to an exercise-instance)
	 * @param statsGiver
	 *            exercise-type specific {@link SubmissionStatisticsGiver} implementor
	 * @param exerDesc
	 *            {@link ExerciseTypeDescriptor} for currently used exercise-type
	 * @param tempMan
	 *            {@link TempFilesManager}
	 * @param exerName
	 *            name (or id) of the exercise-instance to load
	 */
	public SubmDataStatHelper(Localizer localizer,
			List<StatisticalSubmissionInfo<S>> subms,
			SubmissionStatisticsGiver<E, S> statsGiver,
			ExerciseTypeDescriptor<E, S> exerDesc, TempFilesManager tempMan,
			String exerName) {
		this.localizer = localizer;
		this.exerDesc = exerDesc;
		this.exerName = exerName;
		this.tempMan = tempMan;
		answers = new ArrayList<StatisticalSubmissionInfo<S>>(subms);
		this.currStatsGiver = statsGiver;
	}
	
	/**
	 * @return all generated {@link StatisticsInfoColumn}s
	 */
	public List<StatisticsInfoColumn<?>> getAllStatCols() {
		return getStatColumns(Arrays.asList(StatColumnType.values()));
	}
	
	/**
	 * Generates {@link StatisticsInfoColumn}s from all specified {@link StatColumnType}s and returns the generated {@link StatisticsInfoColumn}s.
	 * 
	 * @param includedCols
	 *            {@link StatColumnType}s to include
	 * @return list of generated {@link StatisticsInfoColumn}s
	 */
	public List<StatisticsInfoColumn<?>> getStatColumns(
			List<StatColumnType> includedCols) {
			
		List<StatisticsInfoColumn<?>> res = new ArrayList<StatisticsInfoColumn<?>>();
		
		for (StatColumnType colType : includedCols) {
			if (colType == StatColumnType.TYPE_SPEC) {
				if (specColumns == null) {
					loadStatInfoColumn(colType);
				}
				res.addAll(specColumns);
			} else {
				StatisticsInfoColumn<?> col = colsByType.get(colType);
				if (col == null) {
					loadStatInfoColumn(colType);
					col = colsByType.get(colType);
				}
				res.add(col);
			}
		}
		return res;
		
	}
	
	/**
	 * Loads {@link StatisticsInfoColumn} matching given {@link StatColumnType} and current data in the {@link SubmDataStatHelper} if the
	 * {@link StatisticsInfoColumn} is not already loaded.
	 * 
	 * @param colType
	 *            {@link StatColumnType} for which to load the data
	 */
	private void loadStatInfoColumn(StatColumnType colType) {
		if (colType == StatColumnType.TYPE_SPEC) {
			specColumns = currStatsGiver.getAsTabularData();
		} else if (colType == StatColumnType.VISUALIZE_BTN) {
			List<Button> res = new ArrayList<Button>();
			for (int i = 0; i < answers.size(); i++) {
				final int ind = i;
				Button btn = StandardUIFactory.getButton(
						localizer.getUIText(StandardUIConstants.VISUALIZE),
						null);
						
				btn.addClickListener(new Button.ClickListener() {
					
					/**
					 * 
					 */
					private static final long serialVersionUID = -3307319962895003537L;
					
					@Override
					public void buttonClick(ClickEvent event) {
						viewUserSubmission(answers.get(ind));
					}
				});
				
				res.add(btn);
			}
			colsByType
					.put(colType,
							new StatisticsInfoColumn<Button>(
									localizer
											.getUIText(StandardUIConstants.VISUALIZE),
									localizer
											.getUIText(StandardUIConstants.STATS_COL_DESC_VISUALIZE),
									Button.class, res, false));
									
		} else {
			StatisticsInfoColumn<?> statCol = getStatInfoColumn(
					localizer.getUIText(colType.uiConst),
					localizer.getUIText(colType.descUiConst), colType.colType,
					colType.dataParser, answers, colType.exportable);
			colsByType.put(colType, statCol);
		}
	}
	
	/**
	 * Parses a {@link StatisticsInfoColumn} from given arguments.
	 * 
	 * @param title
	 *            title for the {@link StatisticsInfoColumn}
	 * @param desc
	 *            description for the {@link StatisticsInfoColumn}
	 * @param type
	 *            type of data values of the {@link StatisticsInfoColumn}
	 * @param parser
	 *            {@link StatRowParser} for parsing data-values for the column
	 * @param stats
	 *            list of {@link StatisticalSubmissionInfo}-objects from which to parse data
	 * @param exportable
	 *            whether resulting {@link StatisticsInfoColumn} is exportable
	 * @return parsed {@link StatisticsInfoColumn}
	 */
	private static <X, S extends SubmissionInfo> StatisticsInfoColumn<X> getStatInfoColumn(
			String title, String desc, Class<X> type, StatRowParser parser,
			List<StatisticalSubmissionInfo<S>> stats, boolean exportable) {
		List<X> res = new ArrayList<X>();
		for (int i = 0; i < stats.size(); i++) {
			res.add(type.cast(parser.parse(stats.get(i))));
		}
		return new StatisticsInfoColumn<X>(title, desc, type, res, exportable);
	}
	
	/**
	 * Loads a {@link SubmissionVisualizer} with given {@link StatisticalSubmissionInfo} and {@link TempFilesManager} and with parameters from current state of
	 * the {@link SubmDataStatHelper}.
	 * 
	 * @param statInfo
	 *            {@link StatisticalSubmissionInfo} to load
	 * @param tempManToUse
	 *            {@link TempFilesManager} to use
	 * @return initialized {@link SubmissionVisualizer}
	 * @throws ExerciseException
	 *             if something goes wrong when initializing the submission-viewer
	 */
	private SubmissionVisualizer<?, ?> getSubmissionViewerFor(
			StatisticalSubmissionInfo<?> statInfo, TempFilesManager tempManToUse)
					throws ExerciseException {
		SubmissionVisualizer<E, S> res = exerDesc.newSubmissionVisualizer(exerName,localizer);
		
		SubmissionInfo sInfo = statInfo.getSubmissionData();
		
		// this will throw exception if SInfo is of wrong class;
		// however that should not happen as long as the
		// rowToVis is fetched from 'this'-SubmDataStatCollection
		S castedSInfo = exerDesc.getSubDataClass().cast(sInfo);
		
		E exerData = StubDataFilesHandler.loadExerTypeData(
				exerDesc.getTypeDataClass(), exerDesc.newExerciseXML(),
				exerDesc, exerName, tempManToUse);
				
		res.initialize(exerData, castedSInfo, localizer, tempManToUse);
		return res;
		
	}
	
	/**
	 * Loads and opens to a pop-up a {@link SubmissionVisualizer} for given {@link StatisticalSubmissionInfo}-object and currently used exercise-instance and
	 * -type.
	 * 
	 * @param {@link
	 * 			StatisticalSubmissionInfo} for which to load the {@link SubmissionVisualizer}
	 */
	public void viewUserSubmission(StatisticalSubmissionInfo<?> statInfo) {
		
		final Window dialogWindow = new Window(localizer.getUIText(
				StubUiConstants.ANSWERS_FOR_USER, "NAME_PLACE_HOLDER"));
		VerticalLayout dialL = new VerticalLayout();
		dialL.setMargin(true);
		dialL.setSpacing(true);
		dialogWindow.setContent(dialL);
		dialogWindow.setModal(true);
		dialogWindow.setWidth("1100px");
		dialogWindow.setHeight("95%");
		
		dialL.addComponent(new Label(localizer
				.getUIText(StandardUIConstants.TIME_USED)
				+ ": "
				+ FormattingUtils.formatSeconds(statInfo.getTimeOnTask())));
				
		dialL.addComponent(new Label(localizer
				.getUIText(StandardUIConstants.SCORE)
				+ ": "
				+ statInfo.getEvalution()));
				
		tempMan.initialize();
		final TempFilesManager tempManForVis = tempMan.createChild();
		try {
			SubmissionVisualizer<?, ?> vis = getSubmissionViewerFor(statInfo,
					tempManForVis);
			dialL.addComponent(vis.getView());
		} catch (ExerciseException e) {
			e.printStackTrace();
			tempManForVis.shutdown();
		}
		
		dialogWindow.addCloseListener(new CloseListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 8437686439637510732L;
			
			@Override
			public void windowClose(CloseEvent e) {
				// not absolutely necessary but a good idea
				tempManForVis.shutdown();
			}
		});
		
		Button closeButton = new Button(
				localizer.getUIText(StandardUIConstants.CLOSE));
		closeButton.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7706112506186975914L;
			
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().removeWindow(dialogWindow);
			}
		});
		
		dialL.addComponent(closeButton);
		
		UI.getCurrent().addWindow(dialogWindow);
		
	}
	
	/**
	 * @return the statistics-view from the current {@link SubmissionStatisticsGiver}
	 */
	public Component getSpecificStatsView() {
		return currStatsGiver.getView();
	}
	
}
