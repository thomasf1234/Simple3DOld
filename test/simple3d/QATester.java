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
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author ad
 */
public class QATester extends Robot {

    private Application application;

    public QATester(Application application) throws AWTException {
        super();
        this.application = application;
    }

    public void click(Node node) throws AWTException, IOException {
        java.awt.Point originalLocation = java.awt.MouseInfo.getPointerInfo().getLocation();
        Point2D nodeLocation = getGlobalCoordinates(node);
        try {
            mouseMove((int) nodeLocation.getX() + 20, (int) nodeLocation.getY() + 20);
            mousePress(InputEvent.BUTTON1_MASK);
            mouseRelease(InputEvent.BUTTON1_MASK);
            mouseMove((int) originalLocation.getX(), (int) originalLocation.getY());
        } catch (Exception e) {
            saveScreenshot("logs\\errorSnapshot");
            e.printStackTrace();
            org.junit.Assert.fail(e.getMessage());
        }
    }

    public void keyClickN(int key, int count) {
        for (int i = 0; i < count; i++) {
            keyClick(key);
        }
    }

    public void keyClick(int key) {
        keyPress(key);
        keyRelease(key);
    }

    public void moveMouseToNode(Node node, int x, int y) {
        Point2D coords = getGlobalCoordinates(node).add(x, y);
        mouseMove((int) coords.getX(), (int) coords.getY());
    }

    public void saveScreenshot(String fileName) throws AWTException, IOException {
        
        Stage stage = this.application.getStage();
        int x = (int) stage.getX();
        int y = (int) stage.getY();
        int width = (int) stage.getWidth();
        int height = (int) stage.getHeight();
        java.awt.Rectangle area = new java.awt.Rectangle(x, y, width, height);
        BufferedImage image = new Robot().createScreenCapture(area);
        ImageIO.write(image, "png", new File(fileName + ".png"));
    }

    private Point2D getGlobalCoordinates(Node control) {
        return control.localToScreen(control.getLayoutBounds().getMinX(), control.getLayoutBounds().getMinY());
    }
}

//need to add speed
//need to add run script
//need debug mode to capture my input and object coords/states such that we can automate my testing completely
//need to give option to screenshot in order to test orthographic etc. (such as camera swap etc.
//automated testing on vms for all os's and android