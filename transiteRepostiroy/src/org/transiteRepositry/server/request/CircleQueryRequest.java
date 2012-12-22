package org.transiteRepositry.server.request;

import java.util.List;

import org.transiteRepositry.server.request.utils.Address;
import org.transiteRepositry.server.request.utils.Distance;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


@XStreamAlias("CircleQueryRequest")
public class CircleQueryRequest extends AbstractSpatialQuery {

	private Distance distance = null;
	
	@XStreamImplicit(itemFieldName="Address")
	private List<Address> address = null;

	/**
	 * @return the address
	 */
	public List<Address> getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(List<Address> address) {
		this.address = address;
	}

	/**
	 * @return the distance
	 */
	public Distance getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(Distance distance) {
		this.distance = distance;
	}
	
}
