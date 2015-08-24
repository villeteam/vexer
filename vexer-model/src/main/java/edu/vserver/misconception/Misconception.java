package edu.vserver.misconception;

import fi.utu.ville.exercises.model.StubUIConstants;
import fi.utu.ville.standardutils.LocalizableEnum;

/**
 * Enum for Misconceptions.
 */
public enum Misconception implements LocalizableEnum{
	NONE(""), 
	BASE10(StubUIConstants.MP_BASE10),
	BASE10_OVER(StubUIConstants.MP_BASE10_OVER), 
	BASE10_UNDER(StubUIConstants.MP_BASE10_UNDER), 
	ADDITION(StubUIConstants.MP_ADDITION), 
	SUBTRACTION(StubUIConstants.MP_SUBTRACTION), 
	NUMBERLINE(StubUIConstants.MP_NUMBERLINE), 
	NUMEROSITY(StubUIConstants.MP_NUMEROSITY), 
	EQUALITY(StubUIConstants.MP_EQUALITY), 
	MIXOPERATOR(StubUIConstants.MP_MIXOPERATOR), 
	LONG_CALCULATION(StubUIConstants.MP_LONG_CALCULATION), 
	MULTIPLICATION(StubUIConstants.MP_MULTIPLICATION),
	DIVISION(StubUIConstants.MP_DIVISION), 
	TENPAIR(StubUIConstants.MP_TENPAIR), 
	CALCULATION_IN_A_ROW(StubUIConstants.MP_CALCULATION_IN_A_ROW),
	CLOCK(StubUIConstants.MP_CLOCK),
	CLOCK_ADVANCED(StubUIConstants.MP_CLOCK_ADVANCED);
	
	private final String constant;
	private Misconception(String constant){
		this.constant = constant;
	}
	@Override
	public String getLocalizerString() {
		return constant;
	}
	
}