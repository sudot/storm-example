package net.sudot.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import net.sudot.storm.bolts.ClickCounter;
import net.sudot.storm.spouts.ClickReader;


public class TopologyMain {
	public final static String REDIS_HOST = "192.168.11.151";
	public final static int REDIS_PORT = 6379;
	public final static String RESULT_KEY = TopologyMain.class.getName() + ".result";
	public final static String CLICK_KEY = TopologyMain.class.getName() + ".click";

	public static void main(String[] args) throws InterruptedException {
        //Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("click-reader",new ClickReader());
		builder.setBolt("click-counter", new ClickCounter(), 1).globalGrouping("click-reader");
		
        //Configuration
		Config conf = new Config();
		conf.setDebug(false);
		conf.put("redis-host", REDIS_HOST);
		conf.put("redis-port", REDIS_PORT);
		conf.put("result-key", RESULT_KEY);
		conf.put("click-key", CLICK_KEY);
        //Topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("Getting-Started-Toplogie", conf, builder.createTopology());
		System.out.println("storm服务已启动...");
	}
}
