/**
 * 
 */
package org.garambrogne.ucengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.garambrogne.ucengine.event.AbstractClient;
import org.garambrogne.ucengine.event.Connectable;
import org.garambrogne.ucengine.event.Eventable;
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
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("uid", this.uid));
		qparams.add(new BasicNameValuePair("sid", this.sid));
		engine.get("/event", qparams);
	}
}
