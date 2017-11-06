package com.example.demo;

import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author z1006 zly
 * @version 创建时间：2017/11/3 10:50
 */
public class ZookeeperGetChildren implements Watcher {
  private static CountDownLatch countDownLatch=new CountDownLatch(1);
  private static ZooKeeper zooKeeper;
  public static void main(String[] args) throws Exception{
    zooKeeper=new ZooKeeper("192.168.220.128:2181",5000,new ZookeeperGetChildren());
    countDownLatch.await();
    final String s = zooKeeper.create("/zly", "dsg".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    System.out.println("success create node:" + s);
    final String s2 = zooKeeper.create("/zly/c1", "dsg".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    System.out.println("success create node:" + s2);
    //设置为true后，children变化会触发 创建zookeeper时候注册的监听器，或者说回调机制。
    //watcher是一次性的
    final List<String> children = zooKeeper.getChildren("/zly", true);
    System.out.println(children);
    final String s3 = zooKeeper.create("/zly/c2", "dsg".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    System.out.println("success create node:" + s3);
    final String s4 = zooKeeper.create("/zly/c3", "dsg".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    System.out.println("success create node:" + s4);
    Thread.sleep(Integer.MAX_VALUE);
    System.out.println("finish");
  }
  @Override
  public void process(WatchedEvent event) {
    System.out.println("receive watch event:"+event);
    if (event.getState()== Event.KeeperState.SyncConnected){
      if (Event.EventType.None==event.getType()&&null==event.getPath()){
        countDownLatch.countDown();
      }else if(event.getType()== Event.EventType.NodeChildrenChanged){
        try {
          //为true反复注册
          final List<String> children = zooKeeper.getChildren(event.getPath(), false);
          System.out.println("callback:"+children);
        } catch (KeeperException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
