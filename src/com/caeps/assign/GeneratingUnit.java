package com.caeps.assign;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GeneratingUnit extends ConductingEquipment{

	public double maxP;
	public double minP;
	public EquipmentContainer memberOfEquipmentContainer;

	public GeneratingUnit(String rdfId, String n, double maxPow, double minPow, EquipmentContainer memberOfEquipmentContainer){
		super(rdfId,n);
		maxP = maxPow;
		minP = minPow;
		this.memberOfEquipmentContainer = memberOfEquipmentContainer;
	}
	
	static ArrayList<GeneratingUnit> getGeneratingUnit(Document doc, Connection conn, ArrayList<EquipmentContainer> equipmentcontainers){
		ArrayList<GeneratingUnit> generatingUnits=new ArrayList<GeneratingUnit>();
		String query = null;
		PreparedStatement preparedStmt;
		try {
			preparedStmt = conn.prepareStatement(query);
			preparedStmt.execute("DELETE FROM GeneratingUnit");
			NodeList subList = doc.getElementsByTagName("cim:ThermalGeneratingUnit");
			for (int i = 0; i < subList.getLength(); i++) {
				Node nd = subList.item(i);
				String refId = GetParam.getParam(nd, "rdf:ID");
				String name = GetParam.getParam(nd, "cim:IdentifiedObject.name");
				String memOfEquipmentContainer = GetParam.getParam(nd,"cim:Equipment.MemberOf_EquipmentContainer").substring(1);
				Double maxOperatingP = Double.parseDouble(GetParam.getParam(nd,"cim:GeneratingUnit.maxOperatingP"));
				Double minOperatingP = Double.parseDouble(GetParam.getParam(nd,"cim:GeneratingUnit.minOperatingP"));
				query = "insert into ThermalGenUnits values (?,?,?,?,?)";
				preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, refId);
				preparedStmt.setString(2, name);
				preparedStmt.setDouble(3, maxOperatingP);
				preparedStmt.setDouble(4, minOperatingP);
				preparedStmt.setString(5, memOfEquipmentContainer);
				preparedStmt.execute();
				EquipmentContainer subst = EquipmentContainer.searchEquipmentContainer(equipmentcontainers, memOfEquipmentContainer);
				// System.out.println(baseVoltage.localName+subst.localName);
				GeneratingUnit generatingUnitObj = new GeneratingUnit(refId,name, maxOperatingP, minOperatingP, subst);
				generatingUnits.add(generatingUnitObj);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return generatingUnits;
	}
	
	static GeneratingUnit searchGeneratingUnit(ArrayList<GeneratingUnit> ab, String rdfId) {
		GeneratingUnit objectFound = null;
		for (GeneratingUnit objIt : ab) {
			if (objIt.getRdfID().equals(rdfId)) {
				objectFound = objIt;
				break;
			}
		}
		return objectFound;
	}
}
