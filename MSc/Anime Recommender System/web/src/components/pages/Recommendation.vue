<template>
  <div class="page container">
    <b-message type="is-info" class="mb-5">
      <h6 class="has-text-weight-bold">
        Recommended items Step {{ currentStep }}/2
      </h6>
      <div>
        These items recommend to you because {{ explaination }} with
        {{ methodDisplay }}
      </div>
      <div v-if="!isFeedbackGiven">
        Please give feedback for recommended items, we will provide you updated
        recommedned based on your feedback
      </div>
      <div v-else>
        Here are the updated recommended items, please give us feedback you like
        them or not. Thank you.
      </div>

      <b-button type="is-primary" class="confirm-btn" @click="confirmRecommend">
        Give feedback
      </b-button>
    </b-message>
    <div class="columns" v-for="rowNo in noOfRow" :key="rowNo">
      <div
        class="column is-half"
        v-for="colNo in noOfCol(rowNo)"
        :key="colNo * rowNo"
      >
        <RecommendAnime
          :anime="anime(rowNo, colNo)"
          @change="feedbackChange"
          :ref="'rating-anime-' + anime(rowNo, colNo).anime_id"
        />
      </div>
    </div>
  </div>
</template>

<script>
import RecommendAnime from '../RecommendAnime'

export default {
  components: {
    RecommendAnime,
  },
  data() {
    return {
      itemPerRow: 2,
      animes: [],
      feedback: [],
      explaination: null,
      method: null,
      methodDisplay: null,
      isFeedbackGiven: false,
    }
  },
  computed: {
    noOfRow() {
      if (this.animes.length == 0) {
        return 0
      }
      return Math.floor(this.animes.length / this.itemPerRow + 1)
    },
    currentStep() {
      return this.isFeedbackGiven ? 2 : 1
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
    feedbackChange({ animeId, feedback }) {
      // Remove items that have same anime_id
      this.feedback = this.feedback.filter((e) => e.anime_id !== animeId)

      // Set anime_id to match API
      // force giving feedback
      if (feedback != 0) {
        this.feedback.push({ anime_id: animeId, feedback })
      }
    },
    async confirmRecommend() {
      if (this.feedback.length < 5) {
        this.$buefy.toast.open({
          message: 'Please give feedback to all items',
          type: 'is-danger',
        })
        return
      }

      if (this.isFeedbackGiven) {
        // post to backend again
        await this.loadAnime(true)
        this.$router.push({ name: 'thank' })
      } else {
        await this.loadAnime(true)
        for (let i = 0; i < this.animes.length; i++) {
          const animeId = this.animes[i].anime_id
          const recommendAnimeComponent = this.$refs['rating-anime-' + animeId]
          recommendAnimeComponent[0].resetThumbs()
        }
        this.isFeedbackGiven = true
      }
    },
    async loadAnime(withFeedback = false) {
      const user = this.$store.getters.user

      const loadingComponent = this.$buefy.loading.open({
        container: null, //this.isFullPage ? null : this.$refs.element.$el
      })

      let httpCall = null

      if (withFeedback) {
        httpCall = this.$http.post('anime/recommend', {
          user,
          method: this.method,
          anime_feedback: this.feedback,
          count: this.isFeedbackGiven ? 2 : 1,
        })
      } else {
        const method = this.$route.query.method
          ? this.$route.query.method
          : null
        httpCall = this.$http.get('anime/recommend', {
          params: {
            username: user.username,
            genre_list: user.category.join(','),
            method,
          },
        })
      }

      const result = await httpCall

      // Reset feedback
      this.feedback = []

      loadingComponent.close()
      if (this.isFeedbackGiven) return

      this.animes = result.data.animes
      this.explaination = result.data.msg
      this.method = result.data.method.name
      this.methodDisplay = result.data.method.display

      // Debug use
      console.log(this.animes)
    },
  },
  async mounted() {
    this.loadAnime()
  },
}
</script>

<style scoped>
.page.container {
  margin-top: 2vh;
}
.confirm-btn {
  float: right;
}
</style>
