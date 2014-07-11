package fi.utu.ville.exercises.stub;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fi.utu.ville.exercises.model.ExerciseTypeDescriptor;
import fi.utu.ville.exercises.model.GeneralExerciseInfo;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardUIFactory;

/**
 * A view imitating the view used in real ViLLE for showing info about an
 * exercise-instance.
 * 
 * @author Riku Haavisto
 * 
 */
class TestingExerciseInfoView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7323519115445232073L;

	/* assignment icon */
	private final Embedded icon;

	/* fields & labels */
	private final Label titleLabel;

	/**
	 * Constructs a new {@link TestingExerciseInfoView}
	 * 
	 * @param localizer
	 *            {@link Localizer} for localizing the UI
	 * @param exerInfo
	 *            {@link GeneralExerciseInfo} containing general info on the
	 *            exercise (name and description)
	 * @param desc
	 *            {@link ExerciseTypeDescriptor} containing info on the used
	 *            exercise-type
	 */
	public TestingExerciseInfoView(Localizer localizer,
			GeneralExerciseInfo exerInfo, ExerciseTypeDescriptor<?, ?> desc) {

		// setStyleName("studentexercise-description-panel");
		setWidth("100%");

		setSpacing(false);
		setMargin(false);

		HorizontalLayout iconAndTitleLayout = StandardUIFactory
				.getHeaderBarGreen();
		iconAndTitleLayout.setSpacing(false);

		icon = new Embedded(null, (desc != null ? desc.getMediumTypeIcon()
				: TestStubDescription.INSTANCE.getIcon()));

		titleLabel = new Label((exerInfo != null ? exerInfo.getName()
				: TestStubDescription.INSTANCE.getName()));
		// titleLabel.setStyleName("studentexercise-title");
		titleLabel.setSizeUndefined();

		iconAndTitleLayout.addComponent(icon);
		iconAndTitleLayout.setComponentAlignment(icon, Alignment.MIDDLE_LEFT);
		iconAndTitleLayout.addComponent(titleLabel);
		iconAndTitleLayout.setComponentAlignment(titleLabel,
				Alignment.MIDDLE_LEFT);

		iconAndTitleLayout.setExpandRatio(titleLabel, 1.0f);
		iconAndTitleLayout.setExpandRatio(icon, 0f);

		addComponent(iconAndTitleLayout);
		setComponentAlignment(iconAndTitleLayout, Alignment.MIDDLE_LEFT);

		String descString = (exerInfo != null ? exerInfo.getDescription()
				: TestStubDescription.INSTANCE.getDescription());

		ExerciseDescriptionPanel exDescPanel = new ExerciseDescriptionPanel(
				localizer, descString);
		addComponent(exDescPanel);
		setComponentAlignment(exDescPanel, Alignment.MIDDLE_LEFT);

	}
}