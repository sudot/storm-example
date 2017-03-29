package net.sudot.storm.bolts;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class ClickCounter extends BaseBasicBolt {
	Logger logger = LoggerFactory.getLogger(getClass());

	private Jedis jedis;
	private String resultKey;

	@Override
	public void cleanup() {
		jedis.close();
	}

	/**
	 * On create
	 */
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		String host = String.valueOf(stormConf.get("redis-host"));
		int port = Integer.valueOf(String.valueOf(stormConf.get("redis-port")));
		String resultKey = String.valueOf(stormConf.get("result-key"));
		logger.info("ClickCounter初始化redis: Host:{} Port:{}", host, port);
		jedis = new Jedis(host, port);
		this.resultKey = resultKey;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}


	/**
	 * 点击次数统计逻辑
	 * @param input
	 * @param collector
	 */
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String str = input.getString(0);
		logger.info("统计点击次数:{}", str);
		jedis.incr(resultKey); // 执行加1操作
	}
}
