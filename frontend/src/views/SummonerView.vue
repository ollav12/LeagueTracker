<template>
  <UserProfile />
</template>

<script>
import UserProfile from "@/components/Summoner/UserProfile.vue";
import { useSummonerStore } from "@/stores/modules/summoner";
import { useGlobalStore } from "@/stores/global";

export default {
  name: "SummonerView",
  components: {
    UserProfile,
  },
  data() {
    return {
      summonerStore: useSummonerStore(),
      globalStore: useGlobalStore(),
    };
  },
  methods: {
    async fetchSummonerData(region, summoner, tag) {
      await this.summonerStore.summonerDetailsRequest(summoner, region, tag);
    },
  },
  watch: {
    $route: {
      handler(to, from) {
        if (
          !from ||
          from.params.region !== to.params.region ||
          from.params.summoner !== to.params.summoner
        ) {
          const { region, summoner, tag } = to.params;
          let newRegion = this.globalStore.regionsList[region.toLowerCase()];
          this.fetchSummonerData(newRegion, summoner, tag);
        }
      },
      immediate: true,
    },
  },
};
</script>

<style></style>
