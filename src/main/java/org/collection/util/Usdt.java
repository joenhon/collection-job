package org.collection.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.collection.entity.Account;
import org.collection.entity.BTCToken;
import org.collection.entity.Fee;
import org.collection.entity.UTXO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;



public class Usdt {
    private static final Logger log = Logger.getLogger(Usdt.class);
    private static final Map<String,String> headers = new HashMap<>(2);
    static {
        headers.put("Content-Type","application/x-www-form-urlencoded");
    }

    /**
     * 获取费率
     * @return
     */
    public static int getFee(){
        try{
            Fee fee=JSON.parseObject(HttpUtil.get(Account.url3),Fee.class);
            switch (Account.mode){
                case "fastestFee":
                    return fee.getFastestFee().intValue();
                case "halfHourFee":
                    return fee.getHalfHourFee().intValue();
                default:
                    return fee.getHourFee().intValue();
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }
    /**
     * 解锁钱包
     * @param pwd
     * @param timeOut 解锁时间，以秒为单位
     * @return
     */
    public static String unlock(String pwd,Long timeOut){
        JSONObject json= doRequest("walletpassphrase",pwd,timeOut);
        if (isError(json)){
            log.error(json.get("error"));
            return "";
        }
        return json.getString(Account.RESULT);
    }
    /**
     * USDT查询余额
     * @return
     * */
     public static String getBalance(String address){
         JSONObject json = null;
         if (Account.isPeer){
             try {
                 json = doRequest(Account.METHOD_GET_BALANCE,address, Account.propertyid);
             }catch (Exception e){
                 log.error("Parameter error or wrong address");
                 return null;
             }

             if(isError(json)){
                 log.error(json.get("error"));
                 return null;
             }
             return JSONObject.parseObject(json.getString(Account.RESULT)).getString("balance");
         }else {
             json=JSONObject.parseObject(HttpUtil.jsonPost(Account.url2+"/v1/address/addr/",headers,"addr="+address));
             System.out.println(json);
             JSONArray  pa=json.getJSONArray("balance");
             Iterator<Object> a=pa.iterator();
             while (a.hasNext()){
                 //JSONObject token=JSONObject.parseObject(a.next().toString());
                 BTCToken token= JSONObject.parseObject(a.next().toString(),BTCToken.class);
                 if (token.getId().equals(String.valueOf(Account.propertyid))){
                     return token.getValue();
                 }
             }
         }
         return null;
     }

    /**
     * 获取地址下没有用完的UTXO(1)
     * @param minconf
     * @param maxconf
     * @param params
     * @return
     */
    public static String listunspent(int minconf,int maxconf,Object... params){
        JSONObject json=doRequest("listunspent",minconf,maxconf,params);
        if (isError(json)){
            log.error(json.get("error"));
            return "";
        }
        return json.getString(Account.RESULT);
    }

    /**
     *创建有效负载(2)
     * @param amount
     * @return
     */
    public static String createPayLoad(String amount){
        JSONObject json=doRequest("omni_createpayload_simplesend",Account.propertyid,amount);
        if (isError(json)){
            log.error(json.get("error"));
            return "";
        }
        return json.getString(Account.RESULT);
    }

    /**
     *构建交易基础(3)
     * @param params
     * @return
     */
    public static String createRawTransaction(JSONArray params){
        JSONObject json = doRequest("createrawtransaction",params,new UTXO());
        if (isError(json)){
            log.error(json.get("error"));
            return "";
        }
        return json.getString(Account.RESULT);
    }

    /**
     *附加有效载荷输出(4)
     * @param rawtx
     * @param payload
     * @return
     */
    public static String createRawTxOpreturn(String rawtx,String payload){
        JSONObject json= doRequest("omni_createrawtx_opreturn",rawtx,payload);
        if (isError(json)){
            log.error(json.get("error"));
            return "";
        }
        return json.getString(Account.RESULT);
    }

    /**
     * 连接参考/接收器输出(5)
     * @param rawtx
     * @param toAddress
     * @return
     */
    public static String createRawTxReference(String rawtx,String toAddress){
        JSONObject json= doRequest("omni_createrawtx_reference",rawtx,toAddress);
        if (isError(json)){
            log.error(json.get("error"));
            return "";
        }
        return json.getString(Account.RESULT);
    }

    /**
     *指定矿工费并附加变更输出(6)
     * @param rawtx
     * @param changeAddress
     * @param fee
     * @param params
     * @return
     */
    public static String createRawTxChange(String rawtx,String changeAddress,BigDecimal fee,Object... params){
        JSONObject json= doRequest("omni_createrawtx_change",rawtx,params,changeAddress,fee);
        if (isError(json)){
            log.error(json.get("error"));
            return "";
        }
        return json.getString(Account.RESULT);
    }

    /**
     *签署交易(7)
     * @param Hex
     * @return
     */
    public static String signRawTransaction(String Hex){
        JSONObject json= doRequest("signrawtransaction",Hex);
        if (isError(json)){
            log.error(json.get("error"));
            return "";
        }
        return json.getJSONObject(Account.RESULT).getString("hex");
    }

    /**
     * 广播交易(8)
     * @param data
     * @return
     */
    public static String sendrawtransaction(String data){
        JSONObject json= doRequest("sendrawtransaction",data);
        if (isError(json)){
            log.error(json.get("error"));
            System.out.println(json.get("error"));
            return "";
        }
        return json.getString(Account.RESULT);
    }
    /**
     * 创建地址
     * @return
     */
    public static  String newAddress(){
        JSONObject json = doRequest(Account.METHOD_NEW_ADDRESS);
        if(isError(json)){
            log.error(json.get("error"));
            return "";
        }
        return json.getString(Account.RESULT);

    }

    /**
     * 查询所有默认创建地址
     * @return
     */
    public static  String getAddresses(){
        JSONObject json = doRequest("getaddressesbyaccount","");
        if(isError(json)){
            log.error(json.get("error"));
            return "";
        }
        return json.getString(Account.RESULT);
    }

    /**
     * 得到当前区块高度
     * @return
     */
    public static  int getBlockCount(){
        JSONObject json=doRequest("getblockcount");
        if(isError(json)){
            log.error(json.get("error"));
            return 0;
        }
        return Integer.parseInt(json.getString(Account.RESULT));
    }

    /**
     *获取指定高度的区块hash
     * @param index
     * @return
     */
    public static String getBlockHash(int index){
        //usdt无法使用
        JSONObject json=doRequest("getblockhash",index);
        if(isError(json)){
            log.error(json.get("error"));
            return null;
        }
        return json.getString(Account.RESULT);
    }

    /**
     * 获得指定区块高度的utds交易hash
     * @param index
     * @return
     */
    public static  boolean parseBlock(int index) {
        //doRequest("omni_listblocktransactions",279007);
        //{"result":["63d7e22de0cf4c0b7fd60b4b2c9f4b4b781f7fdb8be4bcaed870a8b407b90cf1","6fb25ab84189d136b95d7f733b0659fa5fbd63f476fb1bca340fb4f93de6c912","d54213046d8be80c44258230dd3689da11fdcda5b167f7d10c4f169bd23d1c01"],"id":"1521454868826"}
        JSONObject jsonBlock = doRequest(Account.METHOD_GET_LISTBLOCKTRANSACTIONS, index);
        if (isError(jsonBlock)) {
            log.error("accessing USDT Error,"+jsonBlock.get("error"));
            return false;
        }
        JSONArray jsonArrayTx = jsonBlock.getJSONArray(Account.RESULT);
        if (jsonArrayTx == null || jsonArrayTx.size() == 0) {
            //没有交易
            log.info("Block："+index+",There is no deal");
            return true;
        }
        Iterator<Object> iteratorTxs = jsonArrayTx.iterator();
        while(iteratorTxs.hasNext()){
            String txid = (String) iteratorTxs.next();
            parseTx(txid,1);
        }
        return true;
    }

    /**
     * 获得交易hash详情进行辨别
     * @param txid
     * @param isNew
     * @return
     */
    public static String parseTx(String txid,int isNew){
        /**
         *{"result":{"amount":"50.00000000","divisible":true,"fee":"0.00000257",
         *"txid":"f76d51044f156e6ed84c11e6531db1d6d70799196522c07bd2a8870a21f90220","ismine":true,
         *"type":"Simple Send","confirmations":565,"version":0,"sendingaddress":"mh8tV2mfDa6yHK76t68N3paoGdSmangJDi",
         *"valid":true,"blockhash":"000000000000014cdef6ee8a095b58755efebf913b1ab13bb23adaa33b6f7b05",
         *"blocktime":1523528971,"positioninblock":189,"referenceaddress":"mg5yVUSwGNEJNhYKfyETV2udWok6Q4pgLx",
         *"block":1292526,"propertyid":2,"type_int":0},"id":"1523860978684"}
         */
        JSONObject jsonTransaction = null;
        //if (Account.isPeer) {
            jsonTransaction = doRequest(Account.METHOD_GET_TRANSACTION, txid);
       /* }else {
            jsonTransaction = JSONObject.parseObject(HttpUtil.get(Account.url2+"/v1/transaction/tx/"+txid,null,headers));
        }*/
        if(isError(jsonTransaction)) {
            log.error("Handling USDT tx error"+jsonTransaction.get("error"));
            return "处理USDT tx出错";
        }
        JSONObject jsonTResult = null;
        //if (Account.isPeer) {
            jsonTResult = jsonTransaction.getJSONObject(Account.RESULT);
        /*}else {
            jsonTResult = jsonTransaction;
        }*/
        if (!jsonTResult.getBoolean("valid")) {
            log.info("Not valid data");
            return "不是有效数据";
        }
        int propertyidResult = jsonTResult.getIntValue("propertyid");
        if (propertyidResult!= Account.propertyid) {
            log.info("non USDT data");
            return "非USDT数据";
        }
        //int coinfirm = jsonTResult.getIntValue("confirmations");
        double value = jsonTResult.getDouble("amount");
        if(value >0) {
            String address = jsonTResult.getString("referenceaddress");
            System.out.println("收币地址：" + address);
            //判断是为分配地址
        }
        return jsonTResult.getString("confirmations");
    }


    /**
     *
     * @param json
     * @return
     */
    public static  boolean isError(JSONObject json){
        if( json == null || (StringUtils.isNotEmpty(json.getString("error")) && json.get("error") != "null")){
            return true;
        }
        return false;
    }

    public static  JSONObject doRequest(String method,Object... params){
        JSONObject param = new JSONObject();
        param.put("id",System.currentTimeMillis()+"");
        param.put("jsonrpc","2.0");
        param.put("method",method);
        if(params != null){
            param.put("params",params);
        }
        String creb = Base64.encodeBase64String((Account.username+":"+ Account.password).getBytes());
        Map<String,String> headers = new HashMap<>(2);
        headers.put("Authorization","Basic "+creb);
        headers.put("Content-Type","application/json");
        String resp = "";
        if (Account.METHOD_GET_TRANSACTION.equals(method)){
            try{
                resp = HttpUtil.jsonPost(Account.url,headers,param.toJSONString());
            }catch (Exception e){
                if (e instanceof IOException){
                    resp = "{}";
                }
            }
        }else{
            //System.out.println(param.toJSONString());
            resp = HttpUtil.jsonPost(Account.url,headers,param.toJSONString());
        }
        return JSON.parseObject(resp);
    }

}
