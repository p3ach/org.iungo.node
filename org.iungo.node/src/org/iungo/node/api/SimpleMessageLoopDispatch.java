package org.iungo.node.api;

import org.iungo.message.api.Message;
import org.iungo.result.api.Result;

public class SimpleMessageLoopDispatch extends AbstractMessageLoopDisptach {

	public SimpleMessageLoopDispatch(final Message message, final MessageHandle messageHandle) {
		super(message, messageHandle);
	}

	@Override
	public Result go() {
		return getMessageHandle().go(getMessage());
	}

	@Override
	public String toString() {
		return String.format("%s [ Super : [%s]]", getClass(), super.toString());
	}
}
