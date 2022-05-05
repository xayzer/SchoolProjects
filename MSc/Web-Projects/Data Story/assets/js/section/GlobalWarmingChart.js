Vue.component("globalwarming-section-chart", {
  data() {
    return {
      chartData: [],
      columns: [
        {
          field: "year",
          label: "Year",
          centered: true,
        },
        {
          field: "temp",
          label: "Temperature (°C)",
          numeric: true,
          centered: true,
        },
      ],
      scaleStartingOrigin: 0,
      viewType: "chart",
      loadingData: false,
    };
  },
  computed: {
    isChartView() {
      return this.viewType == "chart";
    },
    isZoomedIn() {
      return this.scaleStartingOrigin;
    },
    chartWidth() {
      return window.innerWidth - 80;
    },
  },
  async mounted() {
    await this.loadData();
    this.generateCharts();
  },
  methods: {
    async loadData() {
      this.loadingData = true;
      const res = await fetch("https://api.npoint.io/4c59b88fd1d4407f657f");
      if (res.ok) {
        const jsonData = await res.json();
        this.chartData = jsonData.globalwarmingData;
      }

      this.loadingData = false;
      return res.ok;
    },
    async refreshData() {
      const result = await this.loadData();
      if (result) {
        this.$buefy.toast.open({
          message: "Global Warming Data is updated",
          type: "is-success",
        });
      } else {
        this.$buefy.toast.open({
          message: "Something Goes wrong !",
          type: "is-danger",
        });
      }
    },
    clearCharts() {
      d3.select("#global-warming-bar").selectAll("*").remove();
      d3.select("#global-warming-line").selectAll("*").remove();
    },
    toggleZoom() {
      if (this.scaleStartingOrigin) {
        this.scaleStartingOrigin = 0;
      } else {
        this.scaleStartingOrigin = 9;
      }
      this.generateCharts();
    },
    toggleView() {
      this.viewType = this.isChartView ? "table" : "chart";
      if (this.isChartView) {
        this.scaleStartingOrigin = 0;
        this.generateCharts();
      }
    },
    generateCharts() {
      this.clearCharts();
      this.generateBarChart();
      this.generateLineChart();
    },
    generateLineChart() {
      const scaleStartingOrigin = this.scaleStartingOrigin;
      const svg = d3.select("#global-warming-line");

      margin = 60;
      width = svg.attr("width") - margin;
      height = svg.attr("height") - margin;

      const xScale = d3.scaleBand().range([0, width - margin / 2]),
        yScale = d3.scaleLinear().range([height * 0.8, 0]);

      const g = svg
        .append("g")
        .attr(
          "transform",
          "translate(" + margin * 0.6 + "," + margin * 0.1 + ")"
        );

      const data = this.chartData;
      xScale.domain(
        data.map(function (d) {
          return d.year;
        })
      );
      yScale.domain([
        scaleStartingOrigin,
        d3.max(data, function (d) {
          return d.temp;
        }),
      ]);

      // Title
      g.append("text")
        .attr("x", margin + 10)
        .attr("y", 0 + margin / 2)
        .attr("text-anchor", "middle")
        .style("font-size", "16px")
        .style("text-decoration", "underline")
        .text("Land Average Temperature");

      // X Labels
      g.append("g")
        .attr("transform", "translate(0," + height + ")")

        .attr("class", "globalwarming__chart-x-label--mobile")
        .call(d3.axisBottom(xScale))
        .selectAll("text")
        .style("text-anchor", "end")
        .attr("dx", "-.8em")
        .attr("dy", ".15em")
        .style("font-size", "14px")
        .attr("transform", "rotate(-90)");

      // Y Labels
      g.append("g")
        .attr("transform", "translate(0," + height * 0.2 + ")")
        .call(
          d3
            .axisLeft(yScale)
            .tickFormat(function (d) {
              return d + "°C";
            })
            .ticks(10)
        );

      // Construct a line generator.
      const X = d3.map(data, (d) => d.year);
      const Y = d3.map(data, (d) => d.temp);
      const I = d3.range(X.length);
      defined = (d, i) => !isNaN(X[i]) && !isNaN(Y[i]);
      const D = d3.map(data, defined);

      const line = d3
        .line()
        .defined((i) => D[i])
        .curve(d3.curveLinear)
        .x((i) => xScale(X[i]))
        .y((i) => yScale(Math.max(scaleStartingOrigin, Y[i])));

      const strokeLinecap = "round", // stroke line cap of the line
        strokeLinejoin = "round", // stroke line join of the line
        strokeWidth = 3, // stroke width of line, in pixels
        strokeOpacity = 1; // stroke opacity of line

      g.append("path")
        .attr("transform", "translate(0," + height * 0.2 + ")")
        .attr("fill", "none")
        .attr("stroke", "rgb(111, 134, 209)")
        .attr("stroke-width", strokeWidth)
        .attr("stroke-linecap", strokeLinecap)
        .attr("stroke-linejoin", strokeLinejoin)
        .attr("stroke-opacity", strokeOpacity)
        .attr("d", line(I));

      // Average line
      const sum = d3.sum(data, function (d) {
        return d.temp;
      });

      const average = sum / data.length;

      const averageLine = d3
        .line()
        .x(function (d, i) {
          return xScale(d.year) + i;
        })
        .y(function (d, i) {
          return yScale(average);
        });

      g.append("path")
        .attr("transform", "translate(0," + height * 0.2 + ")")

        .datum(data)
        .attr("class", "globalwarming__chart-path--mean")
        .attr("d", averageLine);

      g.append("text")
        .attr(
          "transform",
          "translate(" + width * 0.9 + "," + height * 0.1 + ")"
        )
        .attr("dy", "1em")
        .attr("text-anchor", "end")
        .style("fill", "red")
        .html("Average: " + average.toFixed(2) + "°C");
    },
    generateBarChart() {
      const scaleStartingOrigin = this.scaleStartingOrigin;
      const svg = d3.select("#global-warming-bar");

      margin = 50;
      width = svg.attr("width") - margin;
      height = svg.attr("height") - margin;

      const xScale = d3.scaleBand().range([0, width]).padding(0.4),
        yScale = d3.scaleLinear().range([height * 0.8, 0]);

      const g = svg
        .append("g")
        .attr(
          "transform",
          "translate(" + margin * 0.7 + "," + margin * 0.1 + ")"
        );

      const data = this.chartData;
      xScale.domain(
        data.map(function (d) {
          return d.year;
        })
      );
      yScale.domain([
        scaleStartingOrigin,
        d3.max(data, function (d) {
          return d.temp;
        }),
      ]);

      // Add tooltip
      const tooltip = d3
        .select("body")
        .append("div")
        .attr("class", "globalwarming__chart-tooltip")
        .style("opacity", 0);

      // Title
      g.append("text")
        .attr("x", width / 2)
        .attr("y", 0 + margin / 2)
        .attr("text-anchor", "middle")
        .style("font-size", "16px")
        .style("text-decoration", "underline")
        .text("Land Average Temperature");

      // X Labels
      g.append("g")
        .attr("transform", "translate(0," + height + ")")
        .call(d3.axisBottom(xScale))
        .selectAll("text")
        .style("text-anchor", "end")
        .attr("dx", "-.8em")
        .attr("dy", ".15em")
        .style("font-size", "14px")
        .attr("transform", "rotate(-65)");

      // Y Labels
      g.append("g")
        .attr("transform", "translate(0," + height * 0.2 + ")")
        .call(
          d3
            .axisLeft(yScale)
            .tickFormat(function (d) {
              return d + "°C";
            })
            .ticks(10)
        );

      g.selectAll(".bar")
        .data(data)
        .enter()
        .append("rect")
        .attr("class", "globalwarming__chart-bar")
        .attr("transform", "translate(0," + height * 0.2 + ")")
        .attr("x", function (d) {
          return xScale(d.year);
        })
        .attr("y", function (d) {
          return yScale(scaleStartingOrigin);
        })
        .attr("width", xScale.bandwidth())
        .attr("height", function (d) {
          return height * 0.8 - yScale(scaleStartingOrigin);
        })
        .on("mouseover", function (event, d) {
          d3.select(this).style("fill", "#6fd1d1");
          tooltip
            .text(d.year + ": " + d.temp.toFixed(2) + "°C")
            .style("opacity", 0.8)
            .style("left", event.pageX + 8 + "px")
            .style("top", event.pageY + 3 + "px");
        })
        .on("mouseout", function (d) {
          tooltip.style("opacity", 0);
          d3.select(this).style("fill", "rgb(111, 134, 209)");
        });

      // Animation
      g.selectAll("rect")

        .transition()
        .duration(600)
        .attr("y", function (d) {
          return yScale(d.temp);
        })
        .attr("height", function (d) {
          return Math.max(0, height * 0.8 - yScale(d.temp));
        })
        .delay(function (d, i) {
          // console.log(i);
          return i * 100;
        });

      // Average line
      const sum = d3.sum(data, function (d) {
        return d.temp;
      });

      const average = sum / data.length;

      const line = d3
        .line()
        .x(function (d, i) {
          return xScale(d.year) + i;
        })
        .y(function (d, i) {
          return yScale(average);
        });

      g.append("path")
        .attr("transform", "translate(0," + height * 0.2 + ")")

        .datum(data)
        .attr("class", "globalwarming__chart-path--mean")
        .attr("d", line);

      g.append("text")
        .attr(
          "transform",
          "translate(" + (width + 3) + "," + height * 0.1 + ")"
        )
        .attr("dy", "1em")
        .attr("text-anchor", "end")
        .style("fill", "red")
        .html("Average: " + average + "°C");
    },
  },
  template: `
  <section class="section has-background-success-light">
  <div class="container">
    <div class="columns has-text-centered is-centered">
      <div v-show="isChartView" class="column is-hidden-touch">
        <svg id="global-warming-bar" width="768" height="400"></svg>
      </div>
      <div v-show="isChartView" class="column is-hidden-desktop">
        <svg id="global-warming-line" :width="chartWidth" height="400"></svg>
      </div>
      <div  v-if="!isChartView" class="column is-half">
      <b-table
      :data="chartData"
      :columns="columns"
      paginated
      per-page="5"
      pagination-simple
      pagination-rounded
    ></b-table>
      </div>
    </div>
    <div class="columns is-centered is-vcentered is-multiline is-mobile">
      <div class="column"></div>
      <div class="column is-narrow">
        <b-field v-if="isChartView">
          <b-switch @input="toggleZoom">
            <i class="fas" :class="isZoomedIn?'fa-search-minus':'fa-search-plus'"></i>
          </b-switch>
        </b-field>
        <b-button v-else type="is-primary" @click="refreshData" :loading="loadingData">
          <i class="fas fa-redo"></i>
        </b-button>
      </div>
      <div class="column is-narrow">
        <b-button type="is-primary" @click="toggleView">
          <i class="fas" :class="isChartView?'fa-table':'fa-chart-bar'"></i>
        </b-button>
      </div>
      <div class="column"></div>
    </div>
  </div>
</section>

            `,
});
