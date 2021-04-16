import java.io.*;
import java.net.*;
import java.util.*;

public class MyClient {

	public static void main(String[] args) throws Exception{
		try {
			Socket sock = new Socket("localhost", 50000);
			System.out.println("# java Client updated 24-March, 2021 @ MQ");
			System.out.println("Connected to Server");
			String rcvd = "";
			String[] job;
			int counter = 0;

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
					error(sock);
				}
			} else {
				error(sock);
			}

			//job schedule
			rcvd = receive(sock);

			while(!rcvd.contains("NONE")){
				if(rcvd.length() > 0)
					if(rcvd.contains("JOBN")){
						job = rcvd.split(" ");
						send("GETS Avail " + job[4] + " " + job[5] + " " + job[6], sock);
					} else if(rcvd.equals(".")){
						send("SCHD " + counter + " " + largestServer(si.servers) + " 0", sock);
						counter++;
					} else if(rcvd.equals("OK")||rcvd.contains("JCPL")){
						send("REDY", sock);
					} else if(rcvd.contains("ERR")){
						error(sock);
					} else {
						send("OK", sock);
				}
				rcvd = receive(sock);
				//System.out.println(rcvd);
			}

			//quit protocol
			if(rcvd.contains("NONE")) {
                send("QUIT", sock);
                rcvd = receive(sock);
                if(rcvd.contains("QUIT")) {
                    sock.close();
					System.exit(0);
					System.out.println("aha");
                }
            } else {
                error(sock);
				System.out.println("yeah");
            }

		} catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void send(String str, Socket sOut) throws IOException{
		DataOutputStream dataOut = new DataOutputStream(sOut.getOutputStream());
		dataOut.write(str.getBytes());
		dataOut.flush();
	}

	private static String receive(Socket sIn) throws IOException{
		DataInputStream dataIn = new DataInputStream(sIn.getInputStream());
		String temp = "";
		char cha = '\0';
		while(dataIn.available()!=0){
			cha = (char)dataIn.readByte();
			temp += cha;
		}
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

	private static void error(Socket soc) throws IOException {
        send("QUIT", soc);
        soc.close();
		System.exit(1);
    }

}
