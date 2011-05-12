/**
 * 
 */
package org.garambrogne.ucengine.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.tapestry5.json.JSONObject;
import org.garambrogne.ucengine.UCEngine;
import org.garambrogne.ucengine.rpc.HttpMethod;
import org.garambrogne.ucengine.rpc.Response;

/**
 * @author mlecarme
 * Abstract object that can be connected
 * @see Eventualy
 */
public abstract class Connectable {
	protected UCEngine engine;
	protected Log log = LogFactory.getLog(this.getClass());
	protected String uid, sid;

	public JSONObject connect(String uig, String credential, int start) throws ClientProtocolException, IOException {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		return rawconnect(formparams);
	}
	
	protected JSONObject rawconnect(List<NameValuePair> formparams) throws ClientProtocolException, IOException {
		Response response = engine.execute(HttpMethod.POST, "/presence/", null, formparams);
		JSONObject result = response.getValues().getJSONObject("result");
		this.uid = result.getString("uid");
		this.sid = result.getString("sid");
		return result;

	}
}
