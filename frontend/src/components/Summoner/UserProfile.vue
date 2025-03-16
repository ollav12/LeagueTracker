<template>
    <div class="user-profile">
      <img :src="profileIconUrl" alt="Profile Icon" class="profile-icon" />
      <h2>{{ summonerName }}</h2>
      <p>Level: {{ summonerLevel }}</p>
      <p>Rank: {{ summonerRank }}</p>
      <p>{{ wins }}W - {{ losses }}L ({{ winPercentage}}%)</p>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      profileIconUrl: String,
      summonerName: String,
      summonerLevel: Number,
      summonerRank: String,
      wins: String,
      losses: String,
    },
    computed: {
        winPercentage() {
        const totalGames = this.wins + this.losses;
        if (totalGames === 0) {
            return 0;
        }
        return Math.round((this.wins / totalGames) * 100);
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