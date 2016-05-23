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
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import simpleobj.ObjModelXML;
import simpleobj.SimpleObj;

//http://www.developer.com/java/data/3d-graphics-in-javafx.html
public class Simple3D extends Application {

    private double mouseXOld = 0;
    private double mouseYOld = 0;
    private Group root;
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) {
        //TriangleMesh pyramidMesh = new TriangleMesh();
        SimpleObj mesh = null;
        try {
            mesh = ObjModelXML.read("src\\simple3d\\Suzanne.xml").toSimpleObj();
        } catch (Exception ex) {
            System.out.println(ex);
            Logger.getLogger(Simple3D.class.getName()).log(Level.SEVERE, null, ex);
        }

        final MeshView pyramid = new MeshView(mesh);
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
        
       

        this.root = new Group();
        this.canvas = new Canvas(600, 400);

        root.getChildren().add(pyramid); //http://www.developer.com/java/other/understanding-3d-graphics-in-java.html
         root.getChildren().add(canvas);
        buildAxes(root);

        
        final SimpleCamera camera = new SimpleCamera();

        
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);

        //Scene scene = new Scene(root, 600, 400, true, SceneAntialiasing.BALANCED);
        SubScene subScene = new SubScene(root, 600, 400, true, SceneAntialiasing.BALANCED);
    subScene.setFill(Color.LIGHTGREY);
    subScene.setCamera(camera);
    //scene.setFill(Color.GREY);
        //scene.setCamera(camera);
//http://www.centigrade.de/blog/wp-content/uploads/SurfaceFunctions_013.png

        handleKeyboard(subScene);
        setMouseEvents(subScene);
        
        
        
        //
        BorderPane pane = new BorderPane();
    pane.setCenter(subScene);
    Button button = new Button("Reset");
    button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              camera.reset();   
            }
    });
    final CheckBox checkBox = new CheckBox("Wireframe");
    checkBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              pyramid.setDrawMode(checkBox.isSelected()?DrawMode.LINE:DrawMode.FILL);  
            }
    });
    
    
    ToolBar toolBar = new ToolBar(button, checkBox);
    toolBar.setOrientation(Orientation.VERTICAL);
    pane.setRight(toolBar);
    pane.setPrefSize(600,400);

    Scene scene = new Scene(pane);
        //
        
        

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

    private void handleKeyboard(final SubScene scene) {
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
                    case R:
                        camera.removeTarget();
                        break;
                }

                event.consume();
            }
        });
    }

    private void setMouseEvents(final SubScene scene) {
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
                } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED_TARGET && 3 == 4) {
Node selectedNode = event.getPickResult().getIntersectedNode();
//((MeshView) selectedNode).setDrawMode(DrawMode.LINE);
MeshView selectedObject = ((MeshView) selectedNode);
TriangleMesh mesh =     (TriangleMesh) selectedObject.getMesh();
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
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
         gc.setFill(Color.GREEN);
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        //http://stackoverflow.com/questions/28628702/javafx-2d-part-in-3d-application
 
        Bounds bounds = event.getPickResult().getIntersectedNode().getLayoutBounds();
        gc.strokeRect(bounds.getMaxX(), bounds.getMaxY(), bounds.getWidth(), bounds.getHeight());
        
        
                } else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
                    scene.requestFocus();
                    
                }
            }
        };

        scene.addEventHandler(MouseEvent.ANY, mouseEventHandler);
    }
}

//http://docs.oracle.com/javafx/2/events/processing.htm#CEGJAAFD
//http://www.developer.com/java/other/understanding-3d-graphics-in-java.html
//http://stackoverflow.com/questions/12553013/javafx-overlapping-scenes
//http://stackoverflow.com/questions/28628702/javafx-2d-part-in-3d-application
//http://stackoverflow.com/questions/32726159/how-to-create-fog-in-javafx-3d?rq=1//