/**
 * 
 */
package org.garambrogne.ucengine.event;


/**
 * @author mlecarme
 *
 * Action triggered by an event
 */
public interface FutureEvent {
	public String name();
	public void handle(Event event);
}
