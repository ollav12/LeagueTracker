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
      v-if="hasMoreMatches"
      @click="loadMoreMatches"
      class="show-more-button"
    >
      Show More
    </button>
  </div>
</template>

<script>
export default {
  props: {
    matches: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      displayLimit: 20,
    };
  },
  computed: {
    displayedMatches() {
      return this.matches.slice(0, this.displayLimit);
    },
    hasMoreMatches() {
      return this.matches.length > this.displayLimit;
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
    loadMoreMatches() {
      this.displayLimit += 20;
    },
  },
};
</script>

<style scoped>
.match-history {
  width: 100%;
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
  background-color: white;
  border-radius: 4px;
  padding: 14px;
  display: flex;
  flex-direction: column;
  width: 100%;
  height: auto;
  max-height: 96px;
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
  width: 740px;
  height: 96px;
  padding: 100px;
  margin-top: 10px;
  background-color: #edeef2;
  color: blue;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.show-more-button:hover {
  background-color: #e1e2e6;
}
</style>
