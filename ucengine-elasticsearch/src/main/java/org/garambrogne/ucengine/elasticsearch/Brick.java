/**
 * 
 */
package org.garambrogne.ucengine.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.garambrogne.ucengine.User;
import org.garambrogne.ucengine.event.Event;
import org.garambrogne.ucengine.event.EventHandler;

/**
 * @author mlecarme
 *
 */
public class Brick {
	
	public Brick(User user) {
		Node node;
		node = NodeBuilder.nodeBuilder().node();
		node.start();
		final Client client = node.client();
		user.register("internal.presence.add", new EventHandler() {
			public void handle(Event event) {
				client.prepareIndex("ucengine", "internal.presence.add").setSource(event.getRaw().toString()).execute();
			}
		});
	}

}
