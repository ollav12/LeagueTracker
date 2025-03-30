<template>
  <div class="user-profile">
    <div class="background-all">
      <div class="profile-header">
        <div class="icon-container">
          <img
            :src="getProfileIconUrl"
            alt="Profile Icon"
            class="profile-icon"
          />
          <span class="level">{{ summoner.account.summonerLevel }}</span>
        </div>
        <div class="name-container">
          <h2 class="summoner-name">
            {{ summoner.account.name }}
            <span class="tag">#{{ formatTag(summoner.account.tagLine) }}</span>
          </h2>
          <p class="ladder-rank">
            {{ summoner.account.region }} | Ladder Rank 77,321 (3.71% of top)
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
          class="nav-item"
          :class="{ active: activeTab === 'summary' }"
          @click="activeTab = 'summary'"
        >
          Summary
        </button>
        <button
          class="nav-item"
          :class="{ active: activeTab === 'champions' }"
          @click="activeTab = 'champions'"
        >
          Champions <span class="update-badge">U</span>
        </button>
        <button
          class="nav-item"
          :class="{ active: activeTab === 'mastery' }"
          @click="activeTab = 'mastery'"
        >
          Mastery
        </button>
        <button
          class="nav-item"
          :class="{ active: activeTab === 'live' }"
          @click="activeTab = 'live'"
        >
          Live Game
        </button>
      </div>

      <!-- Tab content -->
      <div class="tab-content">
        <div v-if="activeTab === 'summary'" class="summary-container">
          <div class="rank-container">
            <RankHistory
              :soloRank="getSoloRank"
              :soloWins="summoner.ranked.solo?.wins || 0"
              :soloLosses="summoner.ranked.solo?.losses || 0"
              :soloRankIconUrl="getSoloRankIconUrl"
              :flexRank="getFlexRank"
              :flexWins="summoner.ranked.flex?.wins || 0"
              :flexLosses="summoner.ranked.flex?.losses || 0"
              :flexRankIconUrl="getFlexRankIconUrl"
            />
          </div>
          <div class="match-table">
            <div class="match-container">
              <MatchHistory />
            </div>
          </div>
        </div>

        <div v-else-if="activeTab === 'champions'" class="placeholder-content">
          Champions stats will be shown here
        </div>

        <div v-else-if="activeTab === 'mastery'" class="placeholder-content">
          Mastery information will be shown here
        </div>

        <div v-else-if="activeTab === 'live'" class="placeholder-content">
          Live game data will be shown here
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { storeToRefs } from "pinia";
import { useSummonerStore } from "@/stores/modules/summoner";
import { useGlobalStore } from "@/stores/global.js";
import RankHistory from "@/components/Summoner/RankHistory.vue";
import MatchHistory from "@/components/Summoner/MatchHistory.vue";
import axios from "@/plugins/axios";

export default {
  name: "UserProfile",
  components: {
    RankHistory,
    MatchHistory,
  },

  setup() {
    const summonerStore = useSummonerStore();
    const globalStore = useGlobalStore();
    const { summoner, isUpdating } = storeToRefs(summonerStore);

    return {
      summoner,
      isUpdating,
      globalStore,
    };
  },

  data() {
    return {
      activeTab: "summary",
      cooldownActive: false,
      cooldownSeconds: 0,
      cooldownTimer: null,
      lastUpdatedAt: null,
      hasBeenUpdated: false,
      updateInterval: null,
      updateTicker: 0,
    };
  },

  computed: {
    getProfileIconUrl() {
      return `https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/profile-icons/${this.summoner.account.profileIconId}.jpg`;
    },
    getSoloRankIconUrl() {
      if (!this.summoner.ranked.solo?.tier)
        return "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
      return `https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-${this.summoner.ranked.solo.tier.toLowerCase()}.png`;
    },
    getFlexRankIconUrl() {
      if (!this.summoner.ranked.flex?.tier)
        return "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
      return `https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-${this.summoner.ranked.flex.tier.toLowerCase()}.png`;
    },
    getSoloRank() {
      if (!this.summoner.ranked.solo?.tier) return "Unranked";
      return `${this.summoner.ranked.solo.tier} ${this.summoner.ranked.solo.rank} ${this.summoner.ranked.solo.leaguePoints} LP`;
    },
    getFlexRank() {
      if (!this.summoner.ranked.flex?.tier) return "Unranked";
      return `${this.summoner.ranked.flex.tier} ${this.summoner.ranked.flex.rank} ${this.summoner.ranked.flex.leaguePoints} LP`;
    },
    lastUpdatedText() {
      this.updateTicker;
      if (!this.lastUpdatedAt) return "";

      const secondsAgo = Math.floor((Date.now() - this.lastUpdatedAt) / 1000);
      if (secondsAgo < 60) return `${secondsAgo} seconds ago`;
      const minutesAgo = Math.floor(secondsAgo / 60);
      return `${minutesAgo} minute${minutesAgo !== 1 ? "s" : ""} ago`;
    },
    buttonText() {
      if (this.isUpdating) return "Updating...";
      if (this.cooldownActive) return "Updated";
      return "Update";
    },
  },

  methods: {
    formatTag(tag) {
      return tag?.split("#")[0] || "";
    },
  },
};
</script>

<style scoped>
.match-table {
  margin-top: 59px;
  margin-left: -765px;
}

.summary-container {
  display: flex;
  width: 100%;
  justify-content: flex-start; /* Keep elements spread apart */
}

.rank-container {
  flex: 0 0 332px; /* Fixed width for rank container */
  max-width: 100%; /* But don't overflow on small screens */
}

.match-container {
  flex: 1; /* Take remaining space */
  width: 740;
}

.tag {
  font-size: 24px;
  line-height: 28px;
  font-weight: 400;
  color: rgb(117, 133, 146);
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

/* Adjust padding on the Champions button to accommodate badge */
.nav-item:has(.update-badge) {
  display: flex;
  align-items: center;
  gap: 3px;
}

/* Make badge visible when button is active */
.nav-item.active .update-badge {
  background-color: #ffb900; /* Keep yellow even when button is active */
}

.profile-nav {
  position: relative;
  background-color: transparent;
  z-index: 1;
  padding-left: 400px;
  padding-top: 4px; /* Add top padding */
  padding-bottom: 4px; /* Add bottom padding */
  border-top: 1px solid #e9e9e9; /* Add grey top border */
  border-bottom: 1px solid #e9e9e9; /* Add grey bottom border */
  display: flex; /* Add this to create a flexbox layout */
  flex-wrap: nowrap; /* Prevent wrapping of buttons */
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
  position: relative;
  transition: all 0.2s;
  font-weight: 400;
  border-radius: 3px;
  font-family: "Roboto";
}

.nav-item:hover {
  background-color: #f7f7f9;
}
.nav-item.active {
  color: #4f84ea;
  font-weight: 700;
  background-color: rgba(79, 132, 234, 0.1); /* Light blue background */
}

.user-profile {
  text-align: left;
  position: relative; /* Add this to contain the nav bar */
  overflow-x: hidden; /* Prevent horizontal scrollbar */
  background-color: #edeef2;
  padding-bottom: 20px;
  size: 100%;
}

.ladder-rank {
  margin: 0px 0;
  font-size: 12px;
  line-height: 16px;
  color: rgb(117, 133, 146);
  font-weight: 400;
}

.icon-container {
  position: relative;
  display: inline-block;
  width: 100px; /* Match the profile icon width */
  height: 100px; /* Match the profile icon height */
  margin-right: 20px;
}

.profile-header {
  display: flex;
  align-items: top;
  margin-bottom: 50px;
  padding: 20px;
  position: relative; /* Keep content above the white background */
  padding-top: 40px;
  padding-left: 400px;
}

.profile-icon {
  width: 100%;
  height: 100%;
  border-radius: 20%;
  object-fit: cover; /* Ensures the image covers the entire container */
}

.summoner-name {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  line-height: 28px;
  padding-bottom: 2px;
  color: rgb(32, 45, 55);
}

.level {
  position: absolute;
  bottom: -8px; /* Fixed position from the bottom */
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
  z-index: 2; /* Ensure it's above the image */
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
  align-self: flex-start;
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

.name-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  position: relative;
  padding-bottom: 20px;
}

.cooldown-text {
  position: absolute;
  bottom: 0;
  left: 0;
  margin: 0;
  font-size: 12px;
  color: #8a8a8a;
  height: 16px; /* Fixed height to prevent layout shifts */
}

.rankText {
  font-weight: 700;
  font-size: 20px;
  line-height: 28px;
}

/* Optional: Adjust font size for larger numbers */
@media screen and (max-width: 720px) {
  .level {
    position: absolute;
    bottom: -2px;
    left: 25px;
    transform: translateX(-50%);
    background-color: #202a38;
    color: white;
    border-radius: 50px;
    padding: 1px 6px;
    font-size: 9px;
    min-width: 20px;
    max-width: 80px;
    text-align: center;
  }

  .profile-icon {
    width: 50px;
    height: 50px;
    border-radius: 25%;
    margin-right: 15px;
  }

  .summoner-name {
    margin: 0;
    font-size: 14px;
    font-weight: 550;
  }
}
.background-all {
  background-color: white;
  width: 100%;
  height: 100%;
  margin: 0 auto;
  position: relative;
}

.tab-content {
  background-color: #edeef2; /* Move the grey background here */
  padding-top: 0px; /* Add some spacing after the nav bar */
  width: 100%;
}
</style>
