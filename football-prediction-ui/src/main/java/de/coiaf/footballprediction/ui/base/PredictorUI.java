package de.coiaf.footballprediction.ui.base;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

import javax.inject.Inject;

/**
 *
 */
@Theme("mytheme")
@Viewport("width=device-width")
@CDIUI("")
public class PredictorUI extends UI {

    @Inject
    private CDIViewProvider viewprovider;

    @Inject
    private MainView mainView;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Navigator navigator = new Navigator(this, (ViewDisplay) this.mainView);

        navigator.addProvider(this.viewprovider);
        navigator.addViewChangeListener(this.createViewChangeListener());

        this.setContent(this.mainView);

        navigator.navigateTo(TestViewApplication.VIEW_NAME);
    }

    private ViewChangeListener createViewChangeListener(){
        ViewChangeListener listener = new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {
                View targetView = viewChangeEvent.getNewView();
                // TODO targetView und ggfs. this.mainView anpassen
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {

            }
        };

        return listener;
    }

//    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
//    @VaadinServletConfiguration(ui = PredictorUI.class, productionMode = false)
//    public static class MyUIServlet extends VaadinServlet {
//    }
}
