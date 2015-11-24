package fi.utu.ville.exercises.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.BaseTheme;

import fi.utu.ville.exercises.helpers.StatsGiverHelper.DateFilter;
import fi.utu.ville.exercises.helpers.StatsGiverHelper.EvaluationFilter;
import fi.utu.ville.exercises.helpers.StatsGiverHelper.InvertedFilter;
import fi.utu.ville.exercises.helpers.StatsGiverHelper.MatchAllFilter;
import fi.utu.ville.exercises.helpers.StatsGiverHelper.MatchAnyFilter;
import fi.utu.ville.exercises.helpers.StatsGiverHelper.StatSubmInfoFilter;
import fi.utu.ville.exercises.helpers.StatsGiverHelper.StatSubmInfoFilterConnector;
import fi.utu.ville.exercises.helpers.StatsGiverHelper.TimeOnTaskFilter;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.StandardIcon.Icon;
import fi.utu.ville.standardutils.StandardUIFactory;

/**
 * <p>
 * This class provides a GUI through which a user can create instances of different {@link StatSubmInfoFilter}s, connect them by {@link MatchAllFilter} or
 * {@link MatchAnyFilter} and invert them by {@link InvertedFilter}.
 * </p>
 * <p>
 * By default editors for three default filter-types ({@link DateFilter}, {@link TimeOnTaskFilter} and {@link EvaluationFilter}) are present.
 * </p>
 * <p>
 * Editors for more filter-types can be added by implementing {@link FilterEditorFactory}-interface that can provide a user with general info about certain
 * filter and instantiate {@link FilterEditor}-implementor that can be used to create and edit an instance of certain {@link StatSubmInfoFilter} -implementor.
 * To make the new implementor available to the user construct this class with the implementor included in 'extraFilterFactories'-list.
 * </p>
 * 
 * @author Riku Haavisto
 * 
 * @param <S>
 *            accepted {@link SubmissionInfo} implementor
 */
public class StatSubmInfoFilterEditor<S extends SubmissionInfo> implements
		Serializable {
		
	/**
	 * Implementor of this interface can show certain general information about certain {@link StatSubmInfoFilter} and instantiate {@link FilterEditor}s that
	 * can be used for creating and editing instances of that {@link StatSubmInfoFilter}-type.
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <A>
	 *            accepted {@link SubmissionInfo} implementor
	 */
	public interface FilterEditorFactory<A extends SubmissionInfo> {
		
		/**
		 * Instantiates a new instance of the represented {@link FilterEditor}.
		 * 
		 * @param localizer
		 *            {@link Localizer} that can be passed to new instance {@link FilterEditor} help localizing UI
		 * @return a new {@link FilterEditor}-instance
		 */
		FilterEditor<A> newEditorInstance(Localizer localizer);
		
		/**
		 * Gives a short description of the represented filter that can be created and edited by editor fetched from this {@link FilterEditorFactory}.
		 * 
		 * @param localizer
		 *            {@link Localizer} for localizing UI
		 * @return short description of represented filter
		 */
		String getFilterDesc(Localizer localizer);
		
		/**
		 * Gives a name of the represented filter that can be created and edited by editor fetched from this {@link FilterEditorFactory}.
		 * 
		 * @param localizer
		 *            {@link Localizer} for localizing UI
		 * @return name of represented filter
		 */
		String getFilterName(Localizer localizer);
		
		/**
		 * Returns an icon representing the filter that can be created and edited by editor fetched from this {@link FilterEditorFactory}.
		 * 
		 * @return {@link Resource} of icon for represented filter
		 */
		Resource getFilterIcon();
		
	}
	
	/**
	 * An implementor of this class can create and edit certain {@link StatSubmInfoFilter}-implementor.
	 * 
	 * @author Riku Haavisto
	 * 
	 * @param <A>
	 *            accepted {@link SubmissionInfo} implementor
	 */
	public interface FilterEditor<A extends SubmissionInfo> {
		
		/**
		 * Returns a new independent copy of this {@link FilterEditor} instance with matching state.
		 * 
		 * @return an independent copy of this editor
		 */
		FilterEditor<A> getCopy();
		
		/**
		 * Return {@link StatSubmInfoFilter}-implementor matching current state of this {@link FilterEditor}.
		 * 
		 * @return {@link StatSubmInfoFilter} matching editor's current state
		 */
		StatSubmInfoFilter<A> getFilter();
		
		/**
		 * <p>
		 * Return a string-presentation of the current state of this {@link FilterEditor}.
		 * </p>
		 * <p>
		 * This can be for example (localized) filter-name : values of editable variables. eg. Evaluation : from 0.4 to 0.8 .
		 * </p>
		 * 
		 * @return a textual localized description of this editor's current state
		 */
		String getFilterStateDesc();
		
		/**
		 * Return a {@link Component} containing a GUI through which a user can edit variables of this filter.
		 * 
		 * @return a GUI for editing variables of a certain filter-implementor
		 */
		Component getFilterEditView();
		
		/**
		 * <p>
		 * Returns a {@link Component} representing minimized view of this {@link FilterEditor}s state.
		 * </p>
		 * <p>
		 * This view should be really compact. Default implementations use as their minified-view the icon of represented filter-type (as returned by
		 * corresponding {@link FilterEditorFactory #getFilterIcon()} with {@link #getFilterStateDesc()} as its tooltip.
		 * </p>
		 * 
		 * @return a minified view of the type and current state of this filter-editor
		 */
		Component getMinifiedView();
		
		/**
		 * This method is called whenever user has edited this {@link FilterEditor} through {@link #getFilterEditView()} and tries to 'commit' the edits. If the
		 * filter-editor is left in an inconsistent state this method should return false. It is also possible (and nice) to notify the user on what is wrong
		 * with the current state of the editor ( by eg. adding info to the edit-view or by showing a {@link Notification}).
		 * 
		 * @return true if editor is in consistent state
		 */
		boolean checkAndNotify();
		
	}
	
	/*
	 * 
	 * Implementation of actual 'StatSubmInfoFilterEditor' that can be shown to
	 * user starts here.
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 40221094607340126L;
	private EditorConnectorView<S> mainLevelFilter;
	private final FilterFactoryKeeper<S> ffKeeper = new FilterFactoryKeeper<S>();
	private final StatSubmInfoFilterTable<S> applyTo;
	private final Localizer localizer;
	private Button clearBtn;
	private Button hideShowEditorBtn;
	private Button applyBtn;
	
	private boolean minimized = false;
	
	private final Panel mainLevelPanel = new Panel();
	private final VerticalLayout centeringLayout = new VerticalLayout();
	
	private final VerticalLayout mainLayout = new VerticalLayout();
	
	private final ClickListener clickListener = new ClickListener() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		@Override
		public void buttonClick(ClickEvent event) {
			if (event.getButton() == applyBtn) {
				applyFilter();
			} else if (event.getButton() == clearBtn) {
				clearFilter();
			} else if (event.getButton() == hideShowEditorBtn) {
				setMinimized(!minimized);
			}
		}
		
	};
	
	/**
	 * Constructs a new {@link StatSubmInfoFilterEditor} that can be used to provide the user with a GUI for creating and editing a {@link StatSubmInfoFilter}
	 * that can be applied to given {@link StatSubmInfoFilterTable}.
	 * 
	 * @param applyTo
	 *            {@link StatSubmInfoFilterTable} that is filtered by this editor
	 * @param localizer
	 *            {@link Localizer} for localizing UI
	 * @param extraFilterFactories
	 *            {@link Collection} of all {@link FilterEditorFactory} -implementors that will be added to enable generating custom filters; can be null if
	 *            only default filter-types are required
	 */
	public StatSubmInfoFilterEditor(StatSubmInfoFilterTable<S> applyTo,
			Localizer localizer,
			Collection<FilterEditorFactory<S>> extraFilterFactories) {
		this.localizer = localizer;
		this.applyTo = applyTo;
		if (this.localizer == null || this.applyTo == null) {
			throw new IllegalArgumentException(
					"localizer and applyTo must be non-null: localizer= "
							+ localizer + "; applyTo= " + applyTo);
		}
		
		mainLevelFilter = new EditorConnectorView<S>(null, ffKeeper, localizer);
		init();
		
		if (extraFilterFactories != null) {
			for (FilterEditorFactory<S> extraFF : extraFilterFactories) {
				ffKeeper.registerFilterEditorFactory(extraFF);
			}
		}
		
		doLayout();
	}
	
	private void init() {
		ffKeeper.registerFilterEditorFactory(new DateIntervalFilterEditor<S>(
				localizer));
		ffKeeper.registerFilterEditorFactory(new TimeonTaskFilterEditor<S>(
				localizer));
		ffKeeper.registerFilterEditorFactory(new EvaluationilterEditor<S>(
				localizer));
	}
	
	/**
	 * Clears the current filter of this {@link StatSubmInfoFilterEditor} and updates the table to a non-filtered state.
	 */
	public void clearFilter() {
		mainLevelFilter = new EditorConnectorView<S>(null, ffKeeper, localizer);
		centeringLayout.removeAllComponents();
		centeringLayout.addComponent(mainLevelFilter);
		centeringLayout.setComponentAlignment(mainLevelFilter,
				Alignment.TOP_CENTER);
		applyTo.setFilter(getTotalFilter());
		
	}
	
	/**
	 * Applies the filter currently represented by this {@link StatSubmInfoFilterEditor} to the controlled {@link StatSubmInfoFilterTable}.
	 */
	public void applyFilter() {
		applyTo.setFilter(getTotalFilter());
		
	}
	
	/**
	 * Minimizes or maximizes the actual filter-editor-view.
	 * 
	 * @param minimized
	 *            true if filter-editor should be minimized
	 */
	public void setMinimized(boolean minimized) {
		this.minimized = minimized;
		updateMinimState();
	}
	
	/**
	 * Returns the {@link StatSubmInfoFilter} matching the current state of this {@link StatSubmInfoFilterEditor}. The returned {@link StatSubmInfoFilter} might
	 * actually be made of several {@link StatSubmInfoFilter}s connected by {@link StatSubmInfoFilterConnector}s.
	 * 
	 * @return {@link StatSubmInfoFilter} matching the current state of this {@link StatSubmInfoFilterEditor}
	 */
	public StatSubmInfoFilter<S> getTotalFilter() {
		
		return mainLevelFilter.asFilter();
		
	}
	
	/**
	 * Returns a GUI through which the user can control this filter-editor.
	 * 
	 * @return GUI for this filter-editor
	 */
	public Component getView() {
		return mainLayout;
	}
	
	private void doLayout() {
		
		mainLevelPanel.setWidth("100%");
		
		centeringLayout.setWidth("100%");
		centeringLayout.setMargin(true);
		
		mainLevelPanel.setContent(centeringLayout);
		
		centeringLayout.addComponent(mainLevelFilter);
		centeringLayout.setComponentAlignment(mainLevelFilter,
				Alignment.TOP_CENTER);
				
		mainLayout.addComponent(mainLevelPanel);
		
		hideShowEditorBtn = StandardUIFactory.getButton("show editor", null);
		
		hideShowEditorBtn.addClickListener(clickListener);
		
		clearBtn = StandardUIFactory.getButton("clear filter", null);
		
		clearBtn.addClickListener(clickListener);
		
		applyBtn = StandardUIFactory.getButton("apply filter", Icon.ATTACH);
		
		applyBtn.addClickListener(clickListener);
		
		HorizontalLayout buttons = new HorizontalLayout();
		
		buttons.addComponents(hideShowEditorBtn, clearBtn, applyBtn);
		
		buttons.setSpacing(true);
		buttons.setMargin(true);
		buttons.setSizeUndefined();
		
		mainLayout.addComponent(buttons);
		mainLayout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
		
		updateMinimState();
	}
	
	private void updateMinimState() {
		if (minimized) {
			hideShowEditorBtn.setCaption("show");
			mainLevelPanel.setVisible(false);
		} else {
			hideShowEditorBtn.setCaption("hide");
			mainLevelPanel.setVisible(true);
		}
	}
	
	private static class FilterFactoryKeeper<B extends SubmissionInfo> {
		
		private final List<FilterEditorFactory<B>> factories =
		
		new ArrayList<FilterEditorFactory<B>>();
		
		public void registerFilterEditorFactory(FilterEditorFactory<B> feFactory) {
			factories.add(feFactory);
		}
		
		public List<FilterEditorFactory<B>> getAllEditorFactories() {
			return new ArrayList<FilterEditorFactory<B>>(factories);
		}
		
	}
	
	// *SPECIAL*-FilterEditor implementor used to provide somewhat similar
	// interface to editing filter-connector as normal filter-editors use
	//
	// should not be added to factory-keeper as instances of
	// connectors are created (and must be created) in a special way that
	// gives them some other editors to connect
	
	// there could also be different factory-collection for adding
	// new 'ConnectorTypes' but that would probably be mostly confusing
	// and I cannot think of a situation where one would really need
	// connectors like match-any-two
	
	private static class FilterConnectorEditor<S extends SubmissionInfo>
			implements FilterEditor<S> {
			
		public enum ConnectorType {
			ALL("&"),
			ANY("|");
			
			public final String connector;
			
			private ConnectorType(String connector) {
				this.connector = connector;
			}
			
		}
		
		private final NativeSelect typeSel = new NativeSelect();
		
		public FilterConnectorEditor() {
			for (ConnectorType ct : ConnectorType.values()) {
				typeSel.addItem(ct);
				typeSel.setItemCaption(ct, ct.name());
			}
			typeSel.setNullSelectionAllowed(false);
			typeSel.select(ConnectorType.ALL);
		}
		
		@Override
		public StatSubmInfoFilter<S> getFilter() {
			
			if (getCurrType() == ConnectorType.ALL) {
				return new MatchAllFilter<S>();
			} else {
				return new MatchAnyFilter<S>();
			}
		}
		
		@Override
		public Component getFilterEditView() {
			
			return typeSel;
		}
		
		private ConnectorType getCurrType() {
			
			return (ConnectorType) typeSel.getValue();
		}
		
		@Override
		public Component getMinifiedView() {
			Image img = new Image();
			img.setDescription(getFilterStateDesc());
			return img;
		}
		
		@Override
		public FilterConnectorEditor<S> getCopy() {
			FilterConnectorEditor<S> res = new FilterConnectorEditor<S>();
			res.typeSel.select(getCurrType());
			return res;
		}
		
		@Override
		public String getFilterStateDesc() {
			return getCurrType().name();
		}
		
		@Override
		public boolean checkAndNotify() {
			// always in 'ok'-state
			return true;
		}
	}
	
	/*
	 * The actual editor implementation starts here and is pretty nasty and
	 * hacky: connector-editor extends normal filter-editor and overrides almost
	 * all of its methods, and also has to check by 'instanceof' whether certain
	 * of its children is actually a connector-editor in some places.
	 * 
	 * This hacky implementation should be kept well hidden from the public
	 * interface.
	 */
	
	private static class EditorConnectorView<B extends SubmissionInfo> extends
			
			FilterEditorView<B> implements ClickListener {
			
		/**
		 * 
		 */
		private static final long serialVersionUID = -2768397561770574721L;
		
		private final FilterFactoryKeeper<B> ffKeeper;
		
		private final List<FilterEditorView<B>> children = new ArrayList<FilterEditorView<B>>();
		
		// extra buttons
		private final Button addEditorBtn = new Button();
		private final Button groupToConnectorBtn = new Button();
		
		// direct access to filter-connector editor ( no need to do castings
		// that often...), this is just casted 'theEditor'
		private FilterConnectorEditor<B> actEditor;
		
		public EditorConnectorView(EditorConnectorView<B> parent,
				FilterFactoryKeeper<B> ffKeeper, Localizer localizer) {
			super(new FilterConnectorEditor<B>(), parent, localizer);
			this.actEditor = (FilterConnectorEditor<B>) theEditor;
			this.ffKeeper = ffKeeper;
			ctrlMinified = false;
			initLayout();
		}
		
		public EditorConnectorView(EditorConnectorView<B> parent,
				FilterFactoryKeeper<B> ffKeeper, Localizer localizer,
				FilterConnectorEditor<B> editorToUse, boolean inverted,
				List<FilterEditorView<B>> childrenToCopy, boolean ctrlMinified) {
			super(editorToUse, parent, localizer, inverted, ctrlMinified);
			this.actEditor = editorToUse;
			this.ffKeeper = ffKeeper;
			for (FilterEditorView<B> childToCopy : childrenToCopy) {
				children.add(childToCopy.getCopy(this));
			}
			initLayout();
		}
		
		private void initLayout() {
			
			if (parent == null) {
				minMaxCtrlBtn.setVisible(false);
				ctrlMinified = false;
			}
			
			addEditorBtn.setDescription("Add editor");
			addEditorBtn.setStyleName(BaseTheme.BUTTON_LINK);
			addEditorBtn.addClickListener(this);
			
			groupToConnectorBtn.setDescription("Group to connector");
			groupToConnectorBtn.setStyleName(BaseTheme.BUTTON_LINK);
			groupToConnectorBtn.addClickListener(this);
			
			HorizontalLayout extraControlBtns = new HorizontalLayout();
			
			extraControlBtns.addComponents(addEditorBtn, groupToConnectorBtn);
			
			controlsLayout.addComponent(new Label("|"));
			controlsLayout.addComponent(extraControlBtns);
			
			updateGroupToState();
			updateLayout();
		}
		
		@Override
		public StatSubmInfoFilter<B> asFilter() {
			
			/*
			 * Get base-filter from the super-classes asFilter()-method. If this
			 * filter is inverted the actual connnector-filter must be fetched
			 * from the inverted-filter
			 */
			StatSubmInfoFilterConnector<B> baseFilter;
			if (!inverted) {
				baseFilter = (StatSubmInfoFilterConnector<B>) super.asFilter();
			} else {
				baseFilter = (StatSubmInfoFilterConnector<B>) ((InvertedFilter<B>) super.asFilter()).getUnderlyingFilter();
			}
			
			// add all the children to the connector
			for (FilterEditorView<B> filterEdit : children) {
				baseFilter.connectFilter(filterEdit.asFilter());
			}
			
			// as the underlying connector filter was fetched to be able to add
			// children to it, re-wrap it to inverted-filter if needed
			if (inverted) {
				return new InvertedFilter<B>(baseFilter);
			} else {
				return baseFilter;
			}
		}
		
		private void updateGroupToState() {
			// no mind in adding a sub-group if there is less than three
			// children
			if (children.size() >= 3) {
				groupToConnectorBtn.setEnabled(true);
				groupToConnectorBtn.setVisible(true);
			} else {
				groupToConnectorBtn.setEnabled(false);
				groupToConnectorBtn.setVisible(false);
			}
		}
		
		@Override
		protected void updateBackingEditor(FilterEditor<B> newEditor) {
			this.actEditor = (FilterConnectorEditor<B>) theEditor;
			super.updateBackingEditor(newEditor);
		}
		
		@Override
		protected void updateLayout() {
			// override to update-layout mechanic completely to draw
			// also the children, and to also implement minimizing differently
			
			mainStateView.removeAllComponents();
			mainStateView.setSpacing(true);
			if (ctrlMinified) {
				controlsLayout.setVisible(false);
				
				Image img = new Image();
				String connector = actEditor.getCurrType().connector;
				String desc = actEditor.getCurrType().name() + ": ( ";
				for (int i = 0, n = children.size(); i < n; i++) {
					if (children.get(i).inverted) {
						desc += "NOT ";
					}
					
					desc += children.get(i).theEditor.getFilterStateDesc();
					if (i < n - 1) {
						desc += " " + connector + " ";
					}
				}
				desc += ")";
				img.setDescription(desc);
				mainStateView.addComponent(img);
			} else {
				controlsLayout.setVisible(true);
				
				String connector = actEditor.getCurrType().connector;
				Label startLbl = new Label("(");
				mainStateView.addComponent(startLbl);
				mainStateView.setComponentAlignment(startLbl,
						Alignment.MIDDLE_LEFT);
						
				if (children.isEmpty()) {
					mainStateView.addComponent(new Label("EMPTY"));
				} else {
					for (int i = 0, n = children.size(); i < n; i++) {
						Component childComp = children.get(i);
						mainStateView.addComponent(childComp);
						mainStateView.setComponentAlignment(childComp,
								Alignment.MIDDLE_CENTER);
						if (i < n - 1) {
							Label connLbl = new Label(connector);
							mainStateView.addComponent(connLbl);
							mainStateView.setComponentAlignment(connLbl,
									Alignment.MIDDLE_CENTER);
						}
					}
				}
				Label endLbl = new Label(")");
				mainStateView.addComponent(endLbl);
				mainStateView.setComponentAlignment(endLbl,
						Alignment.MIDDLE_CENTER);
			}
			updateGroupToState();
		}
		
		public void removeEditor(FilterEditorView<B> toRem) {
			// check whether the child is normal child or a connector;
			// if it is a connector, do not delete its children but
			// add them as children of this connector instead
			
			if (toRem instanceof EditorConnectorView) {
				EditorConnectorView<B> childConnView = (EditorConnectorView<B>) toRem;
				List<FilterEditorView<B>> childsChildren = new ArrayList<FilterEditorView<B>>(
						childConnView.children);
				children.remove(childConnView);
				List<FilterEditorView<B>> reAddChildren = new ArrayList<FilterEditorView<B>>();
				for (FilterEditorView<B> childChild : childsChildren) {
					reAddChildren.add(childChild.getCopy(this));
				}
				children.addAll(reAddChildren);
			}
			// if the child to remove is not a connector, just remove it
			else {
				children.remove(toRem);
			}
			updateLayout();
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			super.buttonClick(event);
			
			if (event.getButton() == addEditorBtn) {
				showAddNewPopup();
			} else if (event.getButton() == groupToConnectorBtn) {
				
				showGroupToPopup();
			} else if (event.getButton() == minMaxCtrlBtn) {
				updateLayout();
			}
		}
		
		private FilterEditorView<B> addNewFilterEditor(FilterEditor<B> toAdd) {
			FilterEditorView<B> wrappedEditor = new FilterEditorView<B>(toAdd,
					this, localizer);
					
			children.add(wrappedEditor);
			updateLayout();
			return wrappedEditor;
			
		}
		
		@Override
		public EditorConnectorView<B> getCopy(EditorConnectorView<B> newParent) {
			
			// this copy constructor will handle copying children by adding them
			// to new list
			// and calling their getCopy(parent) method with itself as the new
			// parent
			EditorConnectorView<B> res = new EditorConnectorView<B>(newParent,
					ffKeeper, localizer, actEditor.getCopy(), inverted,
					children, ctrlMinified);
			return res;
		}
		
		private void showAddNewPopup() {
			final Window popup = new Window();
			popup.setWidth("400px");
			popup.setHeight("400px");
			popup.center();
			popup.setModal(true);
			
			VerticalLayout cont = new VerticalLayout();
			cont.setMargin(true);
			cont.setSpacing(true);
			
			GridLayout factories = new GridLayout(3, 3);
			factories.setSpacing(true);
			factories.setMargin(true);
			for (final FilterEditorFactory<B> fef : ffKeeper
					.getAllEditorFactories()) {
				VerticalLayout aFactory = new VerticalLayout();
				
				Image img = new Image(null, fef.getFilterIcon());
				
				img.setDescription(fef.getFilterDesc(localizer));
				
				aFactory.addComponent(img);
				
				aFactory.addComponent(new Label(fef.getFilterName(localizer)));
				
				aFactory.addLayoutClickListener(new LayoutClickListener() {
					
					/**
					 * 
					 */
					private static final long serialVersionUID = -6943926604606589576L;
					
					@Override
					public void layoutClick(LayoutClickEvent event) {
						FilterEditor<B> newEditor = fef
								.newEditorInstance(localizer);
						FilterEditorView<B> wrappedEditor = addNewFilterEditor(newEditor);
						UI.getCurrent().removeWindow(popup);
						wrappedEditor.showEditFilterPopup();
						
					}
				});
				
				factories.addComponent(aFactory);
			}
			cont.addComponent(factories);
			popup.setContent(cont);
			
			UI.getCurrent().addWindow(popup);
		}
		
		@Override
		protected void setNoControls(boolean noControls) {
			super.setNoControls(noControls);
			// override this to also set this for all the children
			for (FilterEditorView<B> child : children) {
				child.setNoControls(noControls);
			}
		}
		
		private void showGroupToPopup() {
			final Window popup = new Window();
			popup.setWidth("400px");
			popup.setHeight("400px");
			popup.center();
			popup.setModal(true);
			
			VerticalLayout cont = new VerticalLayout();
			cont.setMargin(true);
			cont.setSpacing(true);
			
			HorizontalLayout selLayout = new HorizontalLayout();
			
			selLayout.setMargin(true);
			selLayout.setSpacing(true);
			
			// add all chhildren in no-controls mode
			// and list of checkboxes under them to enable user to select which
			// of the children to group under the new connector
			
			final ArrayList<CheckBox> selChildren = new ArrayList<CheckBox>();
			for (int i = 0, n = children.size(); i < n; i++) {
				VerticalLayout aChild = new VerticalLayout();
				FilterEditorView<B> copied = children.get(i).getCopy(null);
				copied.setNoControls(true);
				aChild.addComponent(copied);
				CheckBox childCheckbox = new CheckBox();
				selChildren.add(i, childCheckbox);
				aChild.addComponent(childCheckbox);
				selLayout.addComponent(aChild);
			}
			
			cont.addComponent(selLayout);
			
			Button okButton = StandardUIFactory.getOKButton(localizer);
			
			okButton.addClickListener(new ClickListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = -8713670656337690394L;
				
				@Override
				public void buttonClick(ClickEvent event) {
					HashSet<Integer> selIndexes = new HashSet<Integer>();
					
					for (int i = 0; i < selChildren.size(); i++) {
						if (selChildren.get(i).getValue()) {
							selIndexes.add(i);
						}
					}
					if (selIndexes.isEmpty()
							|| selIndexes.size() == children.size()) {
						Notification
								.show("You must select some but not all children to the new group!");
					} else {
						EditorConnectorView<B> newConnector = new EditorConnectorView<B>(
								EditorConnectorView.this, ffKeeper, localizer);
								
						ArrayList<FilterEditorView<B>> movedChildren = new ArrayList<FilterEditorView<B>>();
						// a bit hacky solution but should work:
						// remove items backwards from the children-list so that
						// any index-shifting won't affect sub-sequent removals
						for (int bI = children.size() - 1; bI > 0; bI--) {
							if (selIndexes.contains(bI)) {
								FilterEditorView<B> addToNew = children
										.remove(bI);
								// add to start of the movedChildren to
								// re-invert the order regardless
								// of the backwards iteration
								FilterEditorView<B> copiedToNewParent = addToNew
										.getCopy(newConnector);
										
								movedChildren.add(0, copiedToNewParent);
							}
						}
						
						// now add the moved children to new connector
						for (int i = 0, n = movedChildren.size(); i < n; i++) {
							// this is more efficient than using the public
							// API...
							newConnector.children.add(movedChildren.get(i));
						}
						newConnector.updateLayout();
						children.add(newConnector);
						updateLayout();
						UI.getCurrent().removeWindow(popup);
						
					}
				}
				
			});
			
			cont.addComponent(okButton);
			
			popup.setContent(cont);
			
			UI.getCurrent().addWindow(popup);
		}
	}
	
	/*
	 * BASIC-filter-editor that is sub-classed by the filter-connector-editor
	 */
	
	private static class FilterEditorView<B extends SubmissionInfo>
			
			extends VerticalLayout implements ClickListener {
			
		/**
		 * 
		 */
		private static final long serialVersionUID = -3738173460239017664L;
		
		protected FilterEditor<B> theEditor;
		
		protected final Localizer localizer;
		protected final EditorConnectorView<B> parent;
		protected boolean inverted;
		protected boolean ctrlMinified;
		
		// buttons
		private final Button remButton = new Button();
		private final Button editButton = new Button();
		private final Button invertButton = new Button();
		
		protected final Button minMaxCtrlBtn = new Button();
		
		// layouts
		protected final HorizontalLayout mainStateView = new HorizontalLayout();
		protected final HorizontalLayout invAndStateView = new HorizontalLayout();
		protected final VerticalLayout ctrlAndMinMaxLayout = new VerticalLayout();
		protected final HorizontalLayout controlsLayout = new HorizontalLayout();
		
		public FilterEditorView(FilterEditor<B> theEditor,
				EditorConnectorView<B> parent, Localizer localizer) {
			this.theEditor = theEditor;
			this.parent = parent;
			this.inverted = false;
			this.ctrlMinified = true;
			this.localizer = localizer;
			doLayout();
		}
		
		public FilterEditorView(FilterEditor<B> theEditor,
				EditorConnectorView<B> parent, Localizer localizer,
				boolean inverted, boolean ctrlMinified) {
			this.theEditor = theEditor;
			this.parent = parent;
			this.inverted = inverted;
			this.ctrlMinified = ctrlMinified;
			this.localizer = localizer;
			doLayout();
		}
		
		public StatSubmInfoFilter<B> asFilter() {
			// add inverted-wrapping if needed
			if (inverted) {
				InvertedFilter<B> asInverted = new InvertedFilter<B>(
						theEditor.getFilter());
				return asInverted;
			} else {
				return theEditor.getFilter();
			}
		}
		
		private void doLayout() {
			
			invertButton.setStyleName(BaseTheme.BUTTON_LINK);
			invertButton.addClickListener(this);
			
			// set initial invert-state icon
			updateInvertedIndicator();
			
			invAndStateView.addComponent(invertButton);
			invAndStateView.setComponentAlignment(invertButton,
					Alignment.MIDDLE_LEFT);
					
			mainStateView.addComponent(theEditor.getMinifiedView());
			
			invAndStateView.addComponent(mainStateView);
			
			minMaxCtrlBtn.setDescription("Minim-maxim");
			minMaxCtrlBtn.setStyleName(BaseTheme.BUTTON_LINK);
			minMaxCtrlBtn.addClickListener(this);
			
			remButton.setStyleName(BaseTheme.BUTTON_LINK);
			remButton.addClickListener(this);
			if (parent == null) {
				remButton.setEnabled(false);
				remButton.setVisible(false);
			}
			
			editButton.setStyleName(BaseTheme.BUTTON_LINK);
			editButton.addClickListener(this);
			
			controlsLayout.addComponents(editButton, remButton);
			controlsLayout.setMargin(true);
			controlsLayout.addStyleName("ville-mild-bg");
			
			ctrlAndMinMaxLayout.addComponents(controlsLayout, minMaxCtrlBtn);
			ctrlAndMinMaxLayout.setComponentAlignment(controlsLayout,
					Alignment.TOP_CENTER);
			ctrlAndMinMaxLayout.setComponentAlignment(minMaxCtrlBtn,
					Alignment.BOTTOM_CENTER);
					
			addComponents(ctrlAndMinMaxLayout, invAndStateView);
			
			setComponentAlignment(ctrlAndMinMaxLayout, Alignment.TOP_CENTER);
			setComponentAlignment(invAndStateView, Alignment.BOTTOM_CENTER);
			
			addStyleName("ville-dashed-border");
			setMargin(true);
			setSpacing(true);
			setSizeUndefined();
			
			updateMinMaxCrtl();
			
		}
		
		protected void updateBackingEditor(FilterEditor<B> newEditor) {
			// overrides the 'theEditor' with new editor instance copied
			// from it; this is used to ensure that the filter-editor under edit
			// is disconnected from actual filter-editor in is 'committed' only
			// when the editor-under-edit is in consistent state (and user wants
			// to commit the changes); also enables canceling the editing
			this.theEditor = newEditor;
			updateLayout();
		}
		
		protected void updateLayout() {
			mainStateView.removeAllComponents();
			mainStateView.addComponent(theEditor.getMinifiedView());
		}
		
		protected void setNoControls(boolean noControls) {
			// hide control-layout and remove listener from invert-button (but
			// do not hide the button as it also indicates current invert-state)
			ctrlAndMinMaxLayout.setVisible(!noControls);
			if (noControls) {
				invertButton.removeClickListener(this);
			} else {
				invertButton.addClickListener(this);
			}
		}
		
		public FilterEditorView<B> getCopy(EditorConnectorView<B> newParent) {
			
			FilterEditorView<B> res = new FilterEditorView<B>(
					theEditor.getCopy(), newParent, localizer, inverted,
					ctrlMinified);
			return res;
		}
		
		private void updateMinMaxCrtl() {
			if (ctrlMinified) {
				
				controlsLayout.setVisible(false);
			} else {
				
				controlsLayout.setVisible(true);
			}
		}
		
		private void updateInvertedIndicator() {
			if (inverted) {
			} else {
			
			}
		}
		
		private void showEditFilterPopup() {
			final Window popup = new Window();
			popup.setWidth("400px");
			popup.setHeight("400px");
			popup.center();
			popup.setModal(true);
			
			VerticalLayout cont = new VerticalLayout();
			cont.setMargin(true);
			cont.setSpacing(true);
			
			final FilterEditor<B> editCopy = theEditor.getCopy();
			
			cont.addComponent(editCopy.getFilterEditView());
			
			popup.setContent(cont);
			
			popup.addCloseListener(new CloseListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 8568957374426173040L;
				
				@Override
				public void windowClose(CloseEvent e) {
					updateLayout();
				}
				
			});
			
			// edit's are committed only if OK-button is clicked and
			// editor's checkAndNotify returns true;
			// closing the popup with other methods effectively cancel
			// any edits done
			
			Button okButton = StandardUIFactory.getOKButton(localizer);
			
			okButton.addClickListener(new ClickListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = -6979725003624509202L;
				
				@Override
				public void buttonClick(ClickEvent event) {
					if (editCopy.checkAndNotify()) {
						UI.getCurrent().removeWindow(popup);
						updateBackingEditor(editCopy);
					}
				}
				
			});
			
			cont.addComponent(okButton);
			
			UI.getCurrent().addWindow(popup);
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			if (event.getButton() == invertButton) {
				inverted = !inverted;
				updateInvertedIndicator();
			} else if (event.getButton() == remButton) {
				if (parent != null) {
					parent.removeEditor(this);
				}
			} else if (event.getButton() == editButton) {
				showEditFilterPopup();
			} else if (event.getButton() == minMaxCtrlBtn) {
				ctrlMinified = !ctrlMinified;
				updateMinMaxCrtl();
			}
		}
	}
	
	/*
	 * 
	 * Implementations for editors of three general filter-types ( done-time,
	 * evaluation, time-on-task) start here.
	 * 
	 * All of these implementations implement both FilterEditor and
	 * FilterEditorFactory at the same time, but that is of course not required;
	 * it is convenient (as eg. icon is readily available to in FilterEditor)
	 * but maybe a bit confusing...
	 */
	
	// TODO: localization of default editors
	
	private static class DateIntervalFilterEditor<A extends SubmissionInfo>
			implements FilterEditor<A>, FilterEditorFactory<A> {
			
		private DateField start = new DateField();
		private DateField end = new DateField();
		private final Localizer localizer;
		
		public DateIntervalFilterEditor(Localizer localizer) {
			this.localizer = localizer;
		}
		
		@Override
		public FilterEditor<A> newEditorInstance(Localizer localizer) {
			return new DateIntervalFilterEditor<A>(localizer);
		}
		
		@Override
		public String getFilterDesc(Localizer localizer) {
			return "date-range-editor";
		}
		
		@Override
		public String getFilterName(Localizer localizer) {
			return "date-range-editor";
		}
		
		@Override
		public Resource getFilterIcon() {
			return null;
		}
		
		@Override
		public StatSubmInfoFilter<A> getFilter() {
			long startMillis = (start.getValue() != null ? start.getValue()
					.getTime() : 0L);
			long endMillis = (end.getValue() != null ? end.getValue().getTime()
					: Long.MAX_VALUE);
			return new DateFilter<A>(startMillis, endMillis);
		}
		
		@Override
		public Component getFilterEditView() {
			VerticalLayout res = new VerticalLayout();
			res.addComponent(new Label("start"));
			res.addComponent(start);
			res.addComponent(new Label("end"));
			res.addComponent(end);
			
			return res;
		}
		
		@Override
		public Component getMinifiedView() {
			Image img = new Image();
			img.setSource(getFilterIcon());
			img.setDescription(getFilterStateDesc());
			
			return img;
		}
		
		@Override
		public DateIntervalFilterEditor<A> getCopy() {
			DateField newStart = new DateField();
			DateField newEnd = new DateField();
			
			newStart.setValue(start.getValue());
			newEnd.setValue(end.getValue());
			
			DateIntervalFilterEditor<A> res = new DateIntervalFilterEditor<A>(
					localizer);
			res.end = newEnd;
			res.start = newStart;
			
			return res;
		}
		
		@Override
		public String getFilterStateDesc() {
			return getFilterName(localizer) + ": from " + start.getValue()
					+ " to " + end.getValue();
		}
		
		@Override
		public boolean checkAndNotify() {
			long startMillis = (start.getValue() != null ? start.getValue()
					.getTime() : 0L);
			long endMillis = (end.getValue() != null ? end.getValue().getTime()
					: Long.MAX_VALUE);
			if (endMillis > startMillis) {
				return true;
			} else {
				Notification
						.show("If both are specified, start-date must come before end-date");
				return false;
			}
		}
		
	}
	
	private static class EvaluationilterEditor<A extends SubmissionInfo>
			implements FilterEditor<A>, FilterEditorFactory<A> {
			
		private final Slider min;
		private final Slider max;
		private final Localizer localizer;
		
		public EvaluationilterEditor(Localizer localizer) {
			this.localizer = localizer;
			min = new Slider();
			max = new Slider();
			min.setMin(0.0);
			min.setMax(1.0);
			max.setMin(0.0);
			max.setMax(1.0);
			min.setResolution(2);
			max.setResolution(2);
			min.setWidth("50%");
			max.setWidth("50%");
		}
		
		@Override
		public FilterEditor<A> newEditorInstance(Localizer localizer) {
			return new EvaluationilterEditor<A>(localizer);
		}
		
		@Override
		public String getFilterDesc(Localizer localizer) {
			return "evaluation-range-editor";
		}
		
		@Override
		public String getFilterName(Localizer localizer) {
			return "evaluation-range-editor";
		}
		
		@Override
		public Resource getFilterIcon() {
			return null;
			// return StandardIcon.STAR_MEDIUM.getIcon();
		}
		
		@Override
		public StatSubmInfoFilter<A> getFilter() {
			return new EvaluationFilter<A>(min.getValue(), max.getValue());
		}
		
		@Override
		public Component getFilterEditView() {
			VerticalLayout res = new VerticalLayout();
			res.setSpacing(true);
			res.setMargin(true);
			res.setWidth("100%");
			res.addComponent(new Label("min"));
			res.addComponent(min);
			res.addComponent(new Label("max"));
			res.addComponent(max);
			
			return res;
		}
		
		@Override
		public Component getMinifiedView() {
			Image img = new Image();
			img.setSource(getFilterIcon());
			img.setDescription(getFilterStateDesc());
			
			return img;
			
		}
		
		@Override
		public EvaluationilterEditor<A> getCopy() {
			
			EvaluationilterEditor<A> res = new EvaluationilterEditor<A>(
					localizer);
			res.min.setValue(min.getValue());
			res.max.setValue(max.getValue());
			
			return res;
		}
		
		@Override
		public String getFilterStateDesc() {
			return getFilterName(localizer) + ": from " + min.getValue()
					+ " to " + max.getValue();
		}
		
		@Override
		public boolean checkAndNotify() {
			if (min.getValue() < max.getValue()) {
				return true;
			} else {
				Notification.show("Max must be at least equal to min");
				return false;
			}
		}
		
	}
	
	private static class TimeonTaskFilterEditor<A extends SubmissionInfo>
			implements FilterEditor<A>, FilterEditorFactory<A> {
			
		private final TextField min;
		private final TextField max;
		
		private final Localizer localizer;
		
		public TimeonTaskFilterEditor(Localizer localizer) {
			min = new TextField();
			max = new TextField();
			this.localizer = localizer;
			
		}
		
		@Override
		public TimeonTaskFilterEditor<A> newEditorInstance(Localizer localizer) {
			return new TimeonTaskFilterEditor<A>(localizer);
		}
		
		@Override
		public String getFilterDesc(Localizer localizer) {
			return "time-on-task-range-editor";
		}
		
		@Override
		public String getFilterName(Localizer localizer) {
			return "time-on-task-range-editor";
		}
		
		@Override
		public Resource getFilterIcon() {
			return null;
			// return StandardIcon.STOP_WATCH_MEDIUM.getIcon();
		}
		
		@Override
		public TimeOnTaskFilter<A> getFilter() {
			return new TimeOnTaskFilter<A>(getMin(), getMax());
		}
		
		private int getMin() {
			String val = min.getValue();
			if (val.length() == 0) {
				return 0;
			} else {
				return Integer.parseInt(val);
			}
		}
		
		private int getMax() {
			String val = max.getValue();
			if (val.length() == 0) {
				return 0;
			} else {
				return Integer.parseInt(val);
			}
		}
		
		@Override
		public Component getFilterEditView() {
			VerticalLayout res = new VerticalLayout();
			res.addComponent(new Label("min"));
			res.addComponent(min);
			res.addComponent(new Label("max"));
			res.addComponent(max);
			
			return res;
		}
		
		@Override
		public Component getMinifiedView() {
			Image img = new Image();
			img.setSource(getFilterIcon());
			img.setDescription(getFilterStateDesc());
			
			return img;
			
		}
		
		@Override
		public TimeonTaskFilterEditor<A> getCopy() {
			
			TimeonTaskFilterEditor<A> res = new TimeonTaskFilterEditor<A>(
					localizer);
			res.min.setValue(getMin() + "");
			res.max.setValue(getMax() + "");
			
			return res;
		}
		
		@Override
		public String getFilterStateDesc() {
			return getFilterName(localizer) + ": from " + getMin() + " to "
					+ getMax();
		}
		
		@Override
		public boolean checkAndNotify() {
			if (getMin() <= getMax()) {
				return true;
			} else {
				Notification.show("Max must be at least equal to min");
				return false;
			}
		}
		
	}
	
}
