<template>
  <div class="background-all">
    <div class="profile-header">
      <div class="icon-container">
        <img :src="getProfileIconUrl" alt="Profile Icon" class="profile-icon" />
        <span class="level">{{ summoner.account?.summonerLevel || 0 }}</span>
      </div>
      <div class="name-container">
        <h2 class="summoner-name">
          {{ summoner.account?.name }}
          <span class="tag">#{{ formatTag(summoner.account?.tagLine) }}</span>
        </h2>
        <p class="ladder-rank">
          {{ summoner.account?.region }} | Ladder Rank 77,321 (3.71% of top)
        </p>
        <button
          class="update-button"
          @click="updateSummoner"
          :disabled="isUpdating || cooldownActive"
        >
          {{ buttonText }}
        </button>
        <p v-if="cooldownActive" class="cooldown-text">
          Refresh available in: {{ cooldownSeconds }} seconds
        </p>
        <p v-else-if="hasBeenUpdated" class="cooldown-text">
          Last updated: {{ lastUpdatedText }}
        </p>
      </div>
    </div>

    <!-- Nav bar -->
    <div class="profile-nav">
      <button
        v-for="tab in tabs"
        :key="tab.name"
        class="nav-item"
        :class="{ active: activeTab === tab.id }"
        @click="activeTab = tab.id"
      >
        {{ tab.name }}
        <span v-if="tab.badge" class="update-badge">U</span>
      </button>
    </div>

    <!-- Tab content -->
    <div class="tab-content">
      <SummaryView v-if="activeTab === 'summary'" />
      <ChampionsView v-else-if="activeTab === 'champions'" />
      <MasteryView v-else-if="activeTab === 'mastery'" />
      <LiveGameView v-else-if="activeTab === 'live'" />
    </div>
  </div>
</template>

<script>
import { storeToRefs } from "pinia";
import { useSummonerStore } from "@/stores/modules/summoner";
import { useGlobalStore } from "@/stores/global";
import { useSettingsStore } from "@/stores/modules/settings";
import SummaryView from "./Summoner/SummaryView.vue";
import ChampionsView from "./Summoner/ChampionsView.vue";
import MasteryView from "./Summoner/MasteryView.vue";
import LiveGameView from "./Summoner/LiveGameView.vue";

export default {
  name: "SummonerView",
  components: {
    SummaryView,
    ChampionsView,
    MasteryView,
    LiveGameView,
  },

  setup() {
    const summonerStore = useSummonerStore();
    const globalStore = useGlobalStore();
    const settingsStore = useSettingsStore();
    const { summoner, isUpdating } = storeToRefs(summonerStore);

    return {
      summoner,
      isUpdating,
      summonerStore,
      globalStore,
      settingsStore,
    };
  },

  data() {
    return {
      activeTab: "summary",
      cooldownActive: false,
      cooldownSeconds: 0,
      hasBeenUpdated: false,
      lastUpdatedText: "",
      tabs: [
        { id: "summary", name: "Summary" },
        { id: "champions", name: "Champions", badge: true },
        { id: "mastery", name: "Mastery" },
        { id: "live", name: "Live Game" },
      ],
    };
  },

  computed: {
    buttonText() {
      return this.isUpdating ? "Updating..." : "Update";
    },

    getProfileIconUrl() {
      if (!this.summoner?.account?.profileIconId) return "";
      return `https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/profile-icons/${this.summoner.account.profileIconId}.jpg`;
    },
  },

  methods: {
    formatTag(tag) {
      return tag || "";
    },

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
          this.settingsStore.setRegion(newRegion);
          this.fetchSummonerData(newRegion, summoner, tag);
        }
      },
      immediate: true,
    },
  },
};
</script>

<style scoped>
.background-all {
  background-color: #edeef2; /* Changed from white to match the theme */
  width: 100%;
  height: 100%;
  margin: 0 auto;
  position: relative;
}

.profile-header {
  background-color: white; /* Add white background to header */
  display: flex;
  align-items: top;
  margin-bottom: 0; /* Changed from 50px to 0 */
  height: 200px;
  padding: 40px 20px 20px 400px;
  position: relative;
}

.icon-container {
  position: relative;
  display: inline-block;
  width: 100px;
  height: 100px;
  margin-right: 20px;
}

.profile-icon {
  width: 100%;
  height: 100%;
  border-radius: 20%;
  object-fit: cover;
}

.level {
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #202a38;
  color: white;
  border-radius: 50px;
  padding: 1px 8px;
  font-size: 10px;
  min-width: 20px;
  max-width: 80px;
  text-align: center;
  z-index: 2;
}

.name-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  position: relative;
  padding-bottom: 20px;
}

.summoner-name {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  line-height: 28px;
  padding-bottom: 2px;
  color: rgb(32, 45, 55);
}

.tag {
  font-size: 24px;
  line-height: 28px;
  font-weight: 400;
  color: rgb(117, 133, 146);
}

.ladder-rank {
  margin: 0;
  font-size: 12px;
  line-height: 16px;
  color: rgb(117, 133, 146);
  font-weight: 400;
}

.update-button {
  margin-top: 8px;
  background-color: #5383e9;
  color: white;
  border: none;
  border-radius: 3px;
  padding: 11px 15px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-family: "Roboto";
  font-weight: 400;
  line-height: 20px;
}

.update-button:hover {
  background-color: #496fd0;
}

.update-button:disabled {
  background-color: #edeef2;
  color: #c5c8cd;
  cursor: default;
}

.cooldown-text {
  position: absolute;
  bottom: 0;
  left: 0;
  margin: 0;
  font-size: 12px;
  color: #8a8a8a;
  height: 16px;
}

.profile-nav {
  background-color: white; /* Add white background to nav */
  position: relative;
  z-index: 1;
  padding: 4px 0 4px 400px;
  border-top: 1px solid #e9e9e9;
  border-bottom: 1px solid #e9e9e9;
  display: flex;
  flex-wrap: nowrap;
  margin-top: 0; /* Ensure no gap */
}

.nav-item {
  padding: 1px 16px;
  margin-right: 5px;
  background: none;
  border: none;
  font-size: 14px;
  line-height: 36px;
  color: rgb(32, 45, 55);
  cursor: pointer;
  transition: all 0.2s;
  font-weight: 400;
  border-radius: 3px;
  font-family: "Roboto";
  display: flex;
  align-items: center;
  gap: 3px;
}

.nav-item:hover {
  background-color: #f7f7f9;
}

.nav-item.active {
  color: #4f84ea;
  font-weight: 700;
  background-color: rgba(79, 132, 234, 0.1);
}

.update-badge {
  display: inline-block;
  background-color: #e99e07;
  color: white;
  font-size: 12px;
  font-weight: bold;
  line-height: 1;
  padding: 4px 5px;
  border-radius: 5px;
  margin-left: 3px;
  position: relative;
  top: -1px;
  font-family: "Roboto";
}

.nav-item.active .update-badge {
  background-color: #ffb900;
}

.tab-content {
  background-color: #edeef2;
  padding-top: 0;
  width: 100%;
  min-height: calc(100vh - 200px); /* Ensure background extends to bottom */
}

@media screen and (max-width: 720px) {
  .level {
    bottom: -2px;
    left: 25px;
    padding: 1px 6px;
    font-size: 9px;
  }

  .profile-icon {
    width: 50px;
    height: 50px;
    border-radius: 25%;
    margin-right: 15px;
  }

  .summoner-name {
    font-size: 14px;
    font-weight: 550;
  }
}
</style>
