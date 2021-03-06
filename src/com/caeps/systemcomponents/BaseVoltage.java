package com.caeps.systemcomponents;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.caeps.gui.PSAnalysisPanel;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseVoltage.
 */
public class BaseVoltage extends IdentifiedObject{

	/** The nominal value. */
	public double nominalValue;
	
	/** The logger. */
	static Logger logger = Logger.getLogger(LoadXMLSQL.class);

	/**
	 * Instantiates a new base voltage.
	 *
	 * @param rdfId the rdf id
	 * @param n the n
	 * @param nomVal the nom val
	 */
	public BaseVoltage(String rdfId, String n, double nomVal){
		super(rdfId, n);
		nominalValue = nomVal;
	}
	
	/**
	 * Gets the list of base voltages in the CIM XML file.
	 *
	 * @param doc the document build from the CIM XML file
	 * @param conn the database connection
	 * @return the base voltages array list of base voltages in the power system
	 */
	static ArrayList<BaseVoltage> getBaseVoltages(Document doc, Connection conn){
		ArrayList<BaseVoltage> baseVoltages = new ArrayList<BaseVoltage>();
		String query = null;
		PreparedStatement preparedStmt = null;
		try {
			NodeList subList = doc.getElementsByTagName("cim:BaseVoltage");
			for (int i = 0; i < subList.getLength(); i++) {
				query = "INSERT INTO BaseVoltage VALUES (?,?,?)";
				Node nd = subList.item(i);
				String refId = GetParam.getParam(nd, "rdf:ID");
				String refName = GetParam.getParam(nd, "cim:IdentifiedObject.name");
				double nominalVoltage = Double.parseDouble(GetParam.getParam(nd,
						"cim:BaseVoltage.nominalVoltage"));
				preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, refId);
				preparedStmt.setString(2, refName);
				preparedStmt.setDouble(3, nominalVoltage);
				preparedStmt.execute();
				BaseVoltage baseVoltageObj = new BaseVoltage(refId, refName, nominalVoltage);
				baseVoltages.add(baseVoltageObj);
				logger.debug("Read the base voltage contents from the XML file");
			}
		} catch (SQLException e) {
			logger.error("SQL Exception Error in loading base voltage details into the database",e);
			PSAnalysisPanel.consoleArea.append("\nSQL Exception Error in loading base voltage details into the database. Check logs for more details");
		}
		return baseVoltages;
	}
	
	/**
	 * Search base voltage.
	 *
	 * @param ab the ab
	 * @param rdfId the rdf id
	 * @return the base voltage
	 */
	static BaseVoltage searchBaseVoltage(ArrayList<BaseVoltage> ab, String rdfId) {
		BaseVoltage objectFound = null;
		for (BaseVoltage objIt : ab) {
			if (objIt.rdfID.equals(rdfId)) {
				objectFound = objIt;
				break;
			}
		}
		return objectFound;
	}
}