package edu.vserver.math;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class MathEditorHelp extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8593710533564522620L;
	private Label help;
	private final String header;
	private final String g;
	private final String h;
	private final String p;
	private final String i;

	public MathEditorHelp(String head, String h, String g, String p, String i) {
		this.h = h;
		this.g = g;
		this.p = p;
		this.i = i;
		this.header = head;
		draw();
	}

	public void draw() {

		addStyleName("math-editorHelp");
		setWidth("90%");
		HorizontalLayout column = new HorizontalLayout();
		column.addStyleName("math-help-column");
		column.setSizeFull();
		column.setSpacing(true);

		VerticalLayout left = new VerticalLayout();
		VerticalLayout center = new VerticalLayout();
		VerticalLayout right = new VerticalLayout();

		left.setWidth("95%");
		center.setWidth("95%");
		right.setWidth("95%");

		left.setMargin(true);
		center.setMargin(true);
		right.setMargin(true);

		left.addStyleName("math-help-column-left");
		center.addStyleName("math-help-column-center");
		right.addStyleName("math-help-column-right");

		Label goals = new Label(g, ContentMode.HTML);
		Label problems = new Label(p, ContentMode.HTML);
		Label ideas = new Label(i, ContentMode.HTML);

		left.addComponent(goals);
		center.addComponent(problems);
		right.addComponent(ideas);

		VerticalLayout content = new VerticalLayout();
		help = new Label(h, ContentMode.HTML);

		content.addComponent(help);

		column.addComponent(left);
		column.addComponent(center);
		column.addComponent(right);

		Label headerlbl = new Label("<h1 class=\"color-h1\">" + header
				+ "</h1>", ContentMode.HTML);
		addComponent(headerlbl);
		addComponent(column);
		addComponent(content);
		setComponentAlignment(column, Alignment.MIDDLE_LEFT);
	}

	public void setHelp(String s) {
		help.setValue(s);
	}
}
