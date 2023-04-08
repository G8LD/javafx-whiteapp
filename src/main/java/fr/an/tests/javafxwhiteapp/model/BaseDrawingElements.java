package fr.an.tests.javafxwhiteapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseDrawingElements {

    public static class TextDrawingElement extends DrawingElement {
        public String text;
        public DrawingPt pos;
        public Map<String,Object> properties; // font, size, color,

        public TextDrawingElement(String text, DrawingPt pos) {
            this.text = text;
            this.pos = pos;
        }

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseText(this);
        }
    }
    public static class LineDrawingElement extends DrawingElement {
        public DrawingPt start;
        public DrawingPt end;
        public Map<String,Object> properties; // width, stroke, color, ..

        public LineDrawingElement(DrawingPt start, DrawingPt end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseLine(this);
        }
    }
    public static class RectangleDrawingElement extends DrawingElement {
        public DrawingPt upLeft;
        public DrawingPt downRight;
        public Map<String,Object> properties; // width, stroke, color, ..

        public RectangleDrawingElement(DrawingPt upLeft, DrawingPt downRight) {
            this.upLeft = upLeft;
            this.downRight = downRight;
        }

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseRect(this);
        }
    }
    public static class CircleDrawingElement extends DrawingElement {
        public DrawingPt center;
        public double radius;
        public Map<String,Object> properties; // width, stroke, color, ..

        public CircleDrawingElement(DrawingPt center, double radius) {
            this.center = center;
            this.radius = radius;
        }

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseCircle(this);
        }
    }

    /**
     * Composite design pattern
     */
    public static class GroupDrawingElement extends DrawingElement {
        public List<DrawingElement> elements = new ArrayList<>();

        public GroupDrawingElement() {
            this.elements = new ArrayList<>();
        }

        public void addAll(DrawingElement... elements) {
            for (DrawingElement element : elements) {
                this.elements.add(element);
            }
        }

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseGroup(this);
        }
    }
    /**
     * Adapter design pattern, for javafx.scene.image.Image
     */
    public static class ImageDrawingElement extends DrawingElement {
        public javafx.scene.image.Image image; // => url, mimeType, data

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseOther(this);
        }
    }

    /**
     * Proxy design pattern
     * example: including part from another document
     */
    public static class ProxyDrawingElement extends DrawingElement {
        public DrawingElement underlying;

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseOther(this);
        }
    }
    /**
     * Decorator design pattern
     * For geometrical affine transformation
     */
    public static class AffineTransformedDrawingElement extends DrawingElement {
        public DrawingElement underlying;
        public DrawingPt translate;
        public double rotateAngle;
        public double scale;

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseOther(this);
        }
    }
}
