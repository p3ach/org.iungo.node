package org.iungo.node.api;


public interface MessageFilters extends MessageFilter {
	
	void add(MessageFilter messageFilter);
	
	void remove(MessageFilter messageFilter);
}
