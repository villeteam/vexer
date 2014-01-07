package edu.vserver.exercises.model;

import java.util.List;

public final class StatisticsInfoColumn<A> {

	private final String name;
	private final List<A> dataObjects;
	private final Class<A> colDataType;
	private final String colDescription;
	private final boolean exportable;

	public StatisticsInfoColumn(String name, String desc, Class<A> colDataType,
			List<A> dataObjects, boolean exportable) {
		this.name = name;
		this.colDescription = desc;
		this.colDataType = colDataType;
		this.dataObjects = dataObjects;
		this.exportable = exportable;
	}

	public String getDescription() {
		return colDescription;
	}

	public String getName() {
		return name;
	}

	public Class<A> getColDataType() {
		return colDataType;
	}

	public List<A> getDataObjects() {
		return dataObjects;
	}

	public boolean isExportable() {
		return exportable;
	}

}