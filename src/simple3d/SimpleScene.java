/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3d;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;

/**
 *
 * @author ad
 */
public class SimpleScene extends SubScene {

    private CameraMan cameraMan;
    
    public SimpleScene(Group root, int width, int height) {
        super(root, width, height, true, SceneAntialiasing.BALANCED);
        setFill(Color.LIGHTGREY);
        this.cameraMan = new CameraMan(this);
        this.cameraMan.setPosition(Point3D.ZERO);
    }
    
    public CameraMan getCameraMan() {
        return this.cameraMan;
    }

}
