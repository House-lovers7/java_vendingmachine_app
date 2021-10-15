package com.example.vendingmachine.dao;

import com.example.vendingmachine.entity.Item;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@ConfigAutowireable
@Dao
public interface ItemDao {

    @Select
    List<Item> selectAll();

    @Select
    List<Item> selectOnSale();

    @Select
    String selectItemName(String itemCd);

    @Insert
    int insert(Item entity);

    @Select
    int selectTargetCount(List<String> allItemCd);

    @Update
    int update(Item entity);

    @BatchUpdate
    int[] batchUpdate(List<Item> entityList);

    @Delete(sqlFile = true)
    int truncate();
}
