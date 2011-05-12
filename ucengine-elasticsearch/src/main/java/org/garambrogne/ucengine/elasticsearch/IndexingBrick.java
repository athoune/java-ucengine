/**
 * 
 */
package org.garambrogne.ucengine.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.garambrogne.ucengine.Brick;
import org.garambrogne.ucengine.event.Event;
import org.garambrogne.ucengine.event.EventHandler;

/**
 * @author mlecarme
 *
 */
public class IndexingBrick extends Brick {
	
	public IndexingBrick() {
		Node node;
		node = NodeBuilder.nodeBuilder().node();
		node.start();
		final Client client = node.client();
		this.register("internal.presence.add", new EventHandler() {
			public void handle(Event event) {
				client.prepareIndex("ucengine", "internal.presence.add")
					.setSource(event.getRaw().toString())
					.execute();
			}
		});
	}
	
	public void suscribe() {
		this.suscribe("internal.presence.add");
	}

}
