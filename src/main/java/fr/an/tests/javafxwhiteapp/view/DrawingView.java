package fr.an.tests.javafxwhiteapp.view;

import fr.an.tests.javafxwhiteapp.model.DrawingDocModel;
import javafx.scene.Node;

public abstract class DrawingView {

    protected DrawingDocModel model;

    public DrawingView(DrawingDocModel model) {
        this.model = model;
    }
    public abstract Node getComponent();
    // more later: Subscriber design-pattern
}
