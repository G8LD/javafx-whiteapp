package fr.an.tests.javafxwhiteapp.model;

import fr.an.tests.javafxwhiteapp.model.BaseDrawingElements.*;

public abstract class DrawingElementVisitor {
    public abstract void caseText(TextDrawingElement p);
    public abstract void caseLine(LineDrawingElement p);
    public abstract void caseRect(RectangleDrawingElement p);
    public abstract void caseCircle(CircleDrawingElement p);
    public abstract void caseGroup(GroupDrawingElement p);
    public abstract void caseOther(DrawingElement p);
}
