package CowsAndBulls;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Network {
    
    
    
    private static String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "Не удалось определить IP-адрес";
        }
    }
}
