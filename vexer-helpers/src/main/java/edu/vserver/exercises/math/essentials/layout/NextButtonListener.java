package edu.vserver.exercises.math.essentials.layout;

import org.vaadin.jouni.animator.shared.AnimType;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class NextButtonListener implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5508691190197789575L;
	// private final MathExerciseView view;
	// private final MathExerciseState state;
	private final MathLayout<?> layout;
	private final TimeStampHandler timeStampHandler;

	public NextButtonListener(MathLayout<?> l, TimeStampHandler timeStampHandler) {
		// this.view = view;
		// this.state = state;
		this.layout = l;
		this.timeStampHandler = timeStampHandler;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		// layout.aNext = layout.getProxy().animate(view,
		// AnimType.FADE_OUT).setDuration(250).setDelay(50);
		timeStampHandler.add(TimeStampHandler.nextButton);
		layout.aNext = layout.getProxy()
				.animate(layout.getHLayout(), AnimType.SIZE).setDuration(500)
				.setDelay(50).setData("x=-" + MathLayout.shiftSize);
		// layout.getProxy().animate(layout.getPrevBtn(),
		// AnimType.SIZE).setDuration(500).setDelay(50).setData("x=-" +
		// MathLayout.shiftSize);
		// layout.getProxy().animate(layout.getNextBtn(),
		// AnimType.SIZE).setDuration(500).setDelay(50).setData("x=-" +
		// MathLayout.shiftSize);
	}
}
