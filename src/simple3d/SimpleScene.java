/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3d;

import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;

/**
 *
 * @author ad
 */
public class SimpleScene extends SubScene {

    public SimpleScene(Group group, int width, int height) {
        super(group, width, height, true, SceneAntialiasing.BALANCED);
        setFill(Color.LIGHTGREY);
        SimpleCamera camera = new SimpleCamera();
        setCamera(camera);
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);
    }

}
