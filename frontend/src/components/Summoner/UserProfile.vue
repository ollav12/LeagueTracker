<template>
  <div class="user-profile">
    <div class="background-all">
      <div class="profile-header">
        <div class="icon-container">
          <img
            :src="localProfileIconUrl"
            alt="Profile Icon"
            class="profile-icon"
          />
          <span class="level">{{ localSummonerLevel }}</span>
        </div>
        <div class="name-container">
          <h2 class="summoner-name">
            {{ this.localSummonerName }}
            <span class="tag"> #{{ formatTag(localTag) }}</span>
          </h2>
          <p class="ladder-rank">EUW | Ladder Rank 77,321 (3.71% of top)</p>
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

      <!-- Add the nav bar here -->
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
        <!-- Summary tab (current content) -->
        <div v-if="activeTab === 'summary'" class="summary-container">
          <div class="rank-container">
            <RankHistory
              :soloRank="soloRank"
              :soloWins="soloWins"
              :soloLosses="soloLosses"
              :soloRankIconUrl="soloRankIconUrl"
              :flexRank="flexRank"
              :flexWins="flexWins"
              :flexLosses="flexLosses"
              :flexRankIconUrl="flexRankIconUrl"
            />
          </div>
          <div class="match-table">
            <div class="match-container">
              <MatchHistory />
            </div>
          </div>
        </div>

        <!-- Placeholder for other tabs -->
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
import axios from "@/plugins/axios";
import RankHistory from "@/components/Summoner/RankHistory.vue";
import MatchHistory from "@/components/Summoner/MatchHistory.vue";

export default {
  components: {
    RankHistory,
    MatchHistory,
  },
  props: {
    puuid: String,
    region: String,
    tag: String,
    profileIconUrl: String,
    summonerName: String,
    summonerLevel: Number,
    soloRank: String,
    soloWins: Number,
    soloLosses: Number,
    flexRank: String,
    flexWins: Number,
    flexLosses: Number,
    soloRankIconUrl: String,
    flexRankIconUrl: String,
    matches: Array,
  },
  data() {
    return {
      localPuuid: this.puuid,
      localRegion: this.region,
      localTag: this.tag,
      localProfileIconUrl: this.profileIconUrl,
      localSummonerName: this.summonerName,
      localSummonerLevel: this.summonerLevel,
      localSummonerRank: this.soloRank,
      localRankSoloIconUrl: this.soloRankIconUrl,
      localRankFlexIconUrl: this.flexRankIconUrl,
      localWins: this.soloWins,
      localLosses: this.soloLosses,
      localSummonerRankFlex: this.flexRank,
      localWinsFlex: this.flexWins,
      localLossesFlex: this.flexLosses,

      isUpdating: false,
      cooldownActive: false,
      cooldownSeconds: 0,
      cooldownTimer: null,
      lastUpdatedAt: null,
      hasBeenUpdated: false,
      updateInterval: null,
      updateTicker: 0,

      localMatches: this.matches,

      activeTab: "summary",
    };
  },
  watch: {
    puuid(newVal) {
      this.localPuuid = newVal;
    },
    region(newVal) {
      this.localRegion = newVal;
    },
    tag(newVal) {
      this.localTag = newVal;
    },
    profileIconUrl(newVal) {
      this.localProfileIconUrl = newVal;
    },
    rankSoloIconUrl(newVal) {
      this.localRankSoloIconUrl = newVal;
    },
    rankFlexIconUrl(newVal) {
      this.localRankFlexIconUrl = newVal;
    },
    summonerName(newVal) {
      this.localSummonerName = newVal;
    },
    summonerLevel(newVal) {
      this.localSummonerLevel = newVal;
    },
    soloRank(newVal) {
      this.localSummonerRank = newVal;
    },
    wins(newVal) {
      this.localWins = newVal;
    },
    losses(newVal) {
      this.localLosses = newVal;
    },
    flexRank(newVal) {
      this.localSummonerRankFlex = newVal;
    },
    winsFlex(newVal) {
      this.localWinsFlex = newVal;
    },
    lossesFlex(newVal) {
      this.localLossesFlex = newVal;
    },
    matches(newVal) {
      this.localMatches = newVal;
    },
  },
  computed: {
    lastUpdatedText() {
      this.updateTicker;
      if (!this.lastUpdatedAt) return "";

      const secondsAgo = Math.floor((Date.now() - this.lastUpdatedAt) / 1000);

      if (secondsAgo < 60) {
        return `${secondsAgo} seconds ago`;
      } else {
        const minutesAgo = Math.floor(secondsAgo / 60);
        return `${minutesAgo} minute${minutesAgo !== 1 ? "s" : ""} ago`;
      }
    },
    buttonText() {
      if (this.isUpdating) return "Updating...";
      if (this.cooldownActive) return `Updated`;
      return "Update";
    },
  },
  mounted() {
    this.$nextTick(() => {
      // Wait for props to be fully available
      this.checkCooldownStatus();
    });
  },
  methods: {
    checkCooldownStatus() {
      // Try multiple possible cooldown keys to handle prop/local value differences
      const possibleKeys = [
        `cooldown_${this.localSummonerName}_${this.localRegion}`,
        `cooldown_${this.summonerName}_${this.region}`,
        `cooldown_${this.$route.params.summoner.split("-")[0]}_${
          this.$route.params.region
        }`,
      ];

      let cooldownExpiration = null;
      // Check all possible keys for an active cooldown
      for (const key of possibleKeys) {
        const value = localStorage.getItem(key);
        if (value) {
          cooldownExpiration = value;
          this.activeCooldownKey = key; // Store the key that worked
          break;
        }
      }

      if (cooldownExpiration) {
        const remainingTime = parseInt(cooldownExpiration) - Date.now();

        // If cooldown is still active
        if (remainingTime > 0) {
          this.cooldownActive = true;
          this.cooldownSeconds = Math.ceil(remainingTime / 1000);

          // Start the cooldown timer
          this.cooldownTimer = setInterval(() => {
            this.cooldownSeconds--;

            if (this.cooldownSeconds <= 0) {
              clearInterval(this.cooldownTimer);
              this.cooldownActive = false;
              localStorage.removeItem(this.activeCooldownKey);
            }
          }, 1000);
        } else {
          // Cleanup expired cooldowns
          localStorage.removeItem(this.activeCooldownKey);
        }
      }
    },
    formatTag(tag) {
      let [newTag, test] = tag.split("#");
      return newTag;
    },
    async updateSummoner() {
      // Existing update code...
      try {
        console.log("Update: fetching new data");
        const summonerResponse = await axios.get(
          `/summoners/${this.localRegion}/${this.localSummonerName}-${this.localTag}`
        );

        console.log(summonerResponse.data);
        // Update the profile data
        this.localProfileIconUrl =
          "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/profile-icons/" +
          summonerResponse.data.summonerProfileIconId +
          ".jpg"; // Adjust based on actual response structure
        this.localSummonerName = summonerResponse.data.summonerName;
        this.localSummonerLevel = summonerResponse.data.summonerLevel;
        this.localPuuid = summonerResponse.data.puuid;

        const rankResponse = await axios.post(`/ranks`, {
          puuid: this.localPuuid,
        });

        console.log(rankResponse.data);
        console.log(rankResponse.data);
        if (!rankResponse.data || rankResponse.data.length === 0) {
          // Set defaults for unranked players
          this.localSummonerRank = "Unranked";
          this.localRankSoloIconUrl =
            "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
          this.localWins = 0;
          this.localLosses = 0;

          this.localSummonerRankFlex = "Unranked";
          this.localRankFlexIconUrl =
            "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
          this.localWinsFlex = 0;
          this.localLossesFlex = 0;
        } else {
          // Process ranks with safety checks
          let soloRankRes = rankResponse.data[0] || {};
          let flexRankRes = rankResponse.data[1] || {};

          if (soloRankRes.queueType === "RANKED_FLEX_SR") {
            soloRankRes = rankResponse.data[1] || {};
            flexRankRes = rankResponse.data[0] || {};
          }

          // Solo queue rank
          if (soloRankRes && soloRankRes.tier) {
            this.localSummonerRank = `${soloRankRes.tier} ${soloRankRes.rank} ${soloRankRes.leaguePoints} LP`;
            this.localRankSoloIconUrl = `https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-${soloRankRes.tier.toLowerCase()}.png`;
            this.localWins = soloRankRes.wins || 0;
            this.localLosses = soloRankRes.losses || 0;
          } else {
            this.localSummonerRank = "Unranked";
            this.localRankSoloIconUrl =
              "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
            this.localWins = 0;
            this.localLosses = 0;
          }

          // Flex queue rank
          if (flexRankRes && flexRankRes.tier) {
            this.localSummonerRankFlex = `${flexRankRes.tier} ${flexRankRes.rank} ${flexRankRes.leaguePoints} LP`;
            this.localRankFlexIconUrl = `https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-${flexRankRes.tier.toLowerCase()}.png`;
            this.localWinsFlex = flexRankRes.wins || 0;
            this.localLossesFlex = flexRankRes.losses || 0;
          } else {
            this.localSummonerRankFlex = "Unranked";
            this.localRankFlexIconUrl =
              "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
            this.localWinsFlex = 0;
            this.localLossesFlex = 0;
          }
        }

        this.isUpdating = false;
        this.startCooldown();

        const currentTime = Date.now();
        this.lastUpdatedAt = currentTime;
        this.hasBeenUpdated = true;

        // Store in localStorage for persistence
        const lastUpdateKey = `lastUpdated_${this.localSummonerName}`;
        localStorage.setItem(lastUpdateKey, currentTime.toString());

        this.startUpdateTimeTracking();
      } catch (error) {
        console.error("Error updating summoner:", error);
        this.isUpdating = false;
        this.$emit("update-error", error);
        this.startCooldown();
      }
    },
    startCooldown() {
      this.cooldownActive = true;
      this.cooldownSeconds = 60; // 1 minute cooldown

      // IMPORTANT: Use the localSummonerName and localRegion for consistency
      const cooldownExpirationTime = Date.now() + this.cooldownSeconds * 1000;
      const cooldownKey = `cooldown_${this.localSummonerName}_${this.localRegion}`;
      localStorage.setItem(cooldownKey, cooldownExpirationTime.toString());

      // Clear any existing timer
      if (this.cooldownTimer) {
        clearInterval(this.cooldownTimer);
      }

      // Start a new countdown timer
      this.cooldownTimer = setInterval(() => {
        this.cooldownSeconds--;

        if (this.cooldownSeconds <= 0) {
          clearInterval(this.cooldownTimer);
          this.cooldownActive = false;
          // Remove cooldown from localStorage when it expires
          localStorage.removeItem(cooldownKey);
        }
      }, 1000);
    },
    startUpdateTimeTracking() {
      // Clear any existing update tracking interval
      if (this.updateInterval) {
        clearInterval(this.updateInterval);
      }

      // Update the "last updated" text every 10 seconds
      this.updateInterval = setInterval(() => {
        // Increment the ticker to trigger reactivity
        this.updateTicker++;
      }, 1000); // Update every 10 seconds
    },
    beforeUnmount() {
      if (this.cooldownTimer) {
        clearInterval(this.cooldownTimer);
      }
      if (this.updateInterval) {
        clearInterval(this.updateInterval);
      }
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
  font-family: Verdana, Geneva, Tahoma, sans-serif;
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
