/**
 * 
 */
package org.garambrogne.ucengine.event;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.garambrogne.ucengine.rpc.ActionAble;
import org.garambrogne.ucengine.rpc.UCEngine;

/**
 * @author mlecarme
 *
 */
public abstract class AbstractClient implements Connectable, ActionAble {
	protected UCEngine engine;
	protected Log log = LogFactory.getLog(this.getClass());
	protected String uid, sid, name;
	
	public AbstractClient(String name) {
		this.name = name;
	}

	public Map<String, Object> metaDataOnConnection() {
		return null;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public void setSid(String sid) {
		this.sid = sid;
	}

	public UCEngine getEngine() {
		return engine;
	}
	
	public void setEngine(UCEngine engine) {
		this.engine = engine;
	}
	
	public String getName() {
		return name;
	}
	
}
