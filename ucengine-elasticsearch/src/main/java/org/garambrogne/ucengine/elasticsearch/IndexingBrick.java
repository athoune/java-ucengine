/**
 * 
 */
package org.garambrogne.ucengine.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.garambrogne.ucengine.Brick;
import org.garambrogne.ucengine.event.Event;
import org.garambrogne.ucengine.event.On;

/**
 * @author mlecarme
 *
 */
public class IndexingBrick extends Brick {
	final Client client;
	public IndexingBrick(String name) {
		super(name);
		Node node;
		node = NodeBuilder.nodeBuilder().node();
		node.start();
		this.client = node.client();
	}
	
	@On("internal.presence.add")
	public void handlePresenceAdd(Event event) {
		client.prepareIndex("ucengine", "internal.presence.add")
		.setSource(event.getRaw().toString())
		.execute();
	}
	
	public void suscribe() {
		//this.suscribe("internal.presence.add");
	}
}
