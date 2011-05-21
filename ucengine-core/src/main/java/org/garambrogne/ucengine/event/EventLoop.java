/**
 * 
 */
package org.garambrogne.ucengine.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tapestry5.json.JSONObject;
import org.garambrogne.ucengine.rpc.HttpException;
import org.garambrogne.ucengine.rpc.Response;
import org.garambrogne.ucengine.rpc.Session;
import org.garambrogne.ucengine.rpc.UCEngine;
import org.garambrogne.ucengine.rpc.UceException;

/**
 * @author mlecarme
 *
 */
public class EventLoop {
	private Session session;
	protected Log log = LogFactory.getLog(this.getClass());
	private Thread eventThread;
	private AtomicBoolean running = new AtomicBoolean(true);
	private Map<String, List<EventHandler>> handlers = new HashMap<String, List<EventHandler>>();
	
	public EventLoop(Session session) {
		this.session = session;
	}
	
	public void register(final Object object) {
		for (final Method m : object.getClass().getMethods()) {
	         if (m.isAnnotationPresent(On.class)) {
	        	 String event = m.getAnnotation(On.class).value();
	        	 if(! handlers.containsKey(event)) {
	        		 handlers.put(event, new ArrayList<EventHandler>());
	        	 }
	        	 handlers.get(event).add(new EventHandler() {
					@Override
					public void handle(Event event) throws Throwable {
						try {
							m.invoke(object, event);
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							System.out.println("IllegalArgumentException");
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							System.out.println("IllegalAccessException");
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							throw e.getCause();
						}
					}
				});
	         }
		}
	}

	public void start(String path) throws HttpException {
		start(path, null);
	}

	public void start(String path, String type) throws HttpException {
		List<NameValuePair> qparams = UCEngine.buildArguments(this.session.getUid(), this.session.getSid());
		qparams.add(new BasicNameValuePair("_async", "lp"));
		if(type != null) {
			qparams.add(new BasicNameValuePair("type", type));
		}
		try {
			final HttpRequestBase request = new HttpGet(session.getEngine().uri("/event", qparams));
			eventThread = new Thread(new Runnable() {
				public void run() {
					int start = 0;
					while(running.get()) {
						Response response = null;
						try {
							request.getParams().setIntParameter("start", start);
							System.out.println(request);
							response = session.getEngine().execute(request);
						} catch (UceException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (HttpException e) {
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
		} catch (URISyntaxException e) {
			throw new HttpException(e);
		}
		eventThread.start();
		System.out.println("Event loop started");
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
				try {
					handler.handle(event);
				} catch (Throwable e) {
					running.set(false);
				}
			}
		}
		System.out.println(event.getType());
		if(handlers.containsKey(event.getType())) {
			System.out.println("got event!");
			for (EventHandler handler : handlers.get(event.getType())) {
				session.getEngine().getPool().execute(new Handler(handler, event));
			}
		}
	}
	
	public void stop() {
		running.set(false);
	}
}
