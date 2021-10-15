import Vue from 'vue';
import Vuex from 'vuex';
import Product from '@/models/product';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    money: 0,
    dividedProducts: [[{rackNo: '00', itemCd: '00000', itemName: '', itemNumber: 0, price: 0}]],
    purchaced: [{rackNo: '00', itemCd: '00000', itemName: '', num: 0}],
  },
  mutations: {
    // 商品リストをセット
    setProducts(state, dProducts: Product[][]) {
      state.dividedProducts = dProducts;
    },
    // 商品購入
    buy(state, product: Product) {
        // 所持金減算
        state.money -=  product.price;
        // 購入品リストに追加
        const isContain = state.purchaced.some((el) => {
          return el.rackNo === product.rackNo && el.itemCd === product.itemCd;
        });
        if (!isContain) {
          // すでに購入品リストにある場合は個数を加算
          state.purchaced.push({rackNo: product.rackNo, itemCd: product.itemCd, itemName: product.itemName, num: 1});
        } else {
          // 初めて購入する商品の場合は購入品リストに新規追加
          const index = state.purchaced.findIndex((el) => {
            return el.rackNo === product.rackNo && el.itemCd === product.itemCd;
          });
          state.purchaced[index].num++;
        }
        // ストック数減算
        let dividedNum = 0;
        let productsNum = 0;
        state.dividedProducts.forEach((products, index) => {
          // 購入した商品のIDと合致するデータのindexを取得
          const num = products.findIndex((el) => {
            return el.rackNo === product.rackNo && el.itemCd === product.itemCd;
          });
          // 配列のindexを設定
          if (num >= 0) {
            productsNum = num;
            dividedNum = index;
          }
        });
        // 指定したindexにある商品のstock数を減算
        state.dividedProducts[dividedNum][productsNum].itemNumber--;
    },
    // 現金投入
    plus(state, money: number) {
      state.money += money;
    },
    // 現金払い戻し
    reset(state) {
      state.money = 0;
    },
  },
  actions: {

  },
  getters: {
    url: (state) => (path: string) => {
      return 'http://192.168.33.11:8080' + path;
    },
    emptyItem: (state) => {
      return {rackNo: '00', itemCd: '00000', itemName: '未設定', price: 0, itemNumber: 0};
    },
  },
});
