package com.example.demo;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * @author z1006 zly
 * @version 创建时间：2017/11/3 10:50
 * 测试zookeeper删除节点权限，对于删除节点的API而言，接口范围是添加权限的子节点，
 * 给一个节点添加权限之后，删除该节点不需要权限，删除其子节点需要权限
 */
public class ZookeeperSimpleAuthDelete implements Watcher{
  private static CountDownLatch countDownLatch=new CountDownLatch(1);
  public static void main(String[] args) throws Exception{
    ZooKeeper zooKeeper=new ZooKeeper("192.168.220.128:2181",5000,new ZookeeperSimpleAuthDelete());
    countDownLatch.await();
    zooKeeper.addAuthInfo("digest", "foo:true".getBytes());
    /*
    * 临时节点不能创建子节点
    * */
    zooKeeper.create("/zly", "dsg".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
    zooKeeper.create("/zly/dsg", "dsg".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
    ZooKeeper zooKeeper2=new ZooKeeper("192.168.220.128:2181",50000,new ZookeeperSimpleAuthDelete());
    try{
      zooKeeper2.delete("/zly/dsg",-1);
    }catch (Exception e){
      e.printStackTrace();
    }
    zooKeeper2.addAuthInfo("digest", "foo:true".getBytes());
    zooKeeper2.delete("/zly/dsg", -1);
    ZooKeeper zooKeeper3=new ZooKeeper("192.168.220.128:2181",50000,new ZookeeperSimpleAuthDelete());
    zooKeeper3.delete("/zly", -1);
  }
  @Override
  public void process(WatchedEvent event) {
    System.out.println("receive watch event:" + event);
    if (event.getState()== Event.KeeperState.SyncConnected){
      countDownLatch.countDown();
    }
  }
}
