/**
 * 
 */
package org.garambrogne.ucengine.event;


/**
 * @author mlecarme
 *
 * Action triggered by an event
 */
public interface EventHandler {
	public String name();
	public void handle(Event event);
}