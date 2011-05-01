/**
 * 
 */
package org.garambrogne.ucengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.tapestry5.json.JSONObject;

/**
 * @author mlecarme
 *
 */
public abstract class Eventualy {
	protected UCEngine engine;
	private Thread eventThread;
	private Map<String, List<FutureEvent>> events = new HashMap<String, List<FutureEvent>>();
	
	public void startLoop(final HttpRequestBase request) {
		eventThread = new Thread(new Runnable() {
			public void run() {
				int start = 0;
				while(true) {
					Response response = null;
					try {
						request.getParams().setIntParameter("start", start);
						System.out.println(request.getRequestLine());
						response = engine.execute(request);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(response.getStatus());
					if(response != null){// && response.getStatus() == 200) {
						for (Object event : response.getValues().getJSONArray("result")) {
							JSONObject jevent = (JSONObject)event;
							System.out.println(jevent);
							start = jevent.getInt("datetime") + 1;
						}
					}
					
				}
			}
		});
		eventThread.run();
	}
	
	public void register(FutureEvent futureEvent) {
		String name = futureEvent.name();
		if(! events.containsKey(name)) {
			events.put(name, new ArrayList<FutureEvent>());
		}
		events.get(name).add(futureEvent);
	}

}
