package fr.an.tests.javafxwhiteapp.model;

import fr.an.tests.javafxwhiteapp.controler.DrawingModelListener;

import java.util.ArrayList;
import java.util.List;

public class DrawingDocModel {
    protected String documentName;
    protected DrawingElement content;

    protected List<DrawingModelListener> listeners = new ArrayList<>();

    public void setContent(DrawingElement content) {
        this.content = content;
        fireModelChange();
    }
    public void addListener(DrawingModelListener p) {
        this.listeners.add(p);
    }
    public void removeListener(DrawingModelListener p) {
        this.listeners.remove(p);
    }
    protected void fireModelChange() {
        for(DrawingModelListener listener : listeners) {
            listener.onModelChange();
        }
    }

    public DrawingElement getContent() {
        return content;
    }

// more later: Publisher design-pattern
}
