package ${package};

import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.math.essentials.level.DiffLevel;
import edu.vserver.exercises.math.essentials.level.LevelMathDataWrapper;
import edu.vserver.exercises.math.essentials.level.LevelMathExecutorWrapper;
import edu.vserver.math.MathEditorHelp;
import edu.vserver.math.MathTabbedEditor;
import edu.vserver.math.MathTabbedEditorWrap;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.UIConstants;

public class ${VilleJavaClassPrefix}TabbedEditor extends
        MathTabbedEditor<${VilleJavaClassPrefix}Data> {

    private static final long serialVersionUID = 2841920972594896399L;
    private ${VilleJavaClassPrefix}Editor easyEditor;
    private ${VilleJavaClassPrefix}Editor normalEditor;
    private ${VilleJavaClassPrefix}Editor hardEditor;
    private Localizer localizer;
    private static String help;

    public ${VilleJavaClassPrefix}TabbedEditor() {

    }

    @Override
    protected VerticalLayout getHelper() {

        String left = localizer.getUIText(UIConstants.HELP_CALCROW_LEFT);
        String center = localizer.getUIText(UIConstants.HELP_CALCROW_CENTER);
        String right = localizer.getUIText(UIConstants.HELP_CALCROW_RIGHT);

        return new MathEditorHelp(localizer.getUIText(UIConstants.CALC_ROW),
                help, left, center, right);
    }

    @Override
    protected MathTabbedEditorWrap<${VilleJavaClassPrefix}Data> getEasyEditor() {
        return easyEditor;
    }

    @Override
    protected MathTabbedEditorWrap<${VilleJavaClassPrefix}Data> getNormalEditor() {
        return normalEditor;
    }

    @Override
    protected MathTabbedEditorWrap<${VilleJavaClassPrefix}Data> getHardEditor() {
        return hardEditor;
    }

    @Override
    protected LevelMathExecutorWrapper<${VilleJavaClassPrefix}Data, ${VilleJavaClassPrefix}SubmissionInfo> getExecutor() {
        return new LevelMathExecutorWrapper<${VilleJavaClassPrefix}Data, ${VilleJavaClassPrefix}SubmissionInfo>(
                new ${VilleJavaClassPrefix}Executor());
    }

    @Override
    protected void typeInitialize(
            LevelMathDataWrapper<${VilleJavaClassPrefix}Data> oldData) {
        this.localizer = getLocalizer();

        if (oldData == null) {
            ${VilleJavaClassPrefix}Data easy = new ${VilleJavaClassPrefix}Data(5, new int[]{5,5});
            ${VilleJavaClassPrefix}Data normal = new ${VilleJavaClassPrefix}Data(10, new int[]{10,10});
            ${VilleJavaClassPrefix}Data hard = new ${VilleJavaClassPrefix}Data(15, new int[]{15,15});

            this.easyEditor = new ${VilleJavaClassPrefix}Editor(easy, getLocalizer());
            this.normalEditor = new ${VilleJavaClassPrefix}Editor(normal, getLocalizer());
            this.hardEditor = new ${VilleJavaClassPrefix}Editor(hard, getLocalizer());
        }

        else {
            this.easyEditor = new ${VilleJavaClassPrefix}Editor(
                    oldData.getForLevel(DiffLevel.EASY), getLocalizer());
            this.normalEditor = new ${VilleJavaClassPrefix}Editor(
                    oldData.getForLevel(DiffLevel.NORMAL), getLocalizer());
            this.hardEditor = new ${VilleJavaClassPrefix}Editor(
                    oldData.getForLevel(DiffLevel.HARD), getLocalizer());
        }

        help = localizer.getUIText(UIConstants.HELP_CALCROW);
        drawEditor();

    }

    @Override
    public void doLayout() {
        drawEditor();

    }

    @Override
    public boolean isOkToExit() {
        return true;
    }

    @Override
    public String getViewName() {
        return "${VilleJavaClassPrefix}Editor";
    }

}
