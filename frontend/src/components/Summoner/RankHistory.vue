<template>
  <div class="rank-history-container">
    <div class="queue-nav">
      <button class="queue-nav-item active">All</button>
      <button class="queue-nav-item">Ranked Solo/Duo</button>
      <button class="queue-nav-item">Ranked Flex</button>
      <button class="queue-nav-item">Aram</button>
      <div class="queue-dropdown">
        <button class="queue-nav-item">
          QueueType <span class="dropdown-arrow">▼</span>
        </button>
        <div class="dropdown-content">
          <a href="#">Normal Draft</a>
          <a href="#">Normal Blind</a>
          <a href="#">Clash</a>
        </div>
      </div>
    </div>

    <div class="rank-history">
      <h3 class="rank-title">Ranked Solo/Duo</h3>
      <!-- Single rank-row containing both current and peak rank -->
      <div class="rank-row">
        <!-- First rank display (current) -->
        <div class="rank-entry">
          <div class="rank-icon-container">
            <img :src="soloRankIconUrl" alt="Solo Rank" class="rank-icon" />
          </div>
          <div class="rank-info">
            <div class="rank-status">
              <span class="rankText">{{ formatRank(soloRank) }}</span>
              <span class="win-loss-text"
                >{{ soloWins }}W {{ soloLosses }}L</span
              >
            </div>
            <div class="stats-row">
              <span class="lp-text">{{ soloLp }}</span>
              <span class="win-percent">Win rate {{ winPercentage }}%</span>
            </div>
          </div>
        </div>

        <!-- Second rank display (peak) -->
        <div class="rank-entry peak-rank">
          <div class="rank-icon-container">
            <img :src="soloRankIconUrl" alt="Peak Rank" class="rank-icon" />
          </div>
          <div class="rank-info">
            <div class="rank-status">
              <span class="rankText">{{ formatRank(soloRank) }}</span>
              <span class="top-tier">Top Tier</span>
            </div>
            <div class="stats-row">
              <span class="lp-text">{{ soloLp }}</span>
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
                <td class="season-row">{{ row.season }}</td>
                <!-- Combined tier cell with both icon and text -->
                <td class="tier-row">
                  <div class="tier-content">
                    <div class="mini-icon-container">
                      <img
                        :src="`https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/ranked-emblems/emblems/ranked-${row.tier.toLowerCase()}.png`"
                        :alt="row.tier"
                        class="mini-rank-icon"
                      />
                    </div>
                    <span class="tier-text">{{
                      formatTierName(row.tier)
                    }}</span>
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

      <h3 class="rank-title">Ranked Flex</h3>
      <div class="rank-row">
        <div class="rank-icon-container">
          <img :src="flexRankIconUrl" alt="Flex Rank" class="rank-icon" />
        </div>
        <div class="rank-info">
          <div class="rank-status">
            <span class="rankText">{{ formatRank(flexRank) }}</span>
            <span class="win-loss-text">{{ flexWins }}W {{ flexLosses }}L</span>
          </div>
          <div class="stats-row">
            <span class="lp-text">{{ flexLp }}</span>
            <span class="win-percent">Win rate {{ winPercentageFlex }}%</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    soloRank: String,
    soloWins: Number,
    soloLosses: Number,
    soloRankIconUrl: String,
    flexRank: String,
    flexWins: Number,
    flexLosses: Number,
    flexRankIconUrl: String,
  },
  data() {
    return {
      soloLp: "",
      flexLp: "",
      showAllSeasons: false,
      data: {
        rows: [],
      },
    };
  },
  mounted() {
    this.fetchSoloranks();
  },
  watch: {
    soloRank(newVal) {
      let [tier, rank, lp, text] = newVal.split(" ");
      this.soloLp = lp + " " + text;
    },
    flexRank(newVal) {
      let [tier, rank, lp, text] = newVal.split(" ");
      this.flexLp = lp + " " + text;
    },
  },
  computed: {
    displayedRows() {
      if (this.showAllSeasons) {
        return this.data.rows;
      } else {
        // Show only the first 5 seasons
        return this.data.rows.slice(0, 5);
      }
    },
    winPercentage() {
      const totalGames = this.soloWins + this.soloLosses;
      if (totalGames === 0) {
        return 0;
      }
      return Math.round((this.soloWins / totalGames) * 100);
    },
    winPercentageFlex() {
      const totalGames = this.flexWins + this.flexLosses;
      if (totalGames === 0) {
        return 0;
      }
      return Math.round((this.flexWins / totalGames) * 100);
    },
  },
  methods: {
    toggleSeasonDisplay() {
      this.showAllSeasons = !this.showAllSeasons;
    },
    async fetchSoloranks() {
      try {
        // Mock data
        const mockData = {
          rows: [
            {
              season: "S2024 S3",
              rankIconId: 13,
              tier: "CHALLENGER",
              leaguePoints: "756",
            },
            {
              season: "S2023 S2",
              rankIconId: 12,
              tier: "GRANDMASTER",
              leaguePoints: "521",
            },
            {
              season: "S13",
              rankIconId: 11,
              tier: "MASTER",
              leaguePoints: "312",
            },
            {
              season: "S12",
              rankIconId: 10,
              tier: "DIAMOND",
              leaguePoints: "75",
            },
            {
              season: "S12",
              rankIconId: 9,
              tier: "PLATINUM",
              leaguePoints: "89",
            },
            { season: "S11", rankIconId: 8, tier: "GOLD", leaguePoints: "45" },
            {
              season: "S11",
              rankIconId: 7,
              tier: "SILVER",
              leaguePoints: "67",
            },
            {
              season: "S10",
              rankIconId: 6,
              tier: "BRONZE",
              leaguePoints: "28",
            },
          ],
        };
        // Update data property
        this.data = mockData;
      } catch (error) {
        console.error(error);
      }
    },
    formatRank(soloRank) {
      let [rank, tier, leaguePoints] = soloRank.split(" "); // Use proper array destructuring

      // Convert to lowercase, then capitalize first letter
      rank = rank.toLowerCase();
      rank = rank.charAt(0).toUpperCase() + rank.slice(1);

      if (tier == "I") {
        tier = "1";
      } else if (tier == "II") {
        tier = "2";
      } else if (tier == "III") {
        tier = "3";
      } else if (tier == "IV") {
        tier = "4";
      } else {
        tier = "";
      }
      return rank + " " + tier; // Return the formatted value
    },
    formatTierName(tier) {
      if (!tier) return "";
      const lowerCaseTier = tier.toLowerCase();
      return lowerCaseTier.charAt(0).toUpperCase() + lowerCaseTier.slice(1);
    },
  },
};
</script>
<style scoped>
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
  border-top: 1px solid #edeef2;
  margin: 0;
  /* Add this to match the table's left margin */
  margin-left: -16px;
}

/* Update button styles */
.view-all-button {
  font-family: "Roboto";
  color: rgb(117, 133, 146);
  font-size: 12px;
  background-color: #f7f7f9; /* Light gray background */
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
  font-family: "Roboto";
  font-size: 11px;
  line-height: 16px;
  font-weight: 400;
  color: rgb(32, 45, 55);
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
  font-family: "Roboto";
  font-weight: 700;
  font-size: 11px;
  line-height: 18px;
  color: rgb(154, 164, 175); /* Slightly darker for better contrast on grey */
  padding: 0px 3px; /* Padding inside the box */
  background-color: #eceff4; /* Light grey background */
  border-radius: 4px; /* Rounded corners */
  display: inline-block; /* Ensure the background only wraps the text */
  margin-left: 8px; /* Space from the left edge */
}

/* Also add this to increase specificity */
.ranks-table th.title-season,
.ranks-table th.title-tier,
.ranks-table th.title-lp {
  font-size: 12px;
  color: rgb(32, 45, 55);
  font-weight: 400;
  line-height: 16px;
  padding-bottom: 5px;
}

.ranks-table thead {
  border-bottom: 1px solid #edeef2;
}

.tier-row {
  font-family: "Roboto";
  font-weight: 400;
  font-size: 12px;
  line-height: 16px;
  color: rgb(117, 133, 146);
  text-align: left;
  padding-left: 20px;
}

.lp-row {
  font-family: "Roboto";
  font-weight: 400px;
  font-size: 12px;
  line-height: 16px;
  color: rgb(117, 133, 146);
  text-align: right;
  padding-right: 10px;
}

.rank-row.ranks-container {
  padding: 16px; /* Remove padding that's causing extra height */
  margin-bottom: 5px; /* Remove bottom margin */
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
  background-color: white;
  margin-top: -5px;
}

.mini-icon-container {
  width: 14px;
  height: 14px;
  display: inline-block;
  overflow: hidden;
}

.mini-rank-icon {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.queue-nav {
  display: flex;
  background-color: white;
  border-radius: 8px;
  margin-bottom: 0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  position: relative;
  height: 44px;
  width: 1080px;
  margin-bottom: 8px;
  margin-top: -17px;
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

.queue-nav-item:hover::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #ddd;
}

.queue-nav-item.active {
  color: #4f84ea;
  font-weight: 700;
}

.queue-nav-item.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #4f84ea;
}

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

.queue-dropdown:hover .dropdown-content {
  display: block;
}

.top-tier {
  font-family: "Roboto";
  font-size: 11px;
  line-height: 14px;
  color: rgb(255, 255, 255);
  font-weight: 700;
  background-color: #4f84ea; /* Blue background color */
  padding: 3px 8px; /* Inner spacing */
  border-radius: 10px; /* Rounded corners */
  display: inline-block; /* Proper block formatting */
  letter-spacing: 0.5px; /* Optional: slight letter spacing for better readability */
}
.peak-rank .rank-icon-container {
  width: 36px; /* Smaller than the 72px for main rank */
  height: 36px;
  margin-right: 20px; /* Same as the main icon container */
  margin-left: 18px; /* Add this to center the smaller icon (72-36)/2 = 18px */
  position: relative; /* Use relative instead of invalid 'left' */
  overflow: hidden;
}

.peak-rank .rank-icon {
  width: 65px;
  height: 65px;
  transform: scale(1.2); /* Scale the icon to fill container better */
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
  color: rgb(154, 164, 175);
  margin: 0 0 4px 0;
  font-weight: 400;
  line-height: 16px;
}

.win-loss-text {
  font-size: 12px;
  color: rgb(154, 164, 175);
  font-weight: 400;
  line-height: 26px;
}

.lp-text {
  font-family: "Roboto";
  color: rgb(117, 133, 146);
  font-size: 12px;
  line-height: 16px;
  font-weight: 400;
}

.rank-history-container {
  width: auto;
  height: auto;
  background-color: #edeef2; /* Grey background */
  border-radius: 0px;
  margin-bottom: 20px;
  padding: 24px;
  box-sizing: border-box;
  padding-left: 400px;
}

.rank-history {
  display: flex;
  flex-direction: column;
}

.rank-title {
  margin-top: 0;
  margin-bottom: 1px;
  font-size: 14px;
  font-weight: 400;
  color: #202a38;
  background-color: white;
  width: 332px;
  height: 30px;
  border-radius: 4px 4px 0 0;
  padding: 10px;
  font-family: "roboto";
  line-height: 14px;
}

/* Add white background boxes around each rank row */
.rank-row {
  display: flex;
  flex-direction: column; /* This is the key change - stack items vertically */
  align-items: flex-start;
  margin-bottom: 16px;
  font-weight: 700;
  background-color: white;
  border-radius: 0px;
  padding: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  width: 332px;
  height: auto; /* Change from fixed height to auto */
}

.rank-entry {
  display: flex;
  width: 100%;
  padding: 6px 0;
}

.rank-icon {
  width: 130px;
  height: 130px;
  object-fit: cover;
  object-position: center 85%;
  transform: scale(1.3);
}

.rank-icon-container {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 20px;
  background-color: #f7f7f9;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-shrink: 0;
}

.rank-info {
  display: flex;
  flex-direction: column;
}

.queue-type {
  font-size: 14px;
  color: #7d7d7d;
  margin: 0 0 4px 0;
  font-weight: 500;
}

.rankText {
  font-size: 20px;
  line-height: 28px;
  margin: 0;
  font-weight: 700;
  color: #202a38;
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
  }

  .rankText {
    font-size: 14px;
  }
}
</style>
