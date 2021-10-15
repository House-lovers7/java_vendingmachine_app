package com.example.vendingmachine.controller;

import com.example.vendingmachine.entity.Item;
import com.example.vendingmachine.json.ItemAddInputJson;
import com.example.vendingmachine.json.ItemEditInputJson;
import com.example.vendingmachine.mapper.FetchOnSaleMapper;
import com.example.vendingmachine.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ItemControllerTest {

    @Autowired
    WebApplicationContext wac;
    @MockBean
    ItemService itemService;
    @Autowired
    ObjectMapper mapper;

    private static final String FETCH_ITEMS_URL = "/api/item/";
    private static final String ADD_ITEM_URL = "/api/item/add";
    private static final String FETCH_ON_SALE_ITEMS_URL = "/api/item/on-sale";

    private static final String EDIT_ITEMS_URL = "/api/item/edit/";

    private MockMvc mvc;

    Item item1 = new Item();
    Item item2 = new Item();
    List<Item> itemList = new ArrayList<>();

    @Before
    public void before() {

        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

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
     * 商品マスタ全件取得
     */
    @Test
    public void fetchItemList() throws Exception {

        when(itemService.fetchItemList()).thenReturn(itemList);

        mvc.perform(get(FETCH_ITEMS_URL))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].itemCd").value(item1.getItemCd()))
                .andExpect(jsonPath("$.[0].itemName").value(item1.getItemName()))
                .andExpect(jsonPath("$.[0].sellFlg").value(item1.getSellFlg()))
                .andExpect(jsonPath("$.[1].itemCd").value(item2.getItemCd()))
                .andExpect(jsonPath("$.[1].itemName").value(item2.getItemName()))
                .andExpect(jsonPath("$.[1].sellFlg").value(item2.getSellFlg()));
    }

    /**
     * 販売中商品のコード＆名前取得
     */
    @Test
    public void fetchOnSaleCdNamePeerList() throws Exception {

        List<FetchOnSaleMapper> onSaleItemList = new ArrayList();

        for (Item OnSaleItem : itemList) {
            FetchOnSaleMapper fetchOnSaleMapper = new FetchOnSaleMapper(OnSaleItem.getItemCd(), OnSaleItem.getItemName());
            onSaleItemList.add(fetchOnSaleMapper);
        }
        when(itemService.fetchItemsOnSaleList()).thenReturn(onSaleItemList);

        //戻り値の確認
        mvc.perform(get(FETCH_ON_SALE_ITEMS_URL))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].itemCd").value(item1.getItemCd()))
                .andExpect(jsonPath("$.[0].itemName").value(item1.getItemName()))
                .andExpect(jsonPath("$.[1].itemCd").value(item2.getItemCd()))
                .andExpect(jsonPath("$.[1].itemName").value(item2.getItemName()));
    }

    /**
     * 商品マスタに商品追加
     * 正常系
     */
    @Test
    public void addItem() throws Exception {

        ItemAddInputJson inputJson = new ItemAddInputJson();
        inputJson.setItemCd("12345");
        String repeatedCharacters = new String(new char[20]).replace("\0", "あ");
        inputJson.setItemName(repeatedCharacters);
        String json = mapper.writeValueAsString(inputJson);
        when(itemService.addItem(inputJson.getItemCd(), inputJson.getItemName())).thenReturn(1);
        mvc.perform(post(ADD_ITEM_URL)
           .contentType(MediaType.APPLICATION_JSON)
           .content(json))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.addedItemNumber").value("1"));
        verify(itemService).addItem(inputJson.getItemCd(), inputJson.getItemName());
    }

    /**
     * 商品マスタに商品追加
     * itemCdが空もしくはnullだった場合
     */
    @Test
    public void addEmptyItemCd() throws Exception {
        ItemAddInputJson inputJson = new ItemAddInputJson();
        inputJson.setItemCd("");
        inputJson.setItemName("アンバサ");
        String json = mapper.writeValueAsString(inputJson);
        mvc.perform(post(ADD_ITEM_URL)
           .contentType(MediaType.APPLICATION_JSON)
           .content(json))
           .andExpect(jsonPath("$").isArray())
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_003"))
           .andExpect(jsonPath("$[0].errMsg").value("商品コードを指定してください。"));

    }

    /**
     * 商品マスタに商品追加
     * itemCdがnullの場合
     */
    @Test
    public void addNullItemCd() throws Exception {
        ItemAddInputJson inputJson = new ItemAddInputJson();
        inputJson.setItemCd(null);
        inputJson.setItemName("アンバサ");
        String json = mapper.writeValueAsString(inputJson);
        mvc.perform(post(ADD_ITEM_URL)
           .contentType(MediaType.APPLICATION_JSON)
           .content(json))
           .andExpect(jsonPath("$").isArray())
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_003"))
           .andExpect(jsonPath("$[0].errMsg").value("商品コードを指定してください。"));

    }

    /**
     * 商品マスタに商品追加
     * itemNameが空の場合
     */
    @Test
    public void addEmptyItemName() throws Exception {
        ItemAddInputJson inputJson = new ItemAddInputJson();
        inputJson.setItemCd("99");
        inputJson.setItemName("");
        String json = mapper.writeValueAsString(inputJson);
        mvc.perform(post(ADD_ITEM_URL)
           .contentType(MediaType.APPLICATION_JSON)
           .content(json))
           .andExpect(jsonPath("$").isArray())
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_009"))
           .andExpect(jsonPath("$[0].errMsg").value("商品名を指定してください。"));

    }

    /**
     * 商品マスタに商品追加
     * itemNameがnullの場合
     */
    @Test
    public void addNullItemName() throws Exception {
        ItemAddInputJson inputJson = new ItemAddInputJson();
        inputJson.setItemCd("99");
        inputJson.setItemName(null);
        String json = mapper.writeValueAsString(inputJson);
        mvc.perform(post(ADD_ITEM_URL)
           .contentType(MediaType.APPLICATION_JSON)
           .content(json))
           .andExpect(jsonPath("$").isArray())
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_009"))
           .andExpect(jsonPath("$[0].errMsg").value("商品名を指定してください。"));

    }

    /**
     * 　　　* 異常系
     * 　　　* 商品CDの文字数(5)オーバーの場合
     */
    @Test
    public void addOverCharItemCd() throws Exception {
        ItemAddInputJson inputJson = new ItemAddInputJson();
        inputJson.setItemCd("123456");
        inputJson.setItemName("アンバサ");
        String json = mapper.writeValueAsString(inputJson);
        mvc.perform(post(ADD_ITEM_URL)
           .contentType(MediaType.APPLICATION_JSON)
           .content(json))
           .andExpect(jsonPath("$").isArray())
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_004"))
           .andExpect(jsonPath("$[0].errMsg").value("商品コードは5桁以内で指定してください。"));
    }

    /**
     * 　　　* 異常系
     * 　　　* 商品名の文字数（50）オーバーの場合
     */
    @Test
    public void addOverCharItemName() throws Exception {
        ItemAddInputJson inputJson = new ItemAddInputJson();
        inputJson.setItemCd("99");
        String repeatedCharacters = new String(new char[21]).replace("\0", "あ");
        inputJson.setItemName(repeatedCharacters);
        String json = mapper.writeValueAsString(inputJson);
        mvc.perform(post(ADD_ITEM_URL)
           .contentType(MediaType.APPLICATION_JSON)
           .content(json))
           .andExpect(jsonPath("$").isArray())
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_010"))
           .andExpect(jsonPath("$[0].errMsg").value("商品名は20桁以内で指定してください。"));
    }




/**
 * 商品マスタの商品更新
 * 正常系
 */
    @Test
    public void editItems() throws Exception {

        ItemEditInputJson editJson = new ItemEditInputJson();
        List<Item> itemList = new ArrayList<>();
        Item testItem1 = new Item();
        Item testItem2 = new Item();
        //更新商品リスト作成
        testItem1.setItemCd("01");
        testItem1.setItemName("Cola");
        testItem1.setSellFlg("0");
        itemList.add(testItem1);
        testItem2.setItemCd("12345");
        String repeatedCharacters = new String(new char[20]).replace("\0", "あ");
        testItem2.setItemName(repeatedCharacters);
        testItem2.setSellFlg("0");
        itemList.add(testItem2);

        //リクエスト内容をセット
        editJson.setItemList(itemList);
        when(itemService.editItem(itemList)).thenReturn(2);

        //jsonの文字列へ変換
        String json = mapper.writeValueAsString(editJson);
        mvc.perform(patch(EDIT_ITEMS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.editedItemNumber").value(2));

        verify(itemService).editItem(editJson.getItemList());

    }

    /**
     * 商品マスタの商品更新
     * 異常系
     * 販売フラグが0,1以外の場合
     */
    @Test
    public void editWrongSellFlgItems() throws Exception {

        ItemEditInputJson editJson = new ItemEditInputJson();
        List<Item> itemList = new ArrayList<>();
        Item testItem = new Item();
        //更新商品リスト作成
        testItem.setItemCd("01");
        testItem.setItemName("Cola");
        testItem.setSellFlg("3");
        itemList.add(testItem);
        editJson.setItemList(itemList);

        String json = mapper.writeValueAsString(editJson);
        mvc.perform(patch(EDIT_ITEMS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_011"))
                .andExpect(jsonPath("$[0].errMsg").value("販売中フラグは0か1で指定してください。"));

    }

    /**
     * 商品マスタの商品更新
     * 異常系
     * 更新商品リストの内容がない場合
     */
    @Test
    public void editNoItems() throws Exception {

        ItemEditInputJson editJson = new ItemEditInputJson();
        List<Item> itemList = new ArrayList<>();
        editJson.setItemList(itemList);
        String json = mapper.writeValueAsString(editJson);
        mvc.perform(patch(EDIT_ITEMS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_012"))
                .andExpect(jsonPath("$[0].errMsg").value("商品リストを指定してください。"));

    }

    /**
     * 商品マスタの商品更新
     * 異常系
     * itemCdが空の場合
     */
    @Test
    public void editEmptyItemCd() throws Exception {

        ItemEditInputJson editJson = new ItemEditInputJson();
        List<Item> itemList = new ArrayList<>();
        Item testItem = new Item();
        //更新商品リスト作成
        testItem.setItemCd("");
        testItem.setItemName("Cola");
        testItem.setSellFlg("1");
        itemList.add(testItem);
        editJson.setItemList(itemList);

        String json = mapper.writeValueAsString(editJson);
        mvc.perform(patch(EDIT_ITEMS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_003"))
                .andExpect(jsonPath("$[0].errMsg").value("商品コードを指定してください。"));
    }

    /**
     * 商品マスタの商品更新
     * 異常系
     * itemCdがNullの場合
     */
    @Test
    public void editNullItemCd() throws Exception {

        ItemEditInputJson editJson = new ItemEditInputJson();
        List<Item> itemList = new ArrayList<>();
        Item testItem = new Item();
        //更新商品リスト作成
        testItem.setItemCd(null);
        testItem.setItemName("Cola");
        testItem.setSellFlg("1");
        itemList.add(testItem);
        editJson.setItemList(itemList);

        String json = mapper.writeValueAsString(editJson);
        mvc.perform(patch(EDIT_ITEMS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_003"))
                .andExpect(jsonPath("$[0].errMsg").value("商品コードを指定してください。"));
    }

    /**
     * 商品マスタの商品更新
     * 異常系
     * itemNameが空の場合
     */
    @Test
    public void editEmptyItemName() throws Exception {

        ItemEditInputJson editJson = new ItemEditInputJson();
        List<Item> itemList = new ArrayList<>();
        Item testItem = new Item();
        //更新商品リスト作成
        testItem.setItemCd("01");
        testItem.setItemName("");
        testItem.setSellFlg("1");
        itemList.add(testItem);
        editJson.setItemList(itemList);

        String json = mapper.writeValueAsString(editJson);
        mvc.perform(patch(EDIT_ITEMS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_009"))
                .andExpect(jsonPath("$[0].errMsg").value("商品名を指定してください。"));
    }

    /**
     * 商品マスタの商品更新
     * 異常系
     * itemNameがNullの場合
     */
    @Test
    public void editNullItemName() throws Exception {

        ItemEditInputJson editJson = new ItemEditInputJson();
        List<Item> itemList = new ArrayList<>();
        Item testItem = new Item();
        //更新商品リスト作成
        testItem.setItemCd("01");
        testItem.setItemName(null);
        testItem.setSellFlg("1");
        itemList.add(testItem);
        editJson.setItemList(itemList);

        when(itemService.editItem(editJson.getItemList())).thenReturn(0);
        String json = mapper.writeValueAsString(editJson);
        mvc.perform(patch(EDIT_ITEMS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_009"))
                .andExpect(jsonPath("$[0].errMsg").value("商品名を指定してください。"));

    }

    /**
     * 商品マスタの商品更新
     * 異常系
     * 商品CDの文字数(5)オーバーの場合
     */
    @Test
    public void editOverCharItemCd() throws Exception {

        ItemEditInputJson editJson = new ItemEditInputJson();
        List<Item> itemList = new ArrayList<>();
        Item testItem = new Item();
        //更新商品リスト作成
        testItem.setItemCd("123456");
        testItem.setItemName("Cola");
        testItem.setSellFlg("1");
        itemList.add(testItem);
        editJson.setItemList(itemList);

        String json = mapper.writeValueAsString(editJson);
        mvc.perform(patch(EDIT_ITEMS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_004"))
                .andExpect(jsonPath("$[0].errMsg").value("商品コードは5桁以内で指定してください。"));
    }

    /**
     * 商品マスタの商品更新
     * 異常系
     * 商品名の文字数（20）オーバーの場合
     */
    @Test
    public void editOverCharItemName() throws Exception {

        ItemEditInputJson editJson = new ItemEditInputJson();
        List<Item> itemList = new ArrayList<>();
        Item testItem = new Item();
        //更新商品リスト作成
        testItem.setItemCd("01");
        String repeatedCharacters = new String(new char[21]).replace("\0", "あ");
        testItem.setItemName(repeatedCharacters);
        testItem.setSellFlg("1");
        itemList.add(testItem);
        editJson.setItemList(itemList);
        String json = mapper.writeValueAsString(editJson);
        mvc.perform(patch(EDIT_ITEMS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$").isArray())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_010"))
                .andExpect(jsonPath("$[0].errMsg").value("商品名は20桁以内で指定してください。"));
    }
}
