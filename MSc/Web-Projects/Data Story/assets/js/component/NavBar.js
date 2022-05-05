Vue.component("nav-bar", {
  props: {
    menuItems: {
      type: Array,
      required: true,
    },
  },
  template: `
<b-navbar spaced>
    <template #brand>
        <b-navbar-item>
        <i class="fas fa-city"></i>
        <span class="brand-text">A Carbon Free story</span>
        </b-navbar-item>
    </template>
    <template #end>
        <b-navbar-item v-for="item in menuItems" :key="item" :href="'#'+item.replace(/ /g,'_')">
            {{ item }}
        </b-navbar-item>
    </template>
</b-navbar>
      `,
});
