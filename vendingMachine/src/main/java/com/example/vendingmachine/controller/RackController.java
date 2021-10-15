package com.example.vendingmachine.controller;
import com.example.vendingmachine.dto.ErrorDto;
import com.example.vendingmachine.entity.StockItem;
import com.example.vendingmachine.json.RackEditInputJson;
import com.example.vendingmachine.json.RackBuyInputJson;
import com.example.vendingmachine.service.RackService;
import com.example.vendingmachine.util.RestValidationUtil;
import com.example.vendingmachine.wrapper.RackEditResponseWrapper;
import com.example.vendingmachine.wrapper.RackBuyResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rack")
@CrossOrigin

public class RackController {
    @Autowired
    RackService rackService;
    @Autowired
    MessageSource messageSource;

    /**
     * 格納商品編集
     *
     * @param rackEditInputJson リクエストJson
     * @return 編集件数
     */

    @PatchMapping("/edit")
    public ResponseEntity editRackItem(
            @RequestBody @Validated RackEditInputJson rackEditInputJson,
            BindingResult bindingResult
    ) {
        //validationチェックでエラーがあった場合は、エラーを返す
        if (bindingResult.hasFieldErrors()) {
            List<ErrorDto> errorList = RestValidationUtil.getFieldValidationErrors(messageSource, bindingResult);
            return ResponseEntity.badRequest().body(errorList);
        }

        //RackServiceクラスのeditRackItemDtoListを呼び出す
        int editedNumber = rackService.editRackItemDtoList(rackEditInputJson.getRackItemList());

        //HttpStatus("OK")とbody(editNumber(RackEditResponseWrapperをnewする))を返却する
        return ResponseEntity.status(HttpStatus.OK).body(new RackEditResponseWrapper(editedNumber));

    }

    /**
     * 商品マスタから全商品取得
     *
     * @return 商品リスト
     */
    //TODO:コメントの内容を元に実装する。
    @GetMapping("/")
    public ResponseEntity<List<StockItem>> fetchStockItems() {
        //RackServiceクラスのfetchStockItemListを呼び出す
        final List<StockItem> stockItemList = rackService.fetchStockItemList();
        //レスポンスを返す
        //HttpStatus("OK")とbody(List<StockItem>)を返却する
        return ResponseEntity.status(HttpStatus.OK).body(stockItemList);
    }

    /**
     * 商品購入
     *
     * @param rackBuyInputJson リクエストJson
     * @return Rack番号に紐づいた商品名
     */
    @PatchMapping("/buy")
    public ResponseEntity buyItem(
            @RequestBody @Validated RackBuyInputJson rackBuyInputJson,
            BindingResult bindingResult
    ) {
        //validationチェックでエラーがあった場合は、エラーを返す
        if (bindingResult.hasFieldErrors()) {
            List<ErrorDto> errorList = RestValidationUtil.getFieldValidationErrors(messageSource, bindingResult);
            return ResponseEntity.badRequest().body(errorList);
        }

        //RackServiceクラスのcallOneItemを呼び出す
        String itemName = rackService.callOneItem(rackBuyInputJson.getRackNo());

        //HttpStatus("OK")とbody(itemName(RackBuyResponseWrapperをnewする))を返却する
        return ResponseEntity.status(HttpStatus.OK).body(new RackBuyResponseWrapper(itemName));

    }
}