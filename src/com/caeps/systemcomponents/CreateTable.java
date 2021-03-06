package com.caeps.systemcomponents;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.caeps.gui.PSAnalysisPanel;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateTable.
 */
public class CreateTable {
	
	/** The logger. */
	static Logger logger = Logger.getLogger(CreateTable.class);
	
	/**
	 * Creates the tables.
	 *
	 * @param conn the conn
	 */
	boolean createTables(Connection conn){
		boolean success=false;
		logger.debug("Creating database..."+conn.toString());
		Statement stmt;
		try {
			
			//create Database
			stmt = conn.createStatement();
			String sql = "DROP DATABASE IF EXISTS PowerSystem";
			stmt.executeUpdate(sql);
			sql = "CREATE DATABASE IF NOT EXISTS PowerSystem";
			stmt.executeUpdate(sql);
			logger.debug("Database created successfully");
			
			// Connect to the created database STUDENTS and create tables
			stmt.executeUpdate("use PowerSystem");
			
			//Create table BaseVoltage
			sql = "DROP TABLE IF EXISTS BaseVoltage";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS BaseVoltage" +
					"(rdfId VARCHAR(255) not NULL, " + 
					" name VARCHAR(255), " +
					" nominalValue VARCHAR(255), " + 
					" PRIMARY KEY (rdfId ))";
			stmt.executeUpdate(sql);
			logger.debug("Created table BaseVoltage in given database successfully");
			
			//Create table Substation
			sql = "DROP TABLE IF EXISTS Substation";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Substation" +
					"(rdfId VARCHAR(255) not NULL, " + 
					" name VARCHAR(255), " +
					" regionRdfId VARCHAR(255), " + 
					" PRIMARY KEY (rdfId))";
			stmt.executeUpdate(sql);
			logger.debug("Created table Substation in given database successfully");
			
			//Create table VoltageLevel
			sql = "DROP TABLE IF EXISTS VoltageLevel";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS VoltageLevel" + 
					"(rdfId VARCHAR(255) not NULL, " + 
					" name VARCHAR(255), " + 
					" substationRdfId VARCHAR(255), " + 
					" baseVoltageRdfId VARCHAR(255), " +
					" PRIMARY KEY (rdfId)," +
					" FOREIGN KEY (substationRdfId) REFERENCES Substation(rdfId)," +
					" FOREIGN KEY (baseVoltageRdfId) REFERENCES BaseVoltage(rdfId))";
			stmt.executeUpdate(sql);
			logger.debug("Created table VoltageLevel in given database successfully");
			
			//Create table EquipmentContainer
			sql = "DROP TABLE IF EXISTS EquipmentContainer";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS EquipmentContainer" + 
					"(rdfId VARCHAR(255) not NULL, " + 
					" name VARCHAR(255), " + 
					" PRIMARY KEY (rdfId))";
			stmt.executeUpdate(sql);
			logger.debug("Created table Equipment Container in given database successfully");
			
			//Create table PowerSystemResource
			sql = "DROP TABLE IF EXISTS PowerSystemResource";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS PowerSystemResource" + 
					"(rdfId VARCHAR(255) not NULL, " + 
					" name VARCHAR(255), " + 
					" PRIMARY KEY (rdfId))";
			stmt.executeUpdate(sql);
			logger.debug("Created table PowerSystemResource in given database successfully");
			
			//Create table GeneratingUnit
			sql = "DROP TABLE IF EXISTS GeneratingUnit";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS GeneratingUnit" +
					"(rdfId VARCHAR(255) not NULL, " + 
					" name VARCHAR(255), " +
					" maxP DECIMAL(7,2), " + 
					" minP DECIMAL(7,2), " + 
					" equimentContainerRdfId VARCHAR(255), " +
					" PRIMARY KEY (rdfId), " +
					" FOREIGN KEY (equimentContainerRdfId) REFERENCES EquipmentContainer(rdfId))";
			stmt.executeUpdate(sql);
			logger.debug("Created table GeneratingUnit in given database successfully");
			
			// Create table RegulatingControl
			sql = "DROP TABLE IF EXISTS RegulatingControl";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS RegulatingControl " +
					"(rdfId VARCHAR(255), " +
					" name VARCHAR(255), " +
					" targetValue DECIMAL(7,2), " +
					" PRIMARY KEY (rdfId))";
			stmt.executeUpdate(sql);
			logger.debug("Created table RegulatingControl successfully");
			
			// Create table SynchronousMachine
			sql = "DROP TABLE IF EXISTS SynchronousMachine";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS SynchronousMachine" +
					"(rdfId VARCHAR(255) not NULL, " + 
					" name VARCHAR(255), " +
					" ratedS DECIMAL(7,2), " +
					" generatingUnitRdfId VARCHAR(255), " +
					" regControlRdfId VARCHAR(255), " +
					" equimentContainerRdfId VARCHAR(255), " +	
					" baseVoltageRdfId VARCHAR(255), " + 
					" PRIMARY KEY (rdfId), " +
					" FOREIGN KEY (generatingUnitRdfId) REFERENCES GeneratingUnit(rdfId), " +
					" FOREIGN KEY (regControlRdfId) REFERENCES RegulatingControl(rdfId), " +
					" FOREIGN KEY (equimentContainerRdfId) REFERENCES EquipmentContainer(rdfId), " +
					" FOREIGN KEY (baseVoltageRdfId) REFERENCES BaseVoltage(rdfId))";
			stmt.executeUpdate(sql);
			logger.debug("Created table SynchronousMachine in given database successfully");
			
			// Create table Analog
			sql = "DROP TABLE IF EXISTS Analog";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Analog " +
					"(rdfId VARCHAR(255), " +
					" name VARCHAR(255), " +
					" normalValue DECIMAL(7,2), " +
					" measurementType VARCHAR(255), " +
					" memberRdfId VARCHAR(255), " +
					" PRIMARY KEY (rdfId), " +
					" FOREIGN KEY (memberRdfId) REFERENCES PowerSystemResource(rdfId))";
			stmt.executeUpdate(sql);
			logger.debug("Created table Analog successfully");

			// Create table Disconnector
			sql = "DROP TABLE IF EXISTS Disconnector";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Disconnector " +
					"(rdfId VARCHAR(255), " +
					" name VARCHAR(255), " +
					" state VARCHAR(255), " +
					" equipmentContainerRdfId VARCHAR(255), " +
					" baseVoltageRdfId VARCHAR(255), " +
					" PRIMARY KEY (rdfId), " +
					" FOREIGN KEY (equipmentContainerRdfId) REFERENCES EquipmentContainer(rdfId),"+
					" FOREIGN KEY (baseVoltageRdfID) REFERENCES BaseVoltage(rdfID))";
			stmt.executeUpdate(sql);
			logger.debug("Created table Disconnector successfully");

			// Create table Breaker
			sql = "DROP TABLE IF EXISTS Breaker";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Breaker " +
					"(rdfId VARCHAR(255), " +
					" name VARCHAR(255), " +
					" state VARCHAR(255), " +
					" equimentContainerRdfId VARCHAR(255), " +
					" baseVoltageRdfId VARCHAR(255), " +
					" PRIMARY KEY (rdfId), " +
					" FOREIGN KEY (equimentContainerRdfId) REFERENCES EquipmentContainer(rdfId),"+
					" FOREIGN KEY (baseVoltageRdfID) REFERENCES BaseVoltage(rdfID))";
			stmt.executeUpdate(sql);
			logger.debug("Created table Breaker successfully");
			
			// Create table PowerTransformer
			sql = "DROP TABLE IF EXISTS PowerTransformer";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS PowerTransformer " +
					"(rdfId VARCHAR(255), " +
					" name VARCHAR(255), " +
					" equimentContainerRdfId VARCHAR(255), " +
					" PRIMARY KEY (rdfId), " +
					" FOREIGN KEY (equimentContainerRdfId) REFERENCES EquipmentContainer(rdfId))";
			stmt.executeUpdate(sql);
			logger.debug("Created table PowerTransformer successfully");

			// Create table TransformerWinding
			sql = "DROP TABLE IF EXISTS TransformerWinding";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS TransformerWinding " +
					"(rdfId VARCHAR(255), " +
					" name VARCHAR(255), " +
					" r DECIMAL(7,4), " +
					" x DECIMAL(7,4), " +
					" transformerRdfId VARCHAR(255), " +
					" baseVoltageRdfId VARCHAR(255), " +
					" PRIMARY KEY (rdfId), " +
					" FOREIGN KEY (transformerRdfID) REFERENCES PowerTransformer(rdfID), " +
					" FOREIGN KEY (baseVoltageRdfID) REFERENCES BaseVoltage(rdfID))";
			stmt.executeUpdate(sql);
			logger.debug("Created table TransformerWinding successfully");

			// Create table Load
			sql = "DROP TABLE IF EXISTS Loads";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS Loads " +
					"(rdfId VARCHAR(255), " +
					" name VARCHAR(255), " +
					" pFixed DECIMAL(7,2), " +
					" qFixed DECIMAL(7,2), " +
					" equimentContainerRdfId VARCHAR(255), " +
					" baseVoltageRdfId VARCHAR(255), " +
					" PRIMARY KEY (rdfId), " +
					" FOREIGN KEY (equimentContainerRdfId) REFERENCES EquipmentContainer(rdfId),"+
					" FOREIGN KEY (baseVoltageRdfID) REFERENCES BaseVoltage(rdfID))";
			stmt.executeUpdate(sql);
			logger.debug("Created table Loads successfully");
			success=true;
		
		} catch (SQLException e) {
			logger.error("Error while creating database and tables",e);
			PSAnalysisPanel.consoleArea.append("\nError while creating the database and tables. Check logs for more details");
			success=false;
		}
		return success;
	}

}
