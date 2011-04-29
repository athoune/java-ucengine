/**
 * 
 */
package org.garambrogne.ucengine;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.nio.reactor.IOReactorException;
import org.junit.Test;

/**
 * @author mlecarme
 *
 */
public class WebClientTest {

	@Test
	public void google() throws URISyntaxException, IOReactorException, MalformedURLException, InterruptedException {
		WebClient client = new WebClient(new URL("http://www.google.fr/"));
		client.execute(null, "http://www.google.fr/");
		client.execute(null, "http://www.google.fr/search?q=java&hl=fr&prmd=ivns&source=lnms&tbm=isch&ei=1pa4TcvKLI22hAfcw9CGDw&sa=X&oi=mode_link&ct=mode&cd=2&ved=0CBkQ_AUoAQ&biw=2271&bih=1139");
		
	}
}
