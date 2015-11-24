package edu.vserver.math;

import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import edu.vserver.exercises.math.essentials.layout.Problem;
import edu.vserver.exercises.math.essentials.level.DiffLevel;
import edu.vserver.exercises.math.essentials.level.LevelMathSubmissionInfo;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.MathIcons;
import fi.utu.ville.standardutils.MathUIFactory;
import fi.utu.ville.standardutils.UIConstants;

public abstract class AbstractLevelMathSubmissionViewer<E extends ExerciseData, F extends LevelMathSubmissionInfo<G>, G extends Problem>
		extends AbstractMathSubmissionViewer<E, F, G> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 3307450304602817973L;
	
	@Override
	protected Layout getStartLayout(F subInfo) {
		VerticalLayout res = new VerticalLayout();
		Button button;
		Localizer localizer = getLocalizer();
		
		if (subInfo.getDiffLevel() == DiffLevel.EASY) {
			
			button = MathUIFactory.getStarButton(
					localizer.getUIText(UIConstants.LEVEL_EASY), localizer);
			button.setIcon(MathIcons.getIcon(MathIcons.STAR_EASY));
		}
		
		else if (subInfo.getDiffLevel() == DiffLevel.NORMAL) {
			
			button = MathUIFactory.getStarButton(
					localizer.getUIText(UIConstants.LEVEL_NORMAL),
					localizer);
			button.setIcon(MathIcons.getIcon(MathIcons.STAR_NORMAL));
		}
		
		else if (subInfo.getDiffLevel() == DiffLevel.HARD) {
			button = MathUIFactory.getStarButton(
					localizer.getUIText(UIConstants.LEVEL_HARD), localizer);
			button.setIcon(MathIcons.getIcon(MathIcons.STAR_HARD));
		} else {
			throw new IllegalArgumentException("DiffLevel not supported: "
					+ subInfo.getDiffLevel());
		}
		
		button.addStyleName("math-levelbutton");
		res.addComponent(button);
		return res;
	}
	
	@Override
	protected String getStartString(F subInfo) {
		String res = "Used difficulty level: ";
		Localizer localizer = getLocalizer();
		
		if (subInfo.getDiffLevel() == DiffLevel.EASY) {
			
			res = res + localizer.getUIText(UIConstants.LEVEL_EASY);
		}
		
		else if (subInfo.getDiffLevel() == DiffLevel.NORMAL) {
			res = res + localizer.getUIText(UIConstants.LEVEL_NORMAL);
		}
		
		else if (subInfo.getDiffLevel() == DiffLevel.HARD) {
			
			res = res + localizer.getUIText(UIConstants.LEVEL_HARD);
			
		} else {
			throw new IllegalArgumentException("DiffLevel not supported: "
					+ subInfo.getDiffLevel());
		}
		
		return res;
	}
	
}
