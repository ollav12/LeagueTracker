<template>
  <!-- Content Area -->

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
</template>

<script>
import axios from "@/plugins/axios"; // Import the configured axios instance
import UserProfile from "@/components/Summoner/UserProfile.vue";
import { useGlobalStore } from "@/stores/global.js";

export default {
  setup() {
    const globalStore = useGlobalStore();

    return {
      globalStore,
    };
  },
  components: {
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
            localStorage.setItem("puuid", this.puuid);
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
        let newRegion = this.globalStore.regionsList[this.region.toLowerCase()];

        const summonerResponse = await axios.get(
          `/summoners/${newRegion}/${summoner}-${tag}`
        );
        // Handle the response data
        console.log(summonerResponse.data);
        // Update the profile data
        this.profileIconUrl =
          "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/profile-icons/" +
          summonerResponse.data.profileIconId +
          ".jpg";
        this.summonerName = summoner;
        this.summonerLevel = summonerResponse.data.summonerLevel;
        this.puuid = summonerResponse.data.puuid;
        localStorage.setItem("puuid", this.puuid);

        const rankResponse = summonerResponse.data.ranked;

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

<style></style>
