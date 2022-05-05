Vue.component("extremeweather", {
  props: {
    anchor: {
      type: String,
      default: "",
    },
  },
  computed: {
    anchorId() {
      return this.anchor.replace(/ /g, "_");
    },
  },
  template: `
  <section class="section" id="extremeweather">
  <a :id="anchorId" class="anchor"></a>
   <div class="container">
    <div class="columns">
      <div class="column">
        <h1 class="title is-1">
          The Extreme Weather in Hong Kong
        </h1>
      </div>
    </div>
    <div class="columns">
      <div class="column">
      The extreme weather is more serious in recent years. You will listen super typhoon and the lowest temperature in Hong Kong. 
      In the chart below, it shows that the number of very hot warning for every decade. The recent decade, the number of Very 
      Hot Warning Days is much more than the average.
      </div>
    </div>
  </div>
</section>
          `,
});
