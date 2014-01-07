package edu.vserver.exercises.stub;

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

import edu.vserver.exercises.model.ExerciseData;
import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.ExerciseTypeDescriptor;
import edu.vserver.exercises.model.StatisticalSubmissionInfo;
import edu.vserver.exercises.model.StatisticsInfoColumn;
import edu.vserver.exercises.model.SubmissionInfo;
import edu.vserver.exercises.model.SubmissionStatisticsGiver;
import edu.vserver.exercises.model.SubmissionVisualizer;
import edu.vserver.standardutils.FormattingUtils;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.StandardUIConstants;
import edu.vserver.standardutils.StandardUIFactory;
import edu.vserver.standardutils.TempFilesManager;

public class SubmDataStatHelper<E extends ExerciseData, S extends SubmissionInfo>
		implements Serializable {

	public static SubmDataStatHelper<? extends ExerciseData, ? extends SubmissionInfo> loadFor(
			Localizer localizer, TempFilesManager tempMan, String exerName,
			ExerciseTypeDescriptor<?, ?> typeDesc) throws ExerciseException {

		return loadWithDesc(localizer, tempMan, exerName, typeDesc);
	}

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

	public enum StatColumnType {
		NAME(StandardUIConstants.NAME, StubUiConstants.STATS_COL_DESC_NAME,
				String.class, new StatRowParser() {
					@Override
					public Object parse(StatisticalSubmissionInfo<?> parseFrom) {
						return "Name-place-holder";
					}
				}, true),

		SCORE(StandardUIConstants.SCORE,
				StandardUIConstants.STATS_COL_DESC_SCORE, Double.class,
				new StatRowParser() {

					@Override
					public Object parse(StatisticalSubmissionInfo<?> parseFrom) {
						return parseFrom.getEvalution();
					}

				}, true),

		DONE_TIME(StandardUIConstants.DATE,
				StandardUIConstants.STATS_COL_DESC_DONE_TIME, Date.class,
				new StatRowParser() {

					@Override
					public Object parse(StatisticalSubmissionInfo<?> parseFrom) {
						return new Date(parseFrom.getDoneTime());
					}

				}, true),

		TIME_ON_TASK(StandardUIConstants.TIME_USED,
				StandardUIConstants.STATS_COL_DESC_TIME_ON_TASK, Integer.class,
				new StatRowParser() {

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
	 * Create new Survey Statistics object
	 * 
	 * @param mainState
	 *            the main state object for DB queries and such
	 * @param assignmentId
	 *            the id of the Survey assignment to be viewed
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

	public List<StatisticsInfoColumn<?>> getAllStatCols() {
		return getStatColumns(Arrays.asList(StatColumnType.values()));
	}

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

	private void loadStatInfoColumn(StatColumnType colType) {
		if (colType == StatColumnType.TYPE_SPEC) {
			specColumns = currStatsGiver.getAsTabularData();
		} else if (colType == StatColumnType.VISUALIZE_BTN) {
			List<Button> res = new ArrayList<Button>();
			for (int i = 0; i < answers.size(); i++) {
				final int ind = i;
				Button btn = StandardUIFactory.getDefaultButton(
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

	private static <X, S extends SubmissionInfo> StatisticsInfoColumn<X> getStatInfoColumn(
			String title, String desc, Class<X> type, StatRowParser parser,
			List<StatisticalSubmissionInfo<S>> stats, boolean exportable) {
		List<X> res = new ArrayList<X>();
		for (int i = 0; i < stats.size(); i++) {
			res.add(type.cast(parser.parse(stats.get(i))));
		}
		return new StatisticsInfoColumn<X>(title, desc, type, res, exportable);
	}

	public SubmissionVisualizer<?, ?> getSubmissionViewerFor(
			StatisticalSubmissionInfo<?> statInfo, TempFilesManager tempManToUse)
			throws ExerciseException {
		SubmissionVisualizer<E, S> res = exerDesc.newSubmissionVisualizer();

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
	 * Views statistics for given user and current survey in a modal popup
	 * window
	 * 
	 * @param userId
	 *            the id of the user whose statistics are viewed
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

	public Component getSpecificStatsView() {
		return currStatsGiver.getView();
	}

}
