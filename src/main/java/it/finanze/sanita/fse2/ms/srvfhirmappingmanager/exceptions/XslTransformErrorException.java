package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.ResultLogEnum;
import lombok.Getter;

/**
 * 
 * Validation error exception.
 *
 */
public class XslTransformErrorException extends RuntimeException {


	/**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = 632699757725733253L;

	
	@Getter
	private final ResultLogEnum result;

	@Getter
	private final String transactionId;

	public XslTransformErrorException(final ResultLogEnum inResult, final String msg, final String inTransactionId) {
		super(msg);
		transactionId = inTransactionId;
		result = inResult;
	}

}
