package com.harshi_solution.inventorymate.service;

import com.harshi_solution.inventorymate.util.SequenceIdTypeEnum;

public interface InfraService {

	String generateNewSequence(SequenceIdTypeEnum seqIdTypeEnum);
}
