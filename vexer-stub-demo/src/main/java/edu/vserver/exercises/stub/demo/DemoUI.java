package edu.vserver.exercises.stub.demo;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

import edu.vserver.exercises.stub.VilleExerStubUI;

@Theme("ville-theme")
@Title("Ville-Plugin-Exercise-Stub Demo")
@SuppressWarnings("serial")
public class DemoUI extends VilleExerStubUI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "edu.vserver.exercises.stub.demo.DemoWidgetSet")
	public static class Servlet extends VaadinServlet {
	}

}
