<template>
  <footer class="fixed-bottom">
    <div class="row h-60">
      <div class="text-light ml-2 mr-5">投入金額を入力</div>
      <button type="button" class="btn btn-lg btn-primary col-lg-1" @click="plus(10)">¥10</button>
      <button type="button" class="btn btn-lg btn-primary col-lg-1" @click="plus(100)">¥100</button>
      <button type="button" class="btn btn-lg btn-primary col-lg-1" @click="plus(1000)">¥1,000</button>
      <button type="button" class="btn btn-lg btn-primary col-lg-1" @click="plus(10000)">¥10,000</button>
      <div class="border bg-light col-lg-2 rounded mr-5 text-right my-auto">
        <div class="h3">{{this.$store.state.money}}</div>
      </div>
      <button type="button" class="btn btn-lg btn-secondary col-lg-2" @click="reset()">払い戻し</button>
    </div>
    <div class="row h-40 text-light mt-1">
      <span class="ml-2 mr-3">購入品リスト</span>
      <div class="mr-3" v-for="product in this.$store.state.purchaced" :key="product.itemCd + product.rackNo" v-if="product.itemCd != '00000'">
        {{product.itemName}}:{{product.num}}個
      </div>
    </div>
  </footer>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component
export default class Footer extends Vue {

  // 現金投入
  private plus(input: string): void {
    this.$store.commit('plus', parseInt(input, 10));
  }
  // 払い戻し
  private reset(): void {
    this.$store.commit('reset');
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
footer {
  background-color: black;
  height: 100px;
  padding: 1%;
}

button {
  margin-right: 1%;
}

.container {
  margin: 1%
}
</style>
