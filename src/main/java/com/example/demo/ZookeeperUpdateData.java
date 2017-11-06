package com.example.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @author z1006 zly
 * @version 创建时间：2017/11/3 10:50
 */
public class ZookeeperUpdateData implements Watcher {
  private static CountDownLatch countDownLatch=new CountDownLatch(1);
  private static ZooKeeper zooKeeper;
  public static void main(String[] args) throws Exception{
    zooKeeper=new ZooKeeper("192.168.220.128:2181",5000,new ZookeeperUpdateData());
    countDownLatch.await();
    final String s = zooKeeper.create("/zly", "dsg".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    System.out.println("success create node:" + s);
    final byte[] data = zooKeeper.getData("/zly", true, null);
    System.out.println("callback:" + new String(data));
    final Stat stat = zooKeeper.setData("/zly", "dsg2".getBytes(), -1);
    System.out.println(stat.getCzxid() + " " + stat.getMzxid() + " " + stat.getVersion());

    final Stat stat2 = zooKeeper.setData("/zly", "dsg2".getBytes(), stat.getVersion());
    System.out.println(stat2.getCzxid() + " " + stat2.getMzxid() + " " + stat2.getVersion());
    try {
      zooKeeper.setData("/zly", "dsg2".getBytes(), stat.getVersion());
    }catch (KeeperException e){
      System.out.println(e.getMessage()+e.getCode());
    }
    System.out.println("finish");

  }
  @Override
  public void process(WatchedEvent event) {
    System.out.println("receive watch event:"+event);
    if (event.getState()== Event.KeeperState.SyncConnected){
      if (Event.EventType.None==event.getType()&&null==event.getPath()){
        countDownLatch.countDown();
      }
    }
  }
}
