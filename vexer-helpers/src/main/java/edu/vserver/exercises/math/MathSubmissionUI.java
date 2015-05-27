package edu.vserver.exercises.math;

import java.util.ArrayList;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

import edu.vserver.exercises.math.essentials.layout.TimeStampHandler;

public interface MathSubmissionUI {

	Button getTimeLineButton(TimeStampHandler timeStampHandler);

	HorizontalLayout getChartContainer(ArrayList<TimeStampHandler> handlers,
			Double[] timesPerTask, int correctAnswers, int i);
}
