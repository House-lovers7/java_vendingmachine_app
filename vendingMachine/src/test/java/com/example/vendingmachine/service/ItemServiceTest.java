package com.example.vendingmachine.service;
import com.example.vendingmachine.dao.ItemDao;
import com.example.vendingmachine.json.ItemAddInputJson;
import com.example.vendingmachine.entity.Item;
import com.example.vendingmachine.exception.ServiceValidationException;
import com.example.vendingmachine.mapper.FetchOnSaleMapper;
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
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;

@RunWith(Enclosed.class)
@SpringBootTest
public class ItemServiceTest {

    /**
     * {@link ItemService#fetchItemList()} メソッドテストクラス
     */
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest
    @Transactional
    public static class FetchItemListTest {

        @Autowired
        ItemService itemService;
        @SpyBean
        ItemDao itemDao;

        Item item1 = new Item();
        Item item2 = new Item();
        List<Item> itemList = new ArrayList<>();

        @Before
        public void before() throws Exception{

            item1.setItemCd("01");
            item1.setItemName("コーラ");
            item1.setSellFlg("1");

            item2.setItemCd("02");
            item2.setItemName("ペプシ");
            item2.setSellFlg("1");

            itemList.add(item1);
            itemList.add(item2);
        }


        /**
         * 正常系
         * 商品マスタ全件取得
         */
        @Test
        public void fetchItemList() {

            when(itemDao.selectAll()).thenReturn(itemList);

            List<Item> actualItemList = itemService.fetchItemList();
            assertThat(actualItemList.size(), is(2));
            assertThat(actualItemList.get(0).getItemCd(), is("01"));
            assertThat(actualItemList.get(0).getItemName(), is("コーラ"));
            assertThat(actualItemList.get(0).getSellFlg(), is("1"));
            assertThat(actualItemList.get(1).getItemCd(), is("02"));
            assertThat(actualItemList.get(1).getItemName(), is("ペプシ"));
            assertThat(actualItemList.get(1).getSellFlg(), is("1"));
        }
    }

    /**
     * {@link ItemService#addItem(String, String)} メソッドテストクラス
     */
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest
    @Transactional
    public static class AddItemTest {

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Autowired
        ItemService itemService;
        @SpyBean
        ItemDao itemDao;

        Item item1 = new Item();
        Item item2 = new Item();
        List<Item> itemList = new ArrayList<>();

        @Before
        public void before() {

            item1.setItemCd("01");
            item1.setItemName("コーラ");
            item1.setSellFlg("1");

            item2.setItemCd("02");
            item2.setItemName("ペプシ");
            item2.setSellFlg("1");

            itemList.add(item1);
            itemList.add(item2);
        }

        /**
         * 正常系
         * 商品マスタに商品追加
         */
        @Test
        public void addSuccessfully() {
            Item addItem = new Item();
            addItem.setItemCd("99");
            addItem.setItemName("アンバサ");
            addItem.setSellFlg("0");

            int addedNumber = itemService.addItem("99", "アンバサ");
            verify(itemDao).insert(addItem);
            assertThat(addedNumber, is(1));

        }

        /**
         * 異常系
         * 商品CDがDBに登録されている商品と重複する場合
         */
        @Test
        public void addItemCdDuplication() {
            doReturn(itemList).when(itemDao).selectAll();
            // 例外クラスと例外メッセージの検証
            expectedException.expect(ServiceValidationException.class);
            expectedException.expectMessage("E_SERVICE_003");
            //商品CDを重複させてエラーを発生させる
            itemService.addItem("01", "ポカリ");
        }

        /**
         * 異常系
         * 商品名がDBに登録されている商品と重複する場合
         */
        @Test
        public void addItemNameDuplication() {
            doReturn(itemList).when(itemDao).selectAll();
            //例外クラスと例外メッセージの検証
            expectedException.expect(ServiceValidationException.class);
            expectedException.expectMessage("E_SERVICE_004");
            //商品名を重複させてエラーを発生させる
            itemService.addItem("99", "コーラ");
        }
    }

    /**
     * {@link ItemService#fetchItemsOnSaleList()} メソッドテストクラス
     */
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest
    @Transactional
    public static class fetchOnSaleCdNamePeerListTest {

        @Autowired
        ItemService itemService;
        @SpyBean
        ItemDao itemDao;

        Item item1 = new Item();
        Item item2 = new Item();
        List<Item> itemList = new ArrayList<>();
        @Before
        public void before() {

            item1.setItemCd("01");
            item1.setItemName("コーラ");
            item1.setSellFlg("1");

            item2.setItemCd("02");
            item2.setItemName("ペプシ");
            item2.setSellFlg("1");

            itemList.add(item1);
            itemList.add(item2);
        }

        /**
         * 正常系
         * 販売中商品のコード＆名前取得
         */
        @Test
        public void fetchSuccessfully() {
            when(itemDao.selectOnSale()).thenReturn(itemList);
            List<FetchOnSaleMapper> fetchOnSaleMapper = itemService.fetchItemsOnSaleList();
            //販売中の商品コードと商品名を確認
            assertThat(fetchOnSaleMapper.size(), is(2));
            assertThat(fetchOnSaleMapper.get(0).getItemCd(), is("01"));
            assertThat(fetchOnSaleMapper.get(0).getItemName(), is("コーラ"));
            assertThat(fetchOnSaleMapper.get(1).getItemCd(), is("02"));
            assertThat(fetchOnSaleMapper.get(1).getItemName(), is("ペプシ"));
        }

        /**
         * 正常系
         * 販売中商品が存在しない場合
         */
        @Test
        public void fetchNoOnSaleItems() {
            List<Item> vacantItemList = new ArrayList<>();
            when(itemDao.selectOnSale()).thenReturn(vacantItemList);
            //販売停止中の商品だけデータをもつ
            List<FetchOnSaleMapper> fetchOnSaleMapper = itemService.fetchItemsOnSaleList();
            //販売中の商品がないことを確認
            assertThat(fetchOnSaleMapper.size(), is(0));
        }
    }


/**
 * {@linkItemService#editItem} メソッドテストクラス
 */
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest
    @Transactional
    public static class EditItemsTest {

        @Rule
        public ExpectedException expectedException = ExpectedException.none();
        @Autowired
        ItemService itemService;
        @SpyBean
        ItemDao itemDao;

        Item item1 = new Item();
        Item item2 = new Item();
        List<Item> itemList = new ArrayList<>();

        @Before
        public void before() {
            item1.setItemCd("01");
            item1.setItemName("コーラ");
            item1.setSellFlg("0");

            item2.setItemCd("02");
            item2.setItemName("ペプシ");
            item2.setSellFlg("1");
            itemList.add(item1);
            itemList.add(item2);
        }

        /**
         * 正常系
         * 商品マスタの商品更新
         */
        @Test
        public void editSuccessfully() {

            //商品編集を実行
            doReturn(2).when(itemDao).selectTargetCount(any());
            doReturn(new int[]{1, 1}).when(itemDao).batchUpdate(any());
            int editNumber = itemService.editItem(itemList);
            ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
            verify(itemDao, times(1)).batchUpdate(Collections.singletonList(captor.capture()));
            assertThat(editNumber, is(2));
        }

        /**
         * 異常系
         * 商品マスタの商品更新（更新対象のデータが存在しない場合）
         */
        @Test
        public void editNoExistItems() {
            doReturn(3).when(itemDao).selectTargetCount(any());
            //例外クラスと例外メッセージの検証
            expectedException.expect(ServiceValidationException.class);
            expectedException.expectMessage("E_SERVICE_005");
            int editNumber = itemService.editItem(itemList);
        }

    }
}
