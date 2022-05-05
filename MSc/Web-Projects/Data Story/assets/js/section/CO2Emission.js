Vue.component("co2-emission", {
  template: `
  <section class="section" >
  <div class="columns" id="co2css1">
    <div class="column is-half">
      <co2-info1></co2-info1>
    </div>
    <div class="column is-half">
      <carbon-chart1 class="column is-narrow"></carbon-chart1>
    </div>
  </div>
  <div class="columns" id="co2css2">
    <div class="column is-two-thirds">
      <carbon-chart2 class="column is-narrow"></carbon-chart2>
    </div>
    <div class="column is-one-third">
      <co2-info2></co2-info2>
    </div>
  </div>
</section>
  `,
});
