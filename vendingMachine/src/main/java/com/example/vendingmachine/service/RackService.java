package com.example.vendingmachine.service;

import com.example.vendingmachine.dao.ItemDao;
import com.example.vendingmachine.dao.RackDao;
import com.example.vendingmachine.entity.Rack;
import com.example.vendingmachine.exception.ServiceValidationException;
import com.example.vendingmachine.dao.StockItemDao;
import com.example.vendingmachine.entity.StockItem;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.vendingmachine.dto.EditStockItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

// TODO:コメントは研修用に記載しているものなので、実装時に必要に応じて削除してください
@Service
public class RackService {

    @Autowired
    RackDao rackDao;

    @Autowired
    ItemDao itemDao;

    @Autowired
    StockItemDao stockItemDao;

    /**
     * Rackの商品をStockItem型で全件取得
     *
     * @return StockItemリスト
     */
    //メソッド名：fetchStockItemList
    // メソッド名は必要に応じて変更可

    //TODO:コメントの内容を元に実装する。
    @Transactional(readOnly = true)
    public List<StockItem> fetchStockItemList() {
        final List<StockItem> stockItemList = stockItemDao.selectAll();
        return stockItemList;
    }


    /**
     * ラックの内容を引数のラック商品リストの内容に編集
     * 指定のラックNoのラックが存在しない要素は編集スキップ
     *
     * @param editStockItemDtoList ラック商品リスト
     * @return 編集ラック商品数
     */
    //メソッド名：editStockItem
    // メソッド名は必要に応じて変更可


    /**
     * 指定のラックNoのラックをラックリストから検索
     * 無ければnullを返す
     *
     * @param rackNo   ラックNo
     * @param rackList ラックリスト
     * @return ラック
     */
    //メソッド名：fetchRackByRackNo
    // editStockItemから呼び出すためのメソッド
    // メソッド名は必要に応じて変更可
    public int editRackItemDtoList(List<EditStockItemDto> editStockItemDtoList) {
        //rackテーブルから全件取得
        List<Rack> rackAllList = rackDao.selectAll();
        //editStockItemDtoListの各要素を確認
        List<Rack> rackUpdateList = new ArrayList<>();
        Rack targetRack ;
        for (EditStockItemDto editStockItemDto : editStockItemDtoList) {
            //editStockItemDtoListのラックNoが一致するラックを取得
            targetRack = fetchRackByRackNo(editStockItemDto.getRackNo(), rackAllList);
            //一致しなければ次の要素の確認へ
            if (targetRack == null) {
                continue;
            }
            //商品数が格納可能数を超えていないか確認
            //超えている場合は(E_SERVICE_002=格納可能数を超えている商品があります。)を投げる
            if (targetRack.getCapacity() < editStockItemDto.getItemNumber()) {
                throw new ServiceValidationException("E_SERVICE_002");
            }
            //商品コード・商品数・価格をセットしてrackUpdateListへ入れる
            targetRack.setItemCd(editStockItemDto.getItemCd());
            targetRack.setItemNumber(editStockItemDto.getItemNumber());
            targetRack.setPrice(editStockItemDto.getPrice());
            rackUpdateList.add(targetRack);
        }

        //更新
        int[] editNumberList = rackDao.batchUpdate(rackUpdateList);
        int editedNumber = Arrays.stream(editNumberList).sum();

        //更新件数を返す
        return editedNumber;

    }


    /**
     * 指定のラックNoのラックをラックリストから検索
     * 無ければnullを返す
     *
     * @param rackNo   ラックNo
     * @param rackList ラックリスト
     * @return ラック
     */
    public Rack fetchRackByRackNo(String rackNo, List<Rack> rackList) {
        for (Rack rack : rackList) {
            if (rackNo.equals(rack.getRackNo())) {
                return rack;
            }
        }
        return null;
    }
//更新対象の RackNoを元に、更新対象の件数を取得


    /**
     * ラックから商品を１つ取り出す
     * ラックの商品数は一つ減らす
     *
     * @param rackNo ラックNo
     * @return 取り出した商品名
     */
    @Transactional
    public String callOneItem(String rackNo) {

        //ラックNoを元に、対象の商品をRackテーブルのレコードを取得
        Rack rack = rackDao.selectOne(rackNo);

        //nullの場合は対象商品がrackテーブルに存在しないため、「E_SERVICE_006=対象のラックナンバーがrackテーブルに存在しません。」を投げる
        if (rack == null) {
            throw new ServiceValidationException("E_SERVICE_006");
        }

        //商品コードをもとに、Itemテーブルから商品名を取得
        String itemName = itemDao.selectItemName(rack.getItemCd());
        //nullの場合は対象商品がitemテーブルに存在しないため、「E_SERVICE_007=対象のラックナンバーがitemテーブルに存在しません。」を投げる
        if (itemName == null) {
            throw new ServiceValidationException("E_SERVICE_007");
        }

        //商品数を確認（0の場合は、「E_SERVICE_001=商品が売り切れです。」を投げる）
        int itemNumber = rack.getItemNumber();
        if (itemNumber == 0) {
            throw new ServiceValidationException("E_SERVICE_001");
        }

        //商品数を一つ減らすため更新
        itemNumber--;
        rack.setItemNumber(itemNumber);
        rackDao.update(rack);

        //商品名を返す
        return itemName;
    }

}
