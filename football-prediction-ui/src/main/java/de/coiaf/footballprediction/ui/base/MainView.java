package de.coiaf.footballprediction.ui.base;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.*;

public class MainView extends VerticalLayout implements ViewDisplay {

    private static final String DEFAULT_HEADLINE = "No display view";

    private MenuBar navigationMenu;
    private VerticalLayout contentContainer;
    private Label headline;

    public MainView() {
        this.init();
    }

    @Override
    public void showView(View view) {
        // 1. remove the current View Type
        this.contentContainer.removeAllComponents();
        // 3. inject the new View
        this.contentContainer.addComponent(view.getViewComponent());

        if (view instanceof AbstractViewApplication) {
            this.headline.setValue(((AbstractViewApplication) view).getTitle());
        }
        else {
            this.headline.setValue(DEFAULT_HEADLINE);
        }
    }

    private void init() {
        this.setSizeFull();
        this.setResponsive(true);
        this.addStyleName("app-shell");
        this.setSpacing(false);

        this.headline = new Label();
        this.addComponent(this.headline);
        this.setComponentAlignment(this.headline, Alignment.TOP_LEFT);
        this.navigationMenu = this.createNavigationMenu();
        this.addComponent(this.navigationMenu);
        this.setComponentAlignment(this.navigationMenu, Alignment.TOP_LEFT);

        VerticalLayout wrapperContentContainer = new VerticalLayout();
        wrapperContentContainer.setSizeFull();
        wrapperContentContainer.setSpacing(false);
        wrapperContentContainer.setMargin(false);

        Label spacer = new Label(" ");
        wrapperContentContainer.addComponent(spacer);
        wrapperContentContainer.setComponentAlignment(spacer, Alignment.TOP_LEFT);

        this.contentContainer = this.createContentContainer();
        wrapperContentContainer.addComponent(this.contentContainer);
        wrapperContentContainer.setComponentAlignment(this.contentContainer, Alignment.TOP_LEFT);
        wrapperContentContainer.setExpandRatio(this.contentContainer, 1.0f);

        this.addComponent(wrapperContentContainer);
        this.setComponentAlignment(wrapperContentContainer, Alignment.TOP_LEFT);
        this.setExpandRatio(wrapperContentContainer, 1.0f);
    }

    private MenuBar createNavigationMenu() {
        MenuBar navigationMenu = new MenuBar();
        MenuBar.Command command = null;

        MenuBar.MenuItem mainItemAdd = navigationMenu.addItem("Add ...", null);
        mainItemAdd.addItem("Import single Season from football-data.co.uk" , selectedItem -> {UI.getCurrent().getNavigator().navigateTo(ViewApplication.VIEW_NAME_IMPORT_RESULTS_FOOTBALL_DATA_CO_UK_FILE_PER_SEASON);});

        MenuBar.MenuItem mainItemPrediction = navigationMenu.addItem("Predictions", null);

        return navigationMenu;
    }

    private VerticalLayout createContentContainer() {
        VerticalLayout content = new VerticalLayout();

        content.setMargin(false);
        content.setSpacing(false);
        content.setSizeFull();

        return content;
    }

}
