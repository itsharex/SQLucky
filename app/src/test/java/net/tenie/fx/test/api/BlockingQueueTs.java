package net.tenie.fx.test.api;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTs{
    public static void main(String[] args)throws Exception{
        //创建一个容量为1的BlockingQueue

        BlockingQueue<String> b=new ArrayBlockingQueue<>(1);
        //启动3个生产者线程
        new Producer(b).start();
        new Producer(b).start();
        new Producer(b).start();
        //启动一个消费者线程
        new Consumer(b).start();

    }
}
class Producer extends Thread{
    private BlockingQueue<String> b;

    public Producer(BlockingQueue<String> b){
        this.b=b;

    }
    public synchronized void run(){
        String [] str=new String[]{
            "java",
            "struts",
            "Spring"
        };
        for(int i=0;i<9999999;i++){
            System.out.println(getName()+"生产者准备生产集合元素！");
            try{

                b.put(str[i%3]);
                sleep(1000);
                //尝试放入元素，如果队列已满，则线程被阻塞

            }catch(Exception e){
            	e.printStackTrace();
            	}
            System.out.println(getName()+"生产完成："+b);
        }

    }
}
class Consumer extends Thread{
    private BlockingQueue<String> b;
    public Consumer(BlockingQueue<String> b){
        this.b=b;
    }
    public  synchronized  void run(){

        while(true){
            System.out.println(getName()+"消费者准备消费集合元素！");
            try{
                sleep(1000);
                //尝试取出元素，如果队列已空，则线程被阻塞
                b.take();
            }catch(Exception e){
            	e.printStackTrace();
            	}
            System.out.println(getName()+"消费完："+b);
        }

    }
}