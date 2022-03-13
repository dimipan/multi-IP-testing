package ping_task_project_CORE;

public class PingResult {
        private String ipAddress;
        private int resultCode;
        private long timeToRespond;

        public PingResult(String ipAddress, int resultCode, long timeToRespond) {
            this.ipAddress = ipAddress;
            this.resultCode = resultCode;
            this.timeToRespond = timeToRespond;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public int getResultCode() {
            return resultCode;
        }

    public long getTimeToRespond() { return timeToRespond; }

    public String toString() {
            return "IpAddress : "+ ipAddress + " -----" + " Result Code : "+ resultCode + " -----" + " RTT : " + timeToRespond + " (ms)";
        }
    }
