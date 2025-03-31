<template>
  <div class="match-history">
    <!-- Header section -->
    <div class="match-history-header">
      <h3 class="history-title">Recent Games</h3>
      <div class="match-stats" v-if="summary.stats">
        <span class="total-games">{{ totalGames }}G</span>
        <span class="wins">{{ wins }}W</span>
        <span class="losses">{{ losses }}L</span>
        <span class="win-rate">{{ winRate }}% WR</span>
      </div>
    </div>

    <!-- Match cards -->
    <div class="match-cards">
      <div
        v-for="(match, index) in displayedMatches"
        :key="match.matchId"
        class="match-card"
        :class="{ win: match.win, loss: !match.win }"
      >
        <div class="match-result">{{ match.win ? "Victory" : "Defeat" }}</div>
        <div class="champion-info">
          <img
            :src="match.champion.iconUrl"
            :alt="match.champion.name"
            class="champion-icon"
          />
          <div class="champion-name">{{ match.champion.name }}</div>
        </div>
      </div>
    </div>

    <!-- Show more button -->
    <button
      v-if="hasMoreMatchesToLoad"
      @click="loadMoreMatches"
      class="show-more-button"
      :disabled="isLoading"
    >
      {{ isLoading ? "Loading..." : "Show more" }}
    </button>
  </div>
</template>

<script>
import { storeToRefs } from "pinia";
import { useSummonerStore } from "@/stores/modules/summoner";

export default {
  name: "MatchHistory",

  setup() {
    const store = useSummonerStore();
    const { summary } = storeToRefs(store);

    return {
      summary,
      loadMoreMatches: store.summaryRequest,
    };
  },

  data() {
    return {
      displayLimit: 20,
    };
  },

  computed: {
    displayedMatches() {
      return this.summary.matches.slice(0, this.displayLimit);
    },

    hasMoreMatchesToLoad() {
      return this.summary.moreMatchesToFetch;
    },

    isLoading() {
      return this.summary.matchesLoading;
    },

    totalGames() {
      return this.summary.stats.totalGames || 0;
    },

    wins() {
      return this.summary.stats.wins || 0;
    },

    losses() {
      return this.summary.stats.losses || 0;
    },

    winRate() {
      return this.summary.stats.winRate || 0;
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
