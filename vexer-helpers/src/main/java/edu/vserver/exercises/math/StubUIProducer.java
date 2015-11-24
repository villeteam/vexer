package edu.vserver.exercises.math;

import java.util.ArrayList;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

import edu.vserver.exercises.math.essentials.layout.TimeStampHandler;

public class StubUIProducer implements MathSubmissionUI {
	
	@Override
	public Button getTimeLineButton(TimeStampHandler currhandler) {
		return new Button();
	}
	
	@Override
	public HorizontalLayout getChartContainer(
			ArrayList<TimeStampHandler> handlers, Double[] timesPerTask,
			int correctAnswers, int i) {
		return new HorizontalLayout();
	}
	
}
