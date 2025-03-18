<template>
  <div class="homeview">
    <h1>LeagueTracker</h1>
    <div class="">
      <SearchForm @formSubmit="redirect" />
    </div>

    <p class="policy-text">
      LeagueTracker.gg isn't endorsed by Riot Games and doesn't reflect the
      views or opinions of Riot Games or anyone officially involved in producing
      or managing Riot Games properties.
      <br />Riot Games, and all associated properties are trademarks or
      registered trademarks of Riot Games, Inc.
    </p>
  </div>
</template>

<script>
import SearchForm from "@/components/Form/SearchForm.vue";

export default {
  components: {
    SearchForm,
  },
  methods: {
    redirect(region, summoner, tag) {
      if (summoner != "") {
        this.$router.push(`/summoner/${region}/${summoner}-${tag}`);
      }
    },
  },
};
</script>

<style scoped>
.homeview {
  display: flex;
  flex-direction: column; /* Stack elements vertically */
  justify-content: center; /* Center horizontally */
  align-items: center; /* Center vertically */
  text-align: center; /* Center text if necessary */
  position: static;
  padding-top: 30vh;
}

.policy-text {
  position: static;
  padding-top: 485px;
  display: flex;
  font-size: small;
}
</style>
