/**
 * 
 */
package org.garambrogne.ucengine;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

/**
 * @author mlecarme
 *
 */
public class WebClientTest {

	@Test
	public void google() throws URISyntaxException {
		WebClient client = new WebClient(new URI("http://www.google.fr/"));
		client.get(new URI("http://www.google.fr/"));
	}
}
