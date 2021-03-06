package com.example.vendingmachine.controller;

import com.example.vendingmachine.dto.EditStockItemDto;
import com.example.vendingmachine.entity.StockItem;
import com.example.vendingmachine.json.RackBuyInputJson;
import com.example.vendingmachine.json.RackEditInputJson;
import com.example.vendingmachine.service.RackService;
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
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RackControllerTest {

    @Autowired
    WebApplicationContext wac;
    @MockBean
    RackService rackService;

    @Autowired
    ObjectMapper mapper;

    private static final String FETCH_RACK_CONTENT = "/api/rack/";
    private static final String BUY_ITEM = "/api/rack/buy";
    private static final String EDIT_RACK_CONTENT = "/api/rack/edit";

    private MockMvc mvc;

    StockItem stockItem1 = new StockItem();
    StockItem stockItem2 = new StockItem();
    List<StockItem> stockItemList = new ArrayList<>();


    @Before
    public void before() {

        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

        //stockItem
        stockItem1.setRackNo("01");
        stockItem1.setItemCd("00001");
        stockItem1.setItemName("?????????");
        stockItem1.setItemNumber(01);
        stockItem1.setPrice(100);

        stockItem2.setRackNo("02");
        stockItem2.setItemCd("00002");
        stockItem2.setItemName("?????????");
        stockItem2.setItemNumber(02);
        stockItem2.setPrice(200);

        stockItemList.add(stockItem1);
        stockItemList.add(stockItem2);


    }

    /**
     * ???????????????????????????
     * ??????
     */
    @Test
    public void fetchRackAllContent() throws Exception {
        when(rackService.fetchStockItemList()).thenReturn(stockItemList);

        mvc.perform(get(FETCH_RACK_CONTENT))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].rackNo").value(stockItem1.getRackNo()))
                .andExpect(jsonPath("$.[0].itemCd").value(stockItem1.getItemCd()))
                .andExpect(jsonPath("$.[0].itemName").value(stockItem1.getItemName()))
                .andExpect(jsonPath("$.[0].itemNumber").value(stockItem1.getItemNumber()))
                .andExpect(jsonPath("$.[0].price").value(stockItem1.getPrice()))
                .andExpect(jsonPath("$.[1].rackNo").value(stockItem2.getRackNo()))
                .andExpect(jsonPath("$.[1].itemCd").value(stockItem2.getItemCd()))
                .andExpect(jsonPath("$.[1].itemName").value(stockItem2.getItemName()))
                .andExpect(jsonPath("$.[1].itemNumber").value(stockItem2.getItemNumber()))
                .andExpect(jsonPath("$.[1].price").value(stockItem2.getPrice()));
    }

    /**
     * ??????????????????????????????
     */
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest
    public static class buyItemTest {

        @Autowired
        WebApplicationContext wac;
        @MockBean
        RackService rackService;
        @Autowired
        ObjectMapper mapper;
        private MockMvc mvc;

        RackBuyInputJson rackBuyInputJson = new RackBuyInputJson();

        @Before
        public void before() {

            mvc = MockMvcBuilders
                    .webAppContextSetup(wac)
                    .build();

        }

        /**
         * ?????????
         */
        @Test
        public void normal() throws Exception {
            rackBuyInputJson.setRackNo("01");
            when(rackService.callOneItem(rackBuyInputJson.getRackNo())).thenReturn("?????????");
            String json = mapper.writeValueAsString(rackBuyInputJson);
            mvc.perform(patch(BUY_ITEM)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.itemName").value("?????????"));

        }

        /**
         * ?????????
         * ?????????No???????????????
         */
        @Test
        public void rackNoEmpty() throws Exception {
            rackBuyInputJson.setRackNo("");
            String json = mapper.writeValueAsString(rackBuyInputJson);
            mvc.perform(patch(BUY_ITEM)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_001"))
                    .andExpect(jsonPath("$[0].errMsg").value("???????????????????????????????????????????????????"));

        }

        /**
         * ?????????
         * ?????????No???Null?????????
         */
        @Test
        public void rackNoNull() throws Exception {
            rackBuyInputJson.setRackNo(null);
            String json = mapper.writeValueAsString(rackBuyInputJson);
            mvc.perform(patch(BUY_ITEM)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_001"))
                    .andExpect(jsonPath("$[0].errMsg").value("???????????????????????????????????????????????????"));

        }

        /**
         * ?????????
         * ?????????No???2????????????????????????
         */
        @Test
        public void rackNoOver() throws Exception {

            rackBuyInputJson.setRackNo("001");

            when(rackService.callOneItem(rackBuyInputJson.getRackNo())).thenReturn("?????????");
            String json = mapper.writeValueAsString(rackBuyInputJson);
            mvc.perform(patch(BUY_ITEM)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_002"))
                    .andExpect(jsonPath("$[0].errMsg").value("????????????????????????2???????????????????????????????????????"));

        }
    }


    /**
     * ??????????????????
     */
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest
    public static class editRackItem {

        @Autowired
        WebApplicationContext wac;
        @MockBean
        RackService rackService;
        @Autowired
        ObjectMapper mapper;
        private MockMvc mvc;

        RackEditInputJson rackEditInputJson = new RackEditInputJson();
        List<EditStockItemDto> editStockItemDtoList = new ArrayList<>();
        EditStockItemDto editStockItemDto1 = new EditStockItemDto();
        EditStockItemDto editStockItemDto2 = new EditStockItemDto();

        @Before
        public void before() {

            mvc = MockMvcBuilders
                    .webAppContextSetup(wac)
                    .build();

        }

        /**
         * ?????????
         */
        @Test
        public void normal() throws Exception {

            editStockItemDto1.setRackNo("01");
            editStockItemDto1.setItemCd("10000");
            editStockItemDto1.setItemNumber(5);
            editStockItemDto1.setPrice(500);
            editStockItemDto2.setRackNo("02");
            editStockItemDto2.setItemCd("20000");
            editStockItemDto2.setItemNumber(99);
            editStockItemDto2.setPrice(999);
            editStockItemDtoList.add(editStockItemDto1);
            editStockItemDtoList.add(editStockItemDto2);

            //?????????????????????????????????
            rackEditInputJson.setRackItemList(editStockItemDtoList);
            when(rackService.editRackItemDtoList(rackEditInputJson.getRackItemList())).thenReturn(2);
            //json?????????????????????
            String json = mapper.writeValueAsString(rackEditInputJson);
            mvc.perform(patch(EDIT_RACK_CONTENT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.editedNumber").value(2));

        }

        /**
         * ?????????
         * RackItemList???????????????
         */
        @Test
        public void rackItemListEmpty () throws Exception {

            //?????????????????????????????????
            rackEditInputJson.setRackItemList(editStockItemDtoList);
            //json?????????????????????
            String json = mapper.writeValueAsString(rackEditInputJson);
            mvc.perform(patch(EDIT_RACK_CONTENT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_013"))
                    .andExpect(jsonPath("$[0].errMsg").value("???????????????????????????????????????????????????"));

        }

        /**
         * ?????????
         * ?????????No???????????????
         */
        @Test
        public void rackNoEmpty() throws Exception {

            editStockItemDto1.setRackNo("");
            editStockItemDto1.setItemCd("10000");
            editStockItemDto1.setItemNumber(5);
            editStockItemDto1.setPrice(500);
            editStockItemDtoList.add(editStockItemDto1);

            //?????????????????????????????????
            rackEditInputJson.setRackItemList(editStockItemDtoList);

            when(rackService.editRackItemDtoList(rackEditInputJson.getRackItemList())).thenReturn(2);
            //json?????????????????????
            String json = mapper.writeValueAsString(rackEditInputJson);
            mvc.perform(patch(EDIT_RACK_CONTENT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_001"))
                    .andExpect(jsonPath("$[0].errMsg").value("???????????????????????????????????????????????????"));

        }

        /**
         * ?????????
         * ?????????No???Null?????????
         */
        @Test
        public void rackNoNull() throws Exception {

            editStockItemDto1.setRackNo(null);
            editStockItemDto1.setItemCd("10000");
            editStockItemDto1.setItemNumber(5);
            editStockItemDto1.setPrice(500);
            editStockItemDtoList.add(editStockItemDto1);

            //?????????????????????????????????
            rackEditInputJson.setRackItemList(editStockItemDtoList);

            when(rackService.editRackItemDtoList(rackEditInputJson.getRackItemList())).thenReturn(2);
            //json?????????????????????
            String json = mapper.writeValueAsString(rackEditInputJson);
            mvc.perform(patch(EDIT_RACK_CONTENT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_001"))
                    .andExpect(jsonPath("$[0].errMsg").value("???????????????????????????????????????????????????"));

        }

        /**
         * ?????????
         * ?????????No???3??????????????????
         */
        @Test
        public void rackNoOver() throws Exception {
            String repeatedRackNo = new String(new char[3]).replace("\0", "1");
            editStockItemDto1.setRackNo(repeatedRackNo);
            editStockItemDto1.setItemCd("10000");
            editStockItemDto1.setItemNumber(5);
            editStockItemDto1.setPrice(500);
            editStockItemDtoList.add(editStockItemDto1);

            //?????????????????????????????????
            rackEditInputJson.setRackItemList(editStockItemDtoList);

            when(rackService.editRackItemDtoList(rackEditInputJson.getRackItemList())).thenReturn(2);
            //json?????????????????????
            String json = mapper.writeValueAsString(rackEditInputJson);
            mvc.perform(patch(EDIT_RACK_CONTENT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_002"))
                    .andExpect(jsonPath("$[0].errMsg").value("????????????????????????2???????????????????????????????????????"));

        }

        /**
         * ?????????
         * ??????????????????????????????
         */
        @Test
        public void itemCdEmpty() throws Exception {

            editStockItemDto1.setRackNo("01");
            editStockItemDto1.setItemCd("");
            editStockItemDto1.setItemNumber(5);
            editStockItemDto1.setPrice(500);
            editStockItemDtoList.add(editStockItemDto1);

            //?????????????????????????????????
            rackEditInputJson.setRackItemList(editStockItemDtoList);

            when(rackService.editRackItemDtoList(rackEditInputJson.getRackItemList())).thenReturn(2);
            //json?????????????????????
            String json = mapper.writeValueAsString(rackEditInputJson);
            mvc.perform(patch(EDIT_RACK_CONTENT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_003"))
                    .andExpect(jsonPath("$[0].errMsg").value("?????????????????????????????????????????????"));

        }

        /**
         * ?????????
         * ??????????????????Null?????????
         */
        @Test
        public void itemCdNull() throws Exception {

            editStockItemDto1.setRackNo("01");
            editStockItemDto1.setItemCd(null);
            editStockItemDto1.setItemNumber(5);
            editStockItemDto1.setPrice(500);
            editStockItemDtoList.add(editStockItemDto1);

            //?????????????????????????????????
            rackEditInputJson.setRackItemList(editStockItemDtoList);

            when(rackService.editRackItemDtoList(rackEditInputJson.getRackItemList())).thenReturn(2);
            //json?????????????????????
            String json = mapper.writeValueAsString(rackEditInputJson);
            mvc.perform(patch(EDIT_RACK_CONTENT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_003"))
                    .andExpect(jsonPath("$[0].errMsg").value("?????????????????????????????????????????????"));

        }


        /**
         * ?????????
         * ??????????????????6??????????????????
         */
        @Test
        public void itemCdOver() throws Exception {
            editStockItemDto1.setRackNo("01");
            String repeatedItemCd = new String(new char[6]).replace("\0", "1");
            editStockItemDto1.setItemCd(repeatedItemCd);
            editStockItemDto1.setItemNumber(5);
            editStockItemDto1.setPrice(500);
            editStockItemDtoList.add(editStockItemDto1);

            //?????????????????????????????????
            rackEditInputJson.setRackItemList(editStockItemDtoList);

            when(rackService.editRackItemDtoList(rackEditInputJson.getRackItemList())).thenReturn(2);
            //json?????????????????????
            String json = mapper.writeValueAsString(rackEditInputJson);
            mvc.perform(patch(EDIT_RACK_CONTENT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_004"))
                    .andExpect(jsonPath("$[0].errMsg").value("??????????????????5???????????????????????????????????????"));

        }

        /**
         * ?????????
         * ????????????Null?????????
         */
        @Test
        public void itemNumberNull() throws Exception {

            editStockItemDto1.setRackNo("01");
            editStockItemDto1.setItemCd("10000");
            editStockItemDto1.setItemNumber(null);
            editStockItemDto1.setPrice(500);
            editStockItemDtoList.add(editStockItemDto1);

            //?????????????????????????????????
            rackEditInputJson.setRackItemList(editStockItemDtoList);

            when(rackService.editRackItemDtoList(rackEditInputJson.getRackItemList())).thenReturn(2);
            //json?????????????????????
            String json = mapper.writeValueAsString(rackEditInputJson);
            mvc.perform(patch(EDIT_RACK_CONTENT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_005"))
                    .andExpect(jsonPath("$[0].errMsg").value("???????????????????????????????????????"));

        }

        /**
         * ?????????
         * ????????????100???????????????
         */
        @Test
        public void itemNumberOver() throws Exception {

            editStockItemDto1.setRackNo("01");
            editStockItemDto1.setItemCd("10000");
            editStockItemDto1.setItemNumber(100);
            editStockItemDto1.setPrice(500);
            editStockItemDtoList.add(editStockItemDto1);

            //?????????????????????????????????
            rackEditInputJson.setRackItemList(editStockItemDtoList);

            when(rackService.editRackItemDtoList(rackEditInputJson.getRackItemList())).thenReturn(2);
            //json?????????????????????
            String json = mapper.writeValueAsString(rackEditInputJson);
            mvc.perform(patch(EDIT_RACK_CONTENT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_006"))
                    .andExpect(jsonPath("$[0].errMsg").value("????????????99???????????????????????????????????????"));

        }

        /**
         * ?????????
         * ?????????Null?????????
         */
        @Test
        public void priceNull() throws Exception {

            editStockItemDto1.setRackNo("01");
            editStockItemDto1.setItemCd("10000");
            editStockItemDto1.setItemNumber(5);
            editStockItemDto1.setPrice(null);
            editStockItemDtoList.add(editStockItemDto1);

            //?????????????????????????????????
            rackEditInputJson.setRackItemList(editStockItemDtoList);

            when(rackService.editRackItemDtoList(rackEditInputJson.getRackItemList())).thenReturn(2);
            //json?????????????????????
            String json = mapper.writeValueAsString(rackEditInputJson);
            mvc.perform(patch(EDIT_RACK_CONTENT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_007"))
                    .andExpect(jsonPath("$[0].errMsg").value("????????????????????????????????????"));

        }

        /**
         * ?????????
         * ?????????1000???????????????
         */
        @Test
        public void priceOver() throws Exception {

            editStockItemDto1.setRackNo("01");
            editStockItemDto1.setItemCd("10000");
            editStockItemDto1.setItemNumber(5);
            editStockItemDto1.setPrice(1000);
            editStockItemDtoList.add(editStockItemDto1);

            //?????????????????????????????????
            rackEditInputJson.setRackItemList(editStockItemDtoList);

            when(rackService.editRackItemDtoList(rackEditInputJson.getRackItemList())).thenReturn(2);
            //json?????????????????????
            String json = mapper.writeValueAsString(rackEditInputJson);
            mvc.perform(patch(EDIT_RACK_CONTENT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(jsonPath("$[0].errCode").value("E_VALIDATION_008"))
                    .andExpect(jsonPath("$[0].errMsg").value("?????????999???????????????????????????????????????"));

        }

    }
}
