package edu.vserver.exercises.math.essentials.layout;

import org.vaadin.jouni.animator.shared.AnimType;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class CheckButtonListener implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5508691190197789575L;
	private final MathExerciseView<?> view;
	private final MathLayout<?> layout;
	private final TimeStampHandler timeStampHandler;

	public CheckButtonListener(MathExerciseView<?> view, MathLayout<?> l,
			TimeStampHandler timeStampHandler) {
		this.view = view;
		this.layout = l;
		this.timeStampHandler = timeStampHandler;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		timeStampHandler.add(TimeStampHandler.checkButton);
		layout.aCheck = layout.getProxy().animate(view, AnimType.FADE_OUT)
				.setDuration(250).setDelay(50);
	}
}
