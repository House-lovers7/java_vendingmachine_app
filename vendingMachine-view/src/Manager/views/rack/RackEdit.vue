<template>
    <div class="container mt-5">
        <h4>Edit Product</h4>
        <p class="alert alert-light">Please input Product information!</p>
        <div class="form-group row">
            <label for="name" class="col-lg-2 col-form-label">Name</label>
            <div class="col-lg-10">
                <label>{{product.itemName}}</label>
            </div>
        </div>
        <div class="form-group row">
            <label for="price" class="col-lg-2 col-form-label">Price</label>
            <div class="col-lg-2">
                <div class="input-group">
                    <div class="input-group-prepend">
                        <span class="input-group-text">¥</span>
                    </div>
                    <input type="text" class="form-control" id="price" name="price" placeholder="Example: 150" v-model="product.price" @blur="price = $event.target.value">
                </div>
            </div>
        </div>
        <div class="form-group row">
            <label for="stock" class="col-lg-2 col-form-label">Stock(Max {{this.$store.state.maxStock}})</label>
            <div class="col-lg-2">
                <div class="input-group">
                    <input type="text" class="form-control" id="stock" name="stock" placeholder="Example: 10" v-model="product.itemNumber" @blur="itemNumber = $event.target.value" number>
                </div>
            </div>
        </div>
        <router-link to="/manager/rack" class="btn btn-secondary mr-2">Cancel</router-link>
        <button type="button" class="btn btn-primary" v-on:click="edit">Submit</button>
    </div>
</template>

<script lang="ts">
import axios from 'axios';
import { Component, Vue } from 'vue-property-decorator';
import HttpErrorResponse from '@/models/httpError';

@Component
export default class RackEdit extends Vue {

    private product = this.$store.getters.emptyProduct;
    private itemNumber = 0;
    private price = 0;

    private created() {
        const itemCd = this.$route.params.id;
        const product = this.$store.getters.getProduct(itemCd);
        this.product = product;
        this.price = this.product.price;
        this.itemNumber = this.product.itemNumber;
    }

    private edit() {

        if (this.itemNumber > this.$store.state.maxStock) {
            alert('Stock数は' + this.$store.state.maxStock + '以下に設定してください。');
            return;
        }

        axios.patch(this.$store.getters.url('/api/rack/edit'), {
            editStockItemDtoList: [{
                rackNo: this.product.rackNo,
                itemCd: this.product.itemCd,
                itemNumber: this.itemNumber,
                price: this.price,
            }],
        }).then(() => {
            this.$router.push({name: 'rack'});
        }).catch((e: HttpErrorResponse) => {
            alert(e.response.data[0].errMsg);
        });
    }
}
</script>
