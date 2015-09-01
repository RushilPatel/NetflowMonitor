import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.*;

public class WebSocketFlowServer extends WebSocketServer
{
    private Set<WebSocket> conns;
    private Thread reader;
    private String iface;
    public WebSocketFlowServer(int port, String iface){
        super(new InetSocketAddress(port));
        conns = new HashSet<>();
        this.iface = iface;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake)
    {
        conns.add(conn);
        if(conns.size() == 1){
        	reader = new Thread(new FlowReader(this, iface));
            reader.start();
        }
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote)
    {
        conns.remove(conn);
        if(conns.size() <= 0){
        	reader.stop();
        }
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message)
    {
        System.out.println("Received: " + message);
    }

    public void sendMessage( String message)
    {
        if ( conns != null & ! conns.isEmpty())
        {
            // make an array to prevent concurrent has
            WebSocket[] socks = conns.toArray(new WebSocket[conns.size()]);
            for (WebSocket sock : socks)
            {
                sock.send(message);
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex)
    {
    	System.out.println("Error Occurred");
    	ex.printStackTrace();
        onClose(conn, 0, "Error", true);
    }
}
