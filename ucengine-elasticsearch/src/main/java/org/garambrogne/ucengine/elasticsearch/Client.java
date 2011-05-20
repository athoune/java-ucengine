/**
 * 
 */
package org.garambrogne.ucengine.elasticsearch;

import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

/**
 * @author mlecarme
 *
 */
public class Client {

	private Node node;
	public Client() {
		node = NodeBuilder.nodeBuilder().node();
		node.start();
	}
	public void stop() {
		node.stop();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = new Client();
		client.stop();
	}

}
