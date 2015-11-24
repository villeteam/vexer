package edu.vserver.exercises.math.essentials.layout;

import java.io.Serializable;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Panel;

public class ToggleButton extends Panel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1027520780276478865L;
	private boolean toggle = false;
	private String onStyle, offStyle, onCaption, offCaption;
	
	private static ClickListener cl = new ClickListener() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 427222284615169728L;
		
		@Override
		public void click(ClickEvent event) {
			ToggleButton btn = (ToggleButton) event.getComponent();
			btn.toggle();
		}
	};
	
	public ToggleButton(boolean toggle, String offStyle, String onStyle,
			String onCaption, String offCaption) {
		super((!toggle ? onCaption : offCaption));
		this.toggle = !toggle;
		this.onStyle = onStyle;
		this.offStyle = offStyle;
		this.onCaption = onCaption;
		this.offCaption = offCaption;
		addClickListener(cl);
		toggle();
	}
	
	public boolean getToggle() {
		return toggle;
	}
	
	public void toggle() {
		toggle = !toggle;
		
		if (toggle) {
			setCaption(onCaption);
			setStyleName(onStyle);
		} else {
			setCaption(offCaption);
			setStyleName(offStyle);
		}
	}
}
