import Vue from 'vue';
import Router from 'vue-router';
import RackList from './views/rack/RackList.vue';
import RackAdd from './views/item/ItemAdd.vue';
import RackEdit from './views/rack/RackEdit.vue';
import ItemList from './views/item/ItemList.vue';
import ItemAdd from './views/item/ItemAdd.vue';
import ItemEdit from './views/item/ItemEdit.vue';

Vue.use(Router);

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/manager/rack',
      name: 'rack',
      component: RackList,
    },
    {
      path: '/manager/rack/add',
      name: 'rack_add',
      component: RackAdd,
    },
    {
      path: '/manager/rack/edit/:id',
      name: 'rack_edit',
      component: RackEdit,
    },
    {
      path: '/manager/items',
      name: 'items',
      component: ItemList,
    },
    {
      path: '/manager/items/add',
      name: 'item_add',
      component: ItemAdd,
    },
    {
      path: '/manager/items/edit/:id',
      name: 'item_edit',
      component: ItemEdit,
    },
  ],
});
