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

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

import me.crime.dao.AddressDao;
import me.crime.dao.CrimeDao;
import me.crime.dao.URCCatagoriesDAO;
import me.factory.DaoBeanFactory;
import me.math.Vertex;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("crime")
public class Crime implements Serializable, XmlReadable {

	public static final long serialVersionUID = 1;

	@XStreamAlias("id")
	private long   id_ = 0;

	@XStreamAlias("urccategory")
	private URCCatagories urccrime_ = null;
	
	@XStreamAlias("category")
	private String   catagory_ = null;
	
	@XStreamAlias("name")
	private String   bussiness_ = "";
	
	@XStreamAlias("startdate")
	private Calendar startDate_ = null;
	
	@XStreamAlias("description")
	private String   description_ = "";
	
	@XStreamAlias("address")
	private Address  address_ = null;
	
	@XStreamAlias("file")
	private String   file_ = "";
	
	@XStreamAlias("county")
	private String   county_ = "";
	
	@XStreamAlias("crimenumber")
	private String      crimeNumber_ = "";
	
	@XStreamAlias("rank")
	private double	rank_ = 0.0;
	
	@XStreamAlias("time")
	private double time_ = 0.0;
	
	protected static Log log_ = LogFactory.getLog(Crime.class);

	public Crime()
	{
		init("");
	}

	public Crime(String cc)
	{
        init(cc);
	}

	private void init(String cc)
	{
	      StringBuffer buff = new StringBuffer();

	        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

	        buff.append( String.format("%02d", now.get(Calendar.MONTH)+1) );
		    buff.append( String.format("%02d", now.get(Calendar.DAY_OF_MONTH)) );
		    buff.append( now.get(Calendar.YEAR) + ".");
		    buff.append( String.format("%02d", now.get(Calendar.HOUR_OF_DAY)) );
		    buff.append( String.format("%02d", now.get(Calendar.MINUTE)) );
		    buff.append( String.format("%02d", now.get(Calendar.SECOND)) + "." );
		    buff.append( String.format("%03d",now.get(Calendar.MILLISECOND)) + "." + cc);

		    crimeNumber_ = buff.toString();
	}

	public long getId() {
		return id_;
	}

	public String getCrimeNumber()
	{
		return crimeNumber_;
	}

	public void setCrimeNumber( String caseNumber)
	{
		crimeNumber_ = caseNumber;
	}

	protected void setId( long ndx) {
		id_ = ndx;
	}

	public URCCatagories getCodes() {
		return urccrime_;
	}

	public void setCodes(URCCatagories list) {
		 urccrime_ = list;
	}

	public Calendar getStartDate() {
		return startDate_;
	}

	public String getDate() {
		if  ( startDate_ != null ) {
			return startDate_.getTime().toString();
		}
		return "";
	}

	public String getDateOnly() {
		String [] month = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

		if  ( startDate_ != null ) {
			StringBuffer buf = new StringBuffer();

			buf.append(month[startDate_.get(Calendar.MONTH)] + " ");
			buf.append(startDate_.get(Calendar.DAY_OF_MONTH) + " ");
			buf.append(startDate_.get(Calendar.YEAR));
			return buf.toString();
		}
		return "";
	}

	public void setStartDate(Calendar startDate_) {
		this.startDate_ = startDate_;
	}


	public String getDescription() {
		return description_;
	}

	public void setDescription(String description_) {
		this.description_ = scrub(description_);
	}

	private String scrub( String str)
	{
		StringBuffer rtn = new StringBuffer();

		for ( int ndx = 0; ndx < str.length(); ndx++ ) {
			switch ( str.charAt(ndx) ) {
			case '"':
				rtn.append("");
				break;
			case '\'':
				rtn.append("");
				break;
			case '`':
				rtn.append("");
				break;
			case ',':
				rtn.append("");
				break;
			case '�':
				rtn.append("");
		    default:
		    	if (Character.isSpaceChar( str.charAt(ndx))) {
		    		rtn.append(' ');
		    	} else {
		    	   rtn.append(str.charAt(ndx));
		    	}
			}
		}

		return rtn.toString();
	}

	public Address getAddress() {
		return address_;
	}

	public void setAddress(Address location_) {
		this.address_ = location_;
	}

	public String getBussiness() {
		return bussiness_;
	}

    public void setBussiness(String name_) {
        String bussiness = name_;
        if  ( name_.length() > 254 ) {
            bussiness = name_.substring(0, 254);
        }
        this.bussiness_ = bussiness;
    }

    public String getFile() {
		return file_;
	}

	public void setFile(String file_) {
		this.file_ = file_;
	}

	public String getCounty()
	{
		return county_;
	}

	public void setCounty(String value)
	{
		county_ = value;
	}

	/**
	 * @return the catagory_
	 */
	public String getCatagory() {
		return catagory_;
	}

	/**
	 * @param catagory_ the catagory_ to set
	 */
	public void setCatagory(String catagory_) {
		this.catagory_ = catagory_;
	}

	/**
	 * 
	 * @return
	 */
	public double getTime() {
		return time_;
	}

	/**
	 * 
	 * @param time
	 */
	public void setTime(double time) {
		time_ = time;
	}

	/**
	 * @throws SQLException 
	 * 
	 */
	public void handleObject(Object obj) throws SQLException {

		if ( obj instanceof Address ) {
			Address addr = Address.class.cast(obj);
			Address locatoin;
			
			AddressDao dao = AddressDao.class.cast(DaoBeanFactory.create().getDaoBean(AddressDao.class));
			
			locatoin = dao.loadAddress(addr.getLocation());
			if (locatoin == null) {
				this.setAddress(addr);
			} else {
				this.setAddress(locatoin);
			}

		} else if ( obj instanceof URCCatagories ) {
			
			URCCatagoriesDAO dao = 
					URCCatagoriesDAO.class.cast(DaoBeanFactory.create().getDaoBean(URCCatagoriesDAO.class));
			
			URCCatagories cat = URCCatagories.class.cast(obj);
			URCCatagories dbcat = dao.findURCbyCatagory(cat.getCatagorie());
			if ( dbcat == null ) {
				dao.save(cat);
				dbcat = dao.findURCbyCatagory(cat.getCatagorie());
			}
			setCodes(dbcat);

		} 
	}
	
	/**
	 * 
	 * @param aState
	 * @return
	 */
	private boolean isValidState(String aState)
	{
		String [] state = { "va", "md", "dc", "district of columbia" };
		
		for ( String s : state) {
			if ( aState.toLowerCase().compareTo(s) == 0) { return true; }
		}
		
		if  ( this.getCounty().toLowerCase().compareTo("dc") == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 */
	public void save()
	{
		CrimeDao dao = CrimeDao.class.cast(DaoBeanFactory.create().getDaoBean(CrimeDao.class));
		
		try {
			
			if ( this.getStartDate() == null && this.getCounty().compareTo("Vienna") == 0) {
				String name = this.getFile().substring(0, this.getFile().indexOf('.'));

				String [] date = name.split("-");
				
				Calendar cal = Calendar.getInstance();
				
				cal.set(Calendar.MONTH, Integer.parseInt(date[0]));
				cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[1]));
				cal.set(Calendar.YEAR, Integer.parseInt(date[2]));
				cal.set(Calendar.HOUR, 12);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.set(Calendar.AM_PM, 0);

				this.setStartDate(cal);
			}
			else if ( this.getStartDate() == null )
			{
				this.setStartDate(Calendar.getInstance());
			}
			else if ( this.getStartDate().get(Calendar.YEAR) < 1999) 
			{
				this.getStartDate().set(Calendar.YEAR, 1999);
			}
			else if ( this.getStartDate().get(Calendar.YEAR) > 2009) 
			{
				this.getStartDate().set(Calendar.YEAR, 2009);
			}
			
			if ( isValidState( this.getAddress().getState() ) ) {
				
				double dts = this.getStartDate().getTimeInMillis() / 86400000.0; //divide milliseconds per day to get days.fraction_of_day
				double dayOrd = Math.floor(dts); //truncate to get whole days
				double timeOrd = (dts - dayOrd) * 24; // track time as hour.fraction_of_hour
				
				this.setTime( timeOrd );
				
				AddressDao locDao = AddressDao.class.cast(DaoBeanFactory.create().getDaoBean(AddressDao.class));
				
				Address location = locDao.loadAddress( this.getAddress().getLocation() );
			    if ( location != null ) {
					 this.setAddress(location);
			    } else {
			    	 locDao.save(this.getAddress());
				}
				
				dao.save(this);
			}
			
		} catch (SQLException e) {

			log_.warn("Unable to save crime: " + this.getCrimeNumber() + "removing arrested");
			try {
				dao.save(this);
			} catch (SQLException e1) {
				log_.error("Unable to save crime: " + this.getCrimeNumber(), e);
			}

		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Vertex toTemporalPoint()
	{	
		return new Vertex( getTime(), getStartDate().get(Calendar.WEEK_OF_YEAR));
	}

	/**
	 * 
	 * @return
	 */
	public String toDetailedString()
	{
		return getDate() + "\n" + getCodes().toString() + "\nBusiness:\n    " + getBussiness() + "\nAddress:\n    " + getAddress() + "\n\n" + getDescription();
	}

	/**
	 * 
	 * @return
	 */
	public double getRank() {
		return rank_;
	}

	/**
	 * 
	 * @param rank_
	 */
	public void setRank(double rank_) {
		this.rank_ = rank_;
	}


}
