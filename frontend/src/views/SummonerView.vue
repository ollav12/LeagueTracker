<template>
  <div>
    <!-- Navigation Bar -->
    <nav>
      <!-- Back to Home Button -->
      <button class="home-button" @click="goHome">
        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368">
          <path d="M160-120v-375l-72 55-48-64 440-336 440 336-48 63-72-54v375H160Zm160-240q-17 0-28.5-11.5T280-400q0-17 11.5-28.5T320-440q17 0 28.5 11.5T360-400q0 17-11.5 28.5T320-360Zm160 0q-17 0-28.5-11.5T440-400q0-17 11.5-28.5T480-440q17 0 28.5 11.5T520-400q0 17-11.5 28.5T480-360Zm160 0q-17 0-28.5-11.5T600-400q0-17 11.5-28.5T640-440q17 0 28.5 11.5T680-400q0 17-11.5 28.5T640-360Z"/>
        </svg>
      </button>

      <!-- Search Form -->
      <div class="searchForm">
        <SearchForm @formSubmit="handleFormSubmit"/>
      </div>

      <!-- Dark Mode Toggle Button -->
      <button id="theme-switch" @click="toggleDarkMode">
        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368">
          <path d="M480-280q-83 0-141.5-58.5T280-480q0-83 58.5-141.5T480-680q83 0 141.5 58.5T680-480q0 83-58.5 141.5T480-280ZM200-440H40v-80h160v80Zm720 0H760v-80h160v80ZM440-760v-160h80v160h-80Zm0 720v-160h80v160h-80ZM256-650l-101-97 57-59 96 100-52 56Zm492 496-97-101 53-55 101 97-57 59Zm-98-550 97-101 59 57-100 96-56-52ZM154-212l101-97 55 53-97 101-59-57Z"/>
        </svg>
        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368">
          <path d="M480-120q-150 0-255-105T120-480q0-150 105-255t255-105q14 0 27.5 1t26.5 3q-41 29-65.5 75.5T444-660q0 90 63 153t153 63q55 0 101-24.5t75-65.5q2 13 3 26.5t1 27.5q0 150-105 255T480-120Z"/>
        </svg>
      </button>
    </nav>

    <!-- Content Area -->
    <div class="page-container">
      <div class="content">
        <div class="user-profile">
          <UserProfile
            :profileIconUrl="profileIconUrl"
            :summonerName="summonerName"
            :summonerLevel="summonerLevel"
            :summonerRank="summonerRank"
            :wins="wins"
            :losses="losses"
            :summonerRankFlex="summonerRankFlex"
            :winsFlex="winsFlex"
            :lossesFlex="lossesFlex"
          />
        </div>
        </div class="matches">
        <MatchHistory :matches="matches" />
        </div>
    </div>
</template>

<script>
import axios from '@/plugins/axios'; // Import the configured axios instance
import SearchForm from '@/components/Form/SearchForm.vue'
import UserProfile from '@/components/Summoner/UserProfile.vue'
import MatchHistory from '@/components/Summoner/MatchHistory.vue'

export default {
  components: {
    SearchForm,
    UserProfile,
    MatchHistory
  },
  data() {
    return {
      puuid: null,
      profileIconUrl: null,
      summonerName: '',
      summonerLevel: null,
      summonerRank: '',
      wins: 0,
      losses: 0,
      summonerRankFlex: '',
      winsFlex: 0,
      lossesFlex: 0,
      matches: [],
      toggle: false,
      darkmode: localStorage.getItem('darkmode'),
      themeSwitch: document.getElementById('themeSwitch')
    }
  },
  mounted() {
    // Set dark mode on load based on localStorage
    const savedMode = localStorage.getItem('darkmode') === 'true';
    this.toggle = savedMode;
    if (savedMode) {
      document.body.classList.add('darkmode');
    }

    // Fetch summoner data based on route parameters
    const { region, summoner } = this.$route.params;
    this.fetchSummonerData(region, summoner);
  },
  methods: {
    goHome() {
      this.$router.push('/')
    },
    toggleDarkMode() {
      this.toggle = !this.toggle
      if (this.toggle) {
        console.log("darkmode active")
        document.body.classList.add('darkmode')
        localStorage.setItem('darkmode', 'active')
      } else {
        console.log("light mode active")
        document.body.classList.remove('darkmode')
        localStorage.setItem('darkmode', null)
      }
    },
    handleFormSubmit(summoner, region) {
      // Handle the form submission event
      console.log(`Summoner: ${summoner}, Region: ${region}`);
      // Fetch the summoner data and update the profileIconUrl
      this.fetchSummonerData(region, summoner);
    },
    async fetchSummonerData(region, summoner) {
      try {
        const summonerResponse = await axios.get(`/summoners/${region}/${summoner}-${region}`);
        // Handle the response data
        console.log(summonerResponse.data);
        // Update the profile data
        this.profileIconUrl = summonerResponse.data.summonerIconUrl; // Adjust based on actual response structure
        this.summonerName = summonerResponse.data.accountData.gameName + "#" + region;
        this.summonerLevel = summonerResponse.data.summonerData.summonerLevel;
        this.puuid = summonerResponse.data.accountData.puuid;

        const rankResponse = await axios.post(`/ranks`, {
          "puuid": this.puuid
        });
      
        console.log(rankResponse.data);
        let soloRank = rankResponse.data[0]
        let flexRank = rankResponse.data[1]
        if (soloRank.queueType == "RANKED_FLEX_SR") {
          soloRank = rankResponse.data[1]
          flexRank = rankResponse.data[0]
        }

        this.summonerRank = soloRank.tier + " " + soloRank.rank + " " + soloRank.leaguePoints + "LP";
        this.wins = soloRank.wins
        this.losses = soloRank.losses

        this.summonerRankFlex = flexRank.tier + " " + flexRank.rank + " " + flexRank.leaguePoints + "LP";
        this.winsFlex = flexRank.wins
        this.lossesFlex = flexRank.losses
        this.matches = summonerResponse.data.matches; // Adjust based on actual response structure
      } catch (error) {
        console.error('Error fetching summoner data:', error);
      }
    }
  }
}
</script>

<style>
/* Navigation Bar Styling */
* {
  margin: 0px;
  padding: 0px;
  box-sizing: border-box;
}

body {
  background-color: var(--background);
}

:root {
  --background: white;
  --base-variant: #e8e9ed;
  --text-color: #9fabb9;
  --button-color: #1c1c1e;
  --primary-color: #3a435d;
  --nav-bar: #5f7fd2;
  --grey-background: #edeef2:
}

/* Dark Mode Toggle Button */
.darkmode {
  --background: #1c1c1e;
  --base-variant: #101425;
  --text-color: #9fabb9;
  --button-color: #1c1c1e;
  --primary-color: #3a435d;
  --nav-bar: #5f7fd2;
}

.page-container {
  position: relative;
  width: 100%;
  padding-top: 60px; /* Equal to navbar height */
  min-height: 100vh; /* Full viewport height */
  box-sizing: border-box;
}

#theme-switch {
  height: 50px;
  width: 50px;
  padding: 0;
  border-radius: 50%;
  background-color: var(--nav-bar);
  display: flex;
  justify-content: center;
  align-items: center;
  border: none;
}

#theme-switch svg {
  fill: var(--button-color);
}

#theme-switch svg:last-child {
  display: none;
}

.darkmode #theme-switch svg:first-child {
  display: none;
}

.darkmode #theme-switch svg:last-child {
  display: block;
}

.summoner-view {
  background-color: var(--base-color);
}

nav {
  width: 100%;
  height: 60px;
  background-color: var(--nav-bar);
  color: var(--base-color);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0px 30px;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1;
}

/* Home Button */
.home-button svg {
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  color: var(--button-color);
  background-color: var(--nav-bar);
  border: none;
  fill: var(--button-color);
}

.home-button {
  border: none;
}

/* Search Form Centering */
.searchForm {
  flex: 1;
  display: flex;
  justify-content: center;
}

.content {
  position: relative;
  width: 100%;
  max-width: 100%; /* Full width as requested */
  padding: 40px 0 0 0; /* Small top padding instead of margin */
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.user-profile {
  text-align: left;
  margin-top: 0;
  margin-left: 200px;
  padding-bottom: 30px;
}

.user-profile::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%; /* Control border width without affecting content */
  height: 1px;
  background-color: #edeef2;
}

.matches {
  text-align: left;
  margin-top: 0;
  margin-left: 300px;
}
</style>