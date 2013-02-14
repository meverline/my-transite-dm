package me.transit.dao.hadoop;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;

/**
 * 
 * @author meverline
 * 
 */
public class Streambuf {
	private final static int BUFFER_SIZE = 1024;
	private InputStream in;
	private byte[] buffer;
	// the number of bytes of real data in the buffer
	private int egptr = 0;
	// the current position in the buffer
	private int gptr = 0;
	
	/**
	 * Create a line reader that reads from the given stream using the given
	 * buffer-size.
	 * 
	 * @param in
	 * @throws IOException
	 */
	Streambuf(InputStream in) {
		this.in = in;
		this.buffer = new byte[Streambuf.BUFFER_SIZE];
	}

	/**
	 * Create a line reader that reads from the given stream using the
	 * <code>io.file.buffer.size</code> specified in the given
	 * <code>Configuration</code>.
	 * 
	 * @param in
	 *            input stream
	 * @param conf
	 *            configuration
	 * @throws IOException
	 */
	public Streambuf(InputStream in, Configuration conf)
			throws IOException {
		this(in);
	}
	
	/**
	 * 
	 * @return
	 */
	private byte sgetc() throws IOException {
		if ( gptr > egptr ) {
			underflow();
		}
		return buffer[gptr++];
	}
	
	/**
	 * 
	 * @return
	 */
	private int in_avail() throws IOException {
		if ( gptr > egptr ) {
			underflow();
		}
		return egptr - gptr;
	}

	/**
	 * Fill the buffer with more data.
	 * 
	 * @return was there more data?
	 * @throws IOException
	 */
	private boolean underflow() throws IOException {
		gptr = 0;
		egptr = in.read(buffer);
		return egptr > 0;
	}

	/**
	 * Close the underlying stream.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		in.close();
	}

	/**
	 * 
	 * @param strBuffer
	 * @return
	 * @throws IOException
	 */
	public int readLine(StringBuffer strBuffer) throws IOException {
					
		strBuffer.delete(0, strBuffer.length());
		while ( in_avail() > 0 ) {
			
			byte c = sgetc();
			switch ( c ) {
				case '\n':
					return in_avail();
				case '\r':
					return in_avail();
				default:
					strBuffer.append(c);
			}
			
		}
		return in_avail();

	}

}