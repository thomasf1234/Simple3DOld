/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3d;

import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;

/**
 *
 * @author ad
 */
public class SimpleCamera extends PerspectiveCamera {

    public final Rotate xRotate;
    public final Rotate yRotate;
    private Point3D target;

    public SimpleCamera() {
        //centre of the window is origin of coordincate system
        super(true);
        this.xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        this.yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        //default target is at infinity along Z to simulate no target
        this.target = Rotate.Z_AXIS.multiply(Math.pow(10, 10)); 
        this.getTransforms().addAll(this.xRotate, this.yRotate);
    }

    public void setTarget(Point3D target) {
        this.target = target;
    }

    public void setTarget(double x, double y, double z) {
        this.target = new Point3D(x, y, z);
    }

    public void lookAtTarget() {
        Point3D forward = getForward();

        double xRotation = Math.toDegrees(Math.asin(-forward.getY()));
        double yRotation = Math.toDegrees(Math.atan2(forward.getX(), forward.getZ()));

        //must set xRotate axis for the correct rotation to follow
        this.xRotate.setAxis(getRight());
        this.xRotate.setAngle(xRotation);
        this.yRotate.setAngle(yRotation);
    }

    public Point3D getPosition() {
        return new Point3D(getTranslateX(), getTranslateY(), getTranslateZ());
    }

    public void setPosition(Point3D position) {
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
        this.setTranslateZ(position.getZ());
    }

    public void moveForward(double value) {
        setPosition(getPosition().add(getForward().multiply(value)));
        lookAtTarget();
    }

    //returns unit vector towards target
    public Point3D getForward() {
        return this.target.subtract(getPosition()).normalize();
    }

    public Point3D getRight() {
        Point3D forward = getForward();
        return Rotate.Y_AXIS.crossProduct(forward.getX(), 0, forward.getZ()).normalize();
    }

    public Point3D getUp() {
        //return Rotate.Y_AXIS.multiply(-1);
        return getRight().crossProduct(getForward()).normalize();
    }

    public void moveRight(double value) {    
        setPosition(getPosition().add(getRight().multiply(value)));
        this.xRotate.setAxis(getRight());
        lookAtTarget();
    }

    public void moveUp(double value) {
        //upper and lower bounds for up/down
        if ((value > 0 && this.xRotate.getAngle() > -80) || (value < 0 && this.xRotate.getAngle() < 80)) {
            setPosition(getPosition().add(getUp().multiply(value)));
            lookAtTarget();
        }
    }

    public void reset() {
        this.setPosition(Point3D.ZERO);
        this.xRotate.setAxis(Rotate.X_AXIS);
        this.xRotate.setAngle(0);
        this.yRotate.setAngle(0);
    }
}
