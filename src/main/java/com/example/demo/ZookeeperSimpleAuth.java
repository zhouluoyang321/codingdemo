package com.example.demo;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * @author z1006 zly
 * @version 创建时间：2017/11/3 10:50
 */
public class ZookeeperSimpleAuth implements Watcher{
  private static CountDownLatch countDownLatch=new CountDownLatch(1);
  public static void main(String[] args) throws Exception{
    ZooKeeper zooKeeper=new ZooKeeper("192.168.220.128:2181",5000,new ZookeeperSimpleAuth());
    countDownLatch.await();
    zooKeeper.addAuthInfo("digest", "foo:true".getBytes());
    zooKeeper.create("/zly", "dsg".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

    ZooKeeper zooKeeper2=new ZooKeeper("192.168.220.128:2181",50000,new ZookeeperSimpleAuth());
    zooKeeper2.getData("/zly",false,null);
  }
  @Override
  public void process(WatchedEvent event) {
    System.out.println("receive watch event:" + event);
    if (event.getState()== Watcher.Event.KeeperState.SyncConnected){
      countDownLatch.countDown();
    }
  }
}
