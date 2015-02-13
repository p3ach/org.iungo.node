package org.iungo.node.api;

import org.iungo.id.api.ID;
import org.iungo.lifecycle.api.DefaultServiceLifecycle;
import org.iungo.lifecycle.api.ServiceLifecycle;
import org.iungo.message.api.Message;
import org.iungo.result.api.Result;
import org.iungo.router.api.Router;

public abstract class AbstractNode implements Node, ServiceLifecycle {
	
	private static final long serialVersionUID = 1L;

	public static final Integer MESSAGE_LOOP_PRIORITY = 0;
	
	private final Long timestamp = System.currentTimeMillis();

	private final Router router;
	
	private final ID id;
	
	private final ServiceLifecycle serviceLifecycle = new DefaultServiceLifecycle();
	
	private final MessageLoops messageLoops = new ConcurrentSkipSetMessageLoops(MESSAGE_LOOPS_ID, MESSAGE_LOOP_PRIORITY);
	
	private final PriorityBlockingMessageLoop rootMessageLoop = new PriorityBlockingMessageLoop(ROOT_MESSAGE_LOOP_ID, MESSAGE_LOOP_PRIORITY);
	
	public AbstractNode(final Router router, final ID id) {
		this.router = router;
		this.id = id;
		
		rootMessageLoop.addReceiveMessageHandle(new IDMessageHandle(NODE_START_ID) {
			@Override
			public Result go(final Message message) {
				return Result.FALSE;
			}
		});
		
		rootMessageLoop.addReceiveMessageHandle(new IDMessageHandle(NODE_PAUSE_ID) {
			@Override
			public Result go(final Message message) {
				return Result.FALSE;
			}
		});
		
		rootMessageLoop.addReceiveMessageHandle(new IDMessageHandle(NODE_STOP_ID) {
			@Override
			public Result go(final Message message) {
				return Result.FALSE;
			}
		});
		
		rootMessageLoop.addReceiveMessageHandle(new IDMessageHandle(NODE_CLOSE_ID) {
			@Override
			public Result go(final Message message) {
				return Result.FALSE;
			}
		});
				
		rootMessageLoop.addReceiveMessageHandle(new IDMessageHandle(NODE_ECHO_REQUEST_ID) {
			@Override
			public Result go(final Message message) {
				return Result.FALSE;
			}
		});
		
		messageLoops.add(rootMessageLoop);
	}
	
	public Long getTimestamp() {
		return timestamp;
	}

	public ID getID() {
		return id;
	}

	public Result receive(final Message message) {
		try {
			return messageLoops.receive(message);
		} catch (final Exception exception) {
			return Result.valueOf(exception);
		}
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s [\nTimestamp [\n%d\n]\nID [\n%s\n]\nLifecycle [\n%s\n]\nMessage Loops [\n%s\n]\n]", AbstractNode.class.getName(), getTimestamp(), getID(), serviceLifecycle, messageLoops);
	}

	/*
	 * ServiceLifecycle.
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
		Result result = serviceLifecycle.setState(state);
		if (result.isTrue()) {
			result = messageLoops.setState(state);
		}
		return result;
	}

}
