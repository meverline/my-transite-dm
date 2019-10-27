package me.crime.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import me.crime.dao.URCCatagoriesDAO;
import me.crime.database.URCCatagories;

@Component(value="loadURCCatagories")
public class LoadURCCatagories {

	protected static Log log_ = LogFactory.getLog(LoadURCCatagories.class);
	private final URCCatagoriesDAO urcCatagoriesDAO;
	
	/**
	 * 
	 * @param urcCatagoriesDAO
	 */
	public LoadURCCatagories(URCCatagoriesDAO urcCatagoriesDAO) {
		this.urcCatagoriesDAO = urcCatagoriesDAO;
	}

	/**
	 * @return the urcCatagoriesDAO
	 */
	protected URCCatagoriesDAO getURCCatagoriesDAO() {
		return urcCatagoriesDAO;
	}

	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void loadURCTable() throws SQLException, ClassNotFoundException
	{		
		// Read in the URC Codes.
		InputStream s = ClassLoader.getSystemResourceAsStream("me/crime/loader/CrimeData.txt");
		if (s == null) {
			 log_.error("unable to find me/crime/loader/CrimeData.txt");
		} else {

			try {
				BufferedReader bf = new BufferedReader(new InputStreamReader(s));

				while (bf.ready()) {
					String word = bf.readLine().trim().toUpperCase();
					if (!word.startsWith("#")) {
						if (word.length() > 0) {
							String[] info = word.split(",");
							URCCatagories urc = getURCCatagoriesDAO().findURCbyCatagory(info[0]);
							if (urc == null) {

								urc = new URCCatagories();

								int rank = Integer.parseInt(info[1].trim());

								if (rank == 1) {
									urc.setCatagorie(info[0].trim());
									urc.setCrimeGroup(info[3].trim());
									getURCCatagoriesDAO().save(urc);
								}
							}
						}
					}

				}
				bf.close();

			} catch (IOException e) {
				log_.error(e.getLocalizedMessage(), e);
			}

		}

	}

}
