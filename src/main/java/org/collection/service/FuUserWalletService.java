package org.collection.service;

import org.collection.entity.FuUserWallet;

import java.util.List;

public interface FuUserWalletService {
    public List<FuUserWallet> selectWallet(int start,int pageSize);
    public int count();
}
