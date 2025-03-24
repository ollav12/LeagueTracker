<template>
  <div class="homeview">
    <h1 class="title">LeagueTracker</h1>
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
.title {
  color: white;
  margin-top: 250px;
}

.homeview {
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* Changed from center to space-between */
  align-items: center;
  text-align: center;
  background-color: #5383e9;
  width: 100%;
  min-height: 100vh; /* Use viewport height instead of % */
  padding: 20px 0; /* Add padding top and bottom */
}

.policy-text {
  position: static;
  padding-top: 485px;
  display: flex;
  font-size: small;
  color: white;
}
</style>
