#storm-example

此示列为学习[《Getting Started With Storm》中文翻译版](http://ifeve.com/getting-started-with-stom-index/)的一个简单实际应用

```
	1.运行net.sudot.storm.TopologyMain.main启动一个本地storm
	2.将项目部署到tomcat
	3.访问http://localhost:端口/
	4.点击[产生点击次数],并观察TopologyMain控制台运行情况,然后再观察页面结果
```

运行流程:
```
net.sudot.storm.TopologyMain启动后
1.执行net.sudot.storm.spouts.ClickReader.open,相当于此类的初始化工作
2.执行net.sudot.storm.bolts.ClickCounter.prepare,相当于此类的初始化工作
3.net.sudot.storm.spouts.ClickReader.nextTuple一直不停的运行
4.每当ClickReader.nextTuple有任务执行并提交到下一步时,即刻运行net.sudot.storm.bolts.ClickCounter.execute
5.运行结束后,所有业务处理完毕且成功,最后执行net.sudot.storm.spouts.ClickReader.ack
6.运行结束后,只要有任一处理失败,最后执行net.sudot.storm.spouts.ClickReader.fail
```
说明
```
spouts相当于一个计算任务的起点和终点
其间参与的所有bolts是整个任务中的单个环节,一个任务由一个或多个bolt组成
运行和运行顺序由如下方式启动和设置
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("click-reader",new ClickReader());
		builder.setBolt("click-counter", new ClickCounter(), 1).globalGrouping("click-reader");
```

