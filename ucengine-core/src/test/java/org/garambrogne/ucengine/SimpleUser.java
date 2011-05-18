/**
 * 
 */
package org.garambrogne.ucengine;

import org.garambrogne.ucengine.event.Event;
import org.garambrogne.ucengine.event.ExitLoopException;
import org.garambrogne.ucengine.event.On;

/**
 * @author mlecarme
 *
 */
public class SimpleUser extends User {

	public SimpleUser(String name) {
		super(name);
	}
	
	@On("internal.presence.add")
	public void handlePresenceAdd(Event event) throws ExitLoopException {
		System.out.println(event.getRaw());
		//if(event.getFrom() == this.uid)
			throw new ExitLoopException();
	}

}
