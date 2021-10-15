<template>
  <div id="manager">
    <Header></Header>
    <router-view/>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import Header from '@/Manager/components/Header.vue';

@Component({
    components: {
        Header,
    },
})
export default class Manager extends Vue {}
</script>
