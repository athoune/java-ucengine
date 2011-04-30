/**
 * 
 */
package org.garambrogne.ucengine;

import org.apache.tapestry5.json.JSONObject;

/**
 * @author mlecarme
 *
 */
public class Response {
	private int status;
	private JSONObject values;
	/**
	 * @param status
	 * @param values
	 */
	public Response(int status, JSONObject values) {
		super();
		this.status = status;
		this.values = values;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @return the values
	 */
	public JSONObject getValues() {
		return values;
	}
	public boolean isError() {
		return values.has("error");
	}
}
