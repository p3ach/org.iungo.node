package org.iungo.node.api;

import org.iungo.message.api.Message;
import org.iungo.result.api.Result;

public interface MessageFilter {
	
	static final Integer UNMATCHED = 2<<0;
	
	static final Integer DENY = 2<<1;
	
	static final Integer PERMIT = 2<<2;
	
	static final Result UNMATCHED_RESULT = new Result(true, null, UNMATCHED);
	
	static final Result DENY_RESULT = new Result(true, null, DENY);
	
	static final Result PERMIT_RESULT = new Result(true, null, PERMIT);

	/**
	 * Apply the filter to the given Message.
	 * @param message
	 * @return
	 */
	Result go(Message message);
}
