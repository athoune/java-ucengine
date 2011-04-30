/**
 * 
 */
package org.garambrogne.ucengine;

/**
 * @author mlecarme
 *
 */
public interface FutureEvent {
	public String name();
	public void handle(Event event);
}
