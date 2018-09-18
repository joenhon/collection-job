package org.collection.dao.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.collection.dao.FuUserWalletDao;
import org.collection.entity.FuUserWallet;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FuUserWalletDaoImpl implements FuUserWalletDao {
    private Logger logger = Logger.getLogger(FuUserWalletDaoImpl.class);
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Override
    public List<FuUserWallet> selectWallet(int start,int pageSize) {
        Map<String,Object> map=new HashMap<>();
        map.put("start",start);
        map.put("end",start+pageSize);
        try{
            return sqlSessionTemplate.selectList("selectWallet",map);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public int count() {
        try{
            return sqlSessionTemplate.selectOne("selectCount");
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
            return 0;
        }
    }
}
