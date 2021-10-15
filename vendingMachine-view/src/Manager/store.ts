import Vue from 'vue';
import Vuex from 'vuex';
import Product from '@/models/product';
import Item from '@/models/item';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    products: [{rackNo: '00', itemCd: '00000', itemName: '', itemNumber: 0, price: 0}],
    items: [{itemCd: '00000', itemName: '', sellFlg: '0'}],
    maxStock: 10,
  },
  mutations: {
    setProducts: (state, products: Product[]) => {
      state.products = products;
    },
    setItems: (state, items: Item[]) => {
      state.items = items;
    },
  },
  actions: {

  },
  getters: {
    url: (state) => (path: string) => {
      return 'http://192.168.33.11:8080' + path;
    },
    emptyProduct: (state) => {
      return {rackNo: '00', itemCd: '00000', itemName: '未設定', price: 0, itemNumber: 0};
    },
    emptyItem: (state) => {
      return {itemCd: '00000', itemName: '未設定', sellFlg: '0'};
    },
    getProduct: (state) => (itemCd: string) => {
      const product = state.products.find((p: Product) => p.itemCd === itemCd);
      return product;
    },
    getItem: (state) => (itemCd: string) => {
      const item = state.items.find((i: Item) => i.itemCd === itemCd);
      return item;
    },
  },
});
