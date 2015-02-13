package org.iungo.node.api;

import org.iungo.id.api.ID;
import org.iungo.lifecycle.api.ServiceLifecycle;
import org.iungo.message.api.ReceiveMessage;

public interface Node extends ReceiveMessage {

	static final String ID_ROOT = Node.class.getName();
	
	static final ID MESSAGE_LOOPS_ID = new ID(ID_ROOT, "Loops", null);
	
	static final ID ROOT_MESSAGE_LOOP_ID = new ID(ID_ROOT, "Loop", "Root");

	static final ID NODE_ECHO_REQUEST_ID = new ID(ID_ROOT, "Echo", "Request");

	static final ID NODE_ECHO_REPLY_ID = new ID(ID_ROOT, "Echo", "Reply");
	
	static final String SERVICE_LIFECYCLE_NAME = ServiceLifecycle.class.getName();
	
	static final ID NODE_START_ID = new ID(ID_ROOT, SERVICE_LIFECYCLE_NAME, String.valueOf(ServiceLifecycle.STARTED));
	
	static final ID NODE_PAUSE_ID = new ID(ID_ROOT, SERVICE_LIFECYCLE_NAME, String.valueOf(ServiceLifecycle.PAUSED));
	
	static final ID NODE_STOP_ID = new ID(ID_ROOT, SERVICE_LIFECYCLE_NAME, String.valueOf(ServiceLifecycle.STOPPED));
	
	static final ID NODE_CLOSE_ID = new ID(ID_ROOT, SERVICE_LIFECYCLE_NAME, String.valueOf(ServiceLifecycle.CLOSED));

	Long getTimestamp();
	
	ID getID();
}
