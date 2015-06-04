package ${package};

import com.vaadin.ui.Layout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.math.MathTabbedEditorWrap;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.UIConstants;

public class ${VilleJavaClassPrefix}Editor implements
        MathTabbedEditorWrap<${VilleJavaClassPrefix}Data> {

    private static final long serialVersionUID = 1L;

    // How many questions are shown to the user. Mathlayout can show max 20.
    private Slider numberOfExercises;

    final ${VilleJavaClassPrefix}Data oldData;

    private final Localizer localizer;

    public ${VilleJavaClassPrefix}Editor(${VilleJavaClassPrefix}Data oldData,
            Localizer localizer) {
        this.localizer = localizer;
        this.oldData = oldData;

    }

    @Override
    public VerticalLayout drawSettings() {

        VerticalLayout view = new VerticalLayout();

        numberOfExercises = new Slider(
                localizer.getUIText(UIConstants.NUMBER_OF_QUESTIONS), 1, 20);
        numberOfExercises.setResolution(0);
        numberOfExercises.setValue(5.0);
        numberOfExercises.setWidth("200px");

        view.addComponent(numberOfExercises);

        return view;

    }

    @Override
    public ${VilleJavaClassPrefix}Data getCurrData() {
        return new ${VilleJavaClassPrefix}Data(numberOfExercises.getValue()
                .intValue(), new int[]{5,5});
    }

    @Override
    public Layout drawEditorLayout() {
        return drawSettings();
    }

    @Override
    public Boolean validateData() {
        return true;
    }

    @Override
    public String setTitleText() {
        return localizer.getUIText("Mathlayout Stub Exercise");
    }

}
