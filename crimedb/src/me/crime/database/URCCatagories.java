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

import me.crime.dao.URCCatagoriesDAO;
import me.factory.DaoBeanFactory;

// Uniform Crime Reports
public class URCCatagories implements Serializable, XmlReadable {

	public static final long serialVersionUID = 1;
	public static final String CAT_DEFAULT = "ALL OTHER OFFENSES";
	public static final String GROUP_DEFAULT = "B";

	private long id_ = 0;
	private String catagorie_ = URCCatagories.CAT_DEFAULT;
	private String group = URCCatagories.GROUP_DEFAULT;

	public long getId() {
		return id_;
	}

	protected void setId(long id_) {
		this.id_ = id_;
	}

	public String getCatagorie() {
		return catagorie_;
	}

	public void setCatagorie(String catagorie_) {
		this.catagorie_ = catagorie_;
	}

	/**
	 * @return the group
	 */
	public String getCrimeGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setCrimeGroup(String group) {
		this.group = group;
	}

	public String asString()
	{
		return "URC: id=" + getId() + " Catagorie=" +  getCatagorie() + " Group=" + getCrimeGroup();
	}


	/**
	 * 
	 */
	public void handleObject(Object obj) {
		// TODO Auto-generated method stub

	}

	public void save() throws SQLException {
		URCCatagoriesDAO dao = 
				URCCatagoriesDAO.class.cast(DaoBeanFactory.create().getDaoBean(URCCatagoriesDAO.class));
		URCCatagories cat;
		cat = dao.findURCbyCatagory(this.catagorie_);
		if (cat == null ) {
			dao.save(this);
		}
	}

}
