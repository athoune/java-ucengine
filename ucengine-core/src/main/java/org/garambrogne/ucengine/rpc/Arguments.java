/**
 * 
 */
package org.garambrogne.ucengine.rpc;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author mlecarme
 *
 */
public class Arguments {
	private List<NameValuePair> values = new ArrayList<NameValuePair>();
	
	public Arguments add(String key, String value) {
		values.add(new BasicNameValuePair(key, value));
		return this;
	}
	
	public List<NameValuePair> values() {
		return values;
	}

}
