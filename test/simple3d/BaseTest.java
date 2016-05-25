/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple3d;

import java.awt.AWTException;
import static org.junit.Assert.fail;
import org.junit.Before;
import simple3d.utils.StopWatch;

/**
 *
 * @author ad
 */
public abstract class BaseTest {
    protected QATester qa;
    
    @Before
    public void setUp() throws InterruptedException, AWTException {
        RunnableApplication runnableApplication = new RunnableApplication("Simple3D");
        runnableApplication.start();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        while (stopWatch.getElapsedTimeSeconds() < 5) {
            if (runnableApplication.getApplicationInstance() != null) {
                break;
            }
        }

        if (runnableApplication.getApplicationInstance() == null) {
            fail("failed to get instance application");
        } else {
            this.qa = new QATester(runnableApplication.getApplicationInstance());
        }
    }
}
