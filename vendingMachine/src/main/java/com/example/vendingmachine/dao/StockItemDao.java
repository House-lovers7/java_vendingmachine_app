package com.example.vendingmachine.dao;

import com.example.vendingmachine.entity.StockItem;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@ConfigAutowireable
@Dao
public interface StockItemDao {

    @Select
    List<StockItem> selectAll();
}
