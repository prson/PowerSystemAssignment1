package com.caeps.assign;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class SynchronousMachine extends ConductingEquipment{

	public double ratedS;
	public GeneratingUnit memberOfGen;
	public RegulatingControl regControl;
	public EquipmentContainer memberOfEquipmentContainer;
	public BaseVoltage baseVoltage;
	
	
	public SynchronousMachine (String rdfId,String name, double ratedS, GeneratingUnit memberOfGen, RegulatingControl regControl, EquipmentContainer memberOfEquipmentContainer, BaseVoltage baseVoltage){
		super(rdfId,name);
		this.ratedS=ratedS;
		this.memberOfGen=memberOfGen;
		this.regControl=regControl;
		this.memberOfEquipmentContainer=memberOfEquipmentContainer;
		this.baseVoltage=baseVoltage;	
	}
	
	static ArrayList<SynchronousMachine> getSynchronousMachine(Document doc, Connection conn, ArrayList<EquipmentContainer> equipmentcontainers, ArrayList<BaseVoltage> baseVoltages, ArrayList<GeneratingUnit> generatingUnits, ArrayList<RegulatingControl> regulatingControls){
		ArrayList<SynchronousMachine> synchronousMachines=new ArrayList<SynchronousMachine>();
		String query = null;
		PreparedStatement preparedStmt;
		try {
			query="DELETE FROM synchronousMachine";
			preparedStmt=conn.prepareStatement(query);
			preparedStmt.execute();
			
			NodeList subList = doc.getElementsByTagName("cim:SynchronousMachine");
			for (int i = 0; i < subList.getLength(); i++) {
				Node nd = subList.item(i);
				String refId = GetParam.getParam(nd, "rdf:ID");
				String name = GetParam.getParam(nd, "cim:IdentifiedObject.name");
				String memOfEquipmentContainer = GetParam.getParam(nd,
						"cim:Equipment.MemberOf_EquipmentContainer").substring(
						1);
				Double ratedS = Double.parseDouble(GetParam.getParam(nd,
						"cim:SynchronousMachine.ratedS"));
				String memOfGenUnitId = GetParam.getParam(nd,
						"cim:SynchronousMachine.MemberOf_GeneratingUnit").substring(1);
				String regulatingControlId = GetParam.getParam(nd,
						"cim:RegulatingCondEq.RegulatingControl").substring(1);
				String baseVoltageId = GetParam.getParam(nd,
						"cim:ConductingEquipment.BaseVoltage").substring(1);
				query = "insert into synchronousMachine values (?,?,?,?,?,?,?)";
				preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, refId);
				preparedStmt.setString(2, name);
				preparedStmt.setString(6, memOfEquipmentContainer);
				preparedStmt.setDouble(3, ratedS);
				preparedStmt.setString(4, memOfGenUnitId);
				preparedStmt.setString(5, regulatingControlId);
				preparedStmt.setString(7, baseVoltageId);
				preparedStmt.execute();
				EquipmentContainer voltLevel = EquipmentContainer.searchEquipmentContainer(
						equipmentcontainers, memOfEquipmentContainer);
				BaseVoltage baseVoltage = BaseVoltage.searchBaseVoltage(baseVoltages,
						baseVoltageId);
				GeneratingUnit genUnit = GeneratingUnit.searchGeneratingUnit(
						generatingUnits, memOfGenUnitId);
				RegulatingControl regControl = RegulatingControl.searchRegulatingControl(
						regulatingControls, regulatingControlId);
				// System.out.println(baseVoltage.localName+subst.localName);
				SynchronousMachine ab = new SynchronousMachine(refId, name,
						ratedS, genUnit, regControl, voltLevel,
						baseVoltage);
				synchronousMachines.add(ab);
				LoadXMLSQL.powerSystemResources.add(ab);
				LoadXMLSQL.conductingEquipments.add(ab);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return synchronousMachines;
	}
}
