package org.collection.dao;

import org.collection.entity.FuUserWallet;

import java.util.List;

public interface FuUserWalletDao {
public List<FuUserWallet> selectWallet(int start,int pageSize);
public int count();
}
