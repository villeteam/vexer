package edu.vserver.exercises.math.essentials.layout;

import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

public class MathNextButtonListener implements ClickListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VerticalLayout assign;
	private int loop;
	private List<VerticalLayout> exercises;
	private Button next;
	private Button check;
	private boolean chart;
	
	public MathNextButtonListener(VerticalLayout assign,
			List<VerticalLayout> exercises, Button next, Button check,
			int loop, boolean chart) {
		this.assign = assign;
		this.exercises = exercises;
		this.next = next;
		this.check = check;
		this.loop = loop;
		this.chart = chart;
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		assign.removeAllComponents();
		
		if (loop < exercises.size()) {
			
			/**
			 * If chart is true the chart (exercises.get(0)) will be shown first
			 */
			if (chart) {
				final VerticalLayout chart = exercises.get(0);
				chart.addStyleName("math-replace");
				assign.addComponent(chart);
			}
			
			assign.addComponent(exercises.get(loop));
			loop++;
			check.setEnabled(true);
			
		}
		next.setEnabled(false);
	}
}
