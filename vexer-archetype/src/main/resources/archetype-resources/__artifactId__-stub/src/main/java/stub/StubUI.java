package ${package}.stub;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

import edu.vserver.exercises.stub.VilleExerStubUI;

@Theme("${artifactId}-stub-theme")
@Title("${artifactId} - Ville-exercise testing-stub")
@SuppressWarnings("serial")
public class StubUI extends VilleExerStubUI
{


}
