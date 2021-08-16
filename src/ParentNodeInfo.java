
    public class ParentNodeInfo
    {
        public ParentNodeInfo(int ip, int udpport, int tcpport, byte freepri, byte freesec)
        {
            this.ip = ip;
            this.udpport = udpport;
            this.tcpport = tcpport;
            this.freepri = freepri;
            this.freesec = freesec;
        }

        public int ip()
        {
            return ip;
        }

        public int udpport()
        {
            return udpport;
        }

        public int tcpport()
        {
            return tcpport;
        }

        public byte freepri()
        {
            return freepri;
        }

        public byte freesec()
        {
            return freesec;
        }

        private int ip;
        private int udpport;
        private int tcpport;
        private byte freepri;
        private byte freesec;
    }