package fi.utu.ville.exercises.model;

import java.util.List;

/**
 * <p>
 * A class for representing a single column of tabular data with certain title, description, type and a list of values (or rows).
 * </p>
 * <p>
 * Multiple {@link StatisticsInfoColumn}s can parsed for same set of input-data can be combined to form a table from multiple sources.
 * </p>
 * 
 * @author Riku Haavisto
 * 
 * @param <A>
 *            type of objects representing values of this column
 */
public final class StatisticsInfoColumn<A> {
	
	private final String name;
	private final List<A> dataObjects;
	private final Class<A> colDataType;
	private final String colDescription;
	private final boolean exportable;
	
	/**
	 * Constructs a new {@link StatisticsInfoColumn}-instance.
	 * 
	 * @param name
	 *            (localized) name for the column
	 * @param desc
	 *            (localized) description for the data represented by the column
	 * @param colDataType
	 *            type of the data-values for this column
	 * @param dataObjects
	 *            list of data-values parsed for this column
	 * @param exportable
	 *            boolean telling whether it makes sense to export data from this column to external tools
	 */
	public StatisticsInfoColumn(String name, String desc, Class<A> colDataType,
			List<A> dataObjects, boolean exportable) {
		this.name = name;
		this.colDescription = desc;
		this.colDataType = colDataType;
		this.dataObjects = dataObjects;
		this.exportable = exportable;
	}
	
	/**
	 * @return (localized) description of the data represented by this column
	 */
	public String getDescription() {
		return colDescription;
	}
	
	/**
	 * @return (localizer) name of this column
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return type of the data-values of this column
	 */
	public Class<A> getColDataType() {
		return colDataType;
	}
	
	/**
	 * @return list of data-values corresponding to this column parsed from some input data
	 */
	public List<A> getDataObjects() {
		return dataObjects;
	}
	
	/**
	 * @return true if the data in this column is exportable to general format (eg. Excel or csv)
	 */
	public boolean isExportable() {
		return exportable;
	}
	
}
