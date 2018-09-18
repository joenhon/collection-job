package org.collection;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainRunJob {

    public static void main(String[] args){
        //启动spring容器
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        synchronized (MainRunJob.class){
            try{
                MainRunJob.class.wait();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
