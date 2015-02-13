package org.iungo.node.api;

import org.iungo.message.api.Message;
import org.iungo.result.api.Result;

public interface MessageHandle {

	Result go(Message message);
}
