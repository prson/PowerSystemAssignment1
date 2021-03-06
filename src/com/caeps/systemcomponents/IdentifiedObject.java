package com.caeps.systemcomponents;


// TODO: Auto-generated Javadoc
/**
 * The Class IdentifiedObject. The main class from which all the components of the powers system is inherited.
 */
public class IdentifiedObject  {
	
	/** The rdf id. */
	public String rdfID;
	
	/** The name. */
	public String name;
	
	/**
	 * Instantiates a new identified object.
	 *
	 * @param rdfId the rdf id
	 * @param n the n
	 */
	public IdentifiedObject(String rdfId, String n){
		rdfID = rdfId;
		name = n;
	}

	/**
	 * Gets the rdf id.
	 *
	 * @return the rdf id
	 */
	public String getRdfID() {
		// TODO Auto-generated method stub
		return rdfID;
	}

	
}