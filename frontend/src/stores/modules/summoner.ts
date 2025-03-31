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
  miniSeries: {
    losses: number;
    progress: string;
    target: number;
    wins: number;
  };
}

interface SummaryState {
  GAMES_TO_LOAD: number;
  matches: any[]; // Replace 'any' with a proper Match interface if you have one
  stats: Record<string, any>; // Replace 'any' with proper stats interface if you have one
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
        console.log("Fecthing summoner data");
        const response = await instance.get(
          `summoners/${region}/${summoner}-${tag}`
        );

        if (!response.data) {
          console.log("Error summoner details request");
          return;
        }
        console.log("response: ", response.data);
        this.summoner.loaded = true;
        const soloRank = response.data.ranked.find(
          (queue: RankedData) => queue.queueType === "RANKED_SOLO_5x5"
        );
        const flexRank = response.data.ranked.find(
          (queue: RankedData) => queue.queueType === "RANKED_FLEX_SR"
        );
        console.log(response);
        this.summoner = {
          account: {
            name: response.data.summonerName,
            tagLine: response.data.tagLine,
            id: response.data.id,
            accountId: response.data.accountId,
            puuid: response.data.puuid,
            profileIconId: response.data.profileIconId,
            revisionDate: response.data.revisionDate,
            summonerLevel: response.data.summonerLevel,
          },
          ranked: {
            solo: soloRank || null,
            flex: flexRank || null,
          },
          loaded: true,
        };
      } catch (error) {
        console.error("Error fetching summoner data:", error);
        this.summoner.loaded = false;
      }
    },
    async summaryRequest() {
      const settingsStore = useSettingsStore();
      console.log("Fetching summary data");
      try {
        const response = await instance.get(`summoners/summary`, {
          params: {
            puuid: this.summoner.account.puuid,
            region: settingsStore.region,
            lastMatchId:
              this.summary.matches.length > 0
                ? this.summary.matches[this.summary.matches.length - 1].matchId
                : null,
            limit: this.summary.GAMES_TO_LOAD,
          },
        });
        console.log("---OVERVIEW---");
        console.log(response.data);

        this.summary = {
          ...this.summary,
          matches: [...this.summary.matches, ...(response.data.matches || [])],
          stats: response.data.stats || {},
          loaded: true,
          matchesLoading: false,
          moreMatchesToFetch: response.data.matches?.length > 0,
        };
      } catch (error) {
        console.error("Error fetching summary data:", error);
        this.summary = {
          ...this.summary,
          loaded: false,
          matchesLoading: false,
          moreMatchesToFetch: false,
        };
      }
    },
  },
});
