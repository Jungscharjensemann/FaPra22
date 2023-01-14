package gcat.editor.view.palletes;

import gcat.editor.view.EditorPalette;

import javax.swing.*;
import java.util.Objects;

public class GoogleVisionPallete extends EditorPalette {

    public GoogleVisionPallete() {
        setTitle("Google Vision API");
        addTemplate(
                "Container",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/swimlane.png"))),
                "swimlane", 280, 280, "Container");

        addTemplate(
                "Icon",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/rounded.png"))),
                "icon;image=" +getClass().getClassLoader().getResource("images/rounded.png"),
                70, 70, "Icon");

        addTemplateWithAttribute(
                "Label",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/rounded.png"))),
                "label;image=" +getClass().getClassLoader().getResource("images/gear.png"),
                130, 50, "Label");

        addTemplate(
                "Rectangle",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/rectangle.png"))),
                null, 100, 60, ""); //160, 120

        addTemplate(
                "Rounded Rectangle",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/rounded.png"))),
                "rounded=1", 160, 120, "");

        addTemplate(
                "Double Rectangle",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/doublerectangle.png"))),
                "rectangle;shape=doubleRectangle", 160, 120, "");

        addTemplate(
                "Ellipse",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/ellipse.png"))),
                "ellipse", 160, 160, "");

        addTemplate(
                "Double Ellipse",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/doubleellipse.png"))),
                "ellipse;shape=doubleEllipse", 160, 160, "");

        addTemplate(
                "Triangle",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/triangle.png"))),
                "triangle", 120, 160, "");

        addTemplate(
                "Rhombus",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/rhombus.png"))),
                "rhombus", 160, 160, "");

        addTemplate(
                "Horizontal Line",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/hline.png"))),
                "line", 160, 10, "");

        addTemplate(
                "Hexagon",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/hexagon.png"))),
                "shape=hexagon", 160, 120, "");

        addTemplate(
                "Cylinder",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/cylinder.png"))),
                "shape=cylinder", 120, 160, "");

        addTemplate(
                "Actor",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/actor.png"))),
                "shape=actor", 120, 160, "");

        addTemplate(
                "Cloud",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/cloud.png"))),
                "ellipse;shape=cloud", 160, 120, "");

        addTemplate("Test", null, "shape=collection", 100, 40, "");


        addEdgeTemplate(
                "Straight",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/straight.png"))),
                "straight", 120, 120, "");

        addEdgeTemplate(
                "Horizontal Connector",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/connect.png"))),
                null, 100, 100, "");

        addEdgeTemplate(
                "Vertical Connector",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/vertical.png"))),
                "vertical", 100, 100, "");

        addEdgeTemplate(
                "Entity Relation",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/entity.png"))),
                "entity", 100, 100, "");

        addEdgeTemplate(
                "Arrow",
                new ImageIcon(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResource("images/arrow.png"))),
                "arrow", 120, 120, "");
    }
}
