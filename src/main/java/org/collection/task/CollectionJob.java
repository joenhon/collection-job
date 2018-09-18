package org.collection.task;

import com.alibaba.fastjson.JSONArray;
import org.apache.log4j.Logger;
import org.collection.entity.Account;
import org.collection.entity.FuUserWallet;
import org.collection.entity.UTXO;
import org.collection.service.FuUserWalletService;
import org.collection.util.Usdt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
@Component
@Lazy(false)
public class CollectionJob  {
    private static final int PAGESIZE = 100;
    @Autowired
    private FuUserWalletService fuUserWalletService;
    private Logger logger=Logger.getLogger(CollectionJob.class);

    /**
     * 归总工作第一层方法，获取数据库已分配地址传入下层方法
     */
    public void  collectionJob(){
        //待汇总集合 地址与余额
        Map<String,String> collectionAddress = new HashMap<>();
        //获取总数除以每页数量得到最大页码
        int totalSize = fuUserWalletService.count();
        int countPage = (totalSize + PAGESIZE -1) / PAGESIZE;
        //根据页码的到每页信息
        for (int i=1;i<=countPage;countPage++){
            int startIndex = (i-1) * PAGESIZE;
            List<FuUserWallet> list = fuUserWalletService.selectWallet(startIndex, PAGESIZE);
            for (int j=0;j<list.size();j++) {
                String va = Usdt.getBalance(list.get(j).getAddress());
                if (va != null && va != "") {
                    //当余额小于最低提现数量则不添加到待汇总集合
                    if (new BigDecimal(va).compareTo(Account.lowestAmount) >= 1) {
                        collectionAddress.put(Account.collectionAddress, va);
                    }
                }
            }
            //传入待汇总集合 开始汇总工作
            job(collectionAddress);
        }
    }

    /**
     *归总工作第二层方法，解析上层传入的集合并得到做手续费的UTXO传入下层方法
     * @param collectionAddress
     */
    public void job(Map<String, String> collectionAddress){
        //获得费率计算出手续费
        BigDecimal fee = BigDecimal.valueOf(404*Usdt.getFee()).divide(BigDecimal.valueOf(1e8));
        //可用于做手续费的UTXO集合
        List<UTXO> payList=UTXO(0,9999999,fee);
        if (payList.size()>0){
            fee = BigDecimal.valueOf(404*Usdt.getFee()).divide(BigDecimal.valueOf(1e8));
            Set<Map.Entry<String, String>> set = collectionAddress.entrySet();
            if(set!=null && set.size()>0) {
                for (Map.Entry<String, String> entry : set) {
                    collection(entry,payList,fee);
                }
            }
        }else {
            UTXO(0,9999999,fee);
            if (payList.size()==0){
                logger.error("No UTXO available for handling fees");
                return;
            }
        }

    }

    /**
     * 归总工作第三次方法，主要的逻辑和rpc调用，完成归总
     * @param entry
     * @param payList
     * @param fee
     */
    public void collection(Map.Entry<String, String> entry,List<UTXO> payList,BigDecimal fee){
        UTXO UTXO=null;
        List<UTXO> objects = JSONArray.parseArray(Usdt.listunspent(0,9999999,entry.getKey()), UTXO.class);
        if (objects.size()==0){
            logger.error("No Usdt available UTXO");
            return;
        }
        List<UTXO> utxos = new ArrayList<>();
        //获得Usdt账本详情
        for (UTXO utxo:objects) {
            if (!utxo.getTxid().equals(payList.get(0).getTxid())){
               utxos.add(utxo);
            }
        }
        JSONArray objects1 = new JSONArray();
        //创建支付Usdt账本
        UTXO UTXO1 = new UTXO();
        //创建支付手续费（BTC）账本
        UTXO UTXO2 = new UTXO();
        if (utxos.size() == 1) {
            //当用户只充值一次时
            UTXO1.setTxid(UTXO.getTxid());
            UTXO1.setVout(UTXO.getVout());
            UTXO2.setTxid(payList.get(0).getTxid());
            UTXO2.setVout(payList.get(0).getVout());

        }else if (utxos.size() > 1){
            //当用户充值多次时
            UTXO1.setTxid(utxos.get(0).getTxid());
            UTXO1.setVout(utxos.get(0).getVout());
            UTXO2.setTxid(utxos.get(1).getTxid());
            UTXO2.setVout(utxos.get(1).getVout());
            fee = BigDecimal.valueOf(0.00000546);
        }
        objects1.add(UTXO1);
        objects1.add(UTXO2);
        //构建交易基础 createRawTransaction 将将账本进行编译，createPayLoad将需要发送的Usdt编译（entry.getValue将发送所有的Usdt）
        String rawtx = Usdt.createRawTxOpreturn(Usdt.createRawTransaction(objects1), Usdt.createPayLoad("0.001"));//entry.getValue()

        //设置接收地址
        String rawtx2 = Usdt.createRawTxReference(rawtx, Account.collectionAddress);

        //重构账本，进行账本验证 UTXO1 支付Usdt账本 UTXO2 支付手续费（BTC）账本
        UTXO1.setScriptPubKey(UTXO.getScriptPubKey());
        UTXO1.setValue(UTXO.getAmount().toString());
        UTXO2.setScriptPubKey(payList.get(0).getScriptPubKey());
        UTXO2.setValue(payList.get(0).getAmount().toString());

        //设置发送地址和手续费（验证账本权限）
        //System.out.println( );
        String Hex = Usdt.createRawTxChange(rawtx2,Account.collectionAddress, fee, UTXO1, UTXO2);

        //签名事务交易
        Hex = Usdt.signRawTransaction(Hex);
        //发送事务交易
        String Hash = Usdt.sendrawtransaction(Hex);
        System.out.println(Hash);
        if (Hash == null || Hash.equals("")){
            payList.clear();
            while (payList.size()==0){
                try {
                    payList=UTXO(1,9999999,fee);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else {
            logger.info("CollectionHash:" + Hash);
            payList.remove(0);
            if (payList.size() == 0) {
                payList = UTXO(0, 9999999,fee);
                if (payList.size() == 0) {
                    logger.error("No UTXO available for handling fees");
                    return;
                }
            }
        }
    }
    /**
     * 获得UTXO集合
     * @param minconf
     * @param maxconf
     * @param fee
     * @return
     */
    public List  UTXO(int minconf,int maxconf,BigDecimal fee){

        //获取所有可使用UTXO
        List<UTXO> objects= JSONArray.parseArray(Usdt.listunspent(minconf,maxconf,Account.collectionAddress),UTXO.class);

        //可用于做手续费的UTXO集合
        List payList=new ArrayList<>();
        for (UTXO UTXO: objects) {
            if (UTXO.getAmount().compareTo(fee)==1){
                payList.add(UTXO);
                //System.out.println(JSON.toJSON(UTXO));
            }
        }
        return payList;
    }
}
