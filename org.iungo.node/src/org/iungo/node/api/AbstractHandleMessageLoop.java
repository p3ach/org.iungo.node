package org.iungo.node.api;

import java.util.concurrent.atomic.AtomicInteger;

import org.iungo.id.api.ID;

/**
 * Abstract (Message)Handle based MessageLoop.
 * 
 * Receive and send MessageHandle(s) use ConcurrentHashMapMessageHandles.
 * @author dick
 *
 */
public abstract class AbstractHandleMessageLoop extends AbstractMessageLoop {

	private static final long serialVersionUID = 1L;

	private final MessageHandles receiveMessageHandles = new ConcurrentHashMapMessageHandles();
	
	private final MessageHandles sendMessageHandles = new ConcurrentHashMapMessageHandles();
	
	public AbstractHandleMessageLoop(final ID id, final Integer priority) {
		super(id, priority);
	}

	protected MessageHandles getReceiveMessageHandles() {
		return receiveMessageHandles;
	}
	
	protected MessageHandles getSendMessageHandles() {
		return sendMessageHandles;
	}
	
	protected void addReceiveMessageHandle(final IDMessageHandle idMessageHande) {
		receiveMessageHandles.add(idMessageHande.getID(), idMessageHande);
	}
	
	protected void removeReceiveMessageHandle(final ID id) {
		receiveMessageHandles.remove(id);
	}
	
	protected void addSendMessageHandle(final IDMessageHandle idMessageHandle) {
		sendMessageHandles.add(idMessageHandle.getID(), idMessageHandle);
	}
	
	protected void removeSendMessageHandle(final ID id) {
		sendMessageHandles.remove(id);
	}

	@Override
	public String toString() {
		return String.format("%s [\nReceive Handles [\n%s\n]\nSend Handles [\n%s\n] Super [\n%s\n]\n]", AbstractHandleMessageLoop.class.getName(), receiveMessageHandles, sendMessageHandles, super.toString());
	}
}
