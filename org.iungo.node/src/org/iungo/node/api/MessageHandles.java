package org.iungo.node.api;

import org.iungo.id.api.ID;


public interface MessageHandles {

	void add(ID id, MessageHandle messageHandle);
	
	void remove(ID id);

	MessageHandle get(ID id);
}
