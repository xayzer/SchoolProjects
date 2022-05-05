<template>
  <div class="container">
    <LineChartGenerator
      :chart-options="chartOptions"
      :chart-data="chartData"
      :chart-id="chartId"
      :dataset-id-key="datasetIdKey"
      :plugins="plugins"
      :css-classes="cssClasses"
      :styles="styles"
      :width="width"
      :height="height"
    />
  </div>
</template>

<script>
import { Line as LineChartGenerator } from 'vue-chartjs/legacy'

import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  LinearScale,
  CategoryScale,
  PointElement,
} from 'chart.js'

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  LineElement,
  LinearScale,
  CategoryScale,
  PointElement
)

export default {
  components: {
    LineChartGenerator,
  },
  name: 'LineChart',
  props: {
    chartId: {
      type: String,
      default: 'line-chart',
    },
    datasetIdKey: {
      type: String,
      default: 'label',
    },
    width: {
      type: Number,
      default: 400,
    },
    height: {
      type: Number,
      default: 300,
    },
    cssClasses: {
      default: '',
      type: String,
    },
    styles: {
      type: Object,
      default: () => {},
    },
    plugins: {
      type: Array,
      default: () => [],
    },
    methodData: {
      type: Object,
      default: null,
    },
  },
  computed: {
    chartData() {
      return {
        labels: this.methodData.label,

        datasets: [
          {
            label: 'Response',
            yAxisID: 'count',
            backgroundColor: '#f87979',
            borderColor: '#f87979',
            data: this.methodData.count,
          },
          {
            label: 'Mean',
            yAxisID: 'mean',
            backgroundColor: '#a27374',
            borderColor: '#a27374',
            data: this.methodData.mean,
          },
        ],
      }
    },
    chartOptions() {
      return {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          count: {
            type: 'linear',
            position: 'left',
            min: 0,
          },

          mean: {
            type: 'linear',
            position: 'right',
            min: 0,
            max: 1,
          },
        },
      }
    },
  },
  data() {
    return {
      name: this.chartId,
    }
  },
}
</script>
