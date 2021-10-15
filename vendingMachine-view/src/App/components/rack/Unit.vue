<template>
  <div class="unit">{{ price }}円</div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';

@Component
export default class Unit extends Vue {
  @Prop(Number)
  private price!: number;
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.unit {
  text-align: center;
}
</style>
