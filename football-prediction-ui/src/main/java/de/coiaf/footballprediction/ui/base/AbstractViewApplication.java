package de.coiaf.footballprediction.ui.base;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

public abstract class AbstractViewApplication<V extends ViewApplication, OS extends ViewApplication.ObserverViewApplication<V>> extends VerticalLayout
        implements ViewApplication, View {

    private Component viewContent;
    private OS observer;

    @PostConstruct
    public final void init() {
        this.assignViewToPresenter();
        this.beforePostConstruct();


        this.setSizeFull();
        this.setMargin (false);
        this.setSpacing (false);

        this.viewContent = this.createViewContent();
        addComponent(this.viewContent);

        this.afterPostConstruct();
    }

    protected void assignViewToPresenter() {
        this.getObserver().setView((V) this);
    }
    protected abstract Component createViewContent();

    protected void beforePostConstruct() {
    }

    protected void afterPostConstruct(){

    }

    protected OS getObserver() {
        return this.observer;
    }

    protected void setObserver(OS observer) {
        this.observer = observer;
    }
}
