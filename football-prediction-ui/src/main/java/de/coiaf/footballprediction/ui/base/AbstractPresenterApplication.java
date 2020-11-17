package de.coiaf.footballprediction.ui.base;

public class AbstractPresenterApplication<V extends ViewApplication> implements ViewApplication.ObserverViewApplication<V>, PresenterApplication<V> {

    private V view;

    protected V getView() {
        return this.view;
    }

    @Override
    public void setView(V view) {
        this.view = view;
    }
}
