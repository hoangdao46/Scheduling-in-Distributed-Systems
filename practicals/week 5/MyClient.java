import java.io.*;
import java.net.*;
public class MyClient {

	public static void main(String[] args) throws Exception{
		try {
			Socket sock = new Socket("localhost", 50000);
			System.out.println("# java Client updated 24-March, 2021 @ MQ");
			System.out.println("Connected to Server");
			String rcvd = "";
			String[] current = new String[1];

			send("HELO", sock);
			rcvd = receive(sock);
			if(rcvd.equals("OK")){
				send("AUTH "+System.getProperty("user.name"), sock);
			}
			rcvd = "";
			rcvd = receive(sock);
			if(rcvd.equals("OK")){
				send("REDY", sock);
			}
			//job schedule
			rcvd = receive(sock);
			System.out.println(rcvd);
//			while(!rcvd.contains("NONE")){
//				if(rcvd.contains("JOBN")){
//					String[] job = rcvd.split(" ");
//			send("GETS Avail", sock);
//					send("SCHD " + job[2] + " super-silk 0", sock);
//					current = job;
//				} else if(rcvd.contains(".")){
//					send("SCHD " + job[2] + " super-silk 0", sock);
//				} else if(rcvd.contains("OK")){
//					send("REDY", sock);
//				} else {
//					send("REDY", sock);
//				}
//				rcvd = receive(sock);
//			}

			//quit protocol
			if(rcvd.contains("JOBN")){
				send("QUIT", sock);
			}

			sock.close();
			System.out.println("End Connection");
		} catch(Exception e){
			System.out.println(e);
		}
	}

	private static void send(String str, Socket soc) throws IOException{
		BufferedWriter dataOut = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
		dataOut.write(str);
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
		return temp;
	}

}
