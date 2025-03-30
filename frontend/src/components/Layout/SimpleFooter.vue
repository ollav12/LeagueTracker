<template>
  <footer>
    <p class="policy-text">
      LeagueStats.gg isn't endorsed by Riot Games and doesn't reflect the views
      or opinions of Riot Games or anyone officially involved in producing or
      managing Riot Games properties.
      <br />Riot Games, and all associated properties are trademarks or
      registered trademarks of Riot Games, Inc.
    </p>
  </footer>
</template>

<script>
export default {
  name: "SimpleFooter",
};
</script>

<style scoped>
.policy-text {
  position: static;
  font-size: small;
  color: white;
  text-align: center;
}
</style>
