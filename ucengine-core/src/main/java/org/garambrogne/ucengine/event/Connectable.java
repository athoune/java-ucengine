/**
 * 
 */
package org.garambrogne.ucengine.event;

import java.util.Map;

import org.garambrogne.ucengine.rpc.Session;


/**
 * @author mlecarme
 * Can be connected
 * @see Eventualy
 */
public interface Connectable {
	public void setUid(String uid);	
	public void setSid(String sid);
	public Map<String, Object> metaDataOnConnection();
	public String getName();
	public void setSession(Session session);
}
