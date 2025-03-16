<template>
    <div class="user-profile">
      <img :src="profileIconUrl" alt="Profile Icon" class="profile-icon" />
      <h2>{{ summonerName }}</h2>
      <p>Level: {{ summonerLevel }}</p>
      <p>Ranked Solo: {{ summonerRank }}    |          {{ wins }}W - {{ losses }}L ({{ winPercentage }}%)</p>
      <p>Ranked Flex: {{ summonerRankFlex }}    |          {{ winsFlex }}W - {{ lossesFlex }}L ({{ winPercentageFlex }}%)</p>
    </div>
  </template>
  
  <script>
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
    computed: {
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
  }
}
  </script>
  
  <style scoped>
  .user-profile {
    text-align: center;
  }
  
  .profile-icon {
    width: 100px;
    height: 100px;
    border-radius: 50%;
  }
  </style>