package edu.vserver.exercises.stub;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import edu.vserver.exercises.model.Executor;
import edu.vserver.exercises.model.SubmissionResult;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.StandardIcon;
import edu.vserver.standardutils.StandardUIConstants;
import edu.vserver.standardutils.StandardUIFactory;

/**
 * A pop-up view imitating the view presented for student in real ViLLE after a
 * successful submission. This view might also contain a feedback component if
 * such is present if {@link SubmissionResult} returned from the
 * {@link Executor}.
 * 
 * @author Riku Haavisto
 * 
 */
class SubmissionPopup extends Window {

	private static final String submissionPopupId = "submissionpopup";
	private static final String submissionPopupRootId = "submissionpopup.root";
	private static final String submpopupCloseButtonId = "submpopup.close";

	private static final long serialVersionUID = -6955458439605857198L;

	public SubmissionPopup(Localizer localizer, SubmissionResult<?> submission) {

		setId(submissionPopupId);

		setStyleName("opaque");
		addStyleName("unclosable-window");
		setModal(true);
		center();

		VerticalLayout root = new VerticalLayout();
		root.setId(submissionPopupRootId);
		root.setSpacing(true);
		root.setMargin(true);

		root.setWidth("700px");
		setContent(root);

		String title = localizer.getUIText(StandardUIConstants.SUBMISSION);
		title = (title.charAt(0) + "").toUpperCase() + title.substring(1)
				+ " - ";

		title += localizer.getUIText(StandardUIConstants.TEST);

		setCaption(title);

		Label result = new Label();
		result.setContentMode(ContentMode.HTML);
		result.setValue("<b>" + localizer.getUIText(StandardUIConstants.RESULT)
				+ "<b>&nbsp;" + submission.getCorrectness());
		root.addComponent(result);
		root.setComponentAlignment(result, Alignment.MIDDLE_LEFT);

		if (submission.getFeedbackComponent() != null) {
			root.addComponent(submission.getFeedbackComponent());
			root.setComponentAlignment(submission.getFeedbackComponent(),
					Alignment.MIDDLE_CENTER);
		}

		Button close = StandardUIFactory.getDefaultButton(
				localizer.getUIText(StandardUIConstants.CLOSE),
				StandardIcon.SUBMIT_ICON.getIcon());
		close.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 7996549476059355446L;

			@Override
			public void buttonClick(ClickEvent event) {
				getUI().removeWindow(SubmissionPopup.this);
			}
		});
		close.setId(submpopupCloseButtonId);

		root.addComponent(close);
		root.setComponentAlignment(close, Alignment.MIDDLE_CENTER);

	}
}
