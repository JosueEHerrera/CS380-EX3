/*  Authors:	Josue Herrera		
/		Kean Jafari
*/

import java.util.*;
import java.net.*;
import java.io.*;

public class Ex3Client{


	public static void main(String[] args) throws Exception{
		Socket socket = new Socket("codebank.xyz", 38103);
		String hexString = "";

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
		
		//Prints # of bytes received
		System.out.print("Data Received: " + total + " bytes.");
		
		//Receives the Bytes 
		for (int i = 0 ;i < total ;i++ ) {
			int stream = is.read();
			hexString += toHex(stream);
			byteArray[i] = (byte)stream;
		}
		
		//Prints Hex String 
		printString(hexString);
		
		//Calculates checksum for array of bytes
		short cksum = checksum(byteArray);

		//Print Checksum
		System.out.println("Checksum Calculated: 0x" + toHex(cksum & 0xFFFF));


		//Relay message to server
		byte[] asArray = new byte[2];
		asArray[0] = (byte)((cksum & 0xFF00) >>> 8);
		asArray[1] = (byte)((cksum & 0x00FF));
		out.write(asArray);

		//Checks response from server
		int serverResponse = is.read();
		if (serverResponse == 1) 
			System.out.println("Response Good.");
		else
			System.out.println("Response Bad.");

		//Closes Output stream and socket to server
		out.close();
		socket.close();
		System.out.println("Disconnected from Server.");

		}catch (Exception e){e.printStackTrace();}
	}
	
	//Converts stream to uppercase hex
	public static String toHex(int stream) {
		return Integer.toHexString(stream).toUpperCase();
	}

	// short to int, then return string
	public static String toHex(short stream) {
		return Integer.toHexString(stream).toUpperCase();
	}
	
	//Prints formatted hex string (max line length = 20)
	public static void printString(String hexString) {
		System.out.println("\t");
		for (int i = 0; i < hexString.length(); i++) {
			if (i % 20 == 0) 
				System.out.print("\n\t");
			System.out.print(hexString.charAt(i));
		}
		System.out.println("\n");
	}

	public static short checksum(byte[] b) {
		int length = b.length;
		int i = 0;
	    long total = 0;
	   	long sum = 0;

	    // add to sum and bit shift
	    while (length > 1) {
	    	sum = sum + ((b[i] << 8 & 0xFF00) | ((b[i+1]) & 0x00FF));
	    	i = i + 2;
	    	length = length - 2;

	    	// splits byte into 2 words, adds them.
	    	if ((sum & 0xFFFF0000) > 0) {
	    		sum = sum & 0xFFFF;
	    		sum++;
	    	}
	    }

	    // calculates and adds overflowed bits, if any
		if (length > 0) {
    		sum += b[i] << 8 & 0xFF00;
    		if ((sum & 0xFFFF0000) > 0) {
    			sum = sum & 0xFFFF;
    			sum++;
    		}
    	}
	    total = (~((sum & 0xFFFF)+(sum >> 16))) & 0xFFFF;
	    return (short)total;
	}	
}
