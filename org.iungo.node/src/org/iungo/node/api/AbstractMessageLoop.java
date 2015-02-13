package org.iungo.node.api;

import org.iungo.id.api.ID;
import org.iungo.lifecycle.api.DefaultServiceLifecycle;
import org.iungo.lifecycle.api.ServiceLifecycle;
import org.iungo.result.api.Result;

/**
 * Abstract MessageLoop implementation.
 * 
 * hashCode() and equals(Object) use the ID.
 * 
 * compareTo(MessageLoop) use the Priority.
 * 
 * Extending classes need to implement ReceiveMessage and SendMessage.
 * 
 * @author dick
 *
 */
public abstract class AbstractMessageLoop implements MessageLoop {

	private static final long serialVersionUID = 1L;

	private final ID id;
	
	private final Integer priority;
	
	private final ServiceLifecycle serviceLifecycle = new DefaultServiceLifecycle();
	
	public AbstractMessageLoop(final ID id, final Integer priority) {
		super();
		this.id = id;
		this.priority = priority;
	}

	protected ServiceLifecycle getServiceLifecycle() {
		return serviceLifecycle;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return id.equals(((AbstractMessageLoop) obj).getID());
	}

	@Override
	public int compareTo(final MessageLoop messageLoop) {
		return priority.compareTo(messageLoop.getPriority());
	}

	/*
	 * Message loop.
	 */
	
	@Override
	public ID getID() {
		return id;
	}
	
	@Override
	public Integer getPriority() {
		return priority;
	}

	/*
	 * Service lifecycle.
	 */
	
	@Override
	public int getState() {
		return serviceLifecycle.getState();
	}

	@Override
	public boolean isState(final int state) {
		return serviceLifecycle.isState(state);
	}

	@Override
	public synchronized Result setState(final int state) {
		return serviceLifecycle.setState(state);
	}

	/*
	 * Object
	 */

	@Override
	public String toString() {
		return String.format("%s [\nID [%s]\nPriority [%d]\nLifecycle [%s]\n]", AbstractMessageLoop.class.getName(), getID(), getPriority(), getServiceLifecycle());
	}
}
