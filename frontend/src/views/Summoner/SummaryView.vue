<template>
  <div class="summary-container">
    <div class="queue-nav">
      <button
        v-for="queue in queueTypes"
        :key="queue.id"
        class="queue-nav-item"
        :class="{ active: activeQueue === queue.id }"
        @click="setQueue(queue.id)"
      >
        {{ queue.name }}
      </button>
      <div class="queue-dropdown">
        <button class="queue-nav-item">
          QueueType <span class="dropdown-arrow">▼</span>
        </button>
        <div class="dropdown-content">
          <a
            v-for="queue in dropdownQueues"
            :key="queue.id"
            href="#"
            @click.prevent="setQueue(queue.id)"
          >
            {{ queue.name }}
          </a>
        </div>
      </div>
    </div>

    <div class="content-container">
      <div class="rank-container">
        <RankHistory v-if="summonerLoaded" />
      </div>
      <div class="match-table">
        <MatchHistory v-if="summaryLoaded" />
      </div>
    </div>
  </div>
</template>

<script>
import { storeToRefs } from "pinia";
import { useSummonerStore } from "@/stores/modules/summoner";
import { useQueueFilterStore } from "@/stores/modules/queueFilter";
import RankHistory from "@/components/Summoner/RankHistory.vue";
import MatchHistory from "@/components/Summoner/MatchHistory.vue";
import { watch } from "vue";

export default {
  name: "SummaryView",
  components: { RankHistory, MatchHistory },

  setup() {
    const summonerStore = useSummonerStore();
    const queueFilterStore = useQueueFilterStore();

    const { summoner, summary, summonerLoaded, summaryLoaded } =
      storeToRefs(summonerStore);
    const { activeQueue } = storeToRefs(queueFilterStore);

    // Watch for changes in summonerLoaded
    watch(summonerLoaded, (isLoaded) => {
      if (isLoaded) {
        console.log("Summoner loaded, fetching summary data");
        summonerStore.summaryRequest();
      }
    });

    const queueTypes = [
      { id: "all", name: "All" },
      { id: "solo", name: "Ranked Solo/Duo" },
      { id: "flex", name: "Ranked Flex" },
      { id: "aram", name: "Aram" },
    ];

    const dropdownQueues = [
      { id: "normal-draft", name: "Normal Draft" },
      { id: "normal-blind", name: "Normal Blind" },
      { id: "clash", name: "Clash" },
    ];

    const setQueue = (queueId) => {
      queueFilterStore.setActiveQueue(queueId);
    };

    return {
      summoner,
      summary,
      summonerLoaded,
      summaryLoaded,
      activeQueue,
      queueTypes,
      dropdownQueues,
      setQueue,
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
