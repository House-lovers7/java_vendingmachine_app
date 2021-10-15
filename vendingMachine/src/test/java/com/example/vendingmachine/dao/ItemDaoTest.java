package com.example.vendingmachine.dao;


import com.example.vendingmachine.entity.Item;
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
public class ItemDaoTest {

    @Autowired
    ItemDao itemDao;

    Item item1 = new Item();
    Item item2 = new Item();

    @Before
    public void before() {

        itemDao.truncate();

        item1.setItemCd("01");
        item1.setItemName("コーラ");
        item1.setSellFlg("1");
        itemDao.insert(item1);

        item2.setItemCd("02");
        item2.setItemName("ペプシ");
        item2.setSellFlg("0");
        itemDao.insert(item2);
    }

    /**
     * Item全件取得
     */
    @Test
    public void selectAll() {

        List<Item> actualItemList = itemDao.selectAll();

        assertThat(actualItemList.size(), is(2));

        Item actualItem1 = actualItemList.get(0);
        Item actualItem2 = actualItemList.get(1);

        assertThat(actualItem1.getItemCd(), is("01"));
        assertThat(actualItem1.getItemName(), is("コーラ"));
        assertThat(actualItem1.getSellFlg(), is("1"));

        assertThat(actualItem2.getItemCd(), is("02"));
        assertThat(actualItem2.getItemName(), is("ペプシ"));
        assertThat(actualItem2.getSellFlg(), is("0"));
    }

    /**
     * 販売中Itemのみ取得
     */
    @Test
    public void selectOnSale() {
        List<Item> actualItemList = itemDao.selectOnSale();
        assertThat(actualItemList.size(), is(1));

        Item actualItem1 = actualItemList.get(0);

        assertThat(actualItem1.getItemCd(), is("01"));
        assertThat(actualItem1.getItemName(), is("コーラ"));
        assertThat(actualItem1.getSellFlg(), is("1"));
    }

    /**
     * ItemName取得
     */
    @Test
    public void selectItemName() {
        String actualItemName = itemDao.selectItemName("01");
        assertThat(actualItemName, is("コーラ"));
    }
}
