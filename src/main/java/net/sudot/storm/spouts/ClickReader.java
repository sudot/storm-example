package net.sudot.storm.spouts;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.UUID;

public class ClickReader extends BaseRichSpout {

	Logger logger = LoggerFactory.getLogger(getClass());
	private Jedis jedis;
	private SpoutOutputCollector collector;
	private String clickKey;

	public void ack(Object msgId) {
		logger.info("处理成功:messageId:{}", msgId);
	}

	public void close() {
		jedis.close();
	}

	public void fail(Object msgId) {
		logger.info("处理失败:messageId:{} word:{}", msgId);
	}

	/**
	 * 读取数据供后续处理
	 */
	public void nextTuple() {
		String content = jedis.rpop(clickKey);
		if (content == null || "nil".equals(content)) {
			try { Thread.sleep(300); } catch (InterruptedException e) {}
		} else {
			String msgId = UUID.randomUUID().toString();
			logger.info("新的统计数据:{}. msgId:{} 分发处理", content, msgId);
			collector.emit(new Values(content), msgId);
		}
	}

	/**
	 * 初始化
	 */
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		String host = String.valueOf(conf.get("redis-host"));
		int port = Integer.valueOf(String.valueOf(conf.get("redis-port")));
		String resultKey = String.valueOf(conf.get("result-key"));
		String clickKey = String.valueOf(conf.get("click-key"));
		logger.info("ClickReader初始化redis: Host:{} Port:{}", host, port);
		jedis = new Jedis(host, port);
		jedis.del(resultKey);
		jedis.del(clickKey);
		this.collector = collector;
		this.clickKey = clickKey;
	}

	/**
	 * Declare the output field "word"
	 */
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}
}
