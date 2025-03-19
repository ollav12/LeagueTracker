<template>
  <div>
    <!-- Navigation Bar -->
    <nav>
      <!-- Back to Home Button -->
      <button class="home-button" @click="goHome">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#5f6368"
        >
          <path
            d="M160-120v-375l-72 55-48-64 440-336 440 336-48 63-72-54v375H160Zm160-240q-17 0-28.5-11.5T280-400q0-17 11.5-28.5T320-440q17 0 28.5 11.5T360-400q0 17-11.5 28.5T320-360Zm160 0q-17 0-28.5-11.5T440-400q0-17 11.5-28.5T480-440q17 0 28.5 11.5T520-400q0 17-11.5 28.5T480-360Zm160 0q-17 0-28.5-11.5T600-400q0-17 11.5-28.5T640-440q17 0 28.5 11.5T680-400q0 17-11.5 28.5T640-360Z"
          />
        </svg>
      </button>

      <!-- Search Form -->
      <div class="searchForm">
        <SearchForm @formSubmit="handleFormSubmit" />
      </div>

      <!-- Dark Mode Toggle Button -->
      <button id="theme-switch" @click="toggleDarkMode">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#5f6368"
        >
          <path
            d="M480-280q-83 0-141.5-58.5T280-480q0-83 58.5-141.5T480-680q83 0 141.5 58.5T680-480q0 83-58.5 141.5T480-280ZM200-440H40v-80h160v80Zm720 0H760v-80h160v80ZM440-760v-160h80v160h-80Zm0 720v-160h80v160h-80ZM256-650l-101-97 57-59 96 100-52 56Zm492 496-97-101 53-55 101 97-57 59Zm-98-550 97-101 59 57-100 96-56-52ZM154-212l101-97 55 53-97 101-59-57Z"
          />
        </svg>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#5f6368"
        >
          <path
            d="M480-120q-150 0-255-105T120-480q0-150 105-255t255-105q14 0 27.5 1t26.5 3q-41 29-65.5 75.5T444-660q0 90 63 153t153 63q55 0 101-24.5t75-65.5q2 13 3 26.5t1 27.5q0 150-105 255T480-120Z"
          />
        </svg>
      </button>
    </nav>

    <!-- Content Area -->
    <div class="page-container">
      <div class="content">
        <div class="user-profile">
          <UserProfile
            :puuid="puuid"
            :region="region"
            :tag="tag"
            :profileIconUrl="profileIconUrl"
            :summonerName="summonerName"
            :summonerLevel="summonerLevel"
            :soloRank="soloRank"
            :soloWins="soloWins"
            :soloLosses="soloLosses"
            :flexRank="flexRank"
            :flexWins="flexWins"
            :flexLosses="flexLosses"
            :soloRankIconUrl="soloRankIconUrl"
            :flexRankIconUrl="flexRankIconUrl"
            :matches="matches"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "@/plugins/axios"; // Import the configured axios instance
import SearchForm from "@/components/Form/SearchForm.vue";
import UserProfile from "@/components/Summoner/UserProfile.vue";

export default {
  components: {
    SearchForm,
    UserProfile,
  },
  data() {
    return {
      puuid: null,
      region: "",
      tag: "",
      profileIconUrl: "",
      soloRankIconUrl: "",
      flexRankIconUrl: "",
      summonerName: "",
      summonerLevel: null,
      soloRank: "",
      soloWins: 0,
      soloLosses: 0,
      flexRank: "",
      flexWins: 0,
      flexLosses: 0,
      toggle: false,
      darkmode: localStorage.getItem("darkmode"),
      themeSwitch: document.getElementById("themeSwitch"),
      matches: [],
    };
  },
  mounted() {
    // Set dark mode on load based on localStorage
    const savedMode = localStorage.getItem("darkmode") === "true";
    this.toggle = savedMode;
    if (savedMode) {
      document.body.classList.add("darkmode");
    }

    // Fetch summoner data based on route parameters
    const { region, summoner, tag } = this.$route.params;
    console.log(`Mounted: /${region}/${summoner}-${tag}`);
    this.fetchSummonerData(region, summoner, tag);
  },
  watch: {
    // Watch for route parameter changes
    "$route.params": {
      handler(newParams) {
        const { region, summoner, tag } = newParams;
        console.log(`Refresh: /${region}/${summoner}-${tag}`);
        // Only fetch if there's actually new parameters
        if (region && summoner) {
          // Parse the summoner-tag format
          const parts = summoner.split("-");
          const summonerName = parts[0];
          const tagValue = parts.length > 1 ? parts[1] : tag;

          // Fetch the new data
          this.fetchSummonerData(region, summonerName, tagValue);
        }
      },
      immediate: false, // Don't trigger on component mount
      deep: true, // Watch nested properties
    },
  },
  methods: {
    goHome() {
      this.$router.push("/");
    },
    toggleDarkMode() {
      this.toggle = !this.toggle;
      if (this.toggle) {
        console.log("darkmode active");
        document.body.classList.add("darkmode");
        localStorage.setItem("darkmode", "true"); // Use "true" instead of "active"
      } else {
        console.log("light mode active");
        document.body.classList.remove("darkmode");
        localStorage.setItem("darkmode", "false"); // Use "false" instead of null
      }
    },
    handleFormSubmit(region, summoner, tag) {
      // Validate that summoner name is not empty before navigating
      if (summoner && summoner.trim() !== "") {
        console.log(`Region: ${region}, Summoner: ${summoner}, Tag: ${tag}`);
        this.$router.push(`/summoner/${region}/${summoner}-${tag}`);
      } else {
        console.log("Search field was empty");
        // Optional: Show a validation message to the user
      }
    },
    async fetchSummonerData(region, summoner, tag) {
      try {
        const cacheKey = `summoner_${region}_${summoner}_${tag}`;
        const cachedData = localStorage.getItem(cacheKey);

        // Check if we have recent cached data (less than 5 minutes old)
        if (cachedData) {
          const data = JSON.parse(cachedData);
          const cacheTime = data.timestamp;
          const now = Date.now();
          const fiveMinutes = 5 * 60 * 1000;

          // If cache is fresh (less than 5 minutes old), use it
          if (now - cacheTime < fiveMinutes) {
            console.log("Fetching: Cached data");
            this.puuid = data.puuid;
            this.profileIconUrl = data.profileIconUrl;
            this.summonerName = data.summonerName;
            this.summonerLevel = data.summonerLevel;
            this.soloRank = data.soloRank;
            this.soloWins = data.soloWins;
            this.soloLosses = data.soloLosses;
            this.flexRank = data.flexRank;
            this.flexWins = data.flexWins;
            this.flexLosses = data.flexLosses;
            this.soloRankIconUrl = data.soloRankIconUrl;
            this.flexRankIconUrl = data.flexRankIconUrl;
            this.matches = data.matches;
            this.tag = data.tag;
            this.region = data.region;
            return; // Skip API calls
          }
        }

        this.region = region;
        this.tag = tag;
        console.log(`Fetching: New Data`);

        const summonerResponse = await axios.get(
          `/summoners/${region}/${summoner}-${tag}`
        );
        // Handle the response data
        console.log(summonerResponse.data);
        // Update the profile data
        this.profileIconUrl =
          "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/profile-icons/" +
          summonerResponse.data.summonerProfileIconId +
          ".jpg";
        this.summonerName = summoner;
        this.summonerLevel = summonerResponse.data.summonerLevel;
        this.puuid = summonerResponse.data.puuid;

        const rankResponse = await axios.post(`/ranks`, {
          puuid: this.puuid,
        });

        console.log(rankResponse.data);
        if (!rankResponse.data || rankResponse.data.length === 0) {
          // Set defaults for unranked players
          this.soloRank = "Unranked";
          this.soloRankIconUrl =
            "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
          this.soloWins = 0;
          this.soloLosses = 0;

          this.flexRank = "Unranked";
          this.flexRankIconUrl =
            "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
          this.flexWins = 0;
          this.flexLosses = 0;
        } else {
          // Process ranks with safety checks
          let soloRankRes = rankResponse.data[0] || {};
          let flexRankRes = rankResponse.data[1] || {};

          if (soloRankRes.queueType === "RANKED_FLEX_SR") {
            soloRankRes = rankResponse.data[1] || {};
            flexRankRes = rankResponse.data[0] || {};
          }

          // Solo queue rank
          if (soloRankRes && soloRankRes.tier) {
            this.soloRank = `${soloRankRes.tier} ${soloRankRes.rank} ${soloRankRes.leaguePoints} LP`;
            this.soloRankIconUrl = `https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-${soloRankRes.tier.toLowerCase()}.png`;
            this.soloWins = soloRankRes.wins || 0;
            this.soloLosses = soloRankRes.losses || 0;
          } else {
            this.soloRank = "Unranked";
            this.soloRankIconUrl =
              "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
            this.soloWins = 0;
            this.soloLosses = 0;
          }

          // Flex queue rank
          if (flexRankRes && flexRankRes.tier) {
            this.flexRank = `${flexRankRes.tier} ${flexRankRes.rank} ${flexRankRes.leaguePoints} LP`;
            this.flexRankIconUrl = `https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-${flexRankRes.tier.toLowerCase()}.png`;
            this.flexWins = flexRankRes.wins || 0;
            this.flexLosses = flexRankRes.losses || 0;
          } else {
            this.flexRank = "Unranked";
            this.flexRankIconUrl =
              "https://raw.communitydragon.org/latest/plugins/rcp-fe-lol-static-assets/global/default/images/ranked-emblem/emblem-iron.png";
            this.flexWins = 0;
            this.flexLosses = 0;
          }
        }

        const matchResponse = await axios.post("/matches", {
          puuid: this.puuid,
        });

        console.log(matchResponse.data);
        this.matches = matchResponse.data;

        // Store data in cache
        const cacheData = {
          timestamp: Date.now(),
          puuid: this.puuid,
          profileIconUrl: this.profileIconUrl,
          summonerName: this.summonerName,
          summonerLevel: this.summonerLevel,
          soloRank: this.soloRank,
          soloWins: this.soloWins,
          soloLosses: this.soloLosses,
          flexRank: this.flexRank,
          flexWins: this.flexWins,
          flexLosses: this.flexLosses,
          soloRankIconUrl: this.soloRankIconUrl,
          flexRankIconUrl: this.flexRankIconUrl,
          matches: this.matches,
          tag: this.tag,
          region: this.region,
        };

        localStorage.setItem(cacheKey, JSON.stringify(cacheData));
      } catch (error) {
        console.error("Error fetching summoner data:", error);
      }
    },
  },
};
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
  --nav-bar: #5383e9;
  --grey-background: #edeef2; /* Fixed syntax error */
}

/* Dark Mode Toggle Button */
.darkmode {
  --background: #1c1c1e;
  --base-variant: #101425;
  --text-color: #9fabb9;
  --button-color: #1c1c1e;
  --primary-color: #3a435d;
  --nav-bar: #5383e9;
  --grey-background: #1a1c23; /* Add dark mode grey */
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
  background-color: #f7f7f9;
}

nav {
  position: relative !important; /* Force fixed positioning */
  width: 100%;
  height: 60px;
  background-color: var(--nav-bar);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0px 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
</style>
