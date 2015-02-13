package org.iungo.node.api;

import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListSet;

import org.iungo.id.api.ID;
import org.iungo.message.api.Message;
import org.iungo.result.api.Result;

/**
 * A Set of MessageLoop(s) sorted according to MessageLoop Priority.
 * @author dick
 *
 */
public class ConcurrentSkipSetMessageLoops extends AbstractMessageLoop implements MessageLoops {

	private static final long serialVersionUID = 1L;

	/**
	 * The Set of MessageLoop(s) ordered by Priority.
	 */
	private final ConcurrentSkipListSet<MessageLoop> loops = new ConcurrentSkipListSet<>(new Comparator<MessageLoop>() {
		@Override
		public int compare(final MessageLoop o1, final MessageLoop o2) {
			return o1.getPriority().compareTo(o2.getPriority());
		}
	});
	
	public ConcurrentSkipSetMessageLoops(final ID key, final Integer priority) {
		super(key, priority);
	}

	@Override
	public synchronized Result setState(int state) {
		Result result = super.setState(state);
		if (result.isTrue()) {
			Iterator<MessageLoop> iterator = loops.iterator();
			while (iterator.hasNext()) {
				MessageLoop messageLoop = iterator.next();
				result = messageLoop.setState(state);
			}
		}
		return result;
	}
	
	@Override
	public Result receive(final Message message) {
		Result result = Result.FALSE; // Default in case there is no MessageLoop defined.
		for (MessageLoop loop : loops) {
			result = loop.receive(message);
			if (result.isTrue()) {
				break;
			}
		}
		return result;
	}

	@Override
	public Result send(final Message message) {
		Result result = Result.FALSE; // Default in case there is no MessageLoop defined.
		for (MessageLoop loop : loops) {
			result = loop.send(message);
			if (result.isTrue()) {
				break;
			}
		}
		return result;
	}

	@Override
	public void add(final MessageLoop messageLoop) {
		loops.add(messageLoop);
	}

	@Override
	public void remove(final MessageLoop messageLoop) {
		loops.remove(messageLoop);
	}

	protected String loopsToString() {
		final StringBuilder result = new StringBuilder(1024);
		result.append("[");
		final Iterator<MessageLoop> iterator = loops.iterator();
		while (iterator.hasNext()) {
			result.append(String.format("\nLoop [\n%s\n]", iterator.next()));
		}
		result.append("\n]");
		return result.toString();
	}
	
	@Override
	public String toString() {
		return String.format("%s [\n%s\n]", ConcurrentSkipSetMessageLoops.class.getName(), loopsToString());
	}
}
