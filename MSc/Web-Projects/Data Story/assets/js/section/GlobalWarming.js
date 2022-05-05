Vue.component("globalwarming-section", {
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
  <section class="section has-background-warning">
  <a :id="anchorId" class="anchor"></a>
  <div class="container">
    <div class="columns">
      <div class="column">
        <h1 class="title is-1">
          <i class="fas fa-globe-americas"></i> Global Warming
        </h1>
      </div>
    </div>
    <div class="columns">
      <div class="column">
        Global warming is referring to the overall temperature of earth is
        raising from year to year. It is casued by air pollution and the heat is
        trapped in atomsphere. As a result the earth looks like a green house
        which become warmer and warmer. The below chart shows the temperature
        changes in recent years.
      </div>
    </div>
  </div>
</section>

          `,
});
