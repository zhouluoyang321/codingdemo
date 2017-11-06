package com.example.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @author z1006 zly
 * @version 创建时间：2017/11/3 10:50
 */
public class ZookeeperIsExistNode implements Watcher {
  private static CountDownLatch countDownLatch=new CountDownLatch(1);
  private static ZooKeeper zooKeeper;
  public static void main(String[] args) throws Exception{
    zooKeeper=new ZooKeeper("192.168.220.128:2181",5000,new ZookeeperIsExistNode());
    countDownLatch.await();

    zooKeeper.exists("/zly", true);
    final String s = zooKeeper.create("/zly", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    zooKeeper.setData("/zly", "dsg".getBytes(), -1);
    final String s1 = zooKeeper.create("/zly/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    zooKeeper.delete("/zly/c1",-1);
    zooKeeper.delete("/zly",-1);
    System.out.println("finish");
    Thread.sleep(Integer.MAX_VALUE);
  }
  @Override
  public void process(WatchedEvent event) {
    System.out.println("receive watch event:"+event);
    if (event.getState()== Event.KeeperState.SyncConnected){
      if (Event.EventType.None==event.getType()&&null==event.getPath()){
        countDownLatch.countDown();
      }else if (Event.EventType.NodeCreated==event.getType()){
        try {
          zooKeeper.exists("/zly", true);
          System.out.println("node create");
        } catch (KeeperException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }else if (Event.EventType.NodeDeleted==event.getType()){
        try {
          zooKeeper.exists("/zly", true);
          System.out.println("node deleted");
        } catch (KeeperException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      else if (Event.EventType.NodeDataChanged==event.getType()){
        try {
          System.out.println("nodedata changed");
          zooKeeper.exists("/zly", true);
        } catch (KeeperException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
