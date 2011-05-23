/**
 * 
 */
package org.garambrogne.ucengine;

import org.garambrogne.ucengine.event.Connectable;
import org.garambrogne.ucengine.event.Eventable;
import org.garambrogne.ucengine.event.impl.AbstractClient;
import org.garambrogne.ucengine.rpc.ActionAble;

/**
 * @author mlecarme
 * Distant plugin
 */
public class Brick extends AbstractClient implements Eventable, Connectable, ActionAble {

	public Brick(String name) {
		super(name);
	}

	@Override
	public String eventPath() {
		// TODO Auto-generated method stub
		return "/event";
	}
	
	public void listenEvent(String event) {
	
	}
	
}
