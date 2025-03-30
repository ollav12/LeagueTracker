<template>
  <UserProfile />
</template>

<script>
import UserProfile from "@/components/Summoner/UserProfile.vue";
import { useSummonerStore } from "@/stores/modules/summoner";

export default {
  name: "SummonerView",
  components: {
    UserProfile,
  },
  setup() {
    const summonerStore = useSummonerStore();

    // Watch route changes to fetch new summoner data
    const fetchSummonerData = async (region, summoner, tag) => {
      await summonerStore.summonerDetailsRequest(summoner, region, tag);
    };

    return {
      fetchSummonerData,
    };
  },
  watch: {
    $route(to, from) {
      console.log("test");
      if (
        !from ||
        from.params.region !== to.params.region ||
        from.params.summoner !== to.params.summoner
      ) {
        const { region, summoner, tag } = to.params;
        const parts = summoner.split("-");
        const summonerName = parts[0];
        const tagValue = parts.length > 1 ? parts[1] : tag;
        this.fetchSummonerData(region, summonerName, tagValue);
      }
    },
  },
};
</script>

<style></style>
