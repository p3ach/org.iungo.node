package org.iungo.node.api;


public interface MessageLoops extends MessageLoop {

	void add(MessageLoop messageLoop);
	
	void remove(MessageLoop messageLoop);
}
