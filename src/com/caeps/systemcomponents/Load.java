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

/**
 * The Class Load.
 */
public class Load extends ConductingEquipment{
	
	/** The pfixed. */
	public double pfixed;
	
	/** The qfixed. */
	public double qfixed;
	
	/** The member of equipment container. */
	public EquipmentContainer memberOfEquipmentContainer;
	
	/** The base voltage. */
	public BaseVoltage baseVoltage;

	/** The logger. */
	static Logger logger = Logger.getLogger(Load.class);

	
	/**
	 * Instantiates a new load.
	 *
	 * @param rdfId the rdf id
	 * @param name the name
	 * @param pfixed the pfixed
	 * @param qfixed the qfixed
	 * @param memberOfEquipmentContainer the member of equipment container
	 * @param baseVoltage the base voltage
	 */
	public Load(String rdfId,String name, double pfixed, double qfixed, 
			EquipmentContainer memberOfEquipmentContainer, BaseVoltage baseVoltage){
		super(rdfId, name);
		this.pfixed = pfixed;
		this.qfixed = qfixed;
		this.memberOfEquipmentContainer = memberOfEquipmentContainer;
		this.baseVoltage = baseVoltage;
	}
	
	/**
	 * Gets the array list loads contained in an CIM XML document and loads it to the database.
	 *
	 * @param doc the document built from the CIM XML File
	 * @param conn the database connection object
	 * @param equipmentcontainers the list of equipment containers in the power system
	 * @param baseVoltages the list of base voltages in the power system
	 * @return the load an array list of load in the CIM XML File
	 */
	static ArrayList<Load> getLoad(Document doc, Connection conn, 
			ArrayList<EquipmentContainer> equipmentcontainers, 
			ArrayList<BaseVoltage> baseVoltages){
		ArrayList<Load> loads = new ArrayList<Load>();
		String query = null;
		PreparedStatement preparedStmt;
		try {
			query = "DELETE FROM Loads";
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.execute();
			NodeList subList = doc.getElementsByTagName("cim:NonConformLoad");
			for (int i = 0; i < subList.getLength(); i++) {
				Node nd = subList.item(i);
				String refId = GetParam.getParam(nd, "rdf:ID");
				String name = GetParam.getParam(nd, "cim:IdentifiedObject.name");
				double pfixed = Double.parseDouble(GetParam.getParam(nd,
						"cim:EnergyConsumer.pfixed"));
				double qfixed = Double.parseDouble(GetParam.getParam(nd,
						"cim:EnergyConsumer.qfixed"));
				String memOfEquipmentContainer = GetParam.getParam(nd,
						"cim:Equipment.MemberOf_EquipmentContainer").substring(1);
				String baseVoltageId = GetParam.getParam(nd,
						"cim:ConductingEquipment.BaseVoltage").substring(1);
				query = "insert into Loads values (?,?,?,?,?,?)";
				preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, refId);
				preparedStmt.setString(2, name);
				preparedStmt.setDouble(3, pfixed);
				preparedStmt.setDouble(4, qfixed);
				preparedStmt.setString(5, memOfEquipmentContainer);
				preparedStmt.setString(6, baseVoltageId);
				preparedStmt.execute();
				EquipmentContainer equipmentContainer = EquipmentContainer.searchEquipmentContainer(
						equipmentcontainers, memOfEquipmentContainer);
				BaseVoltage baseVoltage = BaseVoltage.searchBaseVoltage(baseVoltages,
						baseVoltageId);
				Load ab = new Load(refId, name, 
						pfixed, qfixed, equipmentContainer, baseVoltage);
				loads.add(ab);
				LoadXMLSQL.powerSystemResources.add(ab);
				LoadXMLSQL.conductingEquipments.add(ab);
			}
		} catch (SQLException e) {
			logger.error("SQL Exception Error in loading loads details into the database",e);
			PSAnalysisPanel.consoleArea.append("\nSQL Exception Error in loading loads details into the database. Check logs for more details");
		}
		return loads;
	}
}
