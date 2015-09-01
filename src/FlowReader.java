import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FlowReader implements Runnable{
	private WebSocketFlowServer socket;
	private GeoLocate geoLocate;
	private String iface;
	public FlowReader(WebSocketFlowServer socket, String iface) {
		this.socket = socket;
		geoLocate = new GeoLocate();
		this.iface = iface;
	}

	@Override
	public void run() {
		try {
			Process process = Runtime.getRuntime().exec("sudo tshark -i " + iface + " -T fields -e frame.time -e ip.src -e ip.dst -e col.Protocol -e tcp.srcport -e tcp.dstport -e udp.srcport -e udp.dstport");
			BufferedReader stream = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String data;
			while ((data = stream.readLine()) != null) {
			    Flow flow = parseFlow(data);
			    if(flow != null){
				    socket.sendMessage(flow.toString());
				    try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Flow parseFlow(String data) throws IOException {
		Flow flow = new Flow();
		Scanner in = new Scanner(data);
		try {
			flow.setDate(in.next() + " " + in.next() + " " + in.next());
			flow.setTime(in.next());
			flow.setSrcip(in.next());
			flow.setDstip(in.next());
			flow.setProtocol(in.next());
			try {
				flow.setSrcport(in.nextInt());
				flow.setDstport(in.nextInt());
			} catch (NoSuchElementException e) {
				flow.setDstport(flow.getSrcport());
				flow.setSrcport(0);
			}			
			
			String currentIP = "163.118.78.61";
			
			Location sloc;
			Location dloc;


			if(flow.getSrcip().contains("10.10")){
				flow.setSrcip(currentIP);
			}else if(!flow.getSrcip().contains(".")){
				flow.setSrcip("0.0.0.0");
			}
			
			if(flow.getDstip().contains("10.10")){
				flow.setDstip(currentIP);
			}else if(!flow.getDstip().contains(".")){
				flow.setDstip("0.0.0.0");
			}

			sloc = geoLocate.getLocation(flow.getSrcip());
			dloc = geoLocate.getLocation(flow.getDstip());
			System.out.println("datatime: "+ flow.getTime() +", src: " + flow.getSrcip() + ", dst: " + flow.getDstip());

			flow.setSrcip(sloc.getIp());
			flow.setSlat(sloc.getLatitude());
			flow.setSlon(sloc.getLongitude());
			flow.setScountrycode(sloc.getCountry_code());
			flow.setScountryname(sloc.getCountry_name());
			flow.setScity(sloc.getCity());
			flow.setDstip(dloc.getIp());
			flow.setDlat(dloc.getLatitude());
			flow.setDlon(dloc.getLongitude());
			flow.setDcountrycode(dloc.getCountry_code());
			flow.setDcountryname(dloc.getCountry_name());
			flow.setDcity(dloc.getCity());

			//ignore internal traffic
			if(flow.getSrcip().equals(flow.getDstip())){
				in.close();
				return null;
			}
		} catch (NoSuchElementException e) {
			return null;
		}
		in.close();

		return flow;
	}
}
