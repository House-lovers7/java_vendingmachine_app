<template>
  <div class="out-can border rounded bg-light mx">
    <!-- 取り出し口 -->
    <img src="../assets/output.png" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component
export default class Output extends Vue {}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.out-can {
  height: 300px;
  margin-top: 80px;
  width: 100%;
}

img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center center;
}
</style>
