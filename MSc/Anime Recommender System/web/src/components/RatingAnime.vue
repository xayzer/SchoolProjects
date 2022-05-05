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
      <div class="column has-background-white-bis">
        <b-taglist>
          <b-tag type="is-info" v-for="genre in anime.genre" :key="genre">
            {{ genre }}
          </b-tag>
        </b-taglist>
        <b-rate
          v-model="rating"
          :max="10"
          icon-pack="mdi"
          @change="ratingOnChange"
        ></b-rate>
        <b-button
          type="is-danger"
          icon-left="close-circle-outline"
          size="is-small"
          @click="removeRating"
        >
          remove rating
        </b-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      rating: 0,
    }
  },
  props: {
    anime: {
      type: Object,
      require: true,
    },
  },
  methods: {
    ratingOnChange(e) {
      this.$emit('change', {
        animeId: this.anime.anime_id,
        rating: e,
      })
    },
    removeRating() {
      this.rating = 0
      this.ratingOnChange(0)
    },
  },
}
</script>

<style scoped>
.container {
  margin-bottom: 1rem;
}
</style>
