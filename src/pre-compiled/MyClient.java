import java.io.*;
import java.net.*;
public class MyClient {
	public static void main(String[] args) throws Exception{
		System.out.println("Attempt connection to server");
		Socket s = new Socket("localhost", 50000);
		System.out.println("Connected to Server");
		DataInputStream dataIn = new DataInputStream(s.getInputStream());
		DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());

		String str = "", str2 = "";

		dataOut.writeUTF("HELO");
		str = dataIn.readUTF();
		System.out.println("Server: " + str);
		dataOut.flush();

		dataOut.writeUTF("BYE");
		str = dataIn.readUTF();
		System.out.println("Server: " + str);
		dataOut.flush();

		dataIn.close();
		dataOut.close();
		s.close();
		System.out.println("End Connection");
	}
}
