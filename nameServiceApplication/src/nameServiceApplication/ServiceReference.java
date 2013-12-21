package nameServiceApplication;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: me
 * Date: 12/10/13
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceReference implements Serializable {

    private static final long serialVersionUID = 20101983L;

    private InetAddress inetAddress;
    private int port;


    public ServiceReference(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }


    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getPort() {
        return port;
    }
}
