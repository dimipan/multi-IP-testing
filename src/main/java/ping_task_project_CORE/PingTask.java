package ping_task_project_CORE;

import java.net.InetAddress;
import java.util.Date;
import java.util.concurrent.Callable;

public class PingTask implements Callable<PingResult> {

    private String ipAddress;

    public PingTask(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public PingResult call() {
        InetAddress inet = null;
        long timeToRespond = 0;
        try {
            inet = InetAddress.getByName(ipAddress);
            Date start = new Date();
            int resultCode = inet.isReachable(5000) ? 1 : 0;  // 1->active , 0->inactive
            Date stop = new Date();
            timeToRespond = (stop.getTime() - start.getTime());
            //System.out.println("Response time (RTT): " + timeToRespond + " ms");
            return new PingResult(ipAddress, resultCode, timeToRespond);
        } catch (Exception e) {
            e.printStackTrace();
            return new PingResult(ipAddress, 0, timeToRespond);
        }
    }
}