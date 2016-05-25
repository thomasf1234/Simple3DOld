/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3d;

/**
 *
 * @author ad
 */
public class RunnableApplication implements Runnable {

    private Thread thread;
    private final String threadName;

    RunnableApplication(String name) {
        this.threadName = name;
    }

    @Override
    public void run() {
        Simple3D.startTestMode();
    }

    public void start() {
        if (this.thread == null) {
            this.thread = new Thread(this, this.threadName);
            this.thread.start();
        }
    }

    public Thread getThread() {
        return this.thread;
    }

    public Simple3D getApplicationInstance() {
        return Simple3D.getApplicationInstance();
    }
}
