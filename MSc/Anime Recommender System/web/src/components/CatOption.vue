<template>
  <div class="container">
    <div class="columns">
      <div class="column">
        <b-field>
          <b-checkbox v-model="selected" @input="checkBoxOnClick">{{
            category.title
          }}</b-checkbox>
        </b-field>
      </div>
    </div>

    <div class="columns is-mobile">
      <div
        class="column is-one-third"
        v-for="(cover, idx) in category.covers"
        :key="idx"
      >
        <b-image :src="cover" ratio="112by169"></b-image>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      selected: this.isSelected,
    }
  },
  props: {
    category: {
      type: Object,
      require: true,
    },
    isSelected: {
      type: Boolean,
      default: false,
    },
  },
  methods: {
    uncheck() {
      this.selected = false
    },
    check() {
      this.selected = true
    },
    checkBoxOnClick() {
      this.$emit('change', {
        selected: this.selected,
        title: this.category.title,
      })
    },
  },
}
</script>

<style scoped>
.container {
  padding: 3px;
  border: 1px solid;
  border-color: #ddd;
  border-radius: 5px;
}
</style>
