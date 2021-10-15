<template>
  <div id="content-area" class="border border-dark bg-danger rounded">
    <div id="content">
      <Display></Display>
      <Output></Output>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Display from '@/App/components/Display.vue';
import Output from '@/App/components/Output.vue';

@Component({
  components: {
    Display,
    Output,
  },
})
export default class Machine extends Vue {}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
#content-area {
    /* background-color: #db7272; */
    /* border: 1px solid black; */
    margin: 2% 10% 10% 10%;
}

#content {
    height: 100%;
    margin: 5%;
}
</style>
