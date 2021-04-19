import java.io.*;
import java.net.*;
public class TestInput {
	public static void main(String[] args) throws Exception{
		System.out.println("Attempt connection to server");
		Socket s = new Socket("localhost", 50000);
		System.out.println("Connected to Server");
		DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
		DataInputStream dataIn = new DataInputStream(s.getInputStream());

		dataOut.write("HELO\n".getBytes());
		dataOut.flush();
		
		receive(dataIn);

		dataOut.write("AUTH nerip\n".getBytes());
		dataOut.flush();

		receive(dataIn);

		dataOut.write("REDY\n".getBytes());
		dataOut.flush();

		receive(dataIn);

		dataOut.write("QUIT".getBytes());
		dataOut.flush();

		receive(dataIn);

		dataOut.close();
		dataIn.close();
		s.close();
		System.out.println("End Connection");
	}

	private static String receive(DataInputStream di) throws IOException{
		String temp = "";
		char cha = '\0';		
		while(di.available()!=0){
			cha = (char)di.readByte();
			temp += cha;
		}
		return temp;
	}

}