/**
 * 
 */
package org.garambrogne.ucengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;


/**
 * @author mlecarme
 *
 */
public class User {
	private UCEngine engine;
	private String uid;
	/**
	 * @param uid
	 */
	public User(String uid) {
		super();
		this.uid = uid;
	}
	public void presence(UCEngine engine, String credential) throws ClientProtocolException, IOException {
		this.engine = engine;
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("uid", this.uid));
		formparams.add(new BasicNameValuePair("credential", credential));
		formparams.add(new BasicNameValuePair("metadata[nickname]", this.uid));
		engine.execute(HttpMethod.POST, "/presence/", formparams);
	}
	
}
