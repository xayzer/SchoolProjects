Vue.component("extreme-weather-chart", {
  data: function () {
    return {
      count: 0,
      chartData: [],
      chartTitle: "My D3 Title",
      loadingData: false,
      columns: [
        {
          field: "year",
          label: "Year",
          centered: true,
        },
        {
          field: "totalnumber",
          label: "Total number of very hot warning days",
          numeric: true,
          centered: true,
        },
      ],
    };
  },
  watch: {
    chartData() {
      this.clearBarChart();
      this.generateBarChart();
    },
  },
  async mounted() {
    await this.loadData();
    this.generateBarChart();
  },
  computed: {
    chartWidth() {
      return window.innerWidth / 2 + 200;
    },
  },
  methods: {
    onReload() {
      this.loadData();
    },
    async loadData() {
      this.loadingData = true;
      const res = await fetch("https://api.npoint.io/6890a82fb2902bb10721");
      if (res.ok) {
        const jsonData = await res.json();
        this.chartData = jsonData.chartdata;
        this.chartTitle = jsonData.title;
      }
      this.loadingData = false;
    },
    clearBarChart() {
      d3.select("#d3").selectAll("*").remove();
    },
    generateBarChart() {
      const svg = d3.select("#d3");
      margin = 200;
      width = svg.attr("width") - margin;
      height = svg.attr("height") - margin;

      const xScale = d3.scaleBand().range([0, width]).padding(0.4),
        yScale = d3.scaleLinear().range([height, 0]);

      const g = svg
        .append("g")
        .attr("transform", "translate(" + 100 + "," + 100 + ")");

      const data = this.chartData;
      xScale.domain(
        data.map(function (d) {
          return d.year;
        })
      );
      yScale.domain([
        0,
        d3.max(data, function (d) {
          return d.totalnumber;
        }),
      ]);

      g.append("g")
        .attr("transform", "translate(0," + height + ")")
        .call(d3.axisBottom(xScale))
        .selectAll("text")
        .style("text-anchor", "end")
        .attr("dx", "-.8em")
        .attr("dy", ".15em")
        .style("font-size", "14px")
        .attr("transform", "rotate(-90)");

      g.append("g").call(
        d3
          .axisLeft(yScale)
          .tickFormat(function (d) {
            return d;
          })
          .ticks(10)
      );

      g.selectAll(".bar")
        .data(data)
        .enter()
        .append("rect")
        .attr("class", "bar")
        .attr("x", function (d) {
          return xScale(d.year);
        })
        .attr("y", function (d) {
          return yScale(d.totalnumber);
        })
        .attr("width", xScale.bandwidth())
        .attr("height", function (d) {
          return height - yScale(d.totalnumber);
        });
    },
  },
  template: `
          <section class="section" id="extremeweatherchart">
          <div>
             <h1 style="text-align:center">{{ chartTitle }}</h1>
              <svg id="d3" style="display: block; margin: auto;" :width="chartWidth" height="400">            
              </svg>
          </div>

          <b-table :data="chartData" :columns="columns"></b-table>

          </section>
      `,
});
