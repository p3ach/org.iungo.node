package org.iungo.node.api;

import org.iungo.id.api.ID;
import org.iungo.lifecycle.api.ServiceLifecycle;
import org.iungo.message.api.ReceiveMessage;
import org.iungo.message.api.SendMessage;

public interface MessageLoop extends Comparable<MessageLoop>, ReceiveMessage, SendMessage, ServiceLifecycle {

	ID getID();
	
	Integer getPriority();
}
