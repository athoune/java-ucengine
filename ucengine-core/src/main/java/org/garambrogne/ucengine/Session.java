/**
 * 
 */
package org.garambrogne.ucengine;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tapestry5.json.JSONObject;
import org.garambrogne.ucengine.event.Eventualy;
import org.garambrogne.ucengine.rpc.Response;

/**
 * @author mlecarme
 *
 */
public class Session extends Eventualy {
	protected String uid, sid;
	
	protected Session(UCEngine engine, String uid, String sid) {
		this.engine = engine;
		this.uid = uid;
		this.sid = sid;
	}
	public JSONObject infos() throws HttpException, UceException {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("uid", this.uid));
		qparams.add(new BasicNameValuePair("sid", this.sid));
		Response response = engine.get("/infos", qparams);
		System.out.println(response.getValues());
		return null;
//		return response.getValues().getJSONObject("result");
	}

}
