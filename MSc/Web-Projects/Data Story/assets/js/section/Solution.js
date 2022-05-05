Vue.component("solution-section", {
  data() {
    return {
      // Add solutions here
      solutions: [
        {
          img: "/assets/img/steak-1500x844.jpg",
          title: "Consume less meat",
          content:
            "There are a lot of meat lovers in the world. However, growing a livestock is costing a lot of water and energy which is a source of carbon dioxide. Eating lesser meat could helps to protect the environment and having healther live.",
        },
        {
          img: "/assets/img/publictransport.jpg",
          title: "Using public transportation",
          content:
            "Public transportation use is one of the most effective actions individuals can take to conserve energy. A single person who switches from a 20-mile commuting alone by car to existing public transportation, can reduce their annual CO2 emissions by 20 pounds per day, or more than 48,000 pounds in a year.",
        },
        {
          img: "assets/img/cleanenergy.jpg",
          title: "Use clean energy",
          content:
            "The use of fossil energy and coal energy has led to carbon dioxide emissions on a large scale. If we can use clean energy, it will effectively reduce the rate at which carbon dioxide in the air rises. Such as the use of solar energy, wind energy, ocean energy and so on.",
        },
      ],
    };
  },
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
    <section class="section">
    <a :id="anchorId" class="anchor"></a>
    <div class="container">
        <h1 class="title is-1 has-text-centered	">Carbon Free Solution</h1>
      <div class="columns is-multiline">
        <div v-for="s in solutions" :key="s.title" class="column">
            <div  class="solution card">
                <div class="card-image">
                  <figure class="image is-4by3">
                    <img :src="s.img" alt="solution image">
                  </figure>
                </div>
                <div class="card-content">
                  <div class="media">
                    <div class="media-content">
                      <p class="title is-4">{{ s.title }}</p>
                    </div>
                  </div>
              
                  <div class="content">
                    {{ s.content }}
                  </div>
                </div>
              </div>
        </div>
        </div>
      </div>
    </div>
    </section>
  
          `,
});
