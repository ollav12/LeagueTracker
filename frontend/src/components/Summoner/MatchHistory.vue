<!-- MatchHistory.vue template -->
<template>
  <div class="match-history">
    <!-- Header section -->
    <div class="match-history-header">
      <h3 class="history-title">Recent Games</h3>
      <div class="match-stats">
        <span class="total-games">20G</span>
        <span class="wins">12W</span>
        <span class="losses">8L</span>
        <span class="win-rate">60% WR</span>
      </div>
    </div>

    <!-- Match cards -->
    <div class="match-cards">
      <div
        v-for="(match, index) in displayedMatches"
        :key="index"
        class="match-card"
        :class="{ win: match.win, loss: !match.win }"
      >
        <!-- Match details here -->
        <div class="match-result">{{ match.win ? "Victory" : "Defeat" }}</div>
        <div class="champion-info">
          <img
            :src="match.championIconUrl"
            alt="Champion"
            class="champion-icon"
          />
          <div class="champion-name">{{ match.championName }}</div>
        </div>
        <!-- Additional match details -->
      </div>
    </div>

    <!-- Show more button -->
    <button
      v-if="hasMoreMatchesToLoad"
      @click="loadMoreMatches"
      class="show-more-button"
    >
      Show more
    </button>
  </div>
</template>

<script>
import axios from "@/plugins/axios";
export default {
  data() {
    return {
      matches: [],
      puuid: "",
      displayLimit: 20,
    };
  },
  mounted() {
    this.puuid = localStorage.getItem("puuid");
    this.fetchMatches();
  },
  computed: {
    displayedMatches() {
      return this.matches.slice(0, this.displayLimit);
    },
    hasMoreMatches() {
      return this.matches.length > this.displayLimit;
    },
    hasMoreMatchesToLoad() {
      // This will determine if the button should show
      return this.matches.length >= this.displayLimit;
    },
    totalGames() {
      return this.matches.length;
    },
    wins() {
      return this.matches.filter((match) => match.win).length;
    },
    losses() {
      return this.matches.filter((match) => !match.win).length;
    },
    winRate() {
      return this.totalGames > 0
        ? Math.round((this.wins / this.totalGames) * 100)
        : 0;
    },
  },
  methods: {
    async fetchMatches() {
      console.log("Mounted Match History: loading matches");
      const matchResponse = await axios.post("/matches", {
        puuid: this.puuid,
      });

      console.log(matchResponse.data);
      this.matches = this.generateTestData();
    },
    generateTestData() {
      const champions = [
        {
          name: "Ahri",
          iconUrl:
            "https://ddragon.leagueoflegends.com/cdn/13.24.1/img/champion/Ahri.png",
        },
        {
          name: "Yasuo",
          iconUrl:
            "https://ddragon.leagueoflegends.com/cdn/13.24.1/img/champion/Yasuo.png",
        },
        {
          name: "Jinx",
          iconUrl:
            "https://ddragon.leagueoflegends.com/cdn/13.24.1/img/champion/Jinx.png",
        },
        {
          name: "Lux",
          iconUrl:
            "https://ddragon.leagueoflegends.com/cdn/13.24.1/img/champion/Lux.png",
        },
        {
          name: "Darius",
          iconUrl:
            "https://ddragon.leagueoflegends.com/cdn/13.24.1/img/champion/Darius.png",
        },
        {
          name: "Lee Sin",
          iconUrl:
            "https://ddragon.leagueoflegends.com/cdn/13.24.1/img/champion/LeeSin.png",
        },
        {
          name: "Zed",
          iconUrl:
            "https://ddragon.leagueoflegends.com/cdn/13.24.1/img/champion/Zed.png",
        },
        {
          name: "Caitlyn",
          iconUrl:
            "https://ddragon.leagueoflegends.com/cdn/13.24.1/img/champion/Caitlyn.png",
        },
        {
          name: "Thresh",
          iconUrl:
            "https://ddragon.leagueoflegends.com/cdn/13.24.1/img/champion/Thresh.png",
        },
        {
          name: "Leona",
          iconUrl:
            "https://ddragon.leagueoflegends.com/cdn/13.24.1/img/champion/Leona.png",
        },
      ];

      // Generate 40 test matches
      const testMatches = [];
      for (let i = 0; i < 40; i++) {
        const champion = champions[i % champions.length];
        testMatches.push({
          id: `match-${i}`,
          win: i % 3 !== 0, // 2/3 wins, 1/3 losses for positive win rate
          championName: champion.name,
          championIconUrl: champion.iconUrl,
          kills: Math.floor(Math.random() * 15),
          deaths: Math.floor(Math.random() * 10),
          assists: Math.floor(Math.random() * 20),
          gameType: i % 2 === 0 ? "Ranked Solo" : "Ranked Flex",
          gameDate: `${Math.floor(i / 5) + 1}d ago`,
        });
      }

      // Emit event to update parent component with test data
      this.$emit("update:matches", testMatches);
      return testMatches;
    },
    loadMoreMatches() {
      if (this.hasMoreMatchesToLoad) {
        // Use the computed property
        this.displayLimit += 20;
      }
    },
  },
};
</script>

<style scoped>
.champion-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%; /* Makes the image circular */
  object-fit: cover; /* Ensures the image fills the circle properly */
  border: 1px solid #d2d2d2; /* Optional: adds a subtle border */
}

/* Style the champion info container for better alignment */
.champion-info {
  display: flex;
  align-items: center;
  gap: 8px; /* Space between icon and name */
}

.champion-name {
  font-size: 14px;
  font-weight: 500;
  color: #202d37;
}

.match-history {
  width: 100%;
  margin-bottom: 20px;
}

.match-history-header {
  display: flex;
  align-items: left;
  background-color: white;
  border-radius: 4px;
  padding: 10px 16px;
  margin-bottom: 5px;
  height: 170px;
  width: 740px;
}

.history-title {
  font-size: 14px;
  font-weight: 400;
  color: #202a38;
  margin: 0;
  padding-right: 100px;
}

.match-stats {
  display: flex;
  gap: 10px;
  font-size: 12px;
  color: #758592;
}

.wins {
  color: #5383e9;
}

.losses {
  color: #e84057;
}

.match-cards {
  display: grid;
  grid-template-columns: 1fr;
  gap: 5px;
}

.match-card {
  border-radius: 4px;
  padding: 14px;
  display: flex;
  flex-direction: column;
  width: 740px;
  height: 96px;
}

.match-card.win {
  border-left: 6px solid #5782ea;
  background-color: #ecf2ff;
}

.match-card.loss {
  border-left: 6px solid #ea4156;
  background-color: #fff0f3;
}

.show-more-button {
  width: 740px; /* Match your other elements for now */
  /* Later you can change to 100% when other elements are fixed */
  height: 40px;
  margin-top: 10px;
  background-color: white;
  color: rgb(32, 45, 55);
  font-family: "Roboto";
  font-size: 14px;
  line-height: 20px;
  border: 1px solid #e0e0e2;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 400;
  text-align: center;
  display: block;
}
</style>
