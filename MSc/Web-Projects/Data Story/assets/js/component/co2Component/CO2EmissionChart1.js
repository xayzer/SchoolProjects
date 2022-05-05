Vue.component("carbon-chart1", {
  data() {
    return {
      chartData1: [],
      title: "Different Countries' CO2 Emission From 1960-2018",
    };
  },
  async mounted() {
    await this.loadData1();
    this.generateLineChart();
  },
  methods: {
    chooseMax(countryDatasets) {
      var x = [];
      for (var i = 0; i < countryDatasets.length; i++) {
        x.push(
          d3.max(countryDatasets[i], function (d) {
            return d[1];
          })
        );
      }
      return d3.max(x);
    },
    generateLineChart() {

      var testmax = this.chooseMax(this.chartData1);

      // Chart height & width
      var width = 600;
      var height = 400;
      // Paddings
      var padding = {
        top: 50,
        right: 50,
        bottom: 50,
        left: 50,
      };

      var myxScale = d3
        .scaleLinear()
        .domain([1960, 2018])
        .range([0, width - padding.left - padding.right]);

      var myyScale = d3
        .scaleLinear()
        .domain([0, testmax])
        .range([height - padding.top - padding.bottom, 0]);

      var svg = d3
        .select("#carbon1")
        .append("svg")
        .attr("width", width + "px")
        .attr("height", height + "px");
      var xAxis = d3.axisBottom().scale(myxScale);
      var yAxis = d3.axisLeft().scale(myyScale);

      svg
        .append("g")
        .attr("class", "axis")
        .attr(
          "transform",
          "translate(" + padding.left + "," + (height - padding.bottom) + ")"
        )
        .call(xAxis);

      svg
        .append("g")
        .attr("class", "axis")
        .attr(
          "transform",
          "translate(" + padding.left + "," + padding.top + ")"
        )
        .call(
          yAxis.tickFormat(function (d) {
            return d + "mt/c";
          })
        );

      // Draw data lines
      var linePath = d3
        .line()
        .x(function (d) {
          return myxScale(d[0]);
        })
        .y(function (d) {
          return myyScale(d[1]);
        });

      for (var i = 0; i < this.chartData1.length; i++) {
        var color = ["#FF83FA", "#43CD80", "#4B0082", "#EE0000", "#FF7F00"];
        svg
          .append("g")
          .append("path")
          .attr("class", "line-path")
          .attr(
            "transform",
            "translate(" + padding.left + "," + padding.top + ")"
          )
          .attr("d", linePath(this.chartData1[i]))
          .attr("fill", "none")
          .attr("stroke-width", 3)
          .attr("stroke", color[i])
          .attr("text", "dsadsadsa");

        svg
          .append("g")
          .selectAll("circle")
          .data(this.chartData1[i])
          .enter()
          .append("circle")
          .attr("r", 3.5)
          .attr("transform", function (d) {
            return (
              "translate(" +
              (myxScale(d[0]) + padding.left) +
              "," +
              (myyScale(d[1]) + padding.top) +
              ")"
            );
          })
          .attr("fill", color[i]);
      }
    },
    async loadData1() {
      this.loadingData = true;
      const res = await fetch("https://api.npoint.io/da14fe60efb7e0534fd2");
      if (res.ok) {
        // The data for the first chart
        jsonData = await res.json();
        var obj = jsonData;
        this.chartData1.push(Object.entries(obj[40]));
        this.chartData1[0].pop();
        this.chartData1.push(Object.entries(obj[251]));
        this.chartData1[1].pop();
        this.chartData1.push(Object.entries(obj[81]));
        this.chartData1[2].pop();
        this.chartData1.push(Object.entries(obj[109]));
        this.chartData1[3].pop();
        this.chartData1.push(Object.entries(obj[13]));
        this.chartData1[4].pop();
        this.chartData1 = this.toFloat(this.chartData1);
      }
      this.loadingData = false;
    },
    toFloat(dataSet) {
      for (var i = 0; i < dataSet.length; i++) {
        dataSet[i] = dataSet[i].map((a) => {
          a[0] = parseFloat(a[0]);
          a[1] = parseFloat(a[1]);
          return a;
        });
      }
      return dataSet;
    },
  },
  template: `
          <div>
          <h3>{{ title }}</h3>
          <svg id="carbon1" width="600" height="400"></svg>
          </div>
      `,
});
