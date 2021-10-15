package com.example.vendingmachine.service;
import com.example.vendingmachine.dao.ItemDao;
import com.example.vendingmachine.entity.Item;
import com.example.vendingmachine.exception.ServiceValidationException;
import com.example.vendingmachine.mapper.FetchOnSaleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

@Service
public class ItemService {

    @Autowired
    ItemDao itemDao;

    /**
     * 商品マスタ全件取得
     *
     * @return 商品リスト
     */
    //TODO:コメントの内容を元に実装する。
    @Transactional(readOnly = true)
    public List<Item> fetchItemList() {
        //itemDaoクラスのselectAllを呼び出す
        final List<Item> itemList = itemDao.selectAll();

        //itemListを返却する
        return itemList;
    }

    /**
     * 販売中商品のコードと名前を取得
     *
     * @return コード＆名前リスト
     */
    @Transactional(readOnly = true)
    public List<FetchOnSaleMapper> fetchItemsOnSaleList() {
        final List<Item> itemList = itemDao.selectOnSale();
        List<FetchOnSaleMapper> onSaleItemList = new ArrayList<>();
        for (Item onSaleItem : itemList) {
            FetchOnSaleMapper fetchOnSaleMapper = new FetchOnSaleMapper(onSaleItem.getItemCd(), onSaleItem.getItemName());
            onSaleItemList.add(fetchOnSaleMapper);
//          45~47はラムダ式で下記のようにリファクタリング可能　
//          itemList.forEach(item -> onSaleItemList.add(new FetchOnSaleMapper(item.getItemCd(), item.getItemName())));
        }

        return onSaleItemList ;
    }

    /**
     * 商品マスタに商品追加
     *
     * @param itemCd   商品コード
     * @param itemName 商品名
     * @return 追加件数
     */
    //TODO:コメントの内容を元に実装する。
    @Transactional
    public int addItem(String itemCd, String itemName) {

        //商品マスタに既に登録されている全ての商品情報を抽出する
        //ItemDaoクラスのselectAllを呼び出す
       final List<Item> alreadyAddedItemList = itemDao.selectAll();

        for ( Item alreadyAddedItem : alreadyAddedItemList){
            //商品CDが重複する場合
            // ServiceValidationExceptionクラスのServiceValidationExceptionを使って、例外を投げる
            // 引数(messageCode)はmessages.propertiesの"E_SERVICE_003"とする。
            if ( itemCd.equals(alreadyAddedItem.getItemCd())) {
                throw new ServiceValidationException("E_SERVICE_003");
            }
            //商品名が重複する場合
            // ServiceValidationExceptionクラスのServiceValidationExceptionを使って、例外を投げる
            // 引数(messageCode)はmessages.propertiesの"E_SERVICE_004"とする。
            //商品マスタに登録されている商品と、これから追加しようとする商品が重複していないか判定する
            if ( itemName.equals(alreadyAddedItem.getItemName())) {
                throw new ServiceValidationException("E_SERVICE_004");
            }
        }

        //Itemクラスをインスタンス化(new)して、Itemクラスのメンバーに追加しようとしている商品の情報をセットする
        final Item item = new Item();
        item.setItemCd(itemCd);
        item.setItemName(itemName);
        item.setSellFlg("0");

        //ItemDaoクラスのinsertを使って、商品を商品マスタに追加する
        int addedNumber = itemDao.insert(item);

        //商品マスタに追加された件数を返却する
        return addedNumber;
    }

    /**
     * 商品マスタ更新
     *
     * @param itemList 更新商品リスト
     * @return 更新件数
     */

    @Transactional
    public int editItem(List<Item> itemList) {

        //更新対象の商品コードを元に、更新対象のアイテムコードを取得
        List<String> allItemCd = new ArrayList<String>();
        for (Item item : itemList){
            allItemCd.add(item.getItemCd());
        }
        int intMatch = itemDao.selectTargetCount(allItemCd);

        //登録されている件数 = 更新対象の件数かチェック
        // （一致しない場合はException[E_SERVICE_005=更新対象の商品が存在しません。]を投げる）
        if(!(itemList.size() == intMatch)){
            throw new ServiceValidationException("E_SERVICE_005");
        }

        //ItemDaoクラスのbatchUpdateを使って、商品マスタを更新する
        int[] editedItemArray = itemDao.batchUpdate(itemList);
        //editedItemArrayの各要素を合計して更新された件数を計算
        int editedItemNumber = java.util.Arrays.stream(editedItemArray).sum();

        //商品マスタで更新された件数を返却する
         return editedItemNumber;

    }
}
