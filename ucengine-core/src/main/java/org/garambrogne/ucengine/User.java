/**
 * 
 */
package org.garambrogne.ucengine;

import java.util.HashMap;
import java.util.Map;

import org.garambrogne.ucengine.event.Connectable;
import org.garambrogne.ucengine.event.Eventable;
import org.garambrogne.ucengine.event.impl.AbstractClient;
import org.garambrogne.ucengine.rpc.ActionAble;
import org.garambrogne.ucengine.rpc.HttpException;
import org.garambrogne.ucengine.rpc.Meta;
import org.garambrogne.ucengine.rpc.UceException;



/**
 * @author mlecarme
 *
 */
public class User extends AbstractClient implements Eventable, ActionAble, Connectable {

	public User(String name) {
		super(name);
	}

	@Meta("nickname")
	public String getName() {
		return name;
	}

	@Override
	public Map<String, Object> metaDataOnConnection() {
		Map<String, Object> meta = new HashMap<String, Object>();
		meta.put("nickname", getName());
		return meta;
	}

	@Override
	public String eventPath() {
		return "/event";
	}
	
	public void presence() throws HttpException, UceException {
		getSession().GET("/event");
	}
}
