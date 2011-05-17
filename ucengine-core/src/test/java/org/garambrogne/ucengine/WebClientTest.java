/**
 * 
 */
package org.garambrogne.ucengine;

import java.net.MalformedURLException;

import org.garambrogne.ucengine.event.Event;
import org.garambrogne.ucengine.event.EventHandler;
import org.junit.Test;

/**
 * @author mlecarme
 *
 */
public class WebClientTest {

	@Test
	public void time() throws MalformedURLException, HttpException, UceException {
		UCEngine client = new UCEngine("http://demo.ucengine.org");
		System.out.println(client.time());
	}
	
	@Test
	public void demo() throws MalformedURLException, HttpException, UceException {
		UCEngine engine = new UCEngine("http://demo.ucengine.org");
		final User demo = new User("victor.goya@af83.com");
		final UserSession session = engine.connection(demo, "pwd");
		session.register("internal.presence.add", new EventHandler() {
			public void handle(Event event) {
				System.out.println("I'll stop");
				System.out.println(event.getRaw());
				try {
					session.shutdown();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		session.presence();	
	}
	
	@Test
	public void infos() throws HttpException, MalformedURLException, UceException {
		UCEngine engine = new UCEngine("http://demo.ucengine.org");
		final User victor = new User("victor.goya@af83.com");
		System.out.println(engine.connection(victor, "pwd").infos());
	}
}
