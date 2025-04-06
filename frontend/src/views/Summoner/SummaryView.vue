<template>
  <div class="summary-container">
    <div class="content-container">
      <div class="rank-container">
        <RankHistory v-if="summonerLoaded" />
      </div>
      <div v-if="summonerStore.summary.matches.length" class="match-table">
        <MatchHistory />
      </div>
      <div v-else>No matches found.</div>
    </div>
  </div>
</template>

<script>
import { storeToRefs } from "pinia";
import { useSummonerStore } from "@/stores/modules/summoner";
import RankHistory from "@/components/Summoner/RankHistory.vue";
import MatchHistory from "@/components/Summoner/MatchHistory.vue";
import { watch, onMounted } from "vue";

export default {
  name: "SummaryView",
  components: { RankHistory, MatchHistory },

  setup() {
    const summonerStore = useSummonerStore();
    const { summonerLoaded } = storeToRefs(summonerStore);

    // Watch for changes in summonerLoaded
    watch(
      summonerLoaded,
      async (isLoaded) => {
        if (isLoaded) {
          console.log("Summoner loaded, fetching summary data");
          await summonerStore.summaryRequest();
        }
      },
      { immediate: true }
    ); // Add immediate: true to run on component mount

    return {
      summonerStore, // Return the whole store
      summonerLoaded,
    };
  },
};
</script>

<style scoped>
/* Layout */
.summary-container {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.content-container {
  display: flex;
  gap: 0x;
  margin-left: 0x;
  margin-top: 0px;
}

.rank-container {
  max-width: 100%;
  padding-left: -5px;
  margin-right: -16px;
  margin-top: -18px;
}

.match-table {
  width: 740px;
  padding-top: 5px;
  padding-left: -50px;
}

/* Queue Navigation */
.queue-nav {
  display: flex;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  position: relative;
  height: 44px;
  width: 1080px;
  margin-top: 5px;
  margin-left: 400px;
}

.queue-nav-item {
  background: none;
  border: none;
  padding: 0 12px;
  font-size: 14px;
  color: #777;
  cursor: pointer;
  height: 100%;
  position: relative;
  font-weight: 500;
  transition: color 0.2s;
  white-space: nowrap;
}

.queue-nav-item:hover {
  color: #333;
}

.queue-nav-item:hover::after,
.queue-nav-item.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
}

.queue-nav-item:hover::after {
  background-color: #ddd;
}

.queue-nav-item.active {
  color: #4f84ea;
  font-weight: 700;
}

.queue-nav-item.active::after {
  background-color: #4f84ea;
}

/* Dropdown Menu */
.queue-dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-arrow {
  font-size: 10px;
  margin-left: 4px;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: white;
  min-width: 160px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  z-index: 10;
  top: 100%;
  left: 0;
}

.queue-dropdown:hover .dropdown-content {
  display: block;
}

.dropdown-content a {
  color: #777;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
  font-size: 14px;
}

.dropdown-content a:hover {
  background-color: #f7f7f9;
}

/* Mobile Responsiveness */
@media screen and (max-width: 720px) {
  .queue-nav,
  .content-container {
    margin-left: 0;
    width: 100%;
  }

  .content-container {
    flex-direction: column;
  }

  .rank-container,
  .match-table {
    width: 100%;
  }
}
</style>
