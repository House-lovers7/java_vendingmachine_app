<template>
  <div>
    <div class="ml-2 mr-2">
      <div v-for="(products, index) in this.$store.state.dividedProducts" :key="index" class="row pull-left full-width mt-5">
        <div v-for="product in products" :key="product.id" class="col-lg-2 rack">
          <div class="display mb-4 mt-4">
            <Can :name="product.itemName"></Can>
            <Unit :price="product.price"></Unit>
          </div>
          <BuyButton :isSold="product.itemNumber === 0" v-on:buy="buy(product)"></BuyButton>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Can from '@/App/components/rack/Can.vue'; // @ is an alias to /src
import Unit from '@/App/components/rack/Unit.vue';
import BuyButton from '@/App/components/rack/BuyButton.vue';
import Product from '@/models/product';
import HttpErrorResponse from '@/models/httpError';
import axios, { AxiosResponse } from 'axios';

@Component({
  components: {
    Can,
    Unit,
    BuyButton,
  },
})
export default class Display extends Vue {
  private created() {
    axios.request<Product[]>(this.$store.getters.url('/api/rack/')).then((ps: AxiosResponse<Product[]>) => {
      // 商品の配列を6つずつに分割(products)。6つ埋まったら表示用配列に格納(dividedProducts)。
      const pureProducts = ps.data;
      const dividedProducts: Product[][] = [];
      let products: Product[] = [];
      pureProducts.forEach((product: Product) => {
        products.push(product);
        if (products.length === 6) {
          dividedProducts.push(products);
          products = [];
        }
      });
      // 3段6つに満たない場合は空の商品情報を挿入
      if (products.length < 6 && dividedProducts.length < 3) {
        let recordNum = 3 - dividedProducts.length;
        while (recordNum !== 0) {
          let productsNumPerRecord = 6 - products.length;
          while (productsNumPerRecord !== 0) {
            products.push(this.$store.getters.emptyItem);
            productsNumPerRecord--;
          }
          dividedProducts.push(products);
          products = [];
          recordNum--;
        }
      }
      this.$store.commit('setProducts', dividedProducts);
    }).catch((e: HttpErrorResponse) => {
      alert(e.response.data[0].errMsg);
    });
  }
  // 商品購入
  private buy(product: Product) {
    // 購入可能かをチェック
    // (price = 0はダミーデータ, itemNumber数1以上でしか買えない, 購入後の資金がマイナスにならないようにする)
    if (product.price > 0 && product.itemNumber > 0 && this.$store.state.money - product.price >= 0) {
      axios.patch(this.$store.getters.url('/api/rack/buy'), {
        rackNo: product.rackNo,
      }).then((itemName: AxiosResponse<string>) => {
        this.$store.commit('buy', product);
      }).catch((e: HttpErrorResponse) => {
        alert(e.response.data[0].errMsg);
      });
    } else {
      // エラー処理
      if (product.price === 0) {
        alert('商品が設定されていないボタンです。');
      } else if (product.itemNumber === 0) {
        alert('選択された商品は売り切れです。');
      } else {
        alert('残金が不足しています。お金を入れてください。');
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.rack {
  background-color: #eeecec;
}

.display {
  background-color: #eeecec;
  height: 250px;
}

.button-area {
    height: 20px;
    margin: 1% 0 0 0;
}
</style>
