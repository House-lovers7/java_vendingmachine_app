package com.example.vendingmachine.dao;


import com.example.vendingmachine.entity.Item;
import com.example.vendingmachine.entity.Rack;
import com.example.vendingmachine.entity.StockItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class StockItemDaoTest {

    @Autowired
    ItemDao itemDao;
    @Autowired
    RackDao rackDao;
    @Autowired
    StockItemDao stockItemDao;

    Item item1 = new Item();
    Item item2 = new Item();

    Rack rack1 = new Rack();
    Rack rack2 = new Rack();

    @Before
    public void before() {

        rackDao.truncate();
        itemDao.truncate();

        item1.setItemCd("01");
        item1.setItemName("コーラ");
        item1.setSellFlg("1");
        itemDao.insert(item1);

        item2.setItemCd("02");
        item2.setItemName("ペプシ");
        item2.setSellFlg("0");
        itemDao.insert(item2);

        rack1.setRackNo("1");
        rack1.setItemCd("01");
        rack1.setItemNumber(10);
        rack1.setCapacity(15);
        rack1.setPrice(110);
        rackDao.insert(rack1);

        rack2.setRackNo("2");
        rack2.setItemCd("02");
        rack2.setItemNumber(20);
        rack2.setCapacity(25);
        rack2.setPrice(120);
        rackDao.insert(rack2);
    }

    /**
     * Rack全件取得
     */
    @Test
    public void selectAll() {

        List<StockItem> actualStockItemList = stockItemDao.selectAll();

        assertThat(actualStockItemList.size(), is(2));

        StockItem actualStockItem1 = actualStockItemList.get(0);
        StockItem actualStockItem2 = actualStockItemList.get(1);

        assertThat(actualStockItem1.getRackNo(), is("1"));
        assertThat(actualStockItem1.getItemCd(), is("01"));
        assertThat(actualStockItem1.getItemName(), is("コーラ"));
        assertThat(actualStockItem1.getItemNumber(), is(10));
        assertThat(actualStockItem1.getPrice(), is(110));

        assertThat(actualStockItem2.getRackNo(), is("2"));
        assertThat(actualStockItem2.getItemCd(), is("02"));
        assertThat(actualStockItem2.getItemName(), is("ペプシ"));
        assertThat(actualStockItem2.getItemNumber(), is(20));
        assertThat(actualStockItem2.getPrice(), is(120));

    }
}
