package fi.utu.ville.exercises.stub;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

import fi.utu.ville.exercises.model.ExecutionSettings;
import fi.utu.ville.exercises.model.ExerciseTypeDescriptor;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon;
import fi.utu.ville.standardutils.StandardUIFactory;
import fi.utu.ville.standardutils.TestTempFilesManager;

/**
 * The main-view of ViLLE-exercise-stub providing controls on which
 * {@link ExerciseTypeDescriptor} to test and with which settings, and buttons
 * for loading different aspects of that {@link ExerciseTypeDescriptor}.
 * 
 * @author Riku Haavisto
 * 
 */
class StubStartView extends VerticalLayout {

	/*
	 * The implementation is pretty straight-forward and a bit messy, but if
	 * something needs changing it should be too hard to figure what controls
	 * what.
	 * 
	 * The controls are initialized in roughly the same order they appear in the
	 * actual GUI.
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -4411551015222502328L;

	private static final String studentButtonId = "student";
	private static final String editorButtonId = "editor";
	private static final String submissionViewerButtonId = "submission-viewer";
	private static final String statsViewerButtonId = "stats-viewer";
	private static final String deleteOldFilesButtonId = "delete-files";
	private static final String exerTypeSelectId = "exer-type-select";
	private static final String localesSelectId = "locales-select";
	private static final String execSettingsSelectId = "exec-settings-select";

	private static final String controlsLayoutID = "stub-start-controls-layout";

	private final Button student;
	private final Button editor;
	private final Button submissionViewer;
	private final Button statsViewer;
	private final Button reloadLangFiles;
	private final Button deleteFiles;
	private final Button restartStub;
	private final Button inspectCurrExerData;
	private final Button inspectCurrSubmInfo;

	private final NativeSelect exerType = new NativeSelect();

	private final HorizontalLayout exerInstanceControl = new HorizontalLayout();

	private final NativeSelect localesToTest = new NativeSelect();

	private final NativeSelect execSettings = new NativeSelect();

	public StubStartView() {

		// delete temp
		// TODO : maybe move to stubsessiondata's init..?
		StubSessionData.getInstance().clearTemp();

		setId("stubstartview");

		setSizeFull();
		addStyleName("stub-start");

		List<Locale> locales = StubSessionData.getInstance().getLocalesToTest();

		StubState.getCurrent().getCurrResourceGiver();

		final Localizer localizer = StubState.getCurrent()
				.getCurrResourceGiver();

		exerType.setCaption(localizer
				.getUIText(StubUiConstants.EXER_TYPE_SEL_CAPT));

		localesToTest.setCaption(localizer
				.getUIText(StubUiConstants.UI_LANG_SEL_CAPT));

		execSettings.setCaption(localizer
				.getUIText(StubUiConstants.EXEC_SETTINGS_SEL_CAPT));

		student = StandardUIFactory.getDefaultButton(
				localizer.getUIText(StubUiConstants.TEST_EXECUTOR), null);
		editor = StandardUIFactory.getDefaultButton(
				localizer.getUIText(StubUiConstants.TEST_EDITOR), null);
		submissionViewer = StandardUIFactory.getDefaultButton(
				localizer.getUIText(StubUiConstants.TEST_SUBM_VIEWER), null);
		statsViewer = StandardUIFactory.getDefaultButton(
				localizer.getUIText(StubUiConstants.TEST_STATS_VIEWER), null);
		reloadLangFiles = StandardUIFactory.getDefaultButton(
				localizer.getUIText(StubUiConstants.RELOAD_LANG_FILES), null);
		deleteFiles = StandardUIFactory.getDefaultButton(
				localizer.getUIText(StubUiConstants.DELETE_DATA_FILES), null);
		restartStub = StandardUIFactory.getDefaultButton("restart-stub", null);

		inspectCurrExerData = StandardUIFactory.getDefaultButton(
				"Inspect generated exer-data", null);
		inspectCurrSubmInfo = StandardUIFactory.getDefaultButton(
				"Inspect generated submission-info", null);

		localesToTest.setNullSelectionAllowed(false);

		for (Locale aLocale : locales) {
			localesToTest.addItem(aLocale);
			localesToTest.setItemCaption(aLocale, aLocale.getDisplayName());
		}

		localesToTest.select(StubState.getCurrent().getCurrResourceGiver()
				.getCurrentLocale());

		localesToTest.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				StubState.getCurrent().setCurrResourceGiver(
						new StubResourceGiverImpl((Locale) localesToTest
								.getValue()));
			}

		});

		List<ExerciseTypeDescriptor<?, ?>> types = StubSessionData
				.getInstance().getTypesToTest();

		exerType.setNullSelectionAllowed(false);

		for (ExerciseTypeDescriptor<?, ?> eType : types) {
			exerType.addItem(eType);
			exerType.setItemCaption(eType, eType.getTypeName(StubState
					.getCurrent().getCurrResourceGiver()));
		}

		exerType.select(StubState.getCurrent().getCurrDesc());
		exerType.setImmediate(true);

		exerType.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2571953764327445912L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				StubState.getCurrent().setCurrDesc(
						(ExerciseTypeDescriptor<?, ?>) exerType.getValue());
				handleExerTypeInstances(true);
			}

		});

		execSettings.setNullSelectionAllowed(false);

		for (StubExecutionSettings fbType : StubExecutionSettings.values()) {
			execSettings.addItem(fbType);
			execSettings.setItemCaption(fbType, fbType.name());
		}

		execSettings.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -4502686105300018008L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				StubState.getCurrent().setCurrExecSettings(
						(ExecutionSettings) execSettings.getValue());
			}

		});

		execSettings.select(StubState.getCurrent().getCurrExecSettings());

		student.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -4288966285787740015L;

			@Override
			public void buttonClick(ClickEvent event) {
				ExerciseTypeDescriptor<?, ?> currDesc = StubState.getCurrent()
						.getCurrDesc();
				String currName = StubState.getCurrent().getCurrExerName();
				TestTempFilesManager tempMan = new TestTempFilesManager(
						StubSessionData.getInstance()
								.getStubExerMaterialsTempDir());
				getUI().setContent(
						TestingExerciseView.getViewForStudentTesting(
								currDesc,
								StubState.getCurrent().getCurrResourceGiver(),
								new GeneralExerciseInfoStubImpl(currName,

								StubDataFilesHandler.getExerDescription(
										currDesc, currName)), StubState
										.getCurrent().getCurrExecSettings(),
								tempMan));
			}
		});

		student.setId(studentButtonId);

		editor.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -1084707924947529504L;

			@Override
			public void buttonClick(ClickEvent event) {
				getUI().setContent(
						ExerciseEditorViewStub.getViewFor(StubState
								.getCurrent().getCurrDesc(),
								new GeneralExerciseInfoStubImpl(StubState
										.getCurrent().getCurrExerName(), ""),
								StubState.getCurrent().getCurrResourceGiver()));

			}
		});

		editor.setId(editorButtonId);

		submissionViewer.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -6553191046767443143L;

			@Override
			public void buttonClick(ClickEvent event) {
				TestTempFilesManager tempMan = new TestTempFilesManager(
						StubSessionData.getInstance()
								.getStubExerMaterialsTempDir());
				getUI().setContent(
						SubmissionVisualizerTestingView.getViewFor(StubState
								.getCurrent().getCurrDesc(), StubState
								.getCurrent().getCurrExerName(), StubState
								.getCurrent().getCurrResourceGiver(), tempMan));

			}
		});

		submissionViewer.setId(submissionViewerButtonId);

		statsViewer.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -6553191046767443143L;

			@Override
			public void buttonClick(ClickEvent event) {
				TestTempFilesManager tempMan = new TestTempFilesManager(
						StubSessionData.getInstance()
								.getStubExerMaterialsTempDir());
				getUI().setContent(
						SubmissionDataStatsGiverTestingView.getViewFor(
								StubState.getCurrent().getCurrDesc(), StubState
										.getCurrent().getCurrExerName(),
								StubState.getCurrent().getCurrResourceGiver(),
								tempMan));

			}
		});

		statsViewer.setId(statsViewerButtonId);

		reloadLangFiles.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8774374640619294763L;

			@Override
			public void buttonClick(ClickEvent event) {
				UILanguageGiver.reloadLanguages();

				UI.getCurrent().setContent(new StubStartView());

				Notification.show(localizer
						.getUIText(StubUiConstants.LANG_FILE_RELOAD_DONE_INFO));
			}
		});

		deleteFiles.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -1302501547711901347L;

			@Override
			public void buttonClick(ClickEvent event) {

				File tempDir = new File(StubSessionData.getInstance()
						.getStubExerMaterialsTempDir());

				StubUtil.deleteDirectory(tempDir, false);

				for (ExerciseTypeDescriptor<?, ?> type : StubSessionData
						.getInstance().getTypesToTest()) {
					File exerTypeDir = new File(StubSessionData.getInstance()
							.getTypeBaseDir(type));
					StubUtil.deleteDirectory(exerTypeDir, false);
				}

				UI.getCurrent().setContent(new StubStartView());

				Notification.show(localizer
						.getUIText(StubUiConstants.DELETED_DATA_FILES_INFO));

			}
		});

		Button listIcons = StandardUIFactory.getDefaultButton(
				localizer.getUIText(StubUiConstants.LIST_STANDARD_ICONS), null);

		listIcons.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5199328004585103852L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window iconsWindow = new Window(StubState.getCurrent()
						.getCurrResourceGiver()
						.getUIText(StubUiConstants.STANDARD_ICONS));

				iconsWindow.setContent(new StandardIconGallery());
				iconsWindow.addStyleName("opaque");

				iconsWindow.center();

				UI.getCurrent().addWindow(iconsWindow);

			}
		});

		restartStub.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -1302501547711901347L;

			@Override
			public void buttonClick(ClickEvent event) {

				VaadinSession.getCurrent().close();
				Page.getCurrent().setLocation(Page.getCurrent().getLocation());
			}
		});

		inspectCurrExerData.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -3109633563175480052L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window inspectWindow = new Window(StubState.getCurrent()
						.getCurrResourceGiver()
						.getUIText(StubUiConstants.STANDARD_ICONS));

				inspectWindow.setWidth("80%");
				inspectWindow.setHeight("80%");

				Panel viewPort = new Panel();

				viewPort.setWidth("100%");
				viewPort.setHeight("100%");

				VerticalLayout contL = new VerticalLayout();
				contL.setSizeUndefined();
				contL.setMargin(true);

				viewPort.setContent(contL);
				inspectWindow.setContent(viewPort);

				byte[] bytes = StubDataFilesHandler.loadExerTypeData(StubState
						.getCurrent().getCurrDesc(), StubState.getCurrent()
						.getCurrExerName());
				String asutf = "";
				try {
					asutf = new String(bytes, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Label show = new Label(asutf, ContentMode.PREFORMATTED);

				contL.addComponent(show);
				inspectWindow.addStyleName("opaque");

				inspectWindow.center();

				UI.getCurrent().addWindow(inspectWindow);

			}
		});
		inspectCurrSubmInfo.addClickListener(new Button.ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7517430647557357083L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window inspectWindow = new Window(StubState.getCurrent()
						.getCurrResourceGiver()
						.getUIText(StubUiConstants.STANDARD_ICONS));

				inspectWindow.setWidth("80%");
				inspectWindow.setHeight("80%");

				Panel viewPort = new Panel();

				viewPort.setWidth("100%");
				viewPort.setHeight("100%");

				VerticalLayout contL = new VerticalLayout();
				contL.setSizeUndefined();
				contL.setMargin(true);

				viewPort.setContent(contL);
				inspectWindow.setContent(viewPort);

				byte[] bytes = StubDataFilesHandler.loadLatestBareSubmInfo(
						StubState.getCurrent().getCurrDesc(), StubState
								.getCurrent().getCurrExerName());
				String asutf = "";
				try {
					asutf = new String(bytes, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Label show = new Label(asutf, ContentMode.PREFORMATTED);

				contL.addComponent(show);
				inspectWindow.addStyleName("opaque");

				inspectWindow.center();

				UI.getCurrent().addWindow(inspectWindow);

			}
		});

		deleteFiles.setId(deleteOldFilesButtonId);

		exerType.setId(exerTypeSelectId);
		localesToTest.setId(localesSelectId);
		execSettings.setId(execSettingsSelectId);

		VerticalLayout controlsLayout = new VerticalLayout();
		controlsLayout.setWidth("400px");

		controlsLayout.setMargin(true);
		controlsLayout.setSpacing(true);

		controlsLayout.setId(controlsLayoutID);

		Label title = new Label(
				localizer.getUIText(StubUiConstants.VILLE_EXER_STUB_TITLE));
		title.addStyleName("stub-title");

		controlsLayout.addComponent(title);

		controlsLayout.addComponent(StandardUIFactory
				.getInformationPanel(localizer
						.getUIText(StubUiConstants.STUB_MAIN_INFO)));

		controlsLayout.addComponent(new Label("<hr/>", ContentMode.HTML));

		controlsLayout.addStyleName("stub-start-controls");

		controlsLayout.addComponent(new StubStartSectionTitle(localizer
				.getUIText(StubUiConstants.STUB_SETTINGS_TITLE), localizer
				.getUIText(StubUiConstants.STUB_SETTINGS_INFO)));

		controlsLayout.addComponent(exerType);
		controlsLayout.addComponent(exerInstanceControl);
		controlsLayout.addComponent(localesToTest);
		controlsLayout.addComponent(execSettings);

		controlsLayout.addComponent(new Label("<hr/>", ContentMode.HTML));

		controlsLayout.addComponent(new StubStartSectionTitle(localizer
				.getUIText(StubUiConstants.STUB_TEST_BUTTONS_TITLE), localizer
				.getUIText(StubUiConstants.STUB_TEST_BUTTONS_INFO)));

		controlsLayout.addComponent(student);
		student.setWidth("250px");
		controlsLayout.setComponentAlignment(student, Alignment.MIDDLE_CENTER);
		controlsLayout.addComponent(editor);
		editor.setWidth("250px");
		controlsLayout.setComponentAlignment(editor, Alignment.MIDDLE_CENTER);
		controlsLayout.addComponent(submissionViewer);
		submissionViewer.setWidth("250px");
		controlsLayout.setComponentAlignment(submissionViewer,
				Alignment.MIDDLE_CENTER);
		controlsLayout.addComponent(statsViewer);
		statsViewer.setWidth("250px");
		controlsLayout.setComponentAlignment(statsViewer,
				Alignment.MIDDLE_CENTER);

		controlsLayout.addComponent(new Label("<hr/>", ContentMode.HTML));

		controlsLayout.addComponent(new StubStartSectionTitle(localizer
				.getUIText(StubUiConstants.STUB_EXTRA_BUTTONS_TITLE), localizer
				.getUIText(StubUiConstants.STUB_EXTRA_BUTTONS_INFO)));

		controlsLayout.addComponent(reloadLangFiles);
		reloadLangFiles.setWidth("250px");
		controlsLayout.setComponentAlignment(reloadLangFiles,
				Alignment.MIDDLE_CENTER);
		controlsLayout.addComponent(deleteFiles);
		deleteFiles.setWidth("250px");
		controlsLayout.setComponentAlignment(deleteFiles,
				Alignment.MIDDLE_CENTER);
		controlsLayout.addComponent(listIcons);
		listIcons.setWidth("250px");
		controlsLayout
				.setComponentAlignment(listIcons, Alignment.MIDDLE_CENTER);
		controlsLayout.addComponent(restartStub);
		restartStub.setWidth("250px");
		controlsLayout.setComponentAlignment(restartStub,
				Alignment.MIDDLE_CENTER);

		controlsLayout.addComponent(inspectCurrExerData);
		inspectCurrExerData.setWidth("250px");
		controlsLayout.setComponentAlignment(inspectCurrExerData,
				Alignment.MIDDLE_CENTER);

		controlsLayout.addComponent(inspectCurrSubmInfo);
		inspectCurrSubmInfo.setWidth("250px");
		controlsLayout.setComponentAlignment(inspectCurrSubmInfo,
				Alignment.MIDDLE_CENTER);

		addComponent(controlsLayout);
		setComponentAlignment(controlsLayout, Alignment.TOP_CENTER);

		handleExerTypeInstances(false);

	}

	/**
	 * Handles updating exercise-type specific controls.
	 * 
	 * @param changed
	 *            whether the exercise-type was actually changed or this is only
	 *            the initial set-up
	 */
	private void handleExerTypeInstances(boolean changed) {

		List<String> instancesOfType = StubDataFilesHandler
				.getPresentExerInstances(StubState.getCurrent().getCurrDesc());

		if (changed) {
			StubState.getCurrent().setCurrExerName("");
		}

		exerInstanceControl.removeAllComponents();
		exerInstanceControl.setSpacing(true);

		if (instancesOfType.isEmpty()) {

			setEnabledInstanceDependents(false);

		} else {

			final NativeSelect selInst = new NativeSelect();
			for (String exerName : instancesOfType) {
				selInst.addItem(exerName);
				selInst.setItemCaption(exerName, exerName);
			}
			selInst.setImmediate(true);
			selInst.addItem("");
			selInst.setItemCaption(
					"",
					StubState.getCurrent().getCurrResourceGiver()
							.getUIText(StubUiConstants.NEW_EXER_SEL_CAPTION));

			selInst.addValueChangeListener(new ValueChangeListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = -1781809551510917210L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					StubState.getCurrent().setCurrExerName(
							(String) selInst.getValue());
					if ("".equals(StubState.getCurrent().getCurrExerName())) {
						setEnabledInstanceDependents(false);
					} else {
						setEnabledInstanceDependents(true);
					}
				}
			});

			selInst.setWidth("250px");
			String currVal = !"".equals(StubState.getCurrent()
					.getCurrExerName()) ? StubState.getCurrent()
					.getCurrExerName() : instancesOfType.get(0);

			selInst.setNullSelectionAllowed(false);
			selInst.select(currVal);

			student.setEnabled(true);
			submissionViewer.setEnabled(true);
			statsViewer.setEnabled(true);

			Button delInstance = new Button(null);

			delInstance.setDescription(StubState.getCurrent()
					.getCurrResourceGiver()
					.getUIText(StubUiConstants.DEL_EXER_INSTANCE));

			delInstance.setStyleName(BaseTheme.BUTTON_LINK);

			delInstance.setIcon(StandardIcon.DELETE_ICON.getIcon());

			delInstance.addClickListener(new ClickListener() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 5754319586043288242L;

				@Override
				public void buttonClick(ClickEvent event) {
					String toDel = StubState.getCurrent().getCurrExerName();
					if (!"".equals(toDel)) {
						StubDataFilesHandler.deleteExerInstance(StubState
								.getCurrent().getCurrDesc(), toDel);
						handleExerTypeInstances(true);
						Notification.show(StubState
								.getCurrent()
								.getCurrResourceGiver()
								.getUIText(StubUiConstants.DEL_EXER_INSTANCE,
										toDel));
					} else {
						Notification
								.show(StubState
										.getCurrent()
										.getCurrResourceGiver()
										.getUIText(
												StubUiConstants.NO_INSTANCE_TO_DELETE_SELECTED));
					}
				}

			});

			exerInstanceControl.addComponent(selInst);
			exerInstanceControl.addComponent(delInstance);
		}
	}

	/**
	 * Sets certain buttons enabled or disabled related to views that cannot be
	 * loaded if there are no exercise-instances of currently selected
	 * exercise-type.
	 * 
	 * @param enabled
	 *            whether to enable or disable the buttons
	 */
	private void setEnabledInstanceDependents(boolean enabled) {
		student.setEnabled(enabled);
		submissionViewer.setEnabled(enabled);
		statsViewer.setEnabled(enabled);
	}
}
