/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3d;

import java.awt.AWTException;
import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author ad
 */
public class Simple3DTest extends BaseTest{

    
    public Simple3DTest() {
    }

    @Test
    public void testStart() throws InterruptedException, IOException, AWTException {

        //if(app.checkbo == null)
        //    fail("failed to get control instance");
        Simple3D app = Simple3D.getApplicationInstance();
        //CameraMan cameraMan = app.getSimpleScene().getCameraMan();
        //cameraMan.moveRight(-15);
        //app.getSimpleScene().getRoot().getChildrenUnmodifiable()
        this.qa.moveMouseToSimpleScene();
        Thread.sleep(1000);
        this.qa.keyClickN(java.awt.event.KeyEvent.VK_RIGHT, 5);
        Thread.sleep(1000);
        this.qa.click(app.getSimpleScene().getRoot().getChildrenUnmodifiable().get(0));

        Thread.sleep(2000);
        //Platform.setImplicitExit(false);
        //Platform.exit();

        Thread.sleep(5000);
        System.out.println("WAITED!!!");
    }

}
