package edu.vserver.exercises.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.model.StatisticalSubmissionInfo;
import edu.vserver.exercises.model.StatisticsInfoColumn;
import edu.vserver.exercises.model.SubmissionInfo;
import edu.vserver.exercises.model.SubmissionStatisticsGiver;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.StandardUIConstants;

/**
 * <p>
 * A helper class containing method for filtering
 * {@link StatisticalSubmissionInfo}-objects and with implementors of
 * {@link StatSubmInfoFilter} -interface.
 * </p>
 * <p>
 * Also contains standard implementations of {@link StatSubmInfoFilterConnector}
 * for grouping {@link StatSubmInfoFilter}s ( {@link MatchAllFilter},
 * {@link MatchAnyFilter} ). And {@link StatSubmInfoFilter}-implementor for
 * inverting another filter ({@link InvertedFilter}), and for filtering by
 * general {@link StatisticalSubmissionInfo}-properties ( {@link DateFilter},
 * {@link EvaluationFilter}, {@link TimeOnTaskFilter}).
 * </p>
 * <p>
 * {@link SubmMatcher}-interface and {@link BySubmMatcher}-filter can be used to
 * create {@link StatSubmInfoFilter}s that only care about properties of a
 * {@link SubmissionInfo}-object.
 * </p>
 * 
 * @author Riku Haavisto
 * 
 * @see StatSubmInfoFilterEditor
 * @see StatSubmInfoFilterTable
 * @see StatisticalSubmissionInfo
 * @see SubmissionStatisticsGiver
 * 
 */
public class StatsGiverHelper {

	/**
	 * <p>
	 * Returns a table parsed from the {@link StatisticsInfoColumn}s. <b>It is
	 * absolutely necessary that all {@link StatisticsInfoColumn}s have same
	 * number of data-objects (nulls are allowed) and match the order of input
	 * data.</b>
	 * </p>
	 * <p>
	 * Table's columns will have as their property-id the index of certain
	 * {@link StatisticsInfoColumn} in the {@link StatisticsInfoColumn}-list.
	 * Table's rows will have as their property-ids the index of the row's data
	 * in (all of the) {@link StatisticsInfoColumn #getDataObjects()}-lists.
	 * </p>
	 * <p>
	 * The returned {@link Table} will contain the data, but otherwise is in
	 * default state.
	 * </p>
	 * 
	 * @param colsToLoad
	 *            {@link StatisticsInfoColumn} to load into a table
	 * @return {@link Table} containing data from colsToLoad
	 */
	public static Table getTableFromStatCols(
			List<StatisticsInfoColumn<?>> colsToLoad) {

		Table res = new Table();

		if (colsToLoad.isEmpty()) {
			return res;
		}

		// init table-props
		for (int i = 0; i < colsToLoad.size(); i++) {
			res.addContainerProperty(i, colsToLoad.get(i).getColDataType(),
					null);
			// using the index as column id avoids problems
			// with identical column-names...
			res.setColumnHeader(i, colsToLoad.get(i).getName());
		}

		// all the StatisticsInfoColumns are required to hold
		// as many data-values (nulls are allowed, but dataObj-lists must
		// be of same size)
		int numOfRows = colsToLoad.get(0).getDataObjects().size();
		// parse and load data
		for (int i = 0; i < numOfRows; i++) {
			Object[] cells = new Object[colsToLoad.size()];
			for (int j = 0; j < colsToLoad.size(); j++) {
				cells[j] = colsToLoad.get(j).getDataObjects().get(i);
			}
			res.addItem(cells, i);
		}

		return res;
	}

	/**
	 * Returns ids (indexes in the list) of columns for which
	 * {@link StatisticsInfoColumn #isExportable()} returns false.
	 * 
	 * @param cols
	 *            list of {@link StatisticsInfoColumn}s
	 * @return indices of columns that are not exportable
	 */
	public static Set<Integer> getNonExportableColIds(
			List<StatisticsInfoColumn<?>> cols) {

		Set<Integer> res = new HashSet<Integer>();

		for (int i = 0; i < cols.size(); i++) {
			if (!cols.get(i).isExportable()) {
				res.add(i);
			}
		}

		return res;
	}

	/**
	 * Parses a {@link Component} with simple legend about what kind of data
	 * each column has.
	 * 
	 * @param forCols
	 *            list of {@link StatisticsInfoColumn}s
	 * @param localizer
	 *            {@link Localizer} for localizing UI
	 * @return a legend-{@link Component} parsed in default way
	 */
	public static Component getTableColsLegend(
			List<StatisticsInfoColumn<?>> forCols, Localizer localizer) {
		VerticalLayout res = new VerticalLayout();

		res.addComponent(new Label(localizer
				.getUIText(StandardUIConstants.COLUMNS_LEGEND)));

		for (StatisticsInfoColumn<?> col : forCols) {
			res.addComponent(new Label("--"));
			res.addComponent(new Label(localizer.getUIText(
					StandardUIConstants.DESC_FOR_COL_X, col.getName())));
			res.addComponent(new Label(col.getDescription()));

		}

		return res;
	}

	/**
	 * Returns a new {@link List} containing all
	 * {@link StatisticalSubmissionInfo}-objects from the toFilter list matching
	 * {@link StatSubmInfoFilter} filter.
	 * 
	 * @param toFilter
	 *            {@link List} of {@link StatisticalSubmissionInfo}-objects to
	 *            be filtered
	 * @param filter
	 *            {@link StatSubmInfoFilter} to use
	 * @return {@link List} of {@link StatisticalSubmissionInfo}-objects from
	 *         toFilter matching filter
	 */
	public static <S extends SubmissionInfo>

	List<StatisticalSubmissionInfo<S>> filterStatInfos(
			List<StatisticalSubmissionInfo<S>> toFilter,
			StatSubmInfoFilter<S> filter) {
		List<StatisticalSubmissionInfo<S>> res = new ArrayList<StatisticalSubmissionInfo<S>>();

		for (StatisticalSubmissionInfo<S> anItem : toFilter) {
			if (filter.matches(anItem)) {
				res.add(anItem);
			}
		}
		return res;
	}

	/**
	 * Implementor of {@link StatSubmInfoFilter}-interface can be used to filter
	 * {@link StatisticalSubmissionInfo}-objects.
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <A>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public interface StatSubmInfoFilter<A extends SubmissionInfo> {

		/**
		 * Test whether certain {@link StatisticalSubmissionInfo} matches this
		 * {@link StatSubmInfoFilter}.
		 * 
		 * @param toTest
		 *            {@link StatisticalSubmissionInfo} to test
		 * @return true if toTest matches this filter, false otherwise
		 */
		boolean matches(StatisticalSubmissionInfo<A> toTest);

	}

	/**
	 * Implementors of {@link StatSubmInfoFilterConnector}-interface can connect
	 * several {@link StatSubmInfoFilter}-objects to a single
	 * {@link StatSubmInfoFilter} .
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <A>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public interface StatSubmInfoFilterConnector<A extends SubmissionInfo>
			extends StatSubmInfoFilter<A> {

		/**
		 * @return all the connected {@link StatSubmInfoFilter}s
		 */
		List<StatSubmInfoFilter<A>> getConnectedFilters();

		/**
		 * @param toConnect
		 *            adds {@link StatSubmInfoFilter} toConnect to this
		 *            connector
		 */
		void connectFilter(StatSubmInfoFilter<A> toConnect);

		/**
		 * @param toDisconnect
		 *            removes {@link StatSubmInfoFilter} toDisconnect from this
		 *            connector
		 */
		void disconnectFilter(StatSubmInfoFilter<A> toDisconnect);

	}

	/**
	 * {@link StatSubmInfoFilter} that filters {@link StatisticalSubmissionInfo}
	 * -objects by their {@link StatisticalSubmissionInfo #getDoneTime()} value.
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <A>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public static class DateFilter<A extends SubmissionInfo> implements
			StatSubmInfoFilter<A> {

		private final long startMillis;
		private final long endMillis;

		/**
		 * Constructs a new {@link DateFilter} matching given interval. You can
		 * use {@link Date #getTime()} to get milliseconds to use with this
		 * class.
		 * 
		 * @param startMillis
		 *            start of accepted interval
		 * @param endMillis
		 *            end of accepted interval
		 */
		public DateFilter(long startMillis, long endMillis) {
			this.startMillis = startMillis;
			this.endMillis = endMillis;
			if (this.startMillis >= this.endMillis) {
				throw new IllegalArgumentException(
						"StarMillis must be less than endMillis! Start was: "
								+ this.startMillis + "; end was: "
								+ this.endMillis);
			}
		}

		@Override
		public boolean matches(StatisticalSubmissionInfo<A> toTest) {

			return (toTest.getDoneTime() >= startMillis)
					&& (toTest.getDoneTime() <= endMillis);
		}
	}

	/**
	 * {@link StatSubmInfoFilter} that filters {@link StatisticalSubmissionInfo}
	 * -objects by their {@link StatisticalSubmissionInfo #getEvalution()}
	 * value.
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <A>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public static class EvaluationFilter<A extends SubmissionInfo> implements
			StatSubmInfoFilter<A> {

		private final double min;
		private final double max;

		/**
		 * Constructs a new {@link EvaluationFilter} matching given evaluation
		 * interval.
		 * 
		 * @param min
		 *            minimum matching evaluation value
		 * @param max
		 *            maximum matching evaluation value
		 */
		public EvaluationFilter(double min, double max) {
			this.min = min;
			this.max = max;
			if (this.max < 0.0 || this.min > 1.0 || (this.min >= this.max)) {
				throw new IllegalArgumentException(
						"Max must be more than 0.0 and min must be more than 1.0, "
								+ "and max must be at least equal to min; max was: "
								+ this.max + "; min was " + this.min);
			}

		}

		@Override
		public boolean matches(StatisticalSubmissionInfo<A> toTest) {
			return (toTest.getEvalution() >= min)
					&& (toTest.getEvalution() <= max);
		}
	}

	/**
	 * {@link StatSubmInfoFilter} that filters {@link StatisticalSubmissionInfo}
	 * -objects by their {@link StatisticalSubmissionInfo #getTimeOnTask()}
	 * value.
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <A>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public static class TimeOnTaskFilter<A extends SubmissionInfo> implements
			StatSubmInfoFilter<A> {

		private final int maxTime;
		private final int minTime;

		/**
		 * Constructs a new {@link TimeOnTaskFilter} matching certain interval
		 * of time-on-task values
		 * 
		 * @param min
		 *            minimum matching time-on-task (seconds)
		 * @param max
		 *            maximum matching time-on-task (seconds)
		 */
		public TimeOnTaskFilter(int min, int max) {
			this.minTime = min;
			this.maxTime = max;
			if (this.minTime >= this.maxTime) {
				throw new IllegalArgumentException(
						"Max must be at least equal to min; max was: "
								+ this.maxTime + "; min was: " + this.minTime);
			}
		}

		@Override
		public boolean matches(StatisticalSubmissionInfo<A> toTest) {
			return (toTest.getTimeOnTask() >= minTime)
					&& (toTest.getTimeOnTask() <= maxTime);
		}

	}

	/**
	 * <p>
	 * {@link StatSubmInfoFilterConnector}-implementor that matches a
	 * {@link StatisticalSubmissionInfo}-object if all
	 * {@link StatSubmInfoFilter}s connected by it match the
	 * {@link StatisticalSubmissionInfo}.
	 * </p>
	 * <p>
	 * Matches ALL {@link StatisticalSubmissionInfo}-objects if the set of
	 * connected {@link StatSubmInfoFilter}s is EMPTY.
	 * </p>
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <A>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public static class MatchAllFilter<A extends SubmissionInfo> implements
			StatSubmInfoFilterConnector<A> {

		private final List<StatSubmInfoFilter<A>> filters;

		/**
		 * Constructs a new empty {@link MatchAllFilter}.
		 */
		public MatchAllFilter() {
			this(null);
		}

		/**
		 * Constructs a new {@link MatchAllFilter} consisting of given filters.
		 * If given filters-list is null constructs an empty
		 * {@link MatchAllFilter}.
		 * 
		 * @param filters
		 *            {@link List} of {@link StatSubmInfoFilter}s or null
		 */
		public MatchAllFilter(List<StatSubmInfoFilter<A>> filters) {
			if (filters != null) {
				this.filters = new ArrayList<StatSubmInfoFilter<A>>(filters);
			} else {
				this.filters = new ArrayList<StatSubmInfoFilter<A>>();
			}
		}

		@Override
		public boolean matches(StatisticalSubmissionInfo<A> toTest) {
			boolean res = true;
			for (StatSubmInfoFilter<A> aFilter : filters) {
				if (!aFilter.matches(toTest)) {
					res = false;
					break;
				}
			}
			return res;
		}

		@Override
		public List<StatSubmInfoFilter<A>> getConnectedFilters() {
			return filters;
		}

		@Override
		public void connectFilter(StatSubmInfoFilter<A> toConnect) {
			filters.add(toConnect);
		}

		@Override
		public void disconnectFilter(StatSubmInfoFilter<A> toDisconnect) {
			filters.remove(toDisconnect);
		}

	}

	/**
	 * <p>
	 * {@link StatSubmInfoFilterConnector}-implementor that matches a
	 * {@link StatisticalSubmissionInfo}-object if any of the
	 * {@link StatSubmInfoFilter}s connected by it match the
	 * {@link StatisticalSubmissionInfo}.
	 * </p>
	 * <p>
	 * NEVER matches a {@link StatisticalSubmissionInfo}-object if the set of
	 * connected {@link StatSubmInfoFilter}s is EMPTY.
	 * </p>
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <A>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public static class MatchAnyFilter<A extends SubmissionInfo> implements
			StatSubmInfoFilterConnector<A> {

		private final List<StatSubmInfoFilter<A>> filters;

		/**
		 * Constructs a new empty {@link MatchAnyFilter}.
		 */
		public MatchAnyFilter() {
			this(null);
		}

		/**
		 * Constructs a new {@link MatchAnyFilter} containing all filters in
		 * given list or no filters if null is used.
		 * 
		 * @param filters
		 *            {@link List} of {@link StatSubmInfoFilter}s
		 */
		public MatchAnyFilter(List<StatSubmInfoFilter<A>> filters) {
			if (filters != null) {
				this.filters = new ArrayList<StatSubmInfoFilter<A>>(filters);
			} else {
				this.filters = new ArrayList<StatSubmInfoFilter<A>>();
			}
		}

		@Override
		public boolean matches(StatisticalSubmissionInfo<A> toTest) {
			boolean res = false;
			for (StatSubmInfoFilter<A> aFilter : filters) {
				if (aFilter.matches(toTest)) {
					res = true;
					break;
				}
			}
			return res;
		}

		@Override
		public List<StatSubmInfoFilter<A>> getConnectedFilters() {
			return filters;
		}

		@Override
		public void connectFilter(StatSubmInfoFilter<A> toConnect) {
			filters.add(toConnect);

		}

		@Override
		public void disconnectFilter(StatSubmInfoFilter<A> toDisconnect) {
			filters.remove(toDisconnect);
		}

	}

	/**
	 * {@link StatSubmInfoFilter}-implementor that matches a
	 * {@link StatisticalSubmissionInfo}-object if the
	 * {@link StatSubmInfoFilter} it is inverting does NOT match that
	 * {@link StatisticalSubmissionInfo}.
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <A>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public static class InvertedFilter<A extends SubmissionInfo> implements
			StatSubmInfoFilter<A> {

		private final StatSubmInfoFilter<A> toInvert;

		/**
		 * Constructs a new {@link InvertedFilter} inverting given filter.
		 * 
		 * @param toInvert
		 *            filter to invert.
		 */
		public InvertedFilter(StatSubmInfoFilter<A> toInvert) {
			this.toInvert = toInvert;
		}

		@Override
		public boolean matches(StatisticalSubmissionInfo<A> toTest) {
			return !toInvert.matches(toTest);
		}

		/**
		 * @return the underlying filter in its original (non-inverted) form
		 */
		public StatSubmInfoFilter<A> getUnderlyingFilter() {
			return toInvert;
		}

	}

	/**
	 * Wrapper to implement {@link StatSubmInfoFilter} that only cares about the
	 * value of {@link StatisticalSubmissionInfo #getSubmissionData()}.
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <A>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public static class BySubmMatcher<A extends SubmissionInfo> implements
			StatSubmInfoFilter<A> {

		private final SubmMatcher<A> submMatcher;

		/**
		 * Constructs a new {@link BySubmMatcher} wrapping given
		 * {@link SubmMatcher} to implement {@link StatSubmInfoFilter}.
		 * 
		 * @param submMatcher
		 *            {@link SubmMatcher} to wrap
		 */
		public BySubmMatcher(SubmMatcher<A> submMatcher) {
			this.submMatcher = submMatcher;
		}

		@Override
		public boolean matches(StatisticalSubmissionInfo<A> toTest) {
			return submMatcher.matches(toTest.getSubmissionData());
		}

	}

	/**
	 * Can be used to implement {@link StatSubmInfoFilter} that only cares about
	 * the value of {@link StatisticalSubmissionInfo #getSubmissionData()}. The
	 * implementor must be wrapped with {@link BySubmMatcher}.
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <S>
	 *            accepted {@link SubmissionInfo}-type
	 */
	public interface SubmMatcher<S extends SubmissionInfo> {
		boolean matches(S toTest);
	}

}
