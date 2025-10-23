<template>
  <div class="rank-history-container" v-if="summoner">
    <div class="rank-history">
      <!-- Solo/Duo Section -->
      <template v-if="showSoloRank && summoner.ranked">
        <h3 class="rank-title">Ranked Solo/Duo</h3>
        <div class="rank-row">
          <div class="rank-entry">
            <div class="rank-icon-container">
              <img :src="currentSoloRankUrl" alt="Solo Rank" class="rank-icon"/>
            </div>
            <div class="rank-info">
              <div class="rank-status">
                <span class="rankText">{{ formatRank(this.summoner.ranked.solo.currentRank) }}</span>
                <span class="win-loss-text"
                >{{ soloWins }}W {{ soloLosses }}L</span
                >
              </div>
              <div class="stats-row">
                <span class="lp-text">{{ soloLp("current") }} LP</span>
                <span class="win-percent">Win rate {{ getSoloWinRate }}%</span>
              </div>
            </div>
          </div>

          <!-- Peak rank section -->
          <div class="rank-entry peak-rank">
            <div class="rank-icon-container">
              <img :src="peakSoloRankUrl" alt="Peak Rank" class="rank-icon"/>
            </div>
            <div class="rank-info">
              <div class="rank-status">
                <span class="rankText">{{ formatRank(this.summoner.ranked.solo.peakRank) }}</span>
              </div>
              <div class="peak-stats-row">
                <span class="lp-text">{{ soloLp("peak") }} LP</span>
              </div>
            </div>
          </div>
        </div>

        <div class="ranks-section">
          <div class="rank-row ranks-container">
            <table class="ranks-table">
              <thead>
              <tr>
                <th class="title-season">Season</th>
                <th class="title-tier">Tier</th>
                <!-- Combined column -->
                <th class="title-lp">LP</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(row, i) in displayedRows" :key="i">
                <td class="season-row">{{ row }}</td>
                <!-- Combined tier cell with both icon and text -->

                <td class="tier-row">
                  <div class="tier-content">
                    <div class="mini-icon-container">
                      <img
                          :src="`https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/assets/ux/fonts/texticons/lol/ranks/rank${getRankName(
                            this.summoner.ranked.solo.currentRank
                          )}.png`"
                          :alt="null"
                          class="mini-rank-icon"
                      />
                    </div>
                    <span class="tier-text">{{ formatRank(this.summoner.ranked.solo.currentRank) }}</span>
                  </div>
                </td>
                <td class="lp-row">{{ row.leaguePoints }}</td>
              </tr>
              </tbody>
            </table>

            <div class="view-all-container" v-if="data.rows.length > 5">
              <button @click="toggleSeasonDisplay" class="view-all-button">
                {{ showAllSeasons ? "Close" : "View all season tiers" }}
                <span
                    class="toggle-arrow"
                    :class="{ 'arrow-up': showAllSeasons }"
                >
                  ▼
                </span>
              </button>
            </div>
          </div>
        </div>
      </template>

      <!-- Flex Section -->
      <template v-if="showFlexRank && summoner.ranked">
        <h3 class="rank-title rank-flex-title">
          Ranked Flex
          <span v-if="showFlexRank === 'Unranked'" class="unranked-badge"
          >Unranked</span
          >
        </h3>
        <div v-if="showFlexRank === 'Unranked'" class="unranked-spacer"></div>
        <div v-if="showFlexRank !== 'Unranked'" class="rank-row">
          <div class="rank-entry">
            <div class="rank-icon-container">
              <img :src="currentFlexRankUrl" alt="Flex Rank" class="rank-icon"/>
            </div>
            <div class="rank-info">
              <div class="rank-status">
                <span class="rankText">{{ formatRank(this.summoner.ranked.flex.currentRank) }}</span>
                <span class="win-loss-text"
                >{{ flexWins }}W {{ flexLosses }}L</span
                >
              </div>
              <div class="stats-row">
                <span class="lp-text"> {{ flexLp("current") }} LP</span>
                <span class="win-percent"
                >Win rate {{ winPercentageFlex }}%</span
                >
              </div>
            </div>
          </div>
        </div>
        <div class="ranks-section">
          <div class="rank-row ranks-container">
            <table class="ranks-table">
              <thead>
              <tr>
                <th class="title-season">Season</th>
                <th class="title-tier">Tier</th>
                <th class="title-lp">LP</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(row, i) in displayedFlexRows" :key="i">
                <td class="season-row">{{ row }}</td>
                <td class="tier-row">
                  <div class="tier-content">
                    <div class="mini-icon-container">
                      <img
                          :src="`https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/assets/ux/fonts/texticons/lol/ranks/rank${getRankName(
                            this.summoner.ranked.flex.currentRank
                          )}.png`"
                          :alt="null"
                          class="mini-rank-icon"
                      />
                    </div>
                    <span class="tier-text">{{ formatRank(this.summoner.ranked.flex.currentRank) }}</span>
                  </div>
                </td>
                <td class="lp-row">{{ flexLp("current") }}</td>
              </tr>
              </tbody>
            </table>

            <div class="view-all-container" v-if="flexData.rows.length > 5">
              <button @click="toggleFlexSeasonDisplay" class="view-all-button">
                {{ showAllFlexSeasons ? "Close" : "View all season tiers" }}
                <span
                    class="toggle-arrow"
                    :class="{ 'arrow-up': showAllFlexSeasons }"
                >
                  ▼
                </span>
              </button>
            </div>
          </div>
        </div>
      </template>

      <!-- Show message when ARAM is selected -->
      <div v-if="activeQueue === 'aram'" class="no-rank-message">
        <p>No rank information available for ARAM queue</p>
      </div>
    </div>
  </div>
</template>
<script>
import {storeToRefs} from "pinia";
import {useSummonerStore} from "@/stores/modules/summoner";
import {useQueueFilterStore} from "@/stores/modules/queueFilter";
import {onMounted, onBeforeUnmount} from "vue";

export default {
  name: "RankHistory",
  data() {
    return {
      showAllSeasons: false,
      showAllFlexSeasons: false,
      data: {
        rows: [],
      },
      flexData: {
        rows: [],
      },
    };
  },

  setup() {
    const summonerStore = useSummonerStore();
    const queueFilterStore = useQueueFilterStore();
    const {summoner} = storeToRefs(summonerStore);
    const {activeQueue} = storeToRefs(queueFilterStore);

    // Cleanup function for component unmount
    onBeforeUnmount(() => {
      // Add any cleanup if needed
    });

    return {
      summoner,
      activeQueue,
    };
  },

  computed: {
    soloWins() {
      return this.summoner.ranked.solo?.wins || 0;
    },
    soloLosses() {
      return this.summoner.ranked.solo?.losses || 0;
    },
    getSoloWinRate() {
      const wins = this.soloWins;
      const total = wins + this.soloLosses;
      return total > 0 ? Math.round((wins / total) * 100) : 0;
    },
    flexWins() {
      return this.summoner.ranked.flex?.wins || 0;
    },
    flexLosses() {
      return this.summoner.ranked.flex?.losses || 0;
    },
    currentSoloRankUrl() {
      if (!this.summoner.ranked.solo) {
        return "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
      }
      return this.summoner.ranked.solo.currentRankUrl;
    },
    peakSoloRankUrl() {
      if (!this.summoner.ranked.solo) {
        return "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
      }
      return this.summoner.ranked.solo.peakRankUrl;
    },
    lowestSoloRankUrl() {
      if (!this.summoner.ranked.solo) {
        return "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
      }
      return this.summoner.ranked.solo.lowestRankUrl;
    },
    currentFlexRankUrl() {
      if (!this.summoner.ranked.flex) {
        return "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
      }
      return this.summoner.ranked.flex.currentRankUrl;
    },
    peakFlexRankUrl() {
      if (!this.summoner.ranked.flex) {
        return "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
      }
      return this.summoner.ranked.flex.peakRankUrl;
    },
    lowestFlexRankUrl() {
      if (!this.summoner.ranked.flex) {
        return "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
      }
      return this.summoner.ranked.flex.lowestRankUrl;
    },
    displayedFlexRows() {
      if (this.showAllFlexSeasons) {
        return this.flexData.rows;
      } else {
        return this.flexData.rows.slice(0, 5);
      }
    },
    displayedRows() {
      if (this.showAllSeasons) {
        return this.data.rows;
      } else {
        // Show only the first 5 seasons
        return this.data.rows.slice(0, 5);
      }
    },
    winPercentageFlex() {
      const totalGames = this.flexWins + this.flexLosses;
      if (totalGames === 0) {
        return 0;
      }
      return Math.round((this.flexWins / totalGames) * 100);
    },
    showSoloRank() {
      // Show for 'all', 'solo', and all dropdown queue types
      return (
          this.activeQueue === "all" ||
          this.activeQueue === "solo" ||
          ["normal-draft", "normal-blind", "clash"].includes(this.activeQueue)
      );
    },
    showFlexRank() {
      // Show for 'all', 'flex', and all dropdown queue types
      return (
          this.activeQueue === "all" ||
          this.activeQueue === "flex" ||
          ["normal-draft", "normal-blind", "clash"].includes(this.activeQueue)
      );
    },
  },

  methods: {
    soloLp(rankType) {
      if (rankType === "current") {
        const lp = this.summoner.ranked.solo.currentRank.split(" ")
        return lp[2];
      } else if (rankType === "peak") {
        const lp = this.summoner.ranked.solo.peakRank.split(" ")
        return lp[2];
      } else if (rankType === "lowest") {
        const lp = this.summoner.ranked.solo.lowestRank.split(" ")
        return lp[2];
      } else {
        return 0
      }
    },
    flexLp(rankType) {
      if (rankType === "current") {
        const lp = this.summoner.ranked.flex.currentRank.split(" ")
        return lp[2];
      } else if (rankType === "peak") {
        const lp = this.summoner.ranked.flex.peakRank.split(" ")
        return lp[2];
      } else if (rankType === "lowest") {
        const lp = this.summoner.ranked.flex.lowestRank.split(" ")
        return lp[2];
      } else {
        return 0
      }
    },
    toggleFlexSeasonDisplay() {
      this.showAllFlexSeasons = !this.showAllFlexSeasons;
    },
    toggleSeasonDisplay() {
      this.showAllSeasons = !this.showAllSeasons;
    },
    formatRank(rank) {
      if (!rank) return "Unranked";

      // Split the rank into tier and division
      const [name, division, lp] = rank.split(" ");

      // Convert roman numerals to numbers
      const romanToNumber = {
        I: "1",
        II: "2",
        III: "3",
        IV: "4",
      };

      // Capitalize first letter, lowercase rest
      const formattedTier =
          name.charAt(0).toUpperCase() + name.slice(1).toLowerCase();

      // Check if tier is one without divisions
      const noDivisionTiers = ["challenger", "grandmaster", "master"];
      if (noDivisionTiers.includes(name.toLowerCase())) {
        return formattedTier;
      }

      // For other tiers, include the division number
      const formattedDivision = romanToNumber[division] || "";
      return formattedDivision
          ? `${formattedTier} ${formattedDivision}`
          : formattedTier;
    },
    getRankName(rank) {
      if (!rank) return "";
      let [rankName, tier, lp] = rank.split(" ");
      rankName = rankName.toLowerCase();
      return rankName;
    },
  },
};
</script>
<style scoped>
.unranked-spacer {
  height: 19px;
  width: 100%;
}

/* Remove negative margin for unranked tables */
.unranked-flex-table {
  margin-top: 0px !important;
}

/* Fix title padding */
.rank-flex-title {
  padding-bottom: 8px;
}

.unranked-badge {
  font-family: "Inter";
  font-size: 14px;
  font-weight: 700;
  line-height: 20px;
  color: white;
  text-align: right;
  padding-left: 173px;
}

/* Add styles for the arrow */
.toggle-arrow {
  display: inline-block;
  font-size: 10px;
  margin-left: 6px;
  transition: transform 0.2s ease;
}

/* Rotate the arrow when expanded */
.arrow-up {
  transform: rotate(180deg);
}

.view-all-container {
  text-align: center;
  padding: 0;
  width: 332px;
  border-top: 1px solid #34333B;
  margin: 0;
  /* Add this to match the table's left margin */
  margin-left: -16px;
}

/* Update button styles */
.view-all-button {
  font-family: "Inter";
  color: white;
  font-size: 12px;
  background-color: #34333B; /* Light gray background */
  border: none;
  cursor: pointer;
  padding: 0;
  font-weight: 400;
  line-height: 16px;
  border-radius: 0 0 4px 4px; /* Round only bottom corners */
  transition: background-color 0.2s;
  width: 100%;
  height: 32px; /* Fixed height */
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0px 0px 4px 4px;
}

.tier-content {
  display: flex;
  align-items: center;
  gap: 6px; /* Space between icon and text */
}

.mini-icon-container {
  width: 14px;
  height: 14px;
  display: inline-block;
  overflow: hidden;
  flex-shrink: 0;
}

.title-season .title-tier .title-lp {
  font-family: "Inter";
  font-size: 11px;
  line-height: 16px;
  font-weight: 400;
  color: white;
}

/* Center the icon column */
.ranks-table th.icon-column {
  width: 40px;
  text-align: left;
}

.ranks-table th.title-tier {
  padding-left: 12px;
  text-align: left;
}

.ranks-table th.title-season {
  padding-left: 0px; /* Remove left padding */
  width: 60px;
  text-align: left;
  padding-right: 25px;
  padding-left: 10px;
}

.ranks-table th.title-lp {
  text-align: right;
  width: 70px; /* Set a fixed width */
  padding-right: 10px;
}

.season-row {
  font-family: "Inter";
  font-weight: 700;
  font-size: 11px;
  line-height: 14px;
  color: white; /* Slightly darker for better contrast on grey */
  padding: 0px 4px; /* Padding inside the box */
  background-color: #34333B; /* Light grey background */
  border-radius: 2px; /* Rounded corners */
  display: inline-block; /* Ensure the background only wraps the text */
  margin-left: 8px; /* Space from the left edge */
}

/* Also add this to increase specificity */
.ranks-table th.title-season,
.ranks-table th.title-tier,
.ranks-table th.title-lp {
  font-size: 12px;
  color: white;
  font-weight: 400;
  line-height: 16px;
  padding-bottom: 5px;
}

.ranks-table thead {
  border-bottom: 1px solid #34333B;
}

.tier-row {
  font-family: "Inter";
  font-weight: 400;
  font-size: 12px;
  line-height: 16px;
  color: white;
  text-align: left;
  padding-left: 20px;
}

.lp-row {
  font-family: "Inter";
  font-weight: 400px;
  font-size: 12px;
  line-height: 16px;
  color: white;
  text-align: right;
  padding-right: 10px;
}

.rank-row.ranks-container {
  padding: 16px; /* Remove padding that's causing extra height */
  margin-bottom: -10px; /* Remove bottom margin */
  border-radius: 0 0 0 0; /* Remove any border radius from container */
  box-shadow: none; /* Remove shadow which might be visible */
  background-color: transparent;
}

.icon-row {
  padding-left: 15px;
  width: 20px;
}

.ranks-section {
  margin-top: -30px;
}

.ranks-table {
  width: 332px;
  border-collapse: collapse;
  text-align: left;
  position: relative;
  margin-left: -16px;
  margin-bottom: 0px;
  background-color: #34333B;
  margin-top: -5px;
}

.mini-icon-container {
  width: 20px;
  height: 20px;
  display: inline-block;
  overflow: hidden;
}

.mini-rank-icon {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.peak-rank .rank-icon-container {
  height: 44px;
  width: 44px;
  margin-right: 0px; /* Same as the main icon container */
  margin-left: 14px; /* Add this to center the smaller icon (72-36)/2 = 18px */
  position: right; /* Use relative instead of invalid 'left' */
  overflow: hidden;
}

.peak-rank .rank-icon {
  height: 36px;
  transform: scale(1); /* Scale the icon to fill container better */
  object-position: center 85%; /* Position icon consistently */
}

.peak-rank .rankText {
  font-size: 14px; /* Smaller than the 20px for main rank */
  line-height: 20px;
  margin-right: 80px; /* Adjusted spacing */
  font-weight: 700;
  position: relative;
  margin-left: 20px;
  margin-top: -10px;
}

.peak-rank .lp-text {
  margin-left: 20px;
  margin-bottom: 0px;
  margin-top: -10px;
}

.stats-row {
  display: flex;
  justify-content: space-between;
  align-items: left;
  width: 100%;
  margin-top: 0px;
}

.rank-status {
  display: flex;
  justify-content: space-between;
  align-items: left;
  width: 100%;
  margin-top: 10px;
}

.win-percent {
  font-size: 12px;
  color: white;
  margin: 0 0 4px 0;
  font-weight: 400;
  line-height: 16px;
}

.win-loss-text {
  font-size: 12px;
  color: white;
  font-weight: 400;
  line-height: 26px;
}

.lp-text {
  font-family: "Inter";
  color: white;
  font-size: 12px;
  line-height: 16px;
  font-weight: 400;
}

.rank-history-container {
  width: auto;
  height: auto;
  box-sizing: border-box;
  margin-top: 5px;
}

.rank-history {
  display: flex;
  flex-direction: column;
}

.rank-title {
  margin-top: 0px;
  margin-bottom: 1px;
  font-size: 14px;
  font-weight: 400;
  color: white;
  background-color: #34333B;
  width: 332px;
  height: 30px;
  border-radius: 4px 4px 0 0;
  padding: 10px;
  font-family: "Inter";
  align-items: center;
  display: flex;
  padding-left: 10px;
  line-height: 20px;
}

.rank-row {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 16px;
  font-weight: 700;
  background-color: #34333B;
  border-radius: 0px;
  padding: 16px;
  width: 332px;
  height: auto; /* Change from fixed height to auto */
}

.rank-entry {
  display: flex;
  width: 100%;
  padding: 6px 0;
}

.rank-icon {
  height: 72px;
  object-fit: cover;
  object-position: center 85%;
  transform: scale(0.9);
}

.rank-icon-container {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 20px;
  background-color: #1B5850;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-shrink: 0;
}

.rank-info {
  display: flex;
  flex-direction: column;
}

.rankText {
  font-size: 20px;
  line-height: 28px;
  margin: 0;
  font-weight: 700;
  color: white;
  white-space: nowrap;
  margin-right: 50px;
  flex-shrink: 0;
}

@media screen and (max-width: 720px) {
  .rank-history-container {
    width: 100%;
    height: auto;
  }

  .rank-icon-container {
    width: 60px;
    height: 60px;
  }

  .rank-icon {
    width: 110px;
    height: 110px;
    object-fit: cover;
    object-position: center 85%;
    transform: scale(0.2); /* Changed from 1.3 to 1 */
  }

  .rankText {
    font-size: 14px;
  }
}

.no-rank-message {
  background-color: white;
  border-radius: 4px;
  padding: 16px;
  text-align: center;
  width: 332px;
  color: rgb(117, 133, 146);
  font-family: "Inter";
  font-size: 14px;
}
</style>
