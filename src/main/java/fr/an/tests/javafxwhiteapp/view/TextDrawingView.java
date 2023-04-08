package fr.an.tests.javafxwhiteapp.view;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import fr.an.tests.javafxwhiteapp.controler.DrawingModelListener;
import fr.an.tests.javafxwhiteapp.model.BaseDrawingElements.*;
import fr.an.tests.javafxwhiteapp.model.DrawingDocModel;
import fr.an.tests.javafxwhiteapp.model.DrawingElement;
import fr.an.tests.javafxwhiteapp.model.DrawingElementVisitor;
import fr.an.tests.javafxwhiteapp.model.DrawingPt;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class TextDrawingView extends DrawingView {

    protected BorderPane component;
    protected TextArea textArea;
    protected Button applyButton;

    protected DrawingModelListener innerListener = new DrawingModelListener() {
        @Override
        public void onModelChange() {
            System.out.println("(from subscribe): model to view change");
            refreshModelToView();
        }
    };

    public TextDrawingView(DrawingDocModel model) {
        super(model);
        this.component = new BorderPane();
        this.textArea = new TextArea();
        component.setCenter(textArea);
        this.applyButton = new Button("Apply");
        applyButton.setOnAction(e -> onClickApply());
        component.setBottom(applyButton);
        model.addListener(innerListener);
        refreshModelToView();
    }

    XStream xstream = createXStream();

    static XStream createXStream() {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("Pt", DrawingPt.class);
        xstream.alias("Text", TextDrawingElement.class);
        xstream.alias("Line", LineDrawingElement.class);
        xstream.alias("Circle", CircleDrawingElement.class);
        xstream.alias("Rectangle", RectangleDrawingElement.class);
        xstream.alias("Group", GroupDrawingElement.class);
        return xstream;
    }

    protected void refreshModelToView() {
        DrawingElement content = model.getContent();
        String text = xstream.toXML(content);
        textArea.setText(text);
    }

    private void onClickApply() {
        System.out.println("apply view to model update");
        String text = textArea.getText();
        DrawingElement content = (DrawingElement) xstream.fromXML(text);
        model.setContent(content); // => fireModelChange ..
    }

    @Override
    public Node getComponent() {
        return component;
    }

    private String recursiveElementToText(DrawingElement drawingElement) {
        TextDrawingElementVisitor visitor = new TextDrawingElementVisitor();
        drawingElement.accept(visitor);
        return visitor.result;
    }

    protected static class TextDrawingElementVisitor extends DrawingElementVisitor {
        String result;

        @Override
        public void caseText(TextDrawingElement p) {
            result = "Text(" + p.pos + ",'" + p.text + "')";
        }

        @Override
        public void caseLine(LineDrawingElement p) {
            result = "Line(" + p.start + ", " + p.end + ")";
        }

        @Override
        public void caseRect(RectangleDrawingElement p) {
            result = "Rect(" + p.upLeft + ", " + p.downRight + ")";
        }

        @Override
        public void caseCircle(CircleDrawingElement p) {
            result = "Circle(" + p.center + ", " + p.radius + ")";
        }

        @Override
        public void caseGroup(GroupDrawingElement p) {
            StringBuilder sb = new StringBuilder();
            sb.append("Group[\n");
            for (DrawingElement child : p.elements) {
// *** recurse ***
                child.accept(this);
                sb.append(result);
                sb.append("\n");
            }
            sb.append("]");
            result = sb.toString();
        }

        @Override
        public void caseOther(DrawingElement p) {
            result = "not implemented/recognized drawingElement " + p.getClass().getName();
        }
    }
}

