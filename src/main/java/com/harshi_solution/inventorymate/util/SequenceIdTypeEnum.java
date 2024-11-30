package com.harshi_solution.inventorymate.util;

/**
 * Enum representing different types of sequence IDs.
 * This holds a set of constants
 */
public enum SequenceIdTypeEnum {

	SEQ_TYPE_CONSULTID(0),SEQ_TYPE_PARTYCODE(1),SEQ_TYPE_INSTANCENO(2), SEQ_TYPE_SRNO(3);
	
	int idType;
	
	SequenceIdTypeEnum(int idType){
		this.idType=idType;
	}
	
	public int getIdType() {
		return idType;
	}
	
}
