package com.example.vendingmachine.service;

import com.example.vendingmachine.dao.RackDao;
import com.example.vendingmachine.dao.ItemDao;
import com.example.vendingmachine.dao.StockItemDao;
import com.example.vendingmachine.dto.EditStockItemDto;
import com.example.vendingmachine.entity.Item;
import com.example.vendingmachine.entity.Rack;
import com.example.vendingmachine.entity.StockItem;
import com.example.vendingmachine.exception.ServiceValidationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;


@RunWith(Enclosed.class)
@SpringBootTest
public class RackServiceTest {

    /**
     * fetchStockItemListのテスト
     */
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest
    public static class FetchStockItemListTest {
        @Autowired
        RackService rackService;
        @SpyBean
        StockItemDao stockItemDao;

        StockItem item1 = new StockItem();
        StockItem item2 = new StockItem();
        List<StockItem> mockStockItemList = new ArrayList<>();

        @Before
        public void before() throws Exception {

            //テストデータをセット
            item1.setRackNo("01");
            item1.setItemCd("00001");
            item1.setItemName("コーラ");
            item1.setItemNumber(01);
            item1.setPrice(100);

            item2.setRackNo("02");
            item2.setItemCd("00002");
            item2.setItemName("ペプシ");
            item2.setItemNumber(02);
            item2.setPrice(200);

            mockStockItemList.add(item1);
            mockStockItemList.add(item2);

        }

        /**
         * 正常系
         * 商品マスタ全件取得
         */
        @Test
        public void fetchStockItemList() {
            when(stockItemDao.selectAll()).thenReturn(mockStockItemList);
            List<StockItem> actualStockItemList = rackService.fetchStockItemList();
            assertThat(actualStockItemList.size(), is(2));
            assertThat(actualStockItemList.get(0).getRackNo(), is("01"));
            assertThat(actualStockItemList.get(0).getItemCd(), is("00001"));
            assertThat(actualStockItemList.get(0).getItemName(), is("コーラ"));
            assertThat(actualStockItemList.get(0).getItemNumber(), is(01));
            assertThat(actualStockItemList.get(0).getPrice(), is(100));
            assertThat(actualStockItemList.get(1).getRackNo(), is("02"));
            assertThat(actualStockItemList.get(1).getItemCd(), is("00002"));
            assertThat(actualStockItemList.get(1).getItemName(), is("ペプシ"));
            assertThat(actualStockItemList.get(1).getItemNumber(), is(02));
            assertThat(actualStockItemList.get(1).getPrice(), is(200));

        }

    }
    /**
     * callOneItemのテスト
     */
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest
    public static class callOneItemTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();
        @Autowired
        RackService rackService;
        @SpyBean
        RackDao rackDao;
        @SpyBean
        ItemDao itemDao;

        Rack rack1 = new Rack();
        Rack rack2 = new Rack();
        Item item1 = new Item();
        Item item2 = new Item();

        @Before
        public void before() throws Exception {

            //テストデータをセット
            //rak1のテストデータ
            rack1.setRackNo("01");
            rack1.setItemCd("00001");
            rack1.setItemNumber(1);
            rack1.setCapacity(10);
            rack1.setPrice(100);
            //rak2のテストデータ
            rack2.setRackNo("02");
            rack2.setItemCd("00002");
            rack2.setItemNumber(0);
            rack2.setCapacity(10);
            rack2.setPrice(100);
            //Itemテーブルのテストデータ
            item1.setItemCd("00001");
            item1.setItemName("コーラ");
            item1.setSellFlg("1");
            item2.setItemCd("00002");
            item2.setItemName("ペプシ");
            item2.setSellFlg("1");
        }

        /**
         * 正常系
         * 指定されたラックNoをもとに、商品数を１つ減らす
         */
        @Test
        public void callOneItemTest() {
            //商品名と商品数（1つ減っているか）を確認
            ArgumentCaptor<Rack> captor = ArgumentCaptor.forClass(Rack.class);
            doReturn(rack1).when(rackDao).selectOne(rack1.getRackNo());
            doReturn("コーラ").when(itemDao).selectItemName(rack1.getItemCd());
            doReturn(1).when(rackDao).update(rack1);
            String itemName = rackService.callOneItem("01");
            verify(rackDao, times(1)).update(captor.capture());
            assertThat(itemName, is("コーラ"));
            assertThat(rack1.getItemNumber(), is(0));
        }

        /**
         * 異常系
         * 指定されたラックNoをもとに、商品数を１つ減らす
         * 商品が売り切れの場合
         */
        @Test
        public void callOneItemSoldOutTest() {
            doReturn(rack2).when(rackDao).selectOne("02");
            doReturn(item2.getItemName()).when(itemDao).selectItemName(rack2.getItemCd());
            // 例外クラスと例外メッセージの検証
            expectedException.expect(ServiceValidationException.class);
            expectedException.expectMessage("E_SERVICE_001");
            //商品数が0のラックNoをさせてエラーを発生させる
            rackService.callOneItem("02");
        }

        /**
         * 異常系
         * 指定されたラックNoをもとに、商品数を１つ減らす
         * rackテーブルにデータがない場合
         */
        @Test
        public void callOneItemNoRackDataTest() {
            // 例外クラスと例外メッセージの検証
            doReturn(null).when(rackDao).selectOne(any());
            expectedException.expect(ServiceValidationException.class);
            expectedException.expectMessage("E_SERVICE_006");
            //rackテーブルにデータがないラックNoを指定してエラーを発生させる
            rackService.callOneItem("03");
        }

        /**
         * 異常系
         * 指定されたラックNoをもとに、商品数を１つ減らす
         * itemテーブルにデータがない場合
         */
        @Test
        public void callOneItemNoItemDataTest() {
            Rack rack3 = new Rack();
            rack3.setRackNo("03");
            rack3.setItemCd("00003");
            rack3.setItemNumber(1);
            rack3.setCapacity(10);
            rack3.setPrice(100);
            doReturn(rack3).when(rackDao).selectOne(rack3.getRackNo());
            doReturn(null).when(itemDao).selectItemName(rack3.getItemCd());
            //itemテーブルにデータがないラックNoを指定してエラーを発生させる
            // 例外クラスと例外メッセージの検証
            expectedException.expect(ServiceValidationException.class);
            expectedException.expectMessage("E_SERVICE_007");
            rackService.callOneItem("03");
        }
    }

    /**
     * ラック商品編集のテスト
     */
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest
    @Transactional
    public static class EditStockItemTest {
        @Rule
        public ExpectedException expectedException = ExpectedException.none();
        @Autowired
        RackService rackService;
        @SpyBean
        RackDao rackDao;

        Rack rack1 = new Rack();
        Rack rack2 = new Rack();
        List<Rack> mockRackList = new ArrayList<Rack>();

        @Before
        public void before() throws Exception {
            //テストデータをセット
            //Rackテーブル
            rack1.setRackNo("01");
            rack1.setItemCd("00001");
            rack1.setItemNumber(1);
            rack1.setCapacity(10);
            rack1.setPrice(100);
            rack2.setRackNo("02");
            rack2.setItemCd("00002");
            rack2.setItemNumber(0);
            rack2.setCapacity(99);
            rack2.setPrice(100);
            mockRackList.add(rack1);
            mockRackList.add(rack2);
        }

        /**
         * 正常系
         * 指定されたラックNoをもとに、商品数を１つ減らす
         */
        @Test
        public void editRackItemDtoListTest() {
            List<EditStockItemDto> editStockItemDtoList = new ArrayList<>();
            EditStockItemDto editStockItemDto1 = new EditStockItemDto();
            editStockItemDto1.setRackNo("01");
            editStockItemDto1.setItemCd("10000");
            editStockItemDto1.setItemNumber(5);
            editStockItemDto1.setPrice(500);
            EditStockItemDto editStockItemDto2 = new EditStockItemDto();
            editStockItemDto2.setRackNo("02");
            editStockItemDto2.setItemCd("20000");
            editStockItemDto2.setItemNumber(99);
            editStockItemDto2.setPrice(999);
            editStockItemDtoList.add(editStockItemDto1);
            editStockItemDtoList.add(editStockItemDto2);
            //変更された数と変更内容を確認
            when(rackDao.selectAll()).thenReturn(mockRackList);
            doReturn(new int[]{1, 1}).when(rackDao).batchUpdate(any());
            int editNumber = rackService.editRackItemDtoList(editStockItemDtoList);

            ArgumentCaptor<Rack> captor = ArgumentCaptor.forClass(Rack.class);
            verify(rackDao, times(1)).batchUpdate(Collections.singletonList(captor.capture()));
            assertThat(mockRackList.get(0).getRackNo(), is(editStockItemDto1.getRackNo()));
            assertThat(editNumber, is(editStockItemDtoList.size()));
        }


        /**
         * 異常系
         * 商品数が格納可能数を超えている場合
         */
        @Test
        public void editRackItemDtoListItemNumberOverTest() {
            // 例外クラスと例外メッセージの検証
            expectedException.expect(ServiceValidationException.class);
            expectedException.expectMessage("E_SERVICE_002");
            ArgumentCaptor<Rack> captor = ArgumentCaptor.forClass(Rack.class);
            doReturn(new int[]{1, 1}).when(rackDao).batchUpdate(any());

            List<EditStockItemDto> editStockItemDtoList = new ArrayList<>();
            EditStockItemDto editStockItemDto1 = new EditStockItemDto();
            editStockItemDto1.setRackNo("01");
            editStockItemDto1.setItemCd("10000");
            editStockItemDto1.setItemNumber(11);
            editStockItemDto1.setPrice(500);
            editStockItemDtoList.add(editStockItemDto1);
            //変更された数と変更内容を確認
            int editNumber = rackService.editRackItemDtoList(editStockItemDtoList);

        }
    }
}

