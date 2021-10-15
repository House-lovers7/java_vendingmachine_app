<template>
    <div class="container mt-5">
        <h4>Add Item</h4>
        <p class="alert alert-light">Please input new Product information!</p>
        <form class="mt-5" method="post" action="">
            <div class="form-group row">
                <label for="itemCd" class="col-lg-2 col-form-label">Item CD</label>
                <div class="col-lg-10">
                    <input type="text" class="form-control" id="itemCd" name="itemCd" placeholder="Example: 10000" v-model="itemCd">
                </div>
            </div>
            <div class="form-group row">
                <label for="name" class="col-lg-2 col-form-label">Name</label>
                <div class="col-lg-10">
                    <input type="text" class="form-control" id="name" name="name" placeholder="Example: Coca Cola" v-model="itemName">
                </div>
            </div>
            <router-link to="/manager/items" class="btn btn-secondary mr-2">Cancel</router-link>
            <button type="button" class="btn btn-primary" v-on:click="add">Add</button>
        </form>
    </div>
</template>

<script lang="ts">
import axios from 'axios';
import { Component, Vue } from 'vue-property-decorator';
import HttpErrorResponse from '@/models/httpError';

@Component
export default class ItemAdd extends Vue {

    private itemCd: string = '';
    private itemName: string = '';

    private add() {
        axios.post(this.$store.getters.url('/api/item/add'), {
            itemCd: this.itemCd,
            itemName: this.itemName,
        }).then(() => {
            this.$router.push({name: 'items'});
        }).catch((e: HttpErrorResponse) => {
            alert(e.response.data[0].errMsg);
        });
    }
}
</script>

<style lang="scss">
</style>

