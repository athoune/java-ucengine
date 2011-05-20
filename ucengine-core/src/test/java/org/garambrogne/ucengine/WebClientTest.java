/**
 * 
 */
package org.garambrogne.ucengine;

import java.net.MalformedURLException;

import org.garambrogne.ucengine.rpc.HttpException;
import org.garambrogne.ucengine.rpc.Session;
import org.garambrogne.ucengine.rpc.UCEngine;
import org.garambrogne.ucengine.rpc.UceException;
import org.junit.Test;

/**
 * @author mlecarme
 *
 */
public class WebClientTest {

	@Test
	public void time() throws MalformedURLException, HttpException, UceException {
		UCEngine engine = new UCEngine("http://demo.ucengine.org");
		System.out.println(engine.time());
	}
	
	@Test
	public void demo() throws MalformedURLException, HttpException, UceException {
		UCEngine engine = new UCEngine("http://demo.ucengine.org");
		final User victor = new SimpleUser("victor.goya@af83.com");
		final Session session = engine.connection(victor, "pwd");
		session.listenAllEvents();
		victor.presence();	
	}
	
	@Test
	public void infos() throws HttpException, MalformedURLException, UceException {
		UCEngine engine = new UCEngine("http://demo.ucengine.org");
		User victor = new User("victor.goya@af83.com");
		Session session = engine.connection(victor, "pwd");
		System.out.println(session.infos());
	}
}
