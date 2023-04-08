package fr.an.tests.javafxwhiteapp.view;

import fr.an.tests.javafxwhiteapp.controler.DrawingModelListener;
import fr.an.tests.javafxwhiteapp.model.BaseDrawingElements.*;
import fr.an.tests.javafxwhiteapp.model.DrawingDocModel;
import fr.an.tests.javafxwhiteapp.model.DrawingElement;
import fr.an.tests.javafxwhiteapp.model.DrawingElementVisitor;
import fr.an.tests.javafxwhiteapp.model.DrawingPt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CanvasDrawingView extends DrawingView {
    protected BorderPane component;
    // to add javafx.scene.shape.* objects converted from model
    protected Pane drawingPane;
    protected DefaultToolStateHandler currToolStateHandler = new DefaultSelectToolStateHandler();
    protected ObservableList<Node> currToolShapes = FXCollections.observableArrayList();
    protected CircleDrawingElement currEditLineStartPt;
    protected CircleDrawingElement currEditLineEndPt;
    protected LineDrawingElement currEditLine;

    protected DrawingModelListener innerListener = new DrawingModelListener() {
        @Override
        public void onModelChange() {
            System.out.println("(from subscribe): model to view change");
            refreshModelToView();
        }
    };

    public CanvasDrawingView(DrawingDocModel model) {
        super(model);
        this.component = new BorderPane();
        this.drawingPane = new Pane();
        component.setCenter(drawingPane);
        refreshModelToView();
        model.addListener(innerListener);
        { // button Toolbar
            ToolBar toolBar = new ToolBar();
            component.setTop(toolBar);
            Button resetToolButton = new Button("Reset");
            resetToolButton.setOnAction(e -> onClickToolReset());
            toolBar.getItems().add(resetToolButton);
            Button newLineButton = new Button("+Line");
            newLineButton.setOnAction(e -> onClickToolNewLine());
            toolBar.getItems().add(newLineButton);
        }
        drawingPane.setOnMouseEntered(e -> currToolStateHandler.onMouseEnter());
        drawingPane.setOnMouseMoved(e -> currToolStateHandler.onMouseMove(e));
        drawingPane.setOnMouseClicked(e -> currToolStateHandler.onMouseClick(e));
    }

    protected class DefaultSelectToolStateHandler extends DefaultToolStateHandler {
        @Override
        public void onMouseEnter() {
            drawingPane.setCursor(Cursor.DEFAULT);
        }

        @Override
        public void onMouseMove(MouseEvent event) {

        }

        @Override
        public void onMouseClick(MouseEvent event) {

        }
    }

    protected void updateCurrEditTool() {
        drawingPane.getChildren().removeAll(currToolShapes);
        currToolShapes.clear();
        JavafxDrawingElementVisitor visitor = new JavafxDrawingElementVisitor();
        if (currEditLineStartPt != null) { currEditLineStartPt.accept(visitor); }
        if (currEditLineEndPt != null) { currEditLineEndPt.accept(visitor); }
        if (currEditLine != null) { currEditLine.accept(visitor); }
        drawingPane.getChildren().addAll(currToolShapes);
    }

    private void onClickToolReset() {
        this.currToolStateHandler = new DefaultSelectToolStateHandler();
        currEditLineStartPt = null;
        currEditLineEndPt = null;
        currEditLine = null;
        refreshModelToView();
    }
    private void onClickToolNewLine() {
        this.currToolStateHandler = new StateInit_LineToolStateHandler();
    }
    protected void setToolHandler(DefaultToolStateHandler p) {
        currToolStateHandler = p;
        updateCurrEditTool();
    }

    protected class StateInit_LineToolStateHandler extends DefaultToolStateHandler {
        @Override
        public void onMouseEnter() {
            System.out.println("ok");
            drawingPane.setCursor(Cursor.CROSSHAIR);
        }

        @Override
        public void onMouseMove(MouseEvent event) {

        }

        @Override
        public void onMouseClick(MouseEvent event) {
            double x = event.getX(), y = event.getY();
            DrawingPt pt = new DrawingPt(x, y);
            currEditLineStartPt = new CircleDrawingElement(pt, 2);
            currEditLineEndPt = new CircleDrawingElement(pt, 2);
            currEditLine = new LineDrawingElement(pt, pt);
            updateCurrEditTool();
            setToolHandler(new StatePt1_LineToolStateHandler());
        }
    }

    protected class StatePt1_LineToolStateHandler extends DefaultToolStateHandler {
        @Override
        public void onMouseEnter() {
            drawingPane.setCursor(Cursor.CROSSHAIR);
        }

        @Override
        public void onMouseMove(MouseEvent event) {
            double x = event.getX(), y = event.getY();
            currEditLineEndPt.center = currEditLine.end = new DrawingPt(x, y);
            updateCurrEditTool();
        }

        @Override
        public void onMouseClick(MouseEvent event) {
            LineDrawingElement addToModel = currEditLine;
            GroupDrawingElement content = (GroupDrawingElement) model.getContent();
            content.elements.add(addToModel);
            model.setContent(content);
            currEditLine = null;
            currEditLineStartPt = null;
            currEditLineEndPt = null;
            updateCurrEditTool();
            setToolHandler(new DefaultSelectToolStateHandler());
        }
    }
    @Override
    public Node getComponent() {
        return component;
    }

    protected void refreshModelToView() {
        DrawingElement content = model.getContent();
        drawingPane.getChildren().clear();
        JavafxDrawingElementVisitor visitor = new JavafxDrawingElementVisitor();
        content.accept(visitor);
    }

    protected class JavafxDrawingElementVisitor extends DrawingElementVisitor {
        protected void add(Node node) {
            drawingPane.getChildren().add(node);
        }
        @Override
        public void caseText(TextDrawingElement p) {
            add(new Text(p.pos.x, p.pos.y, p.text));
        }
        @Override
        public void caseLine(LineDrawingElement p) {
            add(new Line(p.start.x, p.start.y, p.end.x, p.end.y));
        }
        @Override
        public void caseRect(RectangleDrawingElement p) {
            add(new Rectangle(p.upLeft.x, p.upLeft.y,
                    p.downRight.x-p.upLeft.x, p.downRight.y-p.upLeft.y));
        }
        @Override
        public void caseCircle(CircleDrawingElement p) {
            add(new Circle(p.center.x, p.center.y, p.radius));
        }
        @Override
        public void caseGroup(GroupDrawingElement p) {
            for(DrawingElement child: p.elements) {
// *** recurse ***
                child.accept(this);
            }
        }
        @Override
        public void caseOther(DrawingElement p) {
// "not implemented/recognized drawingElement "+ p.getClass().getName();
        }
    }
}
