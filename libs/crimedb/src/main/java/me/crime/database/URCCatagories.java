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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

// Uniform Crime Reports
@Entity
@Table(name = "crm_URCCatagories")
public class URCCatagories extends XmlReadable implements Serializable {

	public static final long serialVersionUID = 1;
	public static final String CAT_DEFAULT = "ALL OTHER OFFENSES";
	public static final String GROUP_DEFAULT = "B";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "URC_ID", nullable = true, unique = true)
	private long id_ = 0;

	@Column(name = "CATAGORIE", nullable = true, unique = true)
	private String catagorie_ = URCCatagories.CAT_DEFAULT;

	@Column(name = "CRIMEGROUP", nullable = true, unique = true)
	private String group = URCCatagories.GROUP_DEFAULT;

	/**
	 * 
	 * @return
	 */
	public long getId() {
		return id_;
	}

	/**
	 * 
	 * @param id_
	 */
	public void setId(long id_) {
		this.id_ = id_;
	}

	/**
	 * 
	 * @return
	 */
	public String getCatagorie() {
		return catagorie_;
	}

	/**
	 * 
	 * @param catagorie_
	 */
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
	 * @param group
	 *            the group to set
	 */
	public void setCrimeGroup(String group) {
		this.group = group;
	}

	/**
	 * 
	 * @return
	 */
	public String asString() {
		return "URC: id=" + getId() + " Catagorie=" + getCatagorie() + " Group=" + getCrimeGroup();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.crime.database.XmlReadable#handleObject(java.lang.Object)
	 */
	public void handleObject(Object obj) {
	}

}
