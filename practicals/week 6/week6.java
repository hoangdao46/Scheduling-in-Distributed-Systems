import java.io.*;
import java.net.*;
import java.util.*;

public class week6 {

	public static void main(String[] args) throws Exception{
		try {
			Socket sock = new Socket("localhost", 50000);
			System.out.println("# java Client updated 24-March, 2021 @ MQ");
			System.out.println("Connected to Server");
			String rcvd = "";
			String back = "";
			String[] job;
			String[] current;

			send("HELO", sock);
			rcvd = receive(sock);
			if(rcvd.equals("OK")){
				send("AUTH "+System.getProperty("user.name"), sock);
			}

			SystemInfo si = new SystemInfo();
			int fileread = si.readXML("ds-system.xml");

			rcvd = receive(sock);
			if(fileread == 0){
				if(rcvd.equals("OK")){
					send("REDY", sock);
				} else {
					send("QUIT", sock);
					sock.close();
				}
			} else {
				send("QUIT", sock);
				sock.close();
			}

			//job schedule
			rcvd = receive(sock);
			current = rcvd.split(" ");

			while(!rcvd.contains("NONE")){
				if(rcvd.contains("JOBN")){
					job = rcvd.split(" ");
					send("GETS Avail " + job[4] + " " + job[5] + " " + job[6], sock);
					current = job;
				} else if(rcvd.contains(".") && rcvd.length() == 1){
					send("SCHD " + current[2] + " " + largestServer(si.servers) + " 0", sock);
				} else if(rcvd.contains("OK") || rcvd.contains("JCPL")){
					send("REDY", sock);
				} else if(rcvd.contains("ERR")){
					send("QUIT", sock);
					sock.close();
				} else {
					send("OK", sock);
				}
				rcvd = receive(sock);
			}

			//quit protocol
			if(rcvd.contains("NONE")){
				send("QUIT", sock);
				sock.close();
			}

			sock.close();
			System.out.println("End Connection");
		} catch(Exception e){
			System.out.println(e);
		}
	}

	private static void send(String str, Socket soc) throws IOException{
		PrintWriter dataOut = new PrintWriter(soc.getOutputStream(), true);
		dataOut.print(str);
		dataOut.flush();
	}

	private static String receive(Socket soc) throws IOException{
		String temp = "";
		BufferedReader dataIn = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		StringBuffer buff = new StringBuffer();
		while (dataIn.ready()){
			char[] c = new char[] {1};
			dataIn.read(c);
			buff.append(c);
		}
		temp = buff.toString();
		System.out.println(temp);
		return temp;
	}

	private static String largestServer(List<ServerInfo> servers){
		String bigServer = "";
	        Integer largest = 0;
		for (ServerInfo server : servers) {
	        	if(server.serverCoreCount > largest) {
                		largest = server.serverCoreCount;
                		bigServer = server.serverType;
            		}
        	}
        	return bigServer;
    	}

}
