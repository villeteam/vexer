package edu.vserver.exercises.math;

import java.util.ArrayList;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

import edu.vserver.exercises.math.essentials.layout.TimeStampHandler;

public class MathSubmissionUIFactory {
	
	private static MathSubmissionUI producer = new StubUIProducer();
	
	protected MathSubmissionUIFactory() {
	}
	
	public static void setProducer(MathSubmissionUI uiProducer) {
		producer = uiProducer;
	}
	
	public static Button getTimelineButton(TimeStampHandler timeStampHandler) {
		return producer.getTimeLineButton(timeStampHandler);
	}
	
	public static HorizontalLayout getChartContainer(
			ArrayList<TimeStampHandler> handlers, Double[] timesPerTask, int correctAnswers, int i) {
		return producer.getChartContainer(handlers, timesPerTask, correctAnswers, i);
	}
	
}
