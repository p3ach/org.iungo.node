package org.iungo.node.api;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.iungo.id.api.ID;
import org.iungo.logger.api.Logger;
import org.iungo.logger.api.SimpleLogger;

/**
 * 
 * @author dick
 *
 */
public class ConcurrentHashMapMessageHandles implements MessageHandles {

	private static final Logger logger = new SimpleLogger(ConcurrentHashMapMessageHandles.class.getName());
	
	private final ConcurrentHashMap<ID, MessageHandle> handles = new ConcurrentHashMap<>();
	
	@Override
	public void add(final ID key, final MessageHandle messageHandle) {
		final MessageHandle previous = handles.put(key, messageHandle);
		if (logger.isTrace()) {
			if (previous == null) {
				logger.trace(String.format("Added key [%s] with [%s].", key, messageHandle));
			} else {
				logger.trace(String.format("Added key [%s] with [%s] replacing [%s].", key, messageHandle, previous));
			}
		}
	}

	@Override
	public void remove(final ID key) {
		handles.remove(key);
	}

	@Override
	public MessageHandle get(final ID key) {
		return handles.get(key);
	}

	protected String handlesToString() {
		final StringBuilder text = new StringBuilder(1024);
		text.append("Handles [\n");
		for (Entry<ID, MessageHandle> entry : handles.entrySet()) {
			text.append(String.format("\nHandle [\nID [\n%s\n]\nHandle [\n%s\n]\n]", entry.getKey(), entry.getValue()));
		}
		text.append("\n]");
		return text.toString();
	}
	
	@Override
	public String toString() {
		return String.format("%s [\nHandles [\n%s\n]\n]", ConcurrentHashMapMessageHandles.class.getName(), handlesToString());
	}

}
