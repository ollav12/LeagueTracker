// src/stores/summonerStore.js
import { defineStore } from "pinia";

export const useSummonerStore = defineStore("summoner", {
  state: () => ({
    summonerData: null,
    loading: false,
    error: null,
  }),
  actions: {
    async fetchSummonerData(summonerName) {
      this.loading = true;
      try {
        const response = await fetch(`/api/summoner/${summonerName}`);
        if (!response.ok) throw new Error("Failed to fetch summoner data");
        this.summonerData = await response.json();
      } catch (error) {
        this.error = error.message;
      } finally {
        this.loading = false;
      }
    },
  },
});
