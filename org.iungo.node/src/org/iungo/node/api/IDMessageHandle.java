package org.iungo.node.api;

import org.iungo.id.api.ID;

public abstract class IDMessageHandle implements MessageHandle {

	private final ID id;
	
	public IDMessageHandle(final ID id) {
		super();
		this.id = id;
	}

	public ID getID() {
		return id;
	}

	@Override
	public String toString() {
		return String.format("%s [\nId [\n%s\n]\n]", IDMessageHandle.class.getName(), id);
	}
}
