<template>
  <div class="page container">
    <div class="container">
      <b-message type="is-warning" class="mb-5">
        <div>Following results are filtered by the selected genre(s)</div>
        <div>Assume you watched the following animes and give the rating</div>
        <b-button
          class="has-background-success-light has-text-info-dark confirm-btn"
          @click="confirmRating"
        >
          Confirm
        </b-button>
      </b-message>
    </div>

    <div class="columns" v-for="rowNo in noOfRow" :key="rowNo">
      <div class="column" v-for="colNo in noOfCol(rowNo)" :key="colNo * rowNo">
        <RatingAnime
          :anime="anime(rowNo, colNo)"
          :ref="'rating-anime-' + anime(rowNo, colNo).anime_id"
          @change="ratingOnChange"
        />
      </div>
    </div>
  </div>
</template>
<script>
import RatingAnime from '../RatingAnime'

export default {
  components: {
    RatingAnime,
  },
  data() {
    return {
      rating: [],
      animes: [],
      itemPerRow: 2,
      isSticky: false,
      scrollPosition: 0,
    }
  },
  computed: {
    noOfRow() {
      if (this.animes.length == 0) {
        return 0
      }
      return Math.floor(this.animes.length / this.itemPerRow + 1)
    },
  },
  methods: {
    anime(rowNo, colNo) {
      const colBase = (rowNo - 1) * this.itemPerRow
      return this.animes[colBase + (colNo - 1)]
    },
    noOfCol(rowNo) {
      if (rowNo == 0) return 0
      rowNo -= 1 // Adjust the loop start at 1 instead of 0
      const itemsLeft = this.animes.length - rowNo * this.itemPerRow
      return itemsLeft > this.itemPerRow ? this.itemPerRow : itemsLeft
    },
    ratingOnChange({ animeId, rating }) {
      // Remove items that have same anime_id
      this.rating = this.rating.filter((e) => e.anime_id !== animeId)

      if (rating != 0) {
        // Set anime_id to match API
        this.rating.push({ anime_id: animeId, rating })
      }
    },
    async confirmRating() {
      console.log(this.rating)
      if (this.rating.length < 5) {
        this.$buefy.toast.open({
          message: 'Please rate at least 5 animes',
          type: 'is-danger',
        })
        return
      }

      const result = await this.$http.post('anime/rating', {
        anime_rating: this.rating,
        user: this.$store.getters.user,
      })
      // debug use
      console.log(result)

      this.$router.push({ name: 'recommendation' })
    },
    handleScroll() {
      this.scrollPosition = window.scrollY
      if (this.scrollPosition >= 100) {
        this.isSticky = true
      } else {
        this.isSticky = false
      }
    },
  },
  async mounted() {
    const user = this.$store.getters.user

    // Debug use
    console.log(user.category.join(','))

    const loadingComponent = this.$buefy.loading.open({
      container: null, //this.isFullPage ? null : this.$refs.element.$el
    })

    const result = await this.$http.get('anime/rating', {
      params: {
        genre_list: user.category.join(','),
      },
    })

    loadingComponent.close()

    this.animes = result.data

    // Debug use
    console.log(this.animes)
  },
  created() {
    window.addEventListener('scroll', this.handleScroll)
  },
  destroyed() {
    window.removeEventListener('scroll', this.handleScroll)
  },
}
</script>

<style scoped>
.page.container {
  margin-top: 4vh;
}

@media only screen and (max-width: 768px) {
  .page.container {
    padding-right: 0.9rem;
    padding-left: 0.9rem;
  }
}

.confirm-btn {
  float: right;
}
</style>
