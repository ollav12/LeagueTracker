<template>
  <div class="summoner-view">
    <div class="banner">
      <div class="profile-header">
        <div class="icon-container">
          <img :src="getProfileIconUrl" alt="Profile Icon" class="profile-icon"/>
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
  font-family: "roboto";
  color: white;
}

.tag {
  font-size: 24px;
  line-height: 28px;
  font-weight: 400;
  color: #1b5850;
}

.ladder-rank {
  margin: 0;
  font-size: 12px;
  line-height: 16px;
  color: white;
  font-weight: 400;
}

.update-button {
  margin-top: 8px;
  background-color: #1b5850;
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
  background-color: #1b5850;
  padding-top: 0;
  width: 100%;
  min-height: calc(100vh - 200px);
}
</style>