import java.util.*;
import java.net.*;
import java.io.*;

public class Ex3Client{


	public static void main(String[] args) throws Exception{
		Socket socket = new Socket("codebank.xyz", 38103);

		try{
		//Read in Stream
		InputStream is = socket.getInputStream();
		//Write to Stream
		OutputStream out = socket.getOutputStream();

		//Read in the first byte sent
		int total = is.read();
		System.out.println("Reading " + total + " bytes.");
		
		//Byte array 
		byte[] byteArray = new byte[total];
		System.out.println("Data Received: ");
		
		//Receives the Bytes 
		for (int i = 0 ;i < total ;i++ ) {
			int stream = is.read();
			
			if (i % 20 == 0)
				System.out.print("\n\t");
			//Print out the Bytes 
			System.out.printf("%X", stream);
			//Fill in the ByteArray
			byteArray[i] = (byte)stream;
		}
		System.out.println();
		short cksum = checksum(byteArray);


		}catch (Exception e){}
	}


	public static short checksum(byte[] b){
		int length = b.length;
		int i = 0;
	    long total = 0;
	   	long sum = 0;
	    while (length > 0) {
	        sum += (b[i++]&0xff) << 8;
	        if ((--length)==0) break;
	        sum += (b[i++]&0xff);
	        --length;
	    }
	    total = (~((sum & 0xFFFF)+(sum >> 16)))&0xFFFF;
	    return (short)total;
}
		
}