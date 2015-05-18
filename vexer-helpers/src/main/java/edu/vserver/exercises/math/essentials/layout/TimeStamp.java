package edu.vserver.exercises.math.essentials.layout;

import java.io.Serializable;

public class TimeStamp implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4523848581608543195L;
	private final String eventStr;
	private final long time;
	
	public TimeStamp(String eventStr, long time) {
		this.time = time;
		this.eventStr = eventStr;
	}
	
	public String getEventStr() { return eventStr; }
	
	public long getTime() { return time; }
	
	@Override
	public String toString() {
		return "eventStr=\"" + eventStr + "\" time=\"" + time + "\"";
	}
}
