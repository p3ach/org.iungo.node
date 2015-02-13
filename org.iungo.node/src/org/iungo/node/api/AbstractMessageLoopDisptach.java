package org.iungo.node.api;

import org.iungo.message.api.Message;

public abstract class AbstractMessageLoopDisptach implements MessageLoopDispatch {

	private final Message message;
	
	private final MessageHandle messageHandle;
	
	public AbstractMessageLoopDisptach(final Message message, MessageHandle messageHandle) {
		super();
		this.message = message;
		this.messageHandle = messageHandle;
	}

	public Message getMessage() {
		return message;
	}

	public MessageHandle getMessageHandle() {
		return messageHandle;
	}

	@Override
	public String toString() {
		return String.format("%s Message [%s] Handle [%s]", getClass(), message, messageHandle);
	}
}
