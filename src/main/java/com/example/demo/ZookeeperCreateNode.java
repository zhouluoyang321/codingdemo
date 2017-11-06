package com.example.demo;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * @author z1006 zly
 * @version 创建时间：2017/11/3 10:50
 */
public class ZookeeperCreateNode implements Watcher {
  private static CountDownLatch countDownLatch=new CountDownLatch(1);
  public static void main(String[] args) throws Exception{
    ZooKeeper zooKeeper=new ZooKeeper("192.168.220.128:2181",5000,new ZookeeperCreateNode());
    countDownLatch.await();
    final String s = zooKeeper.create("/zly", "dsg".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    System.out.println("success create node:" + s);
    final String s2 = zooKeeper.create("/zly", "dsg".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    System.out.println("success create node:"+s2);
    Thread.sleep(Integer.MAX_VALUE);
    System.out.println("finish");
  }
  @Override
  public void process(WatchedEvent event) {
    System.out.println("receive watch event:"+event);
    if (event.getState()== Event.KeeperState.SyncConnected){
      countDownLatch.countDown();
    }
  }
}
