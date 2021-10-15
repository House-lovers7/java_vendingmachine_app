<template>
    <div class="container mt-5">
        <div class="container">
            <div class="float-left">
                <h3>Rack List</h3>
            </div>
            <div class="float-right">
                <router-link to="/manager/rack/add" class="btn btn-primary">Add to Rack</router-link>
            </div>
        </div>
        <table class="table">
            <thead class="thead-light">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Name</th>
                    <th scope="col">Price</th>
                    <th scope="col">Stock / Max</th>
                    <th scope="col">Action</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(product, index) in this.$store.state.products" :key="index">
                    <th scope="row">{{ index + 1 }}</th>
                    <td>{{ product.itemName }}</td>
                    <td>¥ {{ product.price }}</td>
                    <td><span :class="{'text-danger': product.itemNumber === 0}">{{ product.itemNumber }}</span> / {{$store.state.maxStock}}</td>
                    <td><router-link :to="{ name: 'rack_edit', params: { id: product.itemCd } }" class="btn btn-secondary">Edit</router-link></td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script lang="ts">
import axios, { AxiosResponse } from 'axios';
import { Component, Vue } from 'vue-property-decorator';
import ServerResponse from '@/models/productResponse';
import Product from '@/models/product';
import HttpErrorResponse from '@/models/httpError';

@Component
export default class RackList extends Vue {
    private created() {
        axios.request<Product[]>(this.$store.getters.url('/api/rack/'))
        .then((ps: AxiosResponse<Product[]>) => {
            this.$store.commit('setProducts', ps.data);
        }).catch((e: HttpErrorResponse) => {
            alert('リストの取得に失敗しました。');
        });
    }
}
</script>
