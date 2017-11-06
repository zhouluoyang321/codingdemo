package com.example.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author z1006 zly
 * @version 创建时间：2017/11/3 10:50
 */
public class ZookeeperGetData implements Watcher {
  private static CountDownLatch countDownLatch=new CountDownLatch(1);
  private static ZooKeeper zooKeeper;
  private static Stat stat=new Stat();
  public static void main(String[] args) throws Exception{
    zooKeeper=new ZooKeeper("192.168.220.128:2181",5000,new ZookeeperGetData());
    countDownLatch.await();
    final String s = zooKeeper.create("/zly", "dsg".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    System.out.println("success create node:" + s);
    final byte[] data = zooKeeper.getData("/zly", true, stat);
    System.out.println(stat.getCzxid()+" "+stat.getMzxid()+" "+stat.getVersion());
    System.out.println("callback:"+new String(data));
    zooKeeper.setData("/zly","dsg".getBytes(),-1);
    Thread.sleep(Integer.MAX_VALUE);
    System.out.println("finish");
  }
  @Override
  public void process(WatchedEvent event) {
    System.out.println("receive watch event:"+event);
    if (event.getState()== Event.KeeperState.SyncConnected){
      if (Event.EventType.None==event.getType()&&null==event.getPath()){
        countDownLatch.countDown();
      }else if(event.getType()== Event.EventType.NodeDataChanged){
        try {
          final byte[] data = zooKeeper.getData(event.getPath(), true, stat);
          System.out.println(stat.getCzxid()+" "+stat.getMzxid()+" "+stat.getVersion());
          System.out.println("callback:"+new String(data));
        } catch (KeeperException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
