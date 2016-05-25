/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3d;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author ad
 */
public class QATester extends Robot {

    private Simple3D application;

    public QATester(Simple3D application) throws AWTException {
        super();
        this.application = application;
    }

    public void click(javafx.scene.control.Control control) throws AWTException, IOException {
        java.awt.Point originalLocation = java.awt.MouseInfo.getPointerInfo().getLocation();
        javafx.geometry.Point2D buttonLocation = control.localToScreen(control.getLayoutBounds().getMinX(), control.getLayoutBounds().getMinY());
        try {
            mouseMove((int) buttonLocation.getX(), (int) buttonLocation.getY());
            mousePress(InputEvent.BUTTON1_MASK);
            mouseRelease(InputEvent.BUTTON1_MASK);
            mouseMove((int) originalLocation.getX(), (int) originalLocation.getY());
        } catch (Exception e) {
            takeScreenshot("logs\\errorSnapshot");
            e.printStackTrace();
            org.junit.Assert.fail(e.getMessage());
        }
    }
    
    public void moveMouseToSimpleScene() {
     mouseMove(0,0);   
    }


    public void takeScreenshot(String fileName) throws AWTException, IOException {
        Stage stage = this.application.getStage();
        int x = (int) stage.getX();
        int y = (int) stage.getY();
        int width = (int) stage.getWidth();
        int height = (int) stage.getHeight();
        java.awt.Rectangle area = new java.awt.Rectangle(x, y, width, height);
        BufferedImage image = new Robot().createScreenCapture(area);
        ImageIO.write(image, "png", new File(fileName + ".png"));
    }
}
