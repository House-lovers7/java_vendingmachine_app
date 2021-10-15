package com.example.vendingmachine.dao;

import com.example.vendingmachine.entity.Rack;
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
public interface RackDao {

    @Select
    List<Rack> selectAll();

    @Select
    Rack selectOne(String rackNo);

    @Insert
    int insert(Rack entity);

    @Update
    int update(Rack entity);

    @BatchUpdate
    int[] batchUpdate(List<Rack> entityList);

    @Delete(sqlFile = true)
    int truncate();
}
