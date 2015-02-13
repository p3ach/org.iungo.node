package org.iungo.node.api;

import org.iungo.message.api.Message;
import org.iungo.result.api.Result;

public interface MessageLoopDispatch {

	Message getMessage();
	
	Result go();
}
