import { defineStore } from "pinia";
import axios from "axios";

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
    status: string;
  };
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
      status: "",
    },
  }),

  getters: {
    isLoaded: (state) => state.summoner.status === "success",
    isUpdating: (state) => state.summoner.status === "loading",
    hasError: (state) => state.summoner.status === "error",
  },

  actions: {
    async summonerDetailsRequest(
      summoner: string,
      region: string,
      tag: string
    ) {
      this.summoner.status = "loading";
      try {
        const response = await axios.get(
          `summoner/${region}/${summoner}-${tag}`
        );

        if (!response.data) {
          this.summoner.status = "error";
          return;
        }

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
          status: "success",
        };
      } catch (error) {
        console.error("Error fetching summoner data:", error);
        this.summoner.status = "error";
      }
    },
  },
});
