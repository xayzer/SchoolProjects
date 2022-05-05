Vue.component("co2-title", {
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
    <section class="section has-background-success" id="co2-emission">
    <a :id="anchorId" class="anchor"></a>
    <div class="container">
      <div class="columns">
        <div class="column">
          <h1 class="title is-1">
          <i class="fas fa-lungs"></i>
            The CO2 Emission in the World
          </h1>
        </div>
      </div>
      <div class="columns">
        <div class="column">
          Most of the weather conditions and the raising temperature are caused by the carbon dioxide. Therefore, 
          the biggest problem people face is how to reduce carbon dioxide emissions as much as possible.
          Below are some data on carbon dioxide emissions in different countries.
        </div>
      </div>
    </div>
  </section>
            `,
});


