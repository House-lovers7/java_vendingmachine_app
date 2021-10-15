<template>
  <div class="buy-button">
    <button type="button" class="form-control text-light" @click="buy()" :class="{'bg-dark': isSold, 'bg-warning': !isSold}">
      <span v-if="isSold">売り切れ</span>
      <span v-else>BUY</span>
    </button>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';

@Component
export default class BuyButton extends Vue {

  @Prop(Boolean)
  private isSold!: boolean;
  // 商品購入
  private buy() {
    this.$emit('buy');
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.buy-button {
  margin-bottom: -45px;
  margin-left: 15%;
  margin-right: 15%;
}
</style>
