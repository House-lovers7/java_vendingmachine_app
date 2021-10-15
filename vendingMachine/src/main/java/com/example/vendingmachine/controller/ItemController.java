package com.example.vendingmachine.controller;

import com.example.vendingmachine.dto.ErrorDto;
import com.example.vendingmachine.entity.Item;
import com.example.vendingmachine.json.ItemAddInputJson;
import com.example.vendingmachine.mapper.FetchOnSaleMapper;
import com.example.vendingmachine.json.ItemEditInputJson;
import com.example.vendingmachine.service.ItemService;
import com.example.vendingmachine.util.RestValidationUtil;
import com.example.vendingmachine.wrapper.ItemAddResponseWrapper;
import com.example.vendingmachine.wrapper.ItemEditResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@CrossOrigin
public class ItemController {

    @Autowired
    ItemService itemService;
    @Autowired
    MessageSource messageSource;

    /**
     * 商品マスタから全商品取得
     *
     * @return 商品リスト
     */
    //TODO:コメントの内容を元に実装する。
    @GetMapping("/")
    public ResponseEntity<List<Item>> fetchItems() {

        //ItemServiceクラスのfetchItemListを呼び出す
        final List<Item> itemList = itemService.fetchItemList();

        //レスポンスを返す
        //HttpStatus("OK")とbody(List<Item>)を返却する
        return ResponseEntity.status(HttpStatus.OK).body(itemList);
    }

    /**
     * 商品マスタから販売中商品のコード＆商品名取得
     *
     * @return コード＆商品名リスト
     */
    @GetMapping("/on-sale")
    public ResponseEntity<List<FetchOnSaleMapper>> fetchOnSaleItems() {

        //ItemServiceクラスのfetchSale
        // ItemListを呼び出す
        final List<FetchOnSaleMapper> fetchOnSaleMapper = itemService.fetchItemsOnSaleList();

        //レスポンスを返す
        //HttpStatus("OK")とbody(List<OnSaleItemList>)を返却する
        return ResponseEntity.status(HttpStatus.valueOf("OK")).body(fetchOnSaleMapper);
    }

    /**
     * 商品マスタに商品追加
     *
     * @param itemAddInputJson リクエストJson
     * @return 追加件数
     */
    //TODO:コメントの内容を元に実装する。
    @PostMapping("/add")
    public ResponseEntity addItem(
            @RequestBody @Validated ItemAddInputJson itemAddInputJson,
            BindingResult bindingResult
    ) {

        //validationチェックでエラーがあった場合は、エラーを返す
        if (bindingResult.hasFieldErrors()) {
            List<ErrorDto> errorList = RestValidationUtil.getFieldValidationErrors(messageSource, bindingResult);
            return ResponseEntity.badRequest().body(errorList);
        }

        //ItemServiceクラスのaddItemを呼び出す
        int addedItemNumber = itemService.addItem(itemAddInputJson.getItemCd(), itemAddInputJson.getItemName());

        //HttpStatus("OK")とbody(addedItemNumber(ItemAddResponseWrapperをnewする))を返却する
        return ResponseEntity.status(HttpStatus.OK).body(new ItemAddResponseWrapper(addedItemNumber));
    }

    /**
     * 商品マスタ編集
     *
     * @param itemEditInputJson リクエストJson
     * @return 編集件数
     */
    @PatchMapping("/edit")
    public ResponseEntity editItem(
            @RequestBody @Validated ItemEditInputJson itemEditInputJson,
            BindingResult bindingResult
    ) {

        //validationチェックでエラーがあった場合は、エラーを返す
        if (bindingResult.hasFieldErrors()) {
            List<ErrorDto> errorList = RestValidationUtil.getFieldValidationErrors(messageSource, bindingResult);
            return ResponseEntity.badRequest().body(errorList);
        }

        //ItemServiceクラスのeditItemを呼び出す
        int editedItemNumber = itemService.editItem(itemEditInputJson.getItemList());

        //HttpStatus("OK")とbody(editedItemNumber(ItemEditResponseWrapperをnewする))を返却する
        return ResponseEntity.status(HttpStatus.OK).body(new ItemEditResponseWrapper(editedItemNumber));
    }
}