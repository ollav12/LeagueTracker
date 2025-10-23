<template>
  <div class="match-history">
    <!-- Header section with integrated queue filter -->
    <div class="match-history-header">
      <div class="header-left">
        <h3 class="history-title">Match History</h3>
        <div class="match-stats" v-if="summary.stats">
          <span class="total-games">{{ filteredMatches.length }}G:  </span>
          <span class="wins">{{ stats.wins }}W</span>
          <span class="separator"> - </span>
          <span class="losses">{{ stats.losses }}L</span>
          <span class="separator"> WR </span>
          <span class="win-rate">{{ stats.winRate }}%</span>
        </div>
      </div>

      <!-- Add queue filter dropdown -->
      <div class="queue-filter">
        <div class="queue-selector" @click="toggleDropdown">
          {{ getActiveQueueName() }} <span class="dropdown-arrow">▼</span>
        </div>
        <div class="queue-dropdown" v-if="dropdownOpen">
          <div
              v-for="queue in queueTypes"
              :key="queue.id"
              class="queue-option"
              :class="{ active: activeQueue === queue.id }"
              @click="selectQueue(queue.id)"
          >
            {{ queue.name }}
          </div>
        </div>
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
              {{ getPlayerStats(match).win ? "W" : "L" }}
              <div class="game-duration">
                {{ formatGameDuration(match.info.gameDuration) }}
              </div>
            </div>
          </div>

          <!-- Middle section - can add additional info here if needed -->
          <div class="match-middle-info">
            <!-- Player champion and stats could go here -->
            <div class="champion-info">
              <img
                  :src="getChampionIcon(getPlayerStats(match).championId)"
                  class="champion-icon"
                  :alt="getChampionName(getPlayerStats(match).championId)"
              />
              <div class="player-stats-summary">
                <div class="kda-text">
                  {{ getPlayerStats(match).kills }}/{{
                    getPlayerStats(match).deaths
                  }}/{{ getPlayerStats(match).assists }}
                </div>
              </div>
            </div>
          </div>

          <!-- Right side - Modified Champion Icons Layout -->
          <div class="teams-icons-container">
            <!-- Both teams side by side -->
            <div class="teams-icons-row">
              <!-- Blue team icons -->
              <div class="team-icons blue-team">
                <img
                    v-for="role in [
                    'TOP',
                    'JUNGLE',
                    'MIDDLE',
                    'BOTTOM',
                    'UTILITY',
                  ]"
                    :key="'blue-' + role"
                    :src="
                    getChampionIcon(
                      getPlayerByRole(match, 100, role).championId
                    )
                  "
                    class="preview-champion-icon"
                    :alt="role"
                />
              </div>

              <!-- Red team icons -->
              <div class="team-icons red-team">
                <img
                    v-for="role in [
                    'TOP',
                    'JUNGLE',
                    'MIDDLE',
                    'BOTTOM',
                    'UTILITY',
                  ]"
                    :key="'red-' + role"
                    :src="
                    getChampionIcon(
                      getPlayerByRole(match, 200, role).championId
                    )
                  "
                    class="preview-champion-icon"
                    :alt="role"
                />
              </div>
            </div>
          </div>

          <!-- Expand button -->
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
import {storeToRefs} from "pinia";
import {useSummonerStore} from "@/stores/modules/summoner";
import {useQueueFilterStore} from "@/stores/modules/queueFilter";
import {QUEUE_MODES, QUEUE_FILTER_MAPPING} from "@/constants/queueModes";

export default {
  name: "MatchHistory",

  setup() {
    const summonerStore = useSummonerStore();
    const queueFilterStore = useQueueFilterStore();
    const {summary, summoner} = storeToRefs(summonerStore);
    const {activeQueue} = storeToRefs(queueFilterStore);

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
      dropdownOpen: false, // Add this for dropdown state
      queueTypes: [
        {id: "all", name: "All Games"},
        {id: "solo", name: "Solo/Duo"},
        {id: "flex", name: "Flex 5v5"},
        {id: "aram", name: "ARAM"},
        {id: "normal-draft", name: "Normal Draft"},
        {id: "normal-blind", name: "Normal Blind"},
      ],
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

    // Add these new methods for the queue filter
    toggleDropdown() {
      this.dropdownOpen = !this.dropdownOpen;
    },

    selectQueue(queueId) {
      const queueFilterStore = useQueueFilterStore();
      queueFilterStore.setActiveQueue(queueId);
      this.dropdownOpen = false;
    },

    getActiveQueueName() {
      const queue = this.queueTypes.find((q) => q.id === this.activeQueue);
      return queue ? queue.name : "All Games";
    },

    // Close dropdown when clicking elsewhere
    closeDropdownOutside(event) {
      if (!this.$el.querySelector(".queue-filter")?.contains(event.target)) {
        this.dropdownOpen = false;
      }
    },
  },

  // Add mounted/unmounted hooks to handle dropdown clicks outside
  mounted() {
    document.addEventListener("click", this.closeDropdownOutside);
  },
  beforeUnmount() {
    document.removeEventListener("click", this.closeDropdownOutside);
  },
};
</script>

<style scoped>
.separator {
  color: white; /* Ensure "-" and "WR" remain white */
  margin-right: 5px;
  margin-left: 5px;
}

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
  color: white;
}

.match-history {
  width: 760px;
  margin-top: 5px
}

.match-history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #34333b;
  border-radius: 4px;
  padding: 10px 16px;
  margin-bottom: 5px;
  height: 30px;
  width: 760px;
  color: white;
}

.header-left {
  display: flex;
  align-items: center;
}

/* Queue filter styles */
.queue-filter {
  position: relative;
}

.queue-selector {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px 8px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  font-size: 12px;
  color: white;
  transition: all 0.2s ease;
  width: 100px;
  height: 20px;
  margin-right: -10px;
}

.queue-selector:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

.dropdown-arrow {
  font-size: 8px;
  color: white;
  margin-left: auto;
}

.queue-dropdown {
  position: absolute;
  width: 100px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
  margin-top: 4px;
  overflow: hidden;
}

.queue-option {
  padding: 8px 12px;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 12px;
  color: #34333b;
}

.queue-option:hover {
  background-color: rgba(67, 180, 155, 0.1);
}

.queue-option.active {
  background-color: #43b49b;
  color: white;
}

.match-stats {
  display: flex;
  font-size: 12px;
  color: white;
  font-weight: 400;
  margin-left: 15px;
}

.wins {
  color: #43B49B;
}

.losses {
  color: #E15656;
}

.total-games {
  color: #999999;
  margin-right: 5px;
}

.match-cards {
  display: grid;
  grid-template-columns: 1fr;
  gap: 5px;
}

.match-card {
  border-radius: 4px;
  margin-bottom: 2px;
  background-color: white;
  position: relative;
  box-sizing: border-box;
}

.match-card.win {
  padding-left: 6px;
  background-color: #43b49b;
}

.match-card.loss {
  padding-left: 6px;
  background-color: #e15656;
}

.match-card.expanded {
  height: auto;
}

.match-card-main {
  height: 100px;
  display: flex;
  align-items: center;
}

.match-info {
  width: 130px;
  padding-right: 12px;
  border-right: 1px solid white;
}

.win .game-type {
  font-size: 12px;
  color: #1b5850;
  margin-bottom: 4px;
  font-weight: 700;
}

.loss .game-type {
  font-size: 12px;
  color: #5e3838;
  margin-bottom: 0px;
  font-weight: 700;
}

.time-ago {
  font-size: 11px;
  color: white;
  font-weight: 400;
  margin-top: 20px;
}

.game-result {
  font-size: 17px;
  font-weight: 700;
  display: inline;
}

.win .game-result {
  color: #1b5850;
}

.loss .game-result {
  color: #5e3838;
}

.game-duration {
  font-size: 11px;
  color: white;
  display: inline;
}

/* Middle section style */
.match-middle-info {
  flex: 1;
  display: flex;
  align-items: center;
  padding-left: 15px;
}

/* Champion icon styles */
.champion-icon {
  width: 40px;
  height: 40px;
  border: 1px solid #d2d2d2;
}

/* New styles for the teams icons section */
.teams-icons-container {
  margin-right: 50px; /* 50px padding from right */
  padding: 5px; /* 5px padding overall */
}

.teams-icons-row {
  display: flex;
  gap: 2px; /* 2px padding between teams */
}

.team-icons {
  display: flex;
  flex-direction: column;
  gap: 2px; /* 2px padding between icons */
}

.preview-champion-icon {
  width: 20px;
  height: 20px;
  /* Border radius removed to make icons square */
  object-fit: cover;
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
  color: white;
}

.expand-button {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  cursor: pointer;
  color: transparent;
}

/* Remove the hover effect */
.expand-button:hover {
  transform: none;
}

.match-details {
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  padding: 12px;
}

.teams-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.team h4 {
  margin: 0 0 8px 0;
  color: white;
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
  color: white;
}

.player-stats {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-left: auto;
  font-size: 12px;
  color: white;
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

.placeholder {
  background-color: white;
  border-radius: 50%;
  width: 32px;
  height: 32px;
}

/* Add this new style */
.kda {
  margin-left: auto;
  color: white;
  font-size: 14px;
}

.queue-type {
  font-size: 12px;
  color: white;
  margin-left: 8px;
}

.player-stats-summary {
  display: flex;
  flex-direction: column;
  margin-left: 10px;
}

.kda-text {
  font-size: 14px;
  color: white;
  font-weight: 600;
}
</style>
