<template>
  <div>
    <Machine></Machine>
    <Footer></Footer>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Machine from '@/App/components/Machine.vue';
import Footer from '@/App/components/Footer.vue';

@Component({
  components: {
    Machine,
    Footer,
  },
})
export default class VendingMachine extends Vue {}
</script>
