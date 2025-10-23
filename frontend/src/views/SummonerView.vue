<template>
  <div class="summoner-view">
    <div class="banner">
      <div class="profile-header">
        <div class="icon-container">
          <img :src="getProfileIconUrl" alt="Profile Icon" class="profile-icon"/>
          <span class="ladder-rank">Top 1%</span>
        </div>
        <div class="name-container">
          <h2 class="summoner-name">
            {{ summoner.account?.name }}
            <span class="tag">#{{ formatTag(summoner.account?.tagLine) }}</span>
          </h2>
          <div class="level">
            <div class="square-container">
              <div class="square"></div>
              <div class="square"></div>
              <div class="square"></div>
              <div class="square"></div>
              <div class="square"></div>
              <div class="square"></div>
              <div class="square"></div>
              <div class="square"></div>
              <div class="square"></div>
              <div class="square"></div>
            </div>
            <span class="level-text">{{ summoner.account?.summonerLevel || 0 }}</span>
          </div>
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

      <div class="tab-content">
        <SummaryView v-if="activeTab === 'summary'"/>
        <ChampionsView v-else-if="activeTab === 'champions'"/>
        <MasteryView v-else-if="activeTab === 'mastery'"/>
        <LiveGameView v-else-if="activeTab === 'live'"/>
      </div>
    </div>
  </div>
</template>

<script>
import {storeToRefs} from "pinia";
import {useSummonerStore} from "@/stores/modules/summoner";
import {useGlobalStore} from "@/stores/global";
import {useSettingsStore} from "@/stores/modules/settings";
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
    const {summoner, isUpdating} = storeToRefs(summonerStore);

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
      lastUpdateTimestamp: 0,
      tabs: [
        {id: "summary", name: "Summary"},
        {id: "champions", name: "Champions", badge: true},
        {id: "mastery", name: "Mastery"},
        {id: "live", name: "Live Game"},
      ],
    };
  },

  computed: {
    buttonText() {
      return this.isUpdating ? "Updating..." : "Refresh";
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

    async updateSummoner() {
      try {
        this.isUpdating = true;
        const timestamp = await this.summonerStore.updateSummoner();
        this.lastUpdateTimestamp = timestamp;
        this.hasBeenUpdated = true;
        this.cooldownActive = true;
        this.cooldownSeconds = 600; // 10 minutes

        // Start cooldown timer
        const interval = setInterval(() => {
          this.cooldownSeconds--;
          if (this.cooldownSeconds <= 0) {
            this.cooldownActive = false;
            clearInterval(interval);
          }
        }, 1000);

        // Update last updated text
        const now = new Date(timestamp);
        this.lastUpdatedText = now.toLocaleTimeString();
      } catch (error) {
        console.error("Update failed:", error);
        if (error.message.includes("recently updated")) {
          this.cooldownActive = true;
          this.cooldownSeconds = 600; // Assume full cooldown on 429
          const interval = setInterval(() => {
            this.cooldownSeconds--;
            if (this.cooldownSeconds <= 0) {
              this.cooldownActive = false;
              clearInterval(interval);
            }
          }, 1000);
        }
      } finally {
        this.isUpdating = false;
      }
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
          const {region, summoner, tag} = to.params;
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
.banner {
  background-color: #43b49b;
}

.profile-header {
  display: flex;
  align-items: flex-start;
  position: relative;
  width: 1200px;
  margin: 0 auto;
  height: 150px;
  padding-top: 20px
}

.icon-container {
  position: relative;
  display: inline-block;
  width: 100px;
  height: 100px;
  margin-right: 5px;
}

.profile-icon {
  width: 100%;
  height: 100%;
  border-radius: 2%;
  object-fit: cover;
  border-color: #34333b;
  border-width: 6px;
}

.level {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  background-color: #34333b;
  color: white;
  padding: 2px 8px;
  font-size: 10px;
  min-width: 320px;
  max-width: 340px;
  text-align: right;
  margin-bottom: 5px;
}

.square-container {
  display: flex;
  gap: 3px;
  margin-right: 5px;
  margin-left: -4px;
}

.square {
  width: 26px;
  height: 11px;
  background-color: white;
  border-radius: 2%;
}

.level-text {
  font-size: 12px;
  line-height: 15px;
  font-family: "system-ui";
  font-style: normal;
  font-weight: 400;
}

.ladder-rank {
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #34333b;
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
  margin-top: 1px;
}

.summoner-name {
  font-size: 34px;
  font-weight: 400;
  line-height: 16px;
  margin-bottom: 5px;
  font-family: "Inter";
  font-style: italic;
  color: white;
}

.tag {
  font-size: 34px;
  line-height: 34px;
  font-weight: 400;
  color: #1b5850;
}

.update-button {
  background-color: #1b5850;
  color: white;
  border-radius: 2px;
  padding: 10px 30px;
  font-size: 20px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-family: "Inter";
  font-weight: 400;
  line-height: 16px;
}

.update-button:hover {
  background-color: #1b4642;
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
  position: relative;
  z-index: 1;
  width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-wrap: nowrap;
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
  font-family: "Inter";
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
  font-family: "Inter";
}

.nav-item.active .update-badge {
  background-color: #ffb900;
}

.tab-content {
  background-color: #1b5850;
  padding-top: 0;
  width: 100%;
  min-height: calc(100vh - 200px);
}
</style>