package fr.an.tests.javafxwhiteapp.model;

/**
 * abstract base class for Drawing element
 * AST class hierarchy
 * see sub-classes:
 * <ul>
 * <li> TextDrawingElement </li>
 * <li> LineDrawingElement </li>
 * <li> RectanleDrawingElement </li>
 * <li> CircleDrawingElement </li>
 * <li> ImageDrawingElement (adapter design pattern, to image: png/jpg/gif/.. )</li>
 * <li> GroupDrawingElement (composite design-pattern) </li>
 * <li> other.. </li>
 * </ul>
 */
public abstract class DrawingElement {
    /**
     * Visitor design pattern
     * implement in sub-class, to call <code> visitor.caseXX(this); </code>
     */
    public abstract void accept(DrawingElementVisitor visitor);
}
