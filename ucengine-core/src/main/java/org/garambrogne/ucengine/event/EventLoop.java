/**
 * 
 */
package org.garambrogne.ucengine.event;

import org.garambrogne.ucengine.rpc.HttpException;

/**
 * @author mlecarme
 *
 */
public interface EventLoop {
	public void start(String path) throws HttpException;
	public void start(String path, String type) throws HttpException;
	public void register(final Object object);	
}
