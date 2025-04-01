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
        <div class="match-card-main">
          <!-- Left side -->
          <div class="match-info">
            <div class="game-type">
              {{ getQueueType(match.info.queueId) }}
              <div class="time-ago">
                {{ formatTimeAgo(match.info.gameEndTimestamp) }}
              </div>
            </div>
            <div class="game-result">
              {{ getPlayerStats(match).win ? "Victory" : "Defeat" }}
              <div class="game-duration">
                {{ formatGameDuration(match.info.gameDuration) }}
              </div>
            </div>
          </div>

          <!-- Middle - Players -->
          <div class="teams-preview">
            <div class="team-column">
              <!-- Blue team -->
              <div
                v-for="role in ['TOP', 'JUNGLE', 'MIDDLE', 'BOTTOM', 'UTILITY']"
                :key="role"
                class="player-preview"
              >
                <img
                  :src="
                    getChampionIcon(
                      getPlayerByRole(match, 100, role).championId
                    )
                  "
                  class="preview-champion-icon"
                />
                <span class="preview-name">{{
                  getPlayerByRole(match, 100, role).summonerName
                }}</span>
              </div>
            </div>
            <div class="team-column">
              <!-- Red team -->
              <div
                v-for="role in ['TOP', 'JUNGLE', 'MIDDLE', 'BOTTOM', 'UTILITY']"
                :key="role"
                class="player-preview"
              >
                <img
                  :src="
                    getChampionIcon(
                      getPlayerByRole(match, 200, role).championId
                    )
                  "
                  class="preview-champion-icon"
                />
                <span class="preview-name">{{
                  getPlayerByRole(match, 200, role).summonerName
                }}</span>
              </div>
            </div>
          </div>

          <!-- Right side - Expand button -->
          <button
            class="expand-button"
            :class="{
              win: getPlayerStats(match).win,
              loss: !getPlayerStats(match).win,
            }"
            @click="toggleMatchDetails(match.metadata.matchId)"
          >
            ▼
          </button>
        </div>

        <!-- Expanded details (your existing expanded view) -->
        <div
          v-if="expandedMatches.has(match.metadata.matchId)"
          class="match-details"
        >
          <div class="teams-container">
            <div class="team" v-for="team in [100, 200]" :key="team">
              <h4>{{ team === 100 ? "Blue Team" : "Red Team" }}</h4>
              <div class="players-list">
                <div
                  v-for="player in match.info.participants.filter(
                    (p) => p.teamId === team
                  )"
                  :key="player.puuid"
                  class="player-row"
                  :class="{
                    'current-player': player.puuid === summoner.account.puuid,
                  }"
                >
                  <img
                    :src="getChampionIcon(player.championId)"
                    class="small-champion-icon"
                  />
                  <span class="player-name">{{ player.summonerName }}</span>
                  <div class="player-stats">
                    <span class="player-kda"
                      >{{ player.kills }}/{{ player.deaths }}/{{
                        player.assists
                      }}</span
                    >
                    <span class="player-cs"
                      >{{
                        player.totalMinionsKilled + player.neutralMinionsKilled
                      }}
                      CS</span
                    >
                    <span class="player-vision"
                      >Vision: {{ player.visionScore }}</span
                    >
                    <div class="player-items">
                      <img
                        v-for="itemId in [
                          player.item0,
                          player.item1,
                          player.item2,
                          player.item3,
                          player.item4,
                          player.item5,
                          player.item6,
                        ]"
                        :key="itemId"
                        :src="getItemIcon(itemId)"
                        class="item-icon"
                        v-if="itemId !== 0"
                      />
                    </div>
                  </div>
                </div>
              </div>
            </div>
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
      expandedMatches: new Set(), // Track which matches are expanded
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

    toggleMatchDetails(matchId) {
      if (this.expandedMatches.has(matchId)) {
        this.expandedMatches.delete(matchId);
      } else {
        this.expandedMatches.add(matchId);
      }
    },

    getItemIcon(itemId) {
      // Temporary placeholder until item data is implemented
      return `https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/item-icons/${itemId}.png`;
    },

    formatTimeAgo(timestamp) {
      console.log(timestamp);
      const now = Date.now();
      const diffInSeconds = Math.floor((now - timestamp) / 1000);
      console.log(diffInSeconds);
      const minutes = Math.floor(diffInSeconds / 60);
      const hours = Math.floor(minutes / 60);
      const days = Math.floor(hours / 24);
      const weeks = Math.floor(days / 7);
      const months = Math.floor(weeks / 4);

      if (months == 1) return `${months} month ago`;
      else if (months > 1) return `${months} months ago`;

      if (weeks == 1) return `${weeks} week ago`;
      else if (weeks > 1) return `${weeks} weeks ago`;

      if (days == 1) return `${days} day ago`;
      else if (days > 1) return `${days} days ago`;
      if (hours == 1) return `${hours} hour ago`;
      else if (hours > 1) return `${hours} hours ago`;
      if (minutes == 1) return `${minutes} minute ago`;
      else if (minutes > 1) return `${minutes} minutes ago`;
      return "just now";
    },

    formatGameDuration(duration) {
      const hours = Math.floor(duration / 3600);
      const minutes = Math.floor((duration % 3600) / 60);
      const seconds = duration % 60;

      if (hours > 0) {
        return `${hours}h ${minutes.toString().padStart(2, "0")}m ${seconds
          .toString()
          .padStart(2, "0")}s`;
      }
      return `${minutes}m ${seconds.toString().padStart(2, "0")}s`;
    },

    getPlayerByRole(match, teamId, role) {
      return (
        match.info.participants.find(
          (p) => p.teamId === teamId && p.teamPosition === role
        ) || {
          championId: 0,
          summonerName: "Unknown",
        }
      );
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
  margin-bottom: 8px;
  background-color: white;
}

.match-card.win {
  border-left: 6px solid #5782ea;
  background-color: #ecf2ff;
}

.match-card.loss {
  border-left: 6px solid #ea4156;
  background-color: #fff0f3;
}

.match-card.expanded {
  height: auto;
}

.match-card-main {
  height: 96px;
  padding: 12px 0 12px 12px; /* Remove right padding */
  display: flex;
  align-items: center;
}

.match-info {
  width: 130px;
  padding-right: 12px;
  border-right: 1px solid rgba(0, 0, 0, 0.1);
}

.game-type {
  font-size: 12px;
  color: #758592;
  margin-bottom: 4px;
}

.time-ago {
  font-size: 11px;
  color: #9aa4af;
  margin-top: 2px;
}

.game-result {
  font-size: 14px;
  font-weight: 600;
  margin-top: 8px;
}

.win .game-result {
  color: #5383e8;
}

.loss .game-result {
  color: #e84057;
}

.game-duration {
  font-size: 11px;
  color: #9aa4af;
  margin-top: 2px;
}

.teams-preview {
  flex: 1;
  display: flex;
  justify-content: space-between;
  padding: 0 20px;
}

.team-column {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.player-preview {
  display: flex;
  align-items: center;
  gap: 6px;
}

.preview-champion-icon {
  width: 16px;
  height: 16px;
  border-radius: 50%;
}

.preview-name {
  font-size: 12px;
  color: #758592;
}

.expand-button {
  width: 30px;
  height: 96px;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 12px;
  margin-right: 0px; /* Pull button to edge */
  margin-left: 12px; /* Add spacing from content */
  border-top-right-radius: 4px; /* Add this */
  border-bottom-right-radius: 4px; /* Add this */
}

.expand-button.win {
  background-color: #4b73d6;
}

.expand-button.loss {
  background-color: #d63d52;
}

/* Remove the hover effect */
.expand-button:hover {
  transform: none;
}

.match-details {
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  padding: 16px;
}

.teams-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.team h4 {
  margin: 0 0 8px 0;
  color: #758592;
  font-size: 14px;
}

.players-list {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.player-row {
  display: flex;
  align-items: center;
  padding: 4px;
  border-radius: 4px;
  margin-bottom: 4px;
}

.player-row.current-player {
  background-color: rgba(0, 0, 0, 0.05);
}

.small-champion-icon {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  margin-right: 8px;
}

.player-name {
  font-size: 12px;
  font-weight: 500;
  color: #202d37;
}

.player-stats {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-left: auto;
  font-size: 12px;
  color: #758592;
}

.player-items {
  display: flex;
  gap: 2px;
  margin-left: 8px;
}

.item-icon {
  width: 22px;
  height: 22px;
  border-radius: 4px;
  margin-right: 2px;
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
