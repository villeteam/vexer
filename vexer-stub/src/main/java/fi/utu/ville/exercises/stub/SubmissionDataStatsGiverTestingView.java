package fi.utu.ville.exercises.stub;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import fi.utu.ville.exercises.helpers.StatsGiverHelper;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.ExerciseTypeDescriptor;
import fi.utu.ville.exercises.model.StatisticsInfoColumn;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.exercises.model.SubmissionStatisticsGiver;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardUIConstants;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.TempFilesManager;

/**
 * A quick-and-dirty class for loading {@link SubmissionStatisticsGiver}
 * -implementor for testing.
 * 
 * @author Riku Haavisto
 * 
 * @param <E>
 *            tested {@link ExerciseData}-implementor
 * @param <S>
 *            tested {@link SubmissionInfo}-implementor
 */
class SubmissionDataStatsGiverTestingView<E extends ExerciseData, S extends SubmissionInfo>
		extends VerticalLayout {

	private static final Logger logger = Logger
			.getLogger(SubmissionDataStatsGiverTestingView.class.getName());

	private static final String submissionStatGiverTestingViewId = "statgiverview";

	private static final long serialVersionUID = 2019222167594454557L;

	/**
	 * Constructs and returns a new {@link SubmissionDataStatsGiverTestingView}.
	 * Explicit type-parameters are not needed when using this factory-method.
	 * 
	 * @param toLoad
	 *            {@link ExerciseTypeDescriptor} for exer-type for which to load
	 *            a {@link SubmissionStatisticsGiver}
	 * @param exerName
	 *            name (or id) of the exercise-instance to load
	 * @param localizer
	 *            {@link Localizer} for localizing the UI
	 * @param tempMan
	 *            {@link TempFilesManager} for managing temporary files
	 * @return newly constructed {@link SubmissionDataStatsGiverTestingView}
	 */
	public static SubmissionDataStatsGiverTestingView<?, ?> getViewFor(
			ExerciseTypeDescriptor<?, ?> toLoad, String exerName,
			Localizer localizer, TempFilesManager tempMan) {

		SubmDataStatHelper<? extends ExerciseData, ? extends SubmissionInfo> toUse = null;
		try {
			toUse = SubmDataStatHelper.loadFor(localizer, tempMan, exerName,
					toLoad);
		} catch (ExerciseException e) {
			// TODO FIXME; report this to UI also; even in stub..?
			logger.log(Level.SEVERE, "Problem loading statistics-giver", e);
		}
		return getViewFor(toUse, localizer);
	}

	/**
	 * Constructs and returns a new {@link SubmissionDataStatsGiverTestingView}.
	 * 
	 * @param backingDataColl
	 *            {@link SubmDataStatHelper}-helper class holding needed data
	 *            and the actual {@link SubmissionStatisticsGiver}
	 * @param localizer
	 *            {@link Localizer} for localizing the UI
	 * @return newly constructed {@link SubmissionDataStatsGiverTestingView}
	 */
	private static <E extends ExerciseData, S extends SubmissionInfo> SubmissionDataStatsGiverTestingView<E, S> getViewFor(
			SubmDataStatHelper<E, S> backingDataColl, Localizer localizer) {

		return new SubmissionDataStatsGiverTestingView<E, S>(localizer,
				backingDataColl);
	}

	private final SubmDataStatHelper<E, S> backingDataColl;
	private final Localizer localizer;

	/**
	 * Conscructs a new {@link SubmissionDataStatsGiverTestingView}.
	 * 
	 * @param main
	 *            {@link Localizer} for localizing the UI
	 * @param backingDataColl
	 *            {@link SubmDataStatHelper} holding the actual data used in
	 *            this view
	 */
	private SubmissionDataStatsGiverTestingView(Localizer main,
			SubmDataStatHelper<E, S> backingDataColl) {

		setId(submissionStatGiverTestingViewId);

		this.localizer = main;
		this.backingDataColl = backingDataColl;

		doLayout();
	}

	/**
	 * Loads the layout for this view
	 */
	private void doLayout() {
		setMargin(true);
		setSpacing(true);
		setWidth("100%");
		removeAllComponents();

		this.addComponent(new StubViewHeaderBar(localizer, localizer
				.getUIText(StubUiConstants.STATS_TEST_INFO)));
		List<StatisticsInfoColumn<?>> statsCols = backingDataColl
				.getAllStatCols();
		final Component legendComp = StatsGiverHelper.getTableColsLegend(
				statsCols, localizer);
		final Table exportableDataTable = StatsGiverHelper
				.getTableFromStatCols(statsCols);
		final Set<Integer> nonExpIds = StatsGiverHelper
				.getNonExportableColIds(statsCols);
		exportableDataTable.setWidth("100%");

		addComponent(exportableDataTable);

		Button showLegend = new Button(
				localizer.getUIText(StandardUIConstants.COLUMNS_LEGEND));

		showLegend.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 5243795826485743165L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window specViewDial = StandardUIFactory.getModalWindow("80%",
						"spec-stats-view");
				((Layout) specViewDial.getContent()).addComponent(legendComp);
				UI.getCurrent().addWindow(specViewDial);

			}

		});

		addComponent(showLegend);

		Button exportExcelBtn = new Button("Export to excel");

		exportExcelBtn.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8544436928176494097L;

			@Override
			public void buttonClick(ClickEvent event) {

				exportableDataTable.setColumnCollapsingAllowed(true);
				for (Integer id : nonExpIds) {
					exportableDataTable.setColumnCollapsed(id, true);
				}

				ExcelExport exporter = new ExcelExport(exportableDataTable);
				exporter.excludeCollapsedColumns();
				exporter.export();
				for (Integer id : nonExpIds) {
					exportableDataTable.setColumnCollapsed(id, false);
				}
				exportableDataTable.setColumnCollapsingAllowed(false);
			}
		});

		addComponent(exportExcelBtn);

		// addComponent(StandardUIFactory.getHr());

		Button specViewBtn = StandardUIFactory.getButton("spec-stats-view",
				null);

		final Component specView = backingDataColl.getSpecificStatsView();

		if (specView != null) {
			specViewBtn.addClickListener(new Button.ClickListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = -825604045826922358L;

				@Override
				public void buttonClick(ClickEvent event) {
					Window specViewDial = StandardUIFactory.getModalWindow(
							"80%", "spec-stats-view");
					((Layout) specViewDial.getContent()).addComponent(specView);
					UI.getCurrent().addWindow(specViewDial);

				}
			});
		} else {
			specViewBtn.setEnabled(false);
		}
		this.addComponent(specViewBtn);

	}
}
