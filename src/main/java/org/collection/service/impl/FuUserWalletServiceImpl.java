package org.collection.service.impl;

import org.collection.dao.FuUserWalletDao;
import org.collection.entity.FuUserWallet;
import org.collection.service.FuUserWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FuUserWalletServiceImpl implements FuUserWalletService {

    @Autowired
    private FuUserWalletDao fuUserWalletDao;

    @Override
    public List<FuUserWallet> selectWallet(int start,int pageSize) {
        return fuUserWalletDao.selectWallet(start,pageSize);
    }

    @Override
    public int count() {
        return fuUserWalletDao.count();
    }
}
