/**
 * 
 */
package org.garambrogne.ucengine.event;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.json.JSONObject;

/**
 * @author mlecarme
 *
 * Event POJO
 */
public class Event {
	private String id;
	private String domain;
	private String from;
	private int datetime;
	private String type;
	private Map<String, Object> metadata = null;
	private JSONObject raw;

	public Event(JSONObject data) {
		raw = data;
		id = data.getString("id");
		domain = data.getString("domain");
		from = data.getString("from");
		datetime = data.getInt("datetime");
		type = data.getString("type");
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @return the datetime
	 */
	public int getDatetime() {
		return datetime;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @return the metadata
	 */
	public Map<String, Object> getMetadata() {
		if(metadata == null) {
			JSONObject meta = raw.getJSONObject("metadata");
			metadata = new HashMap<String, Object>();
			for (String key: meta.keys()) {
				metadata.put(key, meta.get(key));
			}
		}
		return metadata;
	}
	/**
	 * @return the raw
	 */
	public JSONObject getRaw() {
		return raw;
	}

}
