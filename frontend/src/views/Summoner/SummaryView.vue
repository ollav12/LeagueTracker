<template>
  <div class="summary-container">
    <div class="rank-container" v-if="summonerLoaded">
      <RankHistory/>
    </div>
    <div v-if="summonerStore.summary.matches.length">
      <MatchHistory/>
    </div>
    <div v-else>No matches found.</div>
  </div>
</template>

<script>
import {storeToRefs} from "pinia";
import {useSummonerStore} from "@/stores/modules/summoner";
import RankHistory from "@/components/Summoner/RankHistory.vue";
import MatchHistory from "@/components/Summoner/MatchHistory.vue";
import {watch} from "vue";

export default {
  name: "SummaryView",
  components: {RankHistory, MatchHistory},

  setup() {
    const summonerStore = useSummonerStore();
    const {summonerLoaded} = storeToRefs(summonerStore);

    watch(
        summonerLoaded,
        async (isLoaded) => {
          if (isLoaded) {
            console.log("Summoner loaded, fetching summary data");
            await summonerStore.summaryRequest();
          }
        },
        {immediate: true}
    );

    return {
      summonerStore,
      summonerLoaded,
    };
  },
};
</script>

<style scoped>
.summary-container {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  box-sizing: border-box;
  width: 1200px;
  margin: 0 auto;
}

.rank-container {
  padding-right: 5px;
}
</style>
