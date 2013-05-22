package au.gov.nsw.records.digitalarchive.system;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostNameFinder {

	public String getMyCanonicalHostName() {
        String hostname = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostname = addr.getCanonicalHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return hostname;
    }

    public String getMyIPAddress() {
        String ipAddress = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ipAddress = addr.getHostAddress();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    public String getIPByAddress(String address) {
        String ipAddress = null;
        try {
            InetAddress addr = InetAddress.getByName(address.trim());
            ipAddress = addr.getHostAddress();
         
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ipAddress;
    }

    public String getHostNameByAdress(String address) {
        String hostname = null;
        try {
            InetAddress addr = InetAddress.getByName(address.trim());
            hostname = addr.getHostName();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return hostname;
    }

}
