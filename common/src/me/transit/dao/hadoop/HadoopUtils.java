package me.transit.dao.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class HadoopUtils {
	
	public final static String STOP_ID_LIST = "stopIdList";

	/**
	 * Private function to read a string from DataInput
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String readString(DataInput in) throws IOException {
		StringBuffer buffer = new StringBuffer();
		
		int length = in.readInt();
		for ( int n = 0; n < length; n++ ) {
			buffer.append( in.readChar() );
		}
		return buffer.toString();
	}
	
	/**
	 * Common method to write out String.
	 * @param str
	 * @param out
	 * @throws IOException
	 */
	public static void writeString(String str, DataOutput out) throws IOException {
		out.writeInt(str.length());
		out.writeChars(str);
	}
	
	
	
}
