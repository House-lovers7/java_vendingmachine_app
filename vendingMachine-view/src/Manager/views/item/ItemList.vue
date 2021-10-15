<template>
    <div class="container mt-5">
        <div class="container">
            <div class="float-left">
                <h3>Item List</h3>
            </div>
            <div class="float-right">
                <router-link to="/manager/items/add" class="btn btn-primary">Add Item</router-link>
            </div>
        </div>
        <table class="table">
            <thead class="thead-light">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">ItemCd</th>
                    <th scope="col">Name</th>
                    <th scope="col">Sell Status</th>
                    <th scope="col">Action</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(item, index) in this.$store.state.items" :key="index">
                    <th scope="row">{{ index + 1 }}</th>
                    <td>{{ item.itemCd }}</td>
                    <td>{{ item.itemName }}</td>
                    <td>
                        <span class="badge badge-success" v-if="item.sellFlg === '1'">On Sale</span>
                        <span class="badge badge-danger" v-else>Stop</span>
                    </td>
                    <td><router-link :to="{ name: 'item_edit', params: { id: item.itemCd } }" class="btn btn-secondary">Edit</router-link></td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script lang="ts">
import axios, { AxiosResponse } from 'axios';
import { Component, Vue } from 'vue-property-decorator';
import Item from '@/models/item';
import HttpErrorResponse from '@/models/httpError';

@Component
export default class ItemList extends Vue {

    private created() {
        axios.request<Item[]>(this.$store.getters.url('/api/item/'))
        .then((items: AxiosResponse<Item[]>) => {
            this.$store.commit('setItems', items.data);
        }).catch((e: HttpErrorResponse) => {
            alert('リストの取得に失敗しました。');
        });
    }
}
</script>
