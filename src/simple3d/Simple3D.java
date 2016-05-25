/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3d;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import simpleobj.ObjModelXML;
import simpleobj.SimpleObj;

//http://www.developer.com/java/data/3d-graphics-in-javafx.html
public class Simple3D extends Application {

    private static Simple3D applicationInstance;
    private double mouseXOld = 0;
    private double mouseYOld = 0;
    private Group root;
    private SimpleObj pyramid;

    private Stage stage;
    private static boolean testMode;
    private SimpleScene simpleScene;
    
    public static void startTestMode() {
        testMode = true;
        Simple3D.main(new String[0]);
    }

    public void show() {
        this.stage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        //TriangleMesh pyramidMesh = new TriangleMesh();
        this.pyramid = null;
        try {
            pyramid = ObjModelXML.read("src\\simple3d\\Suzanne.xml").toSimpleObj();
        } catch (Exception ex) {
            System.out.println(ex);
            Logger.getLogger(Simple3D.class.getName()).log(Level.SEVERE, null, ex);
        }

        pyramid.setTranslateX(0);
        pyramid.setTranslateY(0);
        pyramid.setTranslateZ(100);
        pyramid.setScaleX(10);
        pyramid.setScaleY(10);
        pyramid.setScaleZ(10);

        this.root = new Group();

        root.getChildren().add(pyramid); //http://www.developer.com/java/other/understanding-3d-graphics-in-java.html
        buildAxes(root);

        final SimpleScene simpleScene = new SimpleScene(root, 600, 400);
//http://www.centigrade.de/blog/wp-content/uploads/SurfaceFunctions_013.png

        handleKeyboard(simpleScene);
        setMouseEvents(simpleScene);

        //
        BorderPane pane = new BorderPane();
        pane.setCenter(simpleScene);
        Button button = new Button("Reset");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                simpleScene.getCameraMan().reset();
            }
        });
        final CheckBox checkBox = new CheckBox("Wireframe");
        checkBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("x,y: " + checkBox.localToScreen(checkBox.getLayoutBounds().getMinX(), checkBox.getLayoutBounds().getMinY()).toString());
                //pyramid.setDrawMode(checkBox.isSelected() ? DrawMode.LINE : DrawMode.FILL);
            }
        });

        ToolBar toolBar = new ToolBar(button, checkBox);
        toolBar.setOrientation(Orientation.VERTICAL);
        pane.setRight(toolBar);
        pane.setPrefSize(600, 400);

        Scene scene = new Scene(pane);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Simple3D");
        primaryStage.setScene(scene);
        if (Simple3D.testMode) {
            Simple3D.applicationInstance = this;
            this.stage = primaryStage;
            this.simpleScene = simpleScene;
            System.out.println("application klass: " + this.getClass());
            final Stage window = new Stage();
            window.setX(10);
            window.setY(10);
            Scene innerScene = new Scene(new Label("Running in test mode"));
            window.setScene(innerScene);
            window.show();
            primaryStage.show();
        } else {
            primaryStage.show();
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static Simple3D getApplicationInstance() {
        return applicationInstance;
    }

    public Stage getStage() {
        return this.stage;
    }
    
    public SimpleScene getSimpleScene() {
        return this.simpleScene;
    }

    private void buildAxes(Group root) {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(240.0, 1, 1);
        final Box yAxis = new Box(1, 240.0, 1);
        final Box zAxis = new Box(1, 1, 240.0);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        root.getChildren().addAll(xAxis, yAxis, zAxis);
    }

    private void handleKeyboard(final SimpleScene simpleScene) {
        simpleScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                CameraMan cameraMan = simpleScene.getCameraMan();

                switch (event.getCode()) {
                    case L:
                        cameraMan.faceTarget();
                        break;
                    case F:
                        cameraMan.moveForward(3);
                        break;
                    case B:
                        cameraMan.moveForward(-3);
                        break;
                    case UP:
                        cameraMan.moveUp(3);
                        break;
                    case DOWN:
                        cameraMan.moveUp(-3);
                        break;
                    case LEFT:
                        cameraMan.moveRight(-3);
                        break;
                    case RIGHT:
                        cameraMan.moveRight(3);
                        break;
                    case O:
                        cameraMan.reset();
                        break;
                    case R:
                        cameraMan.removeTarget();
                        break;
                    case P:
                        cameraMan.setPerspective(!cameraMan.hasPerspectiveCamera());
                        break;
                    case K:
                        pyramid.setRotationAxis(Rotate.Y_AXIS);
                        pyramid.setRotate(pyramid.getRotate() + 1);
                        break;

                }

                event.consume();
            }
        });
    }

    private void setMouseEvents(final SimpleScene simpleScene) {
        final CameraMan cameraMan = simpleScene.getCameraMan();
        //handles mouse scrolling
        simpleScene.setOnScroll(
                new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();
                cameraMan.moveForward(deltaY);
                event.consume();
            }
        });

        EventHandler mouseEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED || event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    //acquire new mouse coordinates
                    double mouseXNew = event.getSceneX();
                    double mouseYNew = event.getSceneY();

                    if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                        System.out.println("MouseX: " + mouseXNew + ", MouseY: " + mouseYNew);
                        double dx = mouseXNew - mouseXOld;
                        double dy = mouseYNew - mouseYOld;
                        cameraMan.xRotate.setAngle(cameraMan.xRotate.getAngle() - dy / 5);
                        cameraMan.yRotate.setAngle(cameraMan.yRotate.getAngle() + dx / 5);
                    }
                    mouseXOld = mouseXNew;
                    mouseYOld = mouseYNew;
                    event.consume();
                } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    Node selectedNode = event.getPickResult().getIntersectedNode();
                    if (selectedNode != null) {
                        cameraMan.setTarget(selectedNode.getTranslateX(), selectedNode.getTranslateY(), selectedNode.getTranslateZ());
                        cameraMan.faceTarget();
                    }
                } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED_TARGET && 8 == 9) {
                    Node selectedNode = event.getPickResult().getIntersectedNode();
//((MeshView) selectedNode).setDrawMode(DrawMode.LINE);
                    MeshView selectedObject = ((MeshView) selectedNode);
                    TriangleMesh mesh = (TriangleMesh) selectedObject.getMesh();
                    TriangleMesh cloneMesh = new TriangleMesh();
                    cloneMesh.getPoints().addAll(mesh.getPoints());
                    cloneMesh.getTexCoords().addAll(mesh.getTexCoords());
                    cloneMesh.getFaces().addAll(mesh.getFaces());
                    MeshView clone = new MeshView(mesh);
                    clone.setCullFace(CullFace.FRONT);
                    clone.setDrawMode(DrawMode.FILL);
                    PhongMaterial material = new PhongMaterial();
                    material.setDiffuseColor(Color.PINK);
                    material.setSpecularColor(Color.PINK);
                    material.setSpecularPower(-100.0);
                    clone.setMaterial(material);
                    clone.setTranslateX(selectedObject.getTranslateX());
                    clone.setTranslateY(selectedObject.getTranslateY());
                    clone.setTranslateZ(selectedObject.getTranslateZ());
                    clone.setScaleX(11);
                    clone.setScaleY(11);
                    clone.setScaleZ(11);
                    PointLight light = new PointLight();
                    light.setColor(Color.WHITE);
                    light.setTranslateX(selectedObject.getTranslateX());
                    light.setTranslateY(selectedObject.getTranslateY());
                    light.setTranslateZ(selectedObject.getTranslateZ());
                    root.getChildren().addAll(clone, light);

                    //http://stackoverflow.com/questions/28628702/javafx-2d-part-in-3d-application
//                    Bounds bounds = event.getPickResult().getIntersectedNode().getLayoutBounds();
//                    gc.strokeRect(bounds.getMaxX(), bounds.getMaxY(), bounds.getWidth(), bounds.getHeight());
                } else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
                    simpleScene.requestFocus();

                }
            }
        };

        simpleScene.addEventHandler(MouseEvent.ANY, mouseEventHandler);
    }
}

//http://docs.oracle.com/javafx/2/events/processing.htm#CEGJAAFD
//http://www.developer.com/java/other/understanding-3d-graphics-in-java.html
//http://stackoverflow.com/questions/12553013/javafx-overlapping-scenes
//http://stackoverflow.com/questions/28628702/javafx-2d-part-in-3d-application
//http://stackoverflow.com/questions/32726159/how-to-create-fog-in-javafx-3d?rq=1//
