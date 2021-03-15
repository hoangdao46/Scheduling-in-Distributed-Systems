import java.net.*;
import java.io.*;
class MyServer{
	public static void main(String args[])throws Exception{
		ServerSocket ss = new ServerSocket(5000);
		System.out.println("Started Server Connection...");
		Socket s = ss.accept();
		System.out.println("Client Accepted");
		DataInputStream dataIn = new DataInputStream(s.getInputStream());
		DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(System.in));

		String str = "";
		str = dataIn.readUTF();
		while(!str.equals("BYE")){
			System.out.println("Client: "+str);
			dataOut.writeUTF("G'day");
			dataOut.flush();
			str = dataIn.readUTF();
		}
		System.out.println("Client: "+str);
		dataOut.writeUTF("BYE");
		dataOut.flush();
		dataIn.close();
		dataOut.close();
		s.close();
		ss.close();
		System.out.println("End Connection");
	}
}
