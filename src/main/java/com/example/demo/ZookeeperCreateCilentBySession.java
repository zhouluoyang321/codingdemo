package com.example.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author z1006 zly
 * @version 创建时间：2017/11/3 10:50
 */
public class ZookeeperCreateCilentBySession implements Watcher {
  private static CountDownLatch countDownLatch=new CountDownLatch(1);
  public static void main(String[] args) throws Exception{
    ZooKeeper zooKeeper=new ZooKeeper("192.168.220.128:2181",5000,new ZookeeperCreateCilentBySession());
    System.out.println(zooKeeper.getState());
    final long sessionId = zooKeeper.getSessionId();
    final byte[] sessionPasswd = zooKeeper.getSessionPasswd();
    countDownLatch.await();
    //use illegal
    zooKeeper = new ZooKeeper("192.168.220.128:2181",5000,new ZookeeperCreateCilentBySession(),1l,"test".getBytes());
    //use correct
    zooKeeper= new ZooKeeper("192.168.220.128:2181",5000,new ZookeeperCreateCilentBySession(),sessionId,sessionPasswd);
    System.out.println("finish");
    Thread.sleep(Integer.MAX_VALUE);
  }
  @Override
  public void process(WatchedEvent event) {
    System.out.println("receive watch event:"+event);
    if (event.getState()== Event.KeeperState.SyncConnected){
      countDownLatch.countDown();
    }
  }
}
