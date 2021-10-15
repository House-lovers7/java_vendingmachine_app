<template>
    <div class="container mt-5">
        <h4>Edit Item</h4>
        <p class="alert alert-light">Please input Item information!</p>
        <div class="form-group row">
            <label for="name" class="col-lg-2 col-form-label">Item CD</label>
            <div class="col-lg-10">
                <input type="text" class="form-control" id="itemCd" name="itemCd" placeholder="Example: 10000" v-model="item.itemCd">
            </div>
        </div>
        <div class="form-group row">
            <label for="name" class="col-lg-2 col-form-label">Name</label>
            <div class="col-lg-10">
                <input type="text" class="form-control" id="name" name="name" placeholder="Example: Coca Cola" v-model="item.itemName">
            </div>
        </div>
        <div class="form-group row">
            <label class="col-lg-2 col-form-label">Sell Status</label>
            <div class="col-lg-10">
                <div class="form-check form-check-inline">
                    <input type="radio" id="sellFlg_stop" class="form-check-input" name="sellFlg" value="0" v-model="item.sellFlg">
                    <label for="sellFlg_stop" class="form-check-label">Stop</label>
                </div>
                <div class="form-check form-check-inline">
                    <input type="radio"  id="sellFlg_onSale" class="form-check-input" name="sellFlg" value="1" v-model="item.sellFlg">
                    <label for="sellFlg_onSale" class="form-check-label">On Sale</label>
                </div>
            </div>
        </div>
        <router-link to="/manager/items" class="btn btn-secondary mr-2">Cancel</router-link>
        <button type="button" class="btn btn-primary" v-on:click="edit">Submit</button>
    </div>
</template>

<script lang="ts">
import axios from 'axios';
import HttpErrorResponse from '@/models/httpError';
import { Component, Vue } from 'vue-property-decorator';

@Component
export default class ItemEdit extends Vue {

    private item = this.$store.getters.emptyItem;

    private created() {
        const itemCd = this.$route.params.id;
        const item = this.$store.getters.getItem(itemCd);
        this.item = item;
    }

    private edit() {

        axios.patch(this.$store.getters.url('/api/item/edit'), {
            itemList: [{
                itemCd: this.item.itemCd,
                itemName: this.item.itemName,
                sellFlg: this.item.sellFlg,
            }],
        }).then(() => {
            this.$router.push({name: 'items'});
        }).catch((e: HttpErrorResponse) => {
            alert(e.response.data[0].errMsg);
        });
    }
}
</script>
