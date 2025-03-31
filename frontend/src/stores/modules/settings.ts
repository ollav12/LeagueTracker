import { defineStore } from "pinia";

interface SettingsState {
  region: string;
  recentSearches: Array<{
    name: string;
    icon: number;
    region: string;
  }>;
}

export const useSettingsStore = defineStore("settings", {
  state: (): SettingsState => ({
    region: localStorage.getItem("region") || "euw1",
    recentSearches: JSON.parse(localStorage.getItem("recentSearches") || "[]"),
  }),

  actions: {
    setRegion(region: string) {
      this.region = region;
      localStorage.setItem("region", region);
    },

    addRecentSearch(search: { name: string; icon: number; region: string }) {
      // Remove duplicate if exists
      this.recentSearches = this.recentSearches.filter(
        (item) => item.name !== search.name
      );

      // Add to start of array
      this.recentSearches.unshift(search);

      // Keep only last 5 searches
      if (this.recentSearches.length > 5) {
        this.recentSearches.pop();
      }

      // Save to localStorage
      localStorage.setItem(
        "recentSearches",
        JSON.stringify(this.recentSearches)
      );
    },

    clearRecentSearches() {
      this.recentSearches = [];
      localStorage.removeItem("recentSearches");
    },
  },

  getters: {
    currentRegion: (state) => state.region,
    lastSearches: (state) => state.recentSearches,
  },
});
