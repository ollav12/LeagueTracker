<template>
  <div class="match-history">
    <!-- Header section -->
    <div class="match-history-header">
      <h3 class="history-title">Recent Games</h3>
      <div class="match-stats" v-if="summary.stats">
        <span class="total-games">{{ filteredMatches.length }}G</span>
        <span class="wins">{{ stats.wins }}W</span>
        <span class="losses">{{ stats.losses }}L</span>
        <span class="win-rate">{{ stats.winRate }}% WR</span>
      </div>
    </div>

    <!-- Match cards -->
    <div class="match-cards">
      <div
        v-for="match in filteredMatches"
        :key="match.metadata.matchId"
        class="match-card"
        :class="{
          win: getPlayerStats(match).win,
          loss: !getPlayerStats(match).win,
        }"
      >
        <div class="match-result">
          {{ getPlayerStats(match).win ? "Victory" : "Defeat" }}
          <span class="queue-type">{{ getQueueType(match.info.queueId) }}</span>
        </div>
        <div class="champion-info">
          <img
            :src="getChampionIcon(getPlayerStats(match).championId)"
            :alt="getChampionName(getPlayerStats(match).championId)"
            class="champion-icon"
          />
          <div class="champion-name">
            {{ getChampionName(getPlayerStats(match).championId) }}
          </div>
          <div class="kda">
            {{ getPlayerStats(match).kills }}/{{
              getPlayerStats(match).deaths
            }}/{{ getPlayerStats(match).assists }}
          </div>
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
import { useQueueFilterStore } from "@/stores/modules/queueFilter";
import { QUEUE_MODES, QUEUE_FILTER_MAPPING } from "@/constants/queueModes";

export default {
  name: "MatchHistory",

  setup() {
    const summonerStore = useSummonerStore();
    const queueFilterStore = useQueueFilterStore();
    const { summary, summoner } = storeToRefs(summonerStore);
    const { activeQueue } = storeToRefs(queueFilterStore);

    return {
      summary,
      summoner,
      activeQueue,
      loadMoreMatches: summonerStore.moreMatches, // Changed from summaryRequest to moreMatches
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

    currentPuuid() {
      return this.summary?.puuid || this.$store.state.summoner?.account?.puuid;
    },

    filteredMatches() {
      const queueIds = QUEUE_FILTER_MAPPING[this.activeQueue];
      if (!queueIds.length) return this.summary.matches; // Return all matches for 'all' filter

      return this.summary.matches.filter((match) =>
        queueIds.includes(match.info.queueId)
      );
    },

    stats() {
      let wins = 0;
      let losses = 0;

      this.filteredMatches.forEach((match) => {
        const playerStats = this.getPlayerStats(match);
        if (playerStats.win) wins++;
        else losses++;
      });

      return {
        wins,
        losses,
        winRate: Math.round((wins / (wins + losses)) * 100) || 0,
      };
    },
  },

  watch: {
    "summary.matches": {
      handler(matches) {
        this.updateMatchStats(matches);
      },
      immediate: true,
    },
  },

  methods: {
    getChampionIcon(championId) {
      // Temporary placeholder until champion data is implemented
      return `https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/${championId}.png`;
    },
    getChampionName(championId) {
      // Temporary placeholder until champion data is implemented
      return `Champion ${championId}`;
    },
    getPlayerStats(match) {
      const playerParticipant = match.info.participants.find(
        (p) => p.puuid === this.summoner.account.puuid
      );

      if (!playerParticipant) {
        console.warn("Player not found in match:", match.metadata.matchId);
        return {
          win: false,
          championId: 0,
          kills: 0,
          deaths: 0,
          assists: 0,
        };
      }

      return {
        win: playerParticipant.win,
        championId: playerParticipant.championId,
        kills: playerParticipant.kills,
        deaths: playerParticipant.deaths,
        assists: playerParticipant.assists,
        // Add more stats as needed
      };
    },

    getQueueType(queueId) {
      return QUEUE_MODES[queueId] || "Custom Game";
    },

    updateMatchStats(matches) {
      let wins = 0;
      let losses = 0;

      matches.forEach((match) => {
        const stats = this.getPlayerStats(match);
        if (stats.win) {
          wins++;
        } else {
          losses++;
        }
      });

      // Update summary stats
      this.summary.stats = {
        ...this.summary.stats,
        totalGames: wins + losses,
        wins: wins,
        losses: losses,
        winRate: Math.round((wins / (wins + losses)) * 100) || 0,
      };
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

.placeholder {
  background-color: #eaeaea;
  border-radius: 50%;
  width: 32px;
  height: 32px;
}

/* Add this new style */
.kda {
  margin-left: auto;
  color: #666;
  font-size: 14px;
}

.queue-type {
  font-size: 12px;
  color: #758592;
  margin-left: 8px;
}
</style>
