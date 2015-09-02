package com.netflowmonitor.reader;

import com.netflowmonitor.WebSocketFlowServer;
import com.netflowmonitor.dto.Flow;
import com.netflowmonitor.dto.Location;
import com.netflowmonitor.util.GeoLocate;

import java.io.*;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class NetFlowFileReader implements Runnable{
    private WebSocketFlowServer socket;
    private File file;
    public NetFlowFileReader(com.netflowmonitor.WebSocketFlowServer socket, File file) {
        this.socket = socket;
        this.file = file;
    }

    @Override
    public void run() {
        try {
            FileReader fileReader = new FileReader(this.file);
            BufferedReader stream = new BufferedReader(fileReader);
            String data;
            long initTime = System.currentTimeMillis();
            LinkedList<Flow> flows = new LinkedList<>();
            stream.readLine();
            while ((data = stream.readLine()) != null) {
                Flow flow = parseFlow(data);
                if(flow != null){
                    flows.add(flow);
                }
                if(System.currentTimeMillis() - initTime > 2000){
                    if(flows.size() > 0) {
                        initTime = System.currentTimeMillis();
                        StringBuilder sb = new StringBuilder();
                        sb.append("[");
                        for (Flow f : flows) {
                            sb.append(f.toJson()+",");
                        }
                        String message = sb.substring(0, sb.length() - 1) + "]";
                        System.out.println("Sending Message: " + flows.size());
                        flows.clear();
                        socket.sendMessage(message);
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
            flow.setDate(in.next());
            flow.setTime(in.next());
            in.next();
            flow.setProtocol(in.next());

            String [] src = in.next().split(":");
            flow.setSrcip(src[0]);
            flow.setSrcport(Integer.parseInt(src[1]));
            in.next();

            String [] dst = in.next().split(":");
            flow.setDstip(dst[0]);
            flow.setDstport(Integer.parseInt(src[1]));

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

            sloc = GeoLocate.getLocation(flow.getSrcip());
            dloc = GeoLocate.getLocation(flow.getDstip());
//			System.out.println("datatime: " + flow.getTime() + ", src: " + flow.getSrcip() + ", dst: " + flow.getDstip());

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
