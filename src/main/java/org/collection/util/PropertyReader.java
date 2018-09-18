package org.collection.util;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyReader {
    private static Map<String, Map<String, String>> cache = new ConcurrentHashMap();

    public PropertyReader() {
    }

    public static String get(String key, String fileName) {
        if (cache.containsKey(fileName)) {
            Map<String, String> map = (Map)cache.get(fileName);
            return (String)map.get(key);
        } else {
            Map<String, String> map = new HashMap();
            Properties prop = new Properties();
            InputStream in = null;
            try {
                in = new FileInputStream(PropertyReader.class.getResource("/config/").getPath()+fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //PropertyReader.class.getResourceAsStream("/config/" + fileName);

            try {
                if (in != null) {
                    prop.load(in);
                }
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            Set<Entry<Object, Object>> set = prop.entrySet();
            Iterator it = set.iterator();

            while(it.hasNext()) {
                Entry<Object, Object> entry = (Entry)it.next();
                String k = entry.getKey().toString();
                String v = entry.getValue() == null ? null : entry.getValue().toString();
                map.put(k, v);
            }

            cache.put(fileName, map);
            return (String)map.get(key);
        }
    }
    public static Boolean set(String key,String path,String value){
        if(cache.get(path)==null){
            get(key,path);
            System.out.println("修改前重新加载一遍");
        }
        Properties prop = new Properties();
        System.out.println("获取添加或修改前的属性值："+key+"=" + cache.get(path).get(key));
        cache.get(path).put(key, value);
        Iterator it =cache.get(path).entrySet().iterator();
        while (it.hasNext()){
            Entry<Object, Object> entry = (Entry)it.next();
            prop.setProperty(entry.getKey().toString(),entry.getValue().toString());
        }
        // 文件输出流
        try {
            OutputStream fos =new FileOutputStream(PropertyReader.class.getResource("/config/").getPath()+path);
            // 将Properties集合保存到流中
            prop.store(fos,"Record the height of the Block");
            fos.close();// 关闭流
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        //System.out.println("获取添加或修改后的属性值："+key+"=" + cache.get(key));
        cache.remove(path);
        get(key,path);
        return true;
    }

}
