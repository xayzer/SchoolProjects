Vue.component("carbon-chart2", {
  data() {
    return {
      chartData2: [],
    };
  },
  watch: {
    chartData2() {
      this.generateChart();
    },
  },
  async mounted() {
    await this.loadData2();
    this.generateChart();
  },
  methods: {
    async loadData2() {
      this.loadingData = true;
      const res = await fetch("https://api.npoint.io/b10f8bc74ff0a06ba6ed");

      if (res.ok) {
        // The data for the first chart
        var jsonData = await res.json();

        for (var i = 0; i < jsonData.length; i++) {
          jsonData[i].CO2Emissions = parseInt(jsonData[i].CO2Emissions);
        }

        jsonData.sort(function (x, y) {
          return x.CO2Emissions - y.CO2Emissions;
        });

        this.chartData2 = jsonData;

        // console.log(jsonData[0]);
        this.chartData2 = jsonData.splice(-10);
      }
      this.loadingData = false;
    },
    generateChart() {
      am4core.useTheme(am4themes_animated);
      am4core.useTheme(am4themes_kelly);
      var chart = am4core.createFromConfig(
        {
          titles: [
            {
              text: "CO2 Emissions in 2016",
              fontSize: 24,
              marginTop: 20,
              marginBottom: 20,
              fontFamily: "Papyrus",
            },
          ],
          data: this.chartData2,
          series: [
            {
              type: "PictorialStackedSeries",
              orientation: "horizontal",
              labels: {
                template: {
                  text: "{category}: {value.percent.formatNumber('#.0')}%",
                  fontFamily: "Papyrus",
                },
              },
              dataFields: {
                value: "CO2Emissions",
                category: "Country",
              },
              maskSprite: {
                path: snsdPath,
              },
              slices: {
                tooltipY: "10%",
              },
            },
          ],
        },
        "carbon2",
        am4charts.SlicedChart
      );
    },
  },
  template: `
    <div id="carbon2" style="width:60vw; height:60vh;">
    </div>
      `,
});
