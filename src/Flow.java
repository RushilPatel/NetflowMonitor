public class Flow {

	private String date;
	private String time;
	private String protocol;
	private String srcip;
	private String dstip;
	private int srcport;
	private int dstport;
	private float slat;
	private float dlat;
	private float slon;
	private float dlon;
	private String scountryname;
	private String dcountryname;
	private String scountrycode;
	private String dcountrycode;
	private String info;
	private String scity;
	private String dcity;
	

	public String getSrcip() {
		return srcip;
	}

	public void setSrcip(String srcip) {
		this.srcip = srcip;
	}

	public String getDstip() {
		return dstip;
	}

	public void setDstip(String dstip) {
		this.dstip = dstip;
	}

	public int getSrcport() {
		return srcport;
	}

	public void setSrcport(int srcport) {
		this.srcport = srcport;
	}

	public int getDstport() {
		return dstport;
	}

	public void setDstport(int dstport) {
		this.dstport = dstport;
	}

	public float getSlat() {
		return slat;
	}

	public void setSlat(float slat) {
		this.slat = slat;
	}

	public float getDlat() {
		return dlat;
	}

	public void setDlat(float dlat) {
		this.dlat = dlat;
	}

	public float getSlon() {
		return slon;
	}

	public void setSlon(float slon) {
		this.slon = slon;
	}

	public float getDlon() {
		return dlon;
	}

	public void setDlon(float dlon) {
		this.dlon = dlon;
	}

	public String getScountryname() {
		return scountryname;
	}

	public void setScountryname(String scountryname) {
		this.scountryname = scountryname;
	}

	public String getDcountryname() {
		return dcountryname;
	}

	public void setDcountryname(String dcountryname) {
		this.dcountryname = dcountryname;
	}

	public String getScountrycode() {
		return scountrycode;
	}

	public void setScountrycode(String scountrycode) {
		this.scountrycode = scountrycode;
	}

	public String getDcountrycode() {
		return dcountrycode;
	}

	public void setDcountrycode(String dcountrycode) {
		this.dcountrycode = dcountrycode;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInfo() {
		return this.info;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return this.time;
	}
	
	public String getScity(){
		return this.scity;
	}
	
	public String getDcity(){
		return this.dcity;
	}

	public void setScity(String scity){
		this.scity = scity;
	}
	
	public void setDcity(String dcity){
		this.dcity = dcity;
	}
	
	private byte[] ipToBytes(String ip) {
		byte[] bytes = null;
		String[] parts = ip.trim().split("\\.");
		if (parts != null && parts.length == 4) {
			bytes = new byte[4];
			int b = 0;
			try {
				for (int i = 0; i < bytes.length; i++) {
					if (parts[i].equals("*")) {
						bytes[i] = (byte) 255;
					} else {
						bytes[i] = (byte) Integer.parseInt(parts[i]);
					}
					b++;
				}
			} catch (NumberFormatException xcp) {
				bytes = null;
			}
		}
		return bytes;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"date\" : \"" + this.date + " " + this.time + "\",");
		sb.append("\"protocol\" : \"" + this.getProtocol() + "\",");
		sb.append("\"srcip\" : \"" + this.getSrcip() + "\",");
		sb.append("\"dstip\" : \"" + this.getDstip() + "\",");
		sb.append("\"srcport\" : \"" + this.getSrcport() + "\",");
		sb.append("\"dstport\" : \"" + this.getDstport() + "\",");
		sb.append("\"slat\" : \"" + this.getSlat() + "\",");
		sb.append("\"slon\" : \"" + this.getSlon() + "\",");
		sb.append("\"dlon\" : \"" + this.getDlon() + "\",");
		sb.append("\"dlat\" : \"" + this.getDlat() + "\",");
		sb.append("\"dcountryname\" : \"" + this.getDcountryname() + "\",");
		sb.append("\"dcountrycode\" : \"" + this.getDcountrycode() + "\",");
		sb.append("\"scity\" : \"" + this.getScity() + "\",");
		sb.append("\"scountrycode\" : \"" + this.getScountrycode() + "\",");
		sb.append("\"scountryname\" : \"" + this.getScountryname() + "\",");
		sb.append("\"dcity\" : \"" + this.getDcity() + "\",");

		sb.append("\"info\" : \"" + this.getScountryname() + "\"");
		sb.append("}");
		return sb.toString();
	}
}
