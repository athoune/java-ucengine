/**
 * 
 */
package org.garambrogne.ucengine.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.tapestry5.json.JSONObject;
import org.garambrogne.ucengine.rpc.Response;

/**
 * @author mlecarme
 *
 * An event listener
 */
public abstract class Eventualy extends Connectable{
	private Thread eventThread;
	private boolean running = true;
	private Map<String, List<EventHandler>> events = new HashMap<String, List<EventHandler>>();
	
	protected void startLoop(final HttpRequestBase request) {
		eventThread = new Thread(new Runnable() {
			public void run() {
				int start = 0;
				while(running) {
					Response response = null;
					try {
						request.getParams().setIntParameter("start", start);
						response = engine.execute(request);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(response != null){// && response.getStatus() == 200) {
						for (Object event : response.getValues().getJSONArray("result")) {
							JSONObject jevent = (JSONObject)event;
							start = jevent.getInt("datetime") + 1;
							trigger(new Event(jevent));
						}
					}
					
				}
			}
		});
		eventThread.run();
	}
	
	protected void trigger(Event event) {
		log.debug(event);
		class Handler implements Runnable {
			private EventHandler handler;
			private Event event;
			public Handler(EventHandler handler, Event event) {
				this.handler = handler;
				this.event = event;
			}

			public void run() {
				handler.handle(event);
			}
		}
		if(events.containsKey(event.getType())) {
			for (EventHandler future : events.get(event.getType())) {
				engine.getPool().execute(new Handler(future, event));
			}
		}
	}
	
	public void register(String name, EventHandler futureEvent) {
		if(! events.containsKey(name)) {
			events.put(name, new ArrayList<EventHandler>());
		}
		events.get(name).add(futureEvent);
	}

	public void register(String[] names, EventHandler futureEvent) {
		for(int a=0; a < names.length; a++) {
			register(names[a], futureEvent);
		}
	}

	public void shutdown() throws InterruptedException {
		running = false;
		eventThread.join();
	}

}
