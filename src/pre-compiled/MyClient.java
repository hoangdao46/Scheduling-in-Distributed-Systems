import java.io.*;
import java.net.*;
import java.util.*;

public class MyClient {

	public static void main(String[] args) throws Exception{
		try {
			Socket sock = new Socket("localhost", 50000);
			System.out.println("# java Client updated 24-March, 2021 @ MQ");
			System.out.print("# Client-side simulator started with java MyClient");
			String rcvd = "";
			String[] job;
			int counter = 0;
			boolean isNewline = true;
			
			//checks for command line arguments
			for(int i = 0; i < args.length; i++){
				if(args[i].equalsIgnoreCase("-n")) {
					isNewline = true;
				}
				if(!args[i].equals("")){
					if(i <= (args.length-1)){
						System.out.print(" " + args[i]);
					}
				}
			}
			System.out.println("");

			//handshake messages between client and server
			send("HELO", sock, isNewline);
			rcvd = receive(sock, isNewline);
			if(rcvd.equals("OK")){
			send("AUTH "+System.getProperty("user.name"), sock, isNewline); //authenticates username
			} else {
				error(sock, isNewline);
			}

			//read ds-system.xml
			SystemInfo si = new SystemInfo();
			int fileread = si.readXML("ds-system.xml");

			//check if file has been read successfully
			rcvd = receive(sock, isNewline);
			if(fileread == 0){
				if(rcvd.equals("OK")){
					send("REDY", sock, isNewline);
				} else {
					error(sock, isNewline);
				}
			} else {
				error(sock, isNewline);
			}

			//job scheduler
			rcvd = receive(sock, isNewline);
			while(!rcvd.contains("NONE")){
				if(rcvd.length() > 0)
					if(rcvd.contains("JOBN")){
						job = rcvd.split(" ");
						send("GETS Avail " + job[4] + " " + job[5] + " " + job[6], sock, isNewline);
					} else if(rcvd.equals(".")){
						send("SCHD " + counter + " " + largestServer(si.servers) + " 0", sock, isNewline);
						counter++;
					} else if(rcvd.equals("OK")||rcvd.contains("JCPL")){
						send("REDY", sock, isNewline);
					} else if(rcvd.contains("ERR")){
						error(sock, isNewline);
					} else {
						send("OK", sock, isNewline);
				}
				rcvd = receive(sock, isNewline);
			}

			//quit protocol
			if(rcvd.contains("NONE")) {
                send("QUIT", sock, isNewline);
                rcvd = receive(sock, isNewline);
                if(rcvd.contains("QUIT")) {
                    sock.close();
		    System.exit(0);
                }
            } else {
                error(sock, isNewline);
            }

		} catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void send(String str, Socket sOut, boolean newline) throws IOException{
		DataOutputStream dataOut = new DataOutputStream(sOut.getOutputStream());
		if(newline){
			dataOut.write((str+"\n").getBytes());
		} else {
			dataOut.write(str.getBytes());
		}
		dataOut.flush();
	}

	private static String receive(Socket sIn, boolean newline) throws IOException{
		DataInputStream dataIn = new DataInputStream(sIn.getInputStream());
		String temp = "";
		char cha = '\0';
		while(dataIn.available()!=0){
			cha = (char)dataIn.readByte();
			temp += cha;
		}
		if(newline){
			temp = temp.replace("\n", "");
		}
		return temp;
	}

	private static String largestServer(List<ServerInfo> servers){
		String bigServer = ""; //name of biggest server
	        Integer largest = 0;
		for (ServerInfo server : servers) {
	        	if(server.serverCoreCount > largest) { //loop to find largest core count 
                	largest = server.serverCoreCount; 
                	bigServer = server.serverType;
            	}
        	}
        return bigServer;
    }

	private static void error(Socket soc, boolean newline) throws IOException {
        send("QUIT", soc, newline);
        soc.close();
		System.exit(1);
    }

}
