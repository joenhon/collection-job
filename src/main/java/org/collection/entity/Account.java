package org.collection.entity;

import org.collection.util.PropertyReader;

import java.math.BigDecimal;

public class Account {
    public final static String  path="HttpUtilAPI.properties";
    public final static String mode = PropertyReader.get("mode",path);
    public final static boolean isPeer = Boolean.valueOf(PropertyReader.get("isPeer",path));
    public final static String collectionAddress = PropertyReader.get("collectionAddress",path);
    public final static String type=PropertyReader.get("type",path);
    public final static int propertyid = Integer.parseInt(PropertyReader.get("propertyId",path));
    public final static  String url = PropertyReader.get("url",path);
    public final static String url2 = PropertyReader.get("url2",path);
    public final static String url3 = PropertyReader.get("url3",path);
    public final static  String username = PropertyReader.get("username",path);
    public final static  String password = PropertyReader.get("password",path);
    public final static String RESULT = PropertyReader.get("RESULT",path);
    public final static String METHOD_SEND_TO_ADDRESS = PropertyReader.get("METHOD_SEND_TO_ADDRESS",path);
    public final static String METHOD_GET_TRANSACTION = PropertyReader.get("METHOD_GET_TRANSACTION",path);
    public final static String METHOD_GET_BLOCK_COUNT = PropertyReader.get("METHOD_GET_BLOCK_COUNT",path);
    public final static String METHOD_NEW_ADDRESS = PropertyReader.get("METHOD_NEW_ADDRESS",path);
    public final static String METHOD_GET_BALANCE = PropertyReader.get("METHOD_GET_BALANCE",path);
    public final static String METHOD_GET_LISTBLOCKTRANSACTIONS = PropertyReader.get("METHOD_GET_LISTBLOCKTRANSACTIONS",path);
    public final static BigDecimal lowestAmount = new BigDecimal(PropertyReader.get("lowestAmount",path));
    public final static BigDecimal fee = new BigDecimal(PropertyReader.get("fee",path));
}
