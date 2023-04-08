package fr.an.tests.javafxwhiteapp.view;

import javafx.scene.input.MouseEvent;

public abstract class DefaultToolStateHandler {
    public abstract void onMouseEnter();
    public abstract void onMouseMove(MouseEvent event);
    public abstract void onMouseClick(MouseEvent event);
}
