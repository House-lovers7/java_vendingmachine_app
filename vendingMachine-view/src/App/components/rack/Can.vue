<template>
  <div class="can border border-secondary bg-secondary">{{ name }}</div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';

@Component
export default class Can extends Vue {
  @Prop(String)
  private name!: string;
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.can {
  height: calc(100% / 1.2);
  margin: 1% 15% 0 15%;
}
</style>
