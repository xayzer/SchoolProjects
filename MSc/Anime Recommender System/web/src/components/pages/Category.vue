<template>
  <div class="container">
    <b-navbar :fixed-top="isSticky">
      <template #start>
        <div
          class="notification has-background-primary-light is-centered has-text-centered"
          v-if="genreSelected.length == 0"
        >
          <span>Please Select Anime Genre(s)</span>
        </div>

        <div
          class="notification has-background-link-light genre_selected-list"
          v-else
        >
          <b-taglist>
            <b-tag
              class="genre-tag"
              v-for="opt in genreSelected"
              :key="opt"
              type="is-primary"
              closable
              aria-close-label="Close tag"
              @close="categoryChange({ selected: false, title: opt })"
              rounded
            >
              {{ opt }}
            </b-tag>
          </b-taglist>
        </div>
      </template>
      <template v-if="genreSelected.length != 0" #end>
        <b-navbar-item tag="div">
          <b-button
            type="is-primary"
            icon-left="check"
            class="mr-4"
            @click="categoryConfirm"
          >
            Done
          </b-button>
          <b-button
            type="is-danger"
            icon-left="close-circle-outline"
            class="mr-4"
            @click="clearCategory"
          >
            Clear
          </b-button>
        </b-navbar-item>
      </template>
    </b-navbar>
    <div class="columns" v-for="rowNo in noOfRow" :key="rowNo">
      <div
        class="column is-one-quarter"
        v-for="colNo in noOfCol(rowNo)"
        :key="colNo * rowNo"
      >
        <CatOption
          :category="category(rowNo, colNo)"
          :is-selected="isCategorySelected(category(rowNo, colNo).title)"
          @change="categoryChange"
          :ref="'cat-option-' + category(rowNo, colNo).title"
        />
      </div>
    </div>
  </div>
</template>

<script>
import CatOption from '../CatOption'

export default {
  components: {
    CatOption,
  },
  data() {
    return {
      genreOptions: [],
      genreSelected: [],
      catPerRow: 4,
      isSticky: false,
      scrollPosition: 0,
    }
  },
  computed: {
    noOfRow() {
      if (this.genreOptions.length == 0) {
        return 0
      }
      return Math.floor(this.genreOptions.length / this.catPerRow + 1)
    },
  },
  methods: {
    category(rowNo, colNo) {
      const colBase = (rowNo - 1) * this.catPerRow
      return this.genreOptions[colBase + (colNo - 1)]
    },
    noOfCol(rowNo) {
      if (rowNo == 0) return 0
      rowNo -= 1 // Adjust the loop start at 1 instead of 0
      const itemsLeft = this.genreOptions.length - rowNo * this.catPerRow
      return itemsLeft > this.catPerRow ? this.catPerRow : itemsLeft
    },
    categoryChange({ selected, title }) {
      if (selected) {
        if (this.genreSelected.length >= 5) {
          setTimeout(() => {
            const catOptionComponent = this.$refs['cat-option-' + title]
            catOptionComponent[0].uncheck()
          }, 200)

          this.$buefy.toast.open({
            message: 'Maximum genres could be selected is 5',
            type: 'is-danger',
          })
        } else {
          this.genreSelected.push(title)
        }
      } else {
        const catOptionComponent = this.$refs['cat-option-' + title]
        catOptionComponent[0].uncheck()

        const index = this.genreSelected.indexOf(title)

        if (index != -1) {
          this.genreSelected.splice(index, 1)
        }
      }
    },
    categoryConfirm() {
      this.$store.dispatch('setCategory', { newCat: this.genreSelected })
      this.$router.push({ name: 'rating' })
    },
    clearCategory() {
      for (let i = 0; i < this.genreSelected.length; i++) {
        const title = this.genreSelected[i]
        const catOptionComponent = this.$refs['cat-option-' + title]
        catOptionComponent[0].uncheck()
      }

      this.genreSelected = []
    },
    isCategorySelected(title) {
      return this.genreSelected.indexOf(title) !== -1
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
    const reload = this.$route.query.reload ? this.$route.query.reload : null

    const loadingComponent = this.$buefy.loading.open({
      container: null, //this.isFullPage ? null : this.$refs.element.$el
    })

    const result = await this.$http.get('genre', {
      params: {
        reload,
      },
    })

    this.genreOptions = result.data

    const selectedCategory = this.$store.getters.category
    if (selectedCategory) {
      this.genreSelected = selectedCategory
    }

    loadingComponent.close()
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
.container {
  margin-top: 5vh;
}

.genre_selected-list {
  min-width: 280px;
}
</style>
