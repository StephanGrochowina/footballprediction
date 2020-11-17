package de.coiaf.footballprediction.ui.base;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;

@CDIView(TestViewApplication.VIEW_NAME)
@UIScoped
public class TestViewApplication extends AbstractViewApplication {

    public static final String VIEW_NAME = "test";

    @Override
    protected void assignViewToPresenter() {
        // TODO Presenter hinzufügen
    }

    @Override
    protected Component createViewContent() {
        TextArea textArea = new TextArea();

        textArea.setValue("Hier könnte was Sinnvolles passieren.");

        return textArea;
    }

    @Override
    public String getTitle() {
        return "Test";
    }
}
