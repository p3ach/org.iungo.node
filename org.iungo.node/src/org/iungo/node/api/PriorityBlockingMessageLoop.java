package org.iungo.node.api;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.iungo.common.api.DaemonThreadFactory;
import org.iungo.id.api.ID;
import org.iungo.lifecycle.api.ServiceLifecycle;
import org.iungo.logger.api.Logger;
import org.iungo.logger.api.SimpleLogger;
import org.iungo.message.api.Message;
import org.iungo.result.api.Result;

public class PriorityBlockingMessageLoop extends AbstractHandleMessageLoop {

	private static final Logger logger = new SimpleLogger(PriorityBlockingMessageLoop.class.getName());
	
	private static final long serialVersionUID = 1L;

	private final BlockingQueue<MessageLoopDispatch> receive = new PriorityBlockingQueue<>(512, new Comparator<MessageLoopDispatch>() {
		@Override
		public int compare(MessageLoopDispatch o1, MessageLoopDispatch o2) {
			return o1.getMessage().compareTo(o2.getMessage());
		}
	});

	private final AtomicInteger receiveCount = new AtomicInteger();
	
	private volatile Runnable receiveRunnable = null;
	
	private volatile Thread receiveThread = null;
	
	private final BlockingQueue<MessageLoopDispatch> send = new PriorityBlockingQueue<>(512, new Comparator<MessageLoopDispatch>() {
		@Override
		public int compare(MessageLoopDispatch o1, MessageLoopDispatch o2) {
			return o1.getMessage().compareTo(o2.getMessage());
		}
	});
	
	private final AtomicInteger sendCount = new AtomicInteger();
	
	private volatile Runnable sendRunnable = null;
	
	private volatile Thread sendThread = null;
	
	public PriorityBlockingMessageLoop(final ID id, final Integer priority) {
		super(id, priority);
	}
	
	protected Result receivedTake() {
		try {
			return new Result(true, null, receive.take());
		} catch (final InterruptedException interruptedException) {
			return Result.valueOf(interruptedException);
		} catch (final Throwable throwable) {
			return Result.valueOf(throwable);
		}
	}
	
	protected Result sendTake() {
		try {
			return new Result(true, null, send.take());
		} catch (final InterruptedException interruptedException) {
			return Result.valueOf(interruptedException);
		} catch (final Throwable throwable) {
			return Result.valueOf(throwable);
		}
	}
	
	@Override
	public synchronized Result setState(final int state) {
		Result result = super.setState(state);
		if (result.isTrue()) {
			switch (state) {
			case ServiceLifecycle.STARTED:
				/*
				 * Receive.
				 */
				if (receiveRunnable == null) {
					receiveRunnable = new Runnable() {
						@Override
						public void run() {
							while (true) {
								switch (state) {
								case ServiceLifecycle.STARTED:
									Result result = receivedTake();
									if (result.isTrue()) {
										logger.info(result.toString());
									}
									break;
								case ServiceLifecycle.CLOSED:
									return;
								}
							}
						}
					};
					receiveThread = DaemonThreadFactory.valueOf(receiveRunnable, String.format("[%s] Receive", getID()));
					receiveThread.start();
				}
				if (sendRunnable == null) {
					/*
					 * Send.
					 */
					sendRunnable = new Runnable() {
						@Override
						public void run() {
							while (true) {
								switch (state) {
								case ServiceLifecycle.STARTED:
									Result result = sendTake();
									if (result.isTrue()) {
										logger.info(result.toString());
									}
									break;
								case ServiceLifecycle.CLOSED:
									return;
								}
							}
						}
					};
					sendThread = DaemonThreadFactory.valueOf(sendRunnable, String.format("[%s] Send", getID()));
					sendThread.start();
				}
				break;
			case ServiceLifecycle.PAUSED:
				
				break;
			case ServiceLifecycle.STOPPED:
				
				break;

			default:
				break;
			}
		}
		return result;
	}

	@Override
	public Result receive(final Message message) {
		final MessageHandle messageHandle = getReceiveMessageHandles().get(message.getKey());
		if (messageHandle == null) {
			return Result.UNDEFINED;
		}
		final MessageLoopDispatch messageLoopDispatch = new SimpleMessageLoopDispatch(message, messageHandle);
		receive.add(messageLoopDispatch);
		receiveCount.incrementAndGet();
		return new Result(true, null, messageLoopDispatch);
	}

	@Override
	public Result send(final Message message) {
		return Result.UNDEFINED;
	}

	protected String receiveToString() {
		return String.format("Receive [\nQueue [%d]\nCount [%s]\n]", receive.size(), receiveCount);
	}

	protected String sendToString() {
		return String.format("Send [\nQueue [%d]\nCount [%s]\n]", send.size(), sendCount);
	}
	
	@Override
	public String toString() {
		return String.format("%s [\nReceive [\n%s\n]\nSend [\n%s\n]\nSuper [\n%s\n]\n]", PriorityBlockingMessageLoop.class.getName(), receiveToString(), sendToString(), super.toString());
	}

}
