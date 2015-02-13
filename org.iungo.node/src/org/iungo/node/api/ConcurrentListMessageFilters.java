package org.iungo.node.api;

import org.iungo.common.api.ConcurrentLinkedList;
import org.iungo.common.api.ConcurrentList;
import org.iungo.message.api.Message;
import org.iungo.result.api.Result;

public class ConcurrentListMessageFilters implements MessageFilters {

	private final ConcurrentList<MessageFilter> filters = new ConcurrentLinkedList<>();

	@Override
	public void add(final MessageFilter messageFilter) {
		filters.add(messageFilter);
	}

	@Override
	public void remove(final MessageFilter messageFilter) {
		filters.remove(messageFilter);
	}
	
	public Result go(final Message message) {
		Result result = Result.FALSE; 
		for (MessageFilter messageFilter : filters) {
			result = messageFilter.go(message);
			if (result.isFalse()) {
				break;
			}
			final Integer value = result.getValue();
			if (value.equals(DENY) || value.equals(PERMIT)) {
				break;
			}
			if (!value.equals(UNMATCHED)) {
				throw new UnsupportedOperationException();
			}
		}
		return result;
	}


}
