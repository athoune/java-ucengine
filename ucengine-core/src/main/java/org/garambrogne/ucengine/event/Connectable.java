/**
 * 
 */
package org.garambrogne.ucengine.event;

import java.util.Map;

import org.garambrogne.ucengine.rpc.UCEngine;


/**
 * @author mlecarme
 * Can be connected
 * @see Eventualy
 */
public interface Connectable {
	public void setUid(String uid);	
	public void setSid(String sid);
	public Map<String, Object> metaDataOnConnection();
	public void setEngine(UCEngine engine);
	public String getName();
}
