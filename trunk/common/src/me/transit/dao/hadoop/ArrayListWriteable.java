package me.transit.dao.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Writable;

/**
 * Extend ArrayList so that it is writable by Hadoop
 * @author meverline
 *
 * @param <E>
 */
public class ArrayListWriteable<E extends Writable> extends ArrayList<E> implements Writable {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Override
	public void readFields(DataInput in) throws IOException {
		
		ClassLoader loader = this.getClass().getClassLoader();
		
		int num = in.readInt();
		for ( int count = 0; count < num; count++ ) {
			String className = HadoopUtils.readString(in);
			
			try {
				Writable obj = (Writable) loader.loadClass(className).newInstance();
				obj.readFields(in);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * 
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		
		out.writeInt(this.size());
		for ( E item : this) {
			HadoopUtils.writeString(item.getClass().getCanonicalName(), out);
			item.write(out);
		}
		
	}

}
