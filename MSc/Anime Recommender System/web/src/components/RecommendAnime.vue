<template>
  <div class="container">
    <h5 class="title is-6">{{ anime.name }}</h5>
    <div class="columns is-mobile">
      <div class="column is-one-quarter">
        <b-image
          :src="anime.thumbnail_link"
          alt="Anime Cover"
          ratio="112by169"
        ></b-image>
      </div>
      <div class="column has-background-white-ter">
        <b-taglist>
          <b-tag type="is-info" v-for="genre in anime.genre" :key="genre">
            {{ genre }}
          </b-tag>
        </b-taglist>
        <div class="buttons" v-if="isThumbHidden == false">
          <b-button
            :type="isThumbUp ? 'is-info' : ''"
            icon-left="thumb-up-outline"
            @click="thumbUpPress"
          ></b-button>
          <b-button
            :type="isThumbDown ? 'is-danger' : ''"
            icon-left="thumb-down-outline"
            @click="thumbDownPress"
          ></b-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isThumbUp: false,
      isThumbDown: false,
      isThumbHidden: false,
    }
  },
  props: {
    anime: {
      type: Object,
      require: true,
    },
  },
  methods: {
    resetThumbs() {
      this.isThumbUp = false
      this.isThumbDown = false
    },
    hideThumbs() {
      this.isThumbHidden = true
    },
    thumbUpPress() {
      this.isThumbUp = !this.isThumbUp
      if (this.isThumbUp) {
        this.isThumbDown = false
      }
      this.emitEvent()
    },
    thumbDownPress() {
      this.isThumbDown = !this.isThumbDown
      if (this.isThumbDown) {
        this.isThumbUp = false
      }
      this.emitEvent()
    },
    emitEvent() {
      let feedback = this.isThumbUp ? 1 : 0
      feedback = this.isThumbDown ? -1 : feedback

      this.$emit('change', {
        animeId: this.anime.anime_id,
        feedback,
      })
    },
  },
}
</script>

<style scoped>
.container {
  margin-bottom: 1rem;
}
</style>
