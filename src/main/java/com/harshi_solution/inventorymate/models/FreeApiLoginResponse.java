package com.harshi_solution.inventorymate.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.harshi_solution.inventorymate.external.model.response.BaseExtResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@JsonInclude(Include.NON_NULL)
public class FreeApiLoginResponse extends BaseExtResponse{
	
}
