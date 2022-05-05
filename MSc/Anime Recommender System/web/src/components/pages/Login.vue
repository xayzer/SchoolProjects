<template>
  <div class="page container">
    <div class="columns is-centered is-mobile">
      <div class="column is-full has-text-centered">
        <h1 class="title is-1">No Idea is fine</h1>
      </div>
    </div>
    <div class="columns is-centered is-mobile">
      <div
        class="column is-half-mobile is-one-third-tablet is-one-third-desktop"
      >
        <b-field
          label="Name"
          :message="errMsg"
          :type="errMsg.length > 0 ? 'is-danger' : ''"
        >
          <b-input v-model="name" @keydown.native="nameOnEnter"></b-input>
        </b-field>
      </div>
    </div>
    <div class="columns">
      <div class="column">
        <div class="buttons is-centered">
          <b-button
            type="is-primary"
            icon-left="login"
            @click="startBtnOnClick"
          >
            Start
          </b-button>
          <b-button type="is-danger" icon-left="cached" @click="resetOnClick">
            Reset
          </b-button>
        </div>
      </div>
    </div>
    <div class="columns">
      <div class="column has-text-centered">
        <div>This page is used for educational purpose.</div>
        <div>
          The animes' information, images and ratings are come from
          <a href="https://myanimelist.net/">myanimelist.net</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      name: '',
      errMsg: '',
    }
  },
  methods: {
    startBtnOnClick() {
      if (this.name.length < 4) {
        this.errMsg = 'Name has to be at least 4 characters long'
        return
      }

      const username = this.$store.getters.username

      if (username) {
        if (username != this.name) {
          this.resetOnClick(this.name)
        }
      }

      this.$store.dispatch('setName', { name: this.name })
      this.$router.push({ name: 'category' })
    },
    resetOnClick(preAssignName = '') {
      this.$store.dispatch('reset')
      this.name = preAssignName
    },
    nameOnEnter(e) {
      if (e.which !== 13) return
      this.startBtnOnClick()
    },
  },
  mounted() {
    const username = this.$store.getters.username

    if (username) {
      this.name = username
    }
  },
}
</script>

<style scoped>
.page.container {
  background-color: #f7faff;
  height: 40vh;
  margin-top: 25vh;
}
</style>
