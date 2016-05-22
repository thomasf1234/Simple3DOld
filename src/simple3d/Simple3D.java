/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3d;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import simpleobj.ObjModelXML;

//http://www.developer.com/java/data/3d-graphics-in-javafx.html
public class Simple3D extends Application {

    private double mouseXOld = 0;
    private double mouseYOld = 0;

    @Override
    public void start(Stage primaryStage) {
        //TriangleMesh pyramidMesh = new TriangleMesh();
        TriangleMesh mesh = null;
        try {
            mesh = ObjModelXML.read("src\\simple3d\\Suzanne.xml").toTriangleMesh();
        } catch (Exception ex) {
            System.out.println(ex);
            Logger.getLogger(Simple3D.class.getName()).log(Level.SEVERE, null, ex);
        }

        MeshView pyramid = new MeshView(mesh);
        pyramid.setCullFace(CullFace.BACK);
        pyramid.setDrawMode(DrawMode.FILL);
        PhongMaterial material1 = new PhongMaterial();
        material1.setDiffuseColor(Color.BLUE);
        material1.setSpecularColor(Color.LIGHTBLUE);
        material1.setSpecularPower(10.0);
        pyramid.setMaterial(material1);
        pyramid.setTranslateX(0);
        pyramid.setTranslateY(0);
        pyramid.setTranslateZ(100);
        pyramid.setScaleX(10);
        pyramid.setScaleY(10);
        pyramid.setScaleZ(10);

        Group root = new Group();

        root.getChildren().add(pyramid); //http://www.developer.com/java/other/understanding-3d-graphics-in-java.html
        buildAxes(root);

        Scene scene = new Scene(root, 600, 400, true, SceneAntialiasing.BALANCED);
        SimpleCamera camera = new SimpleCamera();

        scene.setFill(Color.GREY);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);

        scene.setCamera(camera);
//http://www.centigrade.de/blog/wp-content/uploads/SurfaceFunctions_013.png

        handleKeyboard(scene);
        setMouseEvents(scene);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Simple3D");
        primaryStage.setScene(scene);
        primaryStage.show();
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

    private void handleKeyboard(final Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                SimpleCamera camera = (SimpleCamera) scene.getCamera();

                switch (event.getCode()) {
                    case L:
                        camera.lookAtTarget();
                        break;
                    case F:
                        camera.moveForward(3);
                        break;
                    case B:
                        camera.moveForward(-3);
                        break;
                    case UP:
                        camera.moveUp(3);
                        break;
                    case DOWN:
                        camera.moveUp(-3);
                        break;
                    case LEFT:
                        camera.moveRight(-3);
                        break;
                    case RIGHT:
                        camera.moveRight(3);
                        break;
                    case O:
                        camera.reset();
                        break;
                }

                event.consume();
            }
        });
    }

    private void setMouseEvents(final Scene scene) {
        final SimpleCamera camera = (SimpleCamera) scene.getCamera();
        //handles mouse scrolling
        scene.setOnScroll(
                new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();
                camera.moveForward(deltaY);
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
                        camera.xRotate.setAngle(camera.xRotate.getAngle() - dy / 5);
                        camera.yRotate.setAngle(camera.yRotate.getAngle() + dx / 5);
                    }
                    mouseXOld = mouseXNew;
                    mouseYOld = mouseYNew;
                    event.consume();
                } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    Node selectedNode = event.getPickResult().getIntersectedNode();
                    if (selectedNode != null) {
                        camera.setTarget(selectedNode.getTranslateX(), selectedNode.getTranslateY(), selectedNode.getTranslateZ());
                        camera.lookAtTarget();
                    }
                }
            }
        };

        scene.addEventHandler(MouseEvent.ANY, mouseEventHandler);
    }
}

//http://docs.oracle.com/javafx/2/events/processing.htm#CEGJAAFD
//http://www.developer.com/java/other/understanding-3d-graphics-in-java.html
