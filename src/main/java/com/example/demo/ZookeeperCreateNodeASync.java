package com.example.demo;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * @author z1006 zly
 * @version 创建时间：2017/11/3 10:50
 */
public class ZookeeperCreateNodeASync implements Watcher {
  private static CountDownLatch countDownLatch=new CountDownLatch(1);
  public static void main(String[] args) throws Exception{
    ZooKeeper zooKeeper=new ZooKeeper("192.168.220.128:2181",5000,new ZookeeperCreateNodeASync());
    countDownLatch.await();
    zooKeeper.create("/zly", "dsg".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new IStringCallBack(), "I am zly.");
    zooKeeper.create("/zly", "dsg".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new IStringCallBack(), "I am zly.");
    zooKeeper.create("/zly", "dsg".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,new IStringCallBack(),"I am zly.");
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
class IStringCallBack implements  AsyncCallback.StringCallback{
  @Override
  /*
  * rc result code
  * path 路径
  * ctx 文本内容
  * name 真实路径
  * */
  public void processResult(int rc, String path, Object ctx, String name) {
    System.out.println("rc:"+rc+",path:"+path+", "+ctx+", real path name:"+name);
  }
}