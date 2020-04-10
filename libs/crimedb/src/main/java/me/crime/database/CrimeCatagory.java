//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright 2009 by Russ Brasser, Mark Everline and Eric Franklin
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package me.crime.database;

public class CrimeCatagory {

	public static final long serialVersionUID = 1;
	
	private long id_ = 0;
	private String crime_ = null;
	
	
	public CrimeCatagory() 
	{
	}
	
	public CrimeCatagory(String s)
	{
		setCrime(s);
	}
	/**
	 * @return the id_
	 */
	public long getId() {
		return id_;
	}
	/**
	 * @param id_ the id_ to set
	 */
	public void setId(long id_) {
		this.id_ = id_;
	}
	/**
	 * @return the crime_
	 */
	public String getCrime() {
		return crime_;
	}
	/**
	 * @param crime_ the crime_ to set
	 */
	public void setCrime(String crime_) {
		this.crime_ = crime_;
	}
	
	private String fixXml(String data) {
		
		StringBuffer rtn = new StringBuffer();
		
		
		for ( int ndx = 0; ndx < data.length(); ndx++ ) {
			switch ( data.charAt(ndx) ) {
			case '&':
				rtn.append("&amp;");
				break;
			case '<':
				rtn.append("&lt;");
				break;
			case '>':
				rtn.append("&gt;");
				break;
			case '"':
				rtn.append("&quot;");
				break;
			case '\'':
				rtn.append("&apos;");
				break;
		    default:
		    	rtn.append(data.charAt(ndx));
			}
		}
		
		return rtn.toString();
	}
	
	public String xml(String indent)
	{
	   return indent + "  <crime id='" + getId() + "' >" + fixXml(getCrime()) + "</crime>";
	}

	
	
}
