<template>
  <div class="page container">
    <div class="buttons">
      <b-button
        type="is-primary"
        icon-left="download-circle-outline"
        @click="downloadFeedback"
        >Feedback</b-button
      >
      <b-button
        type="is-primary"
        icon-left="download-circle-outline"
        @click="downloadRating"
        >Rating</b-button
      >
    </div>
    <!-- KNN -->
    <template v-if="knn">
      <div class="columns">
        <div class="column">
          <h4 class="title is-4">KNN</h4>
          <h5 class="subtitle">1st Recommender Result Analysis</h5>
          <div class="columns">
            <div class="column">
              No. of Response: {{ getStatValue('knn', 'first', 'total') }}
            </div>
            <div class="column">
              Mean: {{ getStatValue('knn', 'first', 'mean') }}
            </div>
          </div>
          <div class="columns">
            <div class="column">
              <LineChart
                :method-data="knn.data.first"
                chart-id="knn_first"
                dataset-id-key="knn_first_data"
              />
            </div>
          </div>
        </div>
        <div class="column">
          <h4 class="title is-4">KNN</h4>
          <h5 class="subtitle">2nd Recommender Result Analysis</h5>
          <div class="columns">
            <div class="column">
              No. of Response: {{ getStatValue('knn', 'second', 'total') }}
            </div>
            <div class="column">
              Mean: {{ getStatValue('knn', 'second', 'mean') }}
            </div>
          </div>
          <div class="columns">
            <div class="column">
              <LineChart
                :method-data="knn.data.second"
                chart-id="knn_second"
                dataset-id-key="knn_second_data"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="columns">
        <div class="column">
          <b-message
            title="KNN 1st VS KNN 2nd Pair T-test result"
            :type="knn.pValue < 0 ? 'is-danger' : 'is-success'"
          >
            {{ knn.testResult }}
          </b-message>
        </div>
      </div>
    </template>
    <!--- Content --->
    <template v-if="content">
      <div class="columns">
        <div class="column">
          <h4 class="title is-4">Content Base</h4>
          <h5 class="subtitle">1st Recommender Result Analysis</h5>
          <div class="columns">
            <div class="column">
              No. of Response: {{ getStatValue('content', 'first', 'total') }}
            </div>
            <div class="column">
              Mean: {{ getStatValue('content', 'first', 'mean') }}
            </div>
          </div>
          <div class="columns">
            <div class="column">
              <LineChart
                :method-data="content.data.first"
                chart-id="content_first"
                dataset-id-key="content_first_data"
              />
            </div>
          </div>
        </div>
        <div class="column">
          <h4 class="title is-4">Content Base</h4>
          <h5 class="subtitle">2nd Recommender Result Analysis</h5>
          <div class="columns">
            <div class="column">
              No. of Response: {{ getStatValue('content', 'second', 'total') }}
            </div>
            <div class="column">
              Mean: {{ getStatValue('content', 'second', 'mean') }}
            </div>
          </div>
          <div class="columns">
            <div class="column">
              <LineChart
                :method-data="content.data.second"
                chart-id="content_second"
                dataset-id-key="content_second_data"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="columns">
        <div class="column">
          <b-message
            title="Content Base 1st VS Content Base 2nd Pair T-test result"
            :type="content.pValue < 0 ? 'is-danger' : 'is-success'"
          >
            {{ content.testResult }}
          </b-message>
        </div>
      </div>
    </template>
    <!--- Total --->
    <template v-if="both">
      <div class="columns">
        <div class="column">
          <h4 class="title is-4">KNN</h4>
          <h5 class="subtitle">Recommender Result Analysis</h5>
          <div class="columns">
            <div class="column">
              No. of Response: {{ getStatValue('both', 'knn', 'total') }}
            </div>
            <div class="column">
              Mean: {{ getStatValue('both', 'knn', 'mean') }}
            </div>
          </div>
          <div class="columns">
            <div class="column">
              <LineChart
                :method-data="both.data.knn"
                chart-id="both_knn"
                dataset-id-key="both_knn_data"
              />
            </div>
          </div>
        </div>
        <div class="column">
          <h4 class="title is-4">Content Base</h4>
          <h5 class="subtitle">Recommender Result Analysis</h5>
          <div class="columns">
            <div class="column">
              No. of Response: {{ getStatValue('both', 'content', 'total') }}
            </div>
            <div class="column">
              Mean: {{ getStatValue('both', 'content', 'mean') }}
            </div>
          </div>
          <div class="columns">
            <div class="column">
              <LineChart
                :method-data="both.data.content"
                chart-id="both_content"
                dataset-id-key="both_content_data"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="columns">
        <div class="column">
          <b-message
            title="KNN VS Content base T-test result"
            :type="both.pValue < 0 ? 'is-danger' : 'is-success'"
          >
            {{ both.testResult }}
          </b-message>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import LineChart from '../LineChart.vue'

export default {
  components: {
    LineChart,
  },
  data() {
    return {
      knn: null,
      content: null,
      both: null,
    }
  },
  methods: {
    getStatValue(methodType, index, type) {
      if (this[methodType]) {
        if (type == 'total') {
          const value = this[methodType].data[index].count
          if (value && value.length > 0) {
            return value.reduce(
              (previousValue, currentValue) => previousValue + currentValue,
              0
            )
          }
        } else if (type == 'mean') {
          const value = this[methodType].data[index].totalMean
          if (value && value != 'nan') {
            return value
          }
        }
      }

      return '-'
    },
    downloadFeedback() {
      this.$http
        .get('evaluation/file/feedback', {
          method: 'GET',
          responseType: 'blob', // important
        })
        .then((response) => {
          let today = new Date()
          const dd = String(today.getDate()).padStart(2, '0')
          const mm = String(today.getMonth() + 1).padStart(2, '0') //January is 0!
          const yyyy = today.getFullYear()

          today = dd + '-' + mm + '-' + yyyy
          const url = window.URL.createObjectURL(new Blob([response.data]))
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', `feedback_${today}.csv`) //or any other extension
          document.body.appendChild(link)
          link.click()
        })
    },
    downloadRating() {
      this.$http
        .get('evaluation/file/rating', {
          method: 'GET',
          responseType: 'blob', // important
        })
        .then((response) => {
          let today = new Date()
          const dd = String(today.getDate()).padStart(2, '0')
          const mm = String(today.getMonth() + 1).padStart(2, '0') //January is 0!
          const yyyy = today.getFullYear()

          today = dd + '-' + mm + '-' + yyyy
          const url = window.URL.createObjectURL(new Blob([response.data]))
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', `rating_${today}.csv`) //or any other extension
          document.body.appendChild(link)
          link.click()
        })
    },
  },
  async mounted() {
    const loadingComponent = this.$buefy.loading.open({
      container: null, //this.isFullPage ? null : this.$refs.element.$el
    })

    const knnResult = await this.$http.get('evaluation/knn')
    const contentResult = await this.$http.get('evaluation/content')
    const bothResult = await this.$http.get('evaluation/')

    const knnData = knnResult.data
    const contentData = contentResult.data
    const bothData = bothResult.data

    loadingComponent.close()

    if (knnData.success && contentData.success && bothData.success) {
      this.knn = knnData
      this.content = contentData
      this.both = bothData
    } else {
      console.log('not success flag')
      console.log(knnData.msg)
      console.log(contentData.msg)
      console.log(bothData.msg)
    }
  },
}
</script>

<style>
.page.container {
  margin-top: 3vh;
}
</style>
