import { defineStore } from "pinia";
import instance from "@/plugins/axios";
import { useSettingsStore } from "./settings";

interface RankedData {
  leagueId: string;
  queueType: string;
  tier: string;
  rank: string;
  summonerId: string;
  puuid: string;
  leaguePoints: number;
  wins: number;
  losses: number;
  veteran: boolean;
  inactive: boolean;
  freshBlood: boolean;
  hotStreak: boolean;
  rankUrl: String;
  miniSeries: {
    losses: number;
    progress: string;
    target: number;
    wins: number;
  };
}

// Add MatchData interface
interface MatchData {
  metadata: {
    matchId: string;
  };
  info: {
    participants: Array<{
      puuid: string;
      championId: number;
      win: boolean;
      kills: number;
      deaths: number;
      assists: number;
    }>;
  };
}

interface SummaryState {
  GAMES_TO_LOAD: number;
  matches: MatchData[]; // Update type from String[] to MatchData[]
  stats: {
    totalGames?: number;
    wins?: number;
    losses?: number;
    winRate?: number;
  };
  loaded: boolean;
  matchesLoading: boolean;
  moreMatchesToFetch: boolean;
}

interface SummonerState {
  summoner: {
    account: {
      name: string;
      tagLine: string;
      id: string;
      accountId: string;
      puuid: string;
      profileIconId: number;
      revisionDate: number;
      summonerLevel: number;
    };
    ranked: {
      solo: RankedData | null;
      flex: RankedData | null;
    };
    loaded: boolean;
  };
  summary: SummaryState;
}

export const useSummonerStore = defineStore("summoner", {
  state: (): SummonerState => ({
    summoner: {
      account: {
        name: "",
        tagLine: "",
        id: "",
        accountId: "",
        puuid: "",
        profileIconId: 0,
        revisionDate: 0,
        summonerLevel: 0,
      },
      ranked: {
        solo: null,
        flex: null,
      },
      loaded: false,
    },
    summary: {
      GAMES_TO_LOAD: 10,
      matches: [],
      stats: {},
      loaded: false,
      matchesLoading: false,
      moreMatchesToFetch: true,
    },
  }),

  getters: {
    summonerLoaded: (state) => state.summoner.loaded,
    summaryLoaded: (state) => state.summary.loaded,
  },

  actions: {
    async summonerDetailsRequest(
      summoner: string,
      region: string,
      tag: string
    ) {
      try {
        console.log("Fetching summoner data");

        // Reset summary state when requesting new summoner
        this.summary.GAMES_TO_LOAD = 10;
        this.summary.matches = [];
        this.summary.stats = {};
        this.summary.loaded = false;
        this.summary.matchesLoading = false;
        this.summary.moreMatchesToFetch = true;

        this.summoner.loaded = false;

        const response = await instance.get(
          `summoners/${region}/${summoner}-${tag}`
        );

        if (!response.data) {
          console.log("Error summoner details request");
          return;
        }

        const soloRank = response.data.ranked.find(
          (queue: RankedData) => queue.queueType === "RANKED_SOLO_5x5"
        );
        const flexRank = response.data.ranked.find(
          (queue: RankedData) => queue.queueType === "RANKED_FLEX_SR"
        );

        // Add rankUrl to each queue type
        if (soloRank) {
          soloRank.rankUrl = `https://res.cloudinary.com/kln/image/upload/v1693310423/${soloRank.tier}.png`;
        }
        if (flexRank) {
          flexRank.rankUrl = `https://res.cloudinary.com/kln/image/upload/v1693310423/${flexRank.tier}.png`;
        }

        this.summoner.account.name = response.data.summonerName;
        this.summoner.account.tagLine = response.data.tagLine;
        this.summoner.account.id = response.data.id;
        this.summoner.account.accountId = response.data.accountId;
        this.summoner.account.puuid = response.data.puuid;
        this.summoner.account.profileIconId = response.data.profileIconId;
        this.summoner.account.revisionDate = response.data.revisionDate;
        this.summoner.account.summonerLevel = response.data.summonerLevel;

        this.summoner.ranked.solo = soloRank || null;
        this.summoner.ranked.flex = flexRank || null;

        this.summoner.loaded = true;
      } catch (error) {
        console.error("Error fetching summoner data:", error);
        this.summoner.loaded = false;
      }
    },

    async summaryRequest() {
      const settingsStore = useSettingsStore();
      console.log("Fetching initial summary data");
      this.summary.matchesLoading = true;

      try {
        const response = await instance.get(`summoners/summary`, {
          params: {
            puuid: this.summoner.account.puuid,
            region: settingsStore.region,
            limit: this.summary.GAMES_TO_LOAD,
          },
        });

        if (response.data) {
          // Complete state replacement
          this.summary.GAMES_TO_LOAD = 10;
          this.summary.matches = response.data.matchDetails || [];
          this.summary.stats = {};
          this.summary.loaded = false;
          this.summary.matchesLoading = false;
          this.summary.moreMatchesToFetch = true;
        }
      } catch (error) {
        console.error("Error fetching initial summary data:", error);
        this.summary = {
          GAMES_TO_LOAD: 10,
          matches: [],
          stats: {},
          loaded: false,
          matchesLoading: false,
          moreMatchesToFetch: false,
        };
      }
    },

    async moreMatches() {
      if (!this.summary.moreMatchesToFetch || this.summary.matchesLoading)
        return;

      const settingsStore = useSettingsStore();
      console.log("Fetching more matches");
      this.summary.matchesLoading = true;

      try {
        const lastMatch = this.summary.matches[this.summary.matches.length - 1];
        const response = await instance.get(`summoners/summary`, {
          params: {
            puuid: this.summoner.account.puuid,
            region: settingsStore.region,
            lastMatchId: lastMatch?.metadata.matchId,
            limit: this.summary.GAMES_TO_LOAD,
          },
        });

        if (response.data) {
          const newMatches = response.data.matchDetails || [];

          // Complete state replacement while preserving existing matches
          this.summary = {
            GAMES_TO_LOAD: 10,
            matches: [...this.summary.matches, ...newMatches],
            stats: {
              totalGames:
                response.data.stats?.totalGames ||
                this.summary.stats.totalGames,
              wins: response.data.stats?.wins || this.summary.stats.wins,
              losses: response.data.stats?.losses || this.summary.stats.losses,
              winRate:
                response.data.stats?.winRate || this.summary.stats.winRate,
            },
            loaded: true,
            matchesLoading: false,
            moreMatchesToFetch:
              newMatches.length === this.summary.GAMES_TO_LOAD,
          };
        }
      } catch (error) {
        console.error("Error fetching more matches:", error);
        this.summary.matchesLoading = false;
        this.summary.moreMatchesToFetch = false;
      }
    },
  },
});
