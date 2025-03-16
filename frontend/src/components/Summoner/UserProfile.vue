<template>
    <div class="user-profile">
      <div class="profile-header">
        <div class="icon-container">
          <img :src="profileIconUrl" alt="Profile Icon" class="profile-icon" />
          <span class="level">{{ summonerLevel }}</span>
        </div>
          <div class="name-container">
          <h2 class="summoner-name">{{ summonerName }}</h2>
          <button class="update-button" @click="updateSummoner" :disabled="isUpdating || cooldownActive">
            {{ buttonText }}
          </button>
          <p v-if="cooldownActive" class="cooldown-text">Refresh available in: {{ cooldownSeconds }} seconds</p>
          <p v-else-if="hasBeenUpdated" class="cooldown-text">Last updated: {{ lastUpdatedText }}</p>
        </div>
      </div>
      <p>Ranked Solo: {{ summonerRank }}|{{ wins }}W - {{ losses }}L ({{ winPercentage }}%)</p>
      <p>Ranked Flex: {{ summonerRankFlex }} | {{ winsFlex }}W - {{ lossesFlex }}L ({{ winPercentageFlex }}%)</p>
    </div>
  </template>
  
  <script>
  import axios from '@/plugins/axios';
  export default {
    props: {
      profileIconUrl: String,
      summonerName: String,
      summonerLevel: Number,
      summonerRank: String,
      wins: Number,
      losses: Number,
      summonerRankFlex: String,
      winsFlex: Number,
      lossesFlex: Number,
    },
    data() {
      return {
        isUpdating: false,
        cooldownActive: false,
        cooldownSeconds: 0,
        cooldownTimer: null,
        lastUpdatedAt: null,
        hasBeenUpdated: false,
        updateInterval: null,
        updateTicker: 0
      }
    },
    computed: {
      lastUpdatedText() {
        this.updateTicker;
        if (!this.lastUpdatedAt)
          return '';
        
        const secondsAgo = Math.floor((Date.now() - this.lastUpdatedAt) / 1000);
        
        if (secondsAgo < 60) {
          return `${secondsAgo} seconds ago`;
        } else {
          const minutesAgo = Math.floor(secondsAgo / 60);
          return `${minutesAgo} minute${minutesAgo !== 1 ? 's' : ''} ago`;
        }
      },
      buttonText() {
        if (this.isUpdating) 
          return 'Updating...';
        if (this.cooldownActive) 
          return `Updated`;
        return 'Update';
      },
      winPercentage() {
        const totalGames = this.wins + this.losses;
        if (totalGames === 0) {
          return 0;
        }
        return Math.round((this.wins / totalGames) * 100);
      },
      winPercentageFlex() {
        const totalGames = this.winsFlex + this.lossesFlex;
        if (totalGames === 0) {
          return 0;
        }
        return Math.round((this.winsFlex / totalGames) * 100);
      }
    },
    mounted() {
      // On mount, restore last updated timestamp if available
      const lastUpdateKey = `lastUpdated_${this.summonerName}`;
      const savedTimestamp = localStorage.getItem(lastUpdateKey);

      if (savedTimestamp) {
        this.lastUpdatedAt = parseInt(savedTimestamp);
        this.hasBeenUpdated = true;
        this.startUpdateTimeTracking();
      }
    },
    methods: {
      async updateSummoner() {
        // Existing update code...
        try {
          this.isUpdating = true;
          const [name, tag] = (this.summonerName || '').split('#');
          const summonerResponse = await axios.get(`/summoners/${tag}/${name}-${tag}`);

          this.isUpdating = false;
          this.startCooldown();

          const currentTime = Date.now();
          this.lastUpdatedAt = currentTime;
          this.hasBeenUpdated = true;

          // Store in localStorage for persistence
          const lastUpdateKey = `lastUpdated_${this.summonerName}`;
          localStorage.setItem(lastUpdateKey, currentTime.toString());

          this.startUpdateTimeTracking();
        } catch (error) {
          console.error('Error updating summoner:', error);
          this.isUpdating = false;
          this.$emit('update-error', error);
          this.startCooldown();
      }
    },
    startCooldown() {
      this.cooldownActive = true;
      this.cooldownSeconds = 10; // 1 minute cooldown
      
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
  }
}
</script>

<style scoped>
.user-profile {
  text-align: left;
  
}

.icon-container {
  position: relative;
  display: inline-block;
}

.profile-header {
  display: flex;
  align-items: top;
  margin-bottom: 15px;
}

.profile-icon {
  width: 100px;
  height: 100px;
  border-radius: 20%;
  margin-right: 15px;
}

.summoner-name {
  margin: 0;
  font-size: 24px;
  font-weight: 550;
}

.level {
  position: absolute;
  bottom: -4px;
  left: 50px;
  transform: translateX(-50%);
  background-color: #202a38;
  color: white;
  border-radius: 50px;
  padding: 1px 8px;
  font-size: 10px;
  min-width: 20px;
  max-width: 80px;
  text-align: center;
}

.update-button {
  margin-top: 8px;
  background-color: #4f84ea;
  color: white;
  border: none;
  border-radius: 3px;
  padding: 10px 10px;
  font-size: 17px;
  cursor: pointer;
  transition: background-color 0.3s;
  align-self: flex-start;
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
}

.cooldown-text {
  margin-top: 5px;
  font-size: 12px;
  color: #8a8a8a;
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
</style>