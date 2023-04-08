package fr.an.tests.javafxwhiteapp.model;

public class DrawingPt {
    public double x;
    public double y;
    public DrawingPt() {
    }
    public DrawingPt(
            double x,
            double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x  + ", " + y + ")";
    }
}
