import {defineStore} from "pinia";
import instance from "@/plugins/axios";
import {useSettingsStore} from "./settings";

interface RankedData {
    queueType: string;
    currentRank: string;
    peakRank: string;
    lowestRank: string;
    wins: number;
    losses: number;
    veteran: boolean;
    inactive: boolean;
    freshBlood: boolean;
    hotStreak: boolean;
    currentRankUrl: string;
    peakRankUrl: string;
    lowestRankUrl: string;
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
    isUpdating: boolean;
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
        isUpdating: false,
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
                    `/api/v1/summoners/${region}/${summoner}-${tag}`
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
                    const current = (soloRank.currentRank).split(" ");
                    const peak = (soloRank.currentRank).split(" ");
                    const lowest = (soloRank.currentRank).split(" ");
                    soloRank.currentRankUrl = `https://res.cloudinary.com/kln/image/upload/v1693310423/${current[0]}.png`;
                    soloRank.peakRankUrl = `https://res.cloudinary.com/kln/image/upload/v1693310423/${lowest[0]}.png`;
                    soloRank.lowestRankUrl = `https://res.cloudinary.com/kln/image/upload/v1693310423/${peak[0]}.png`;
                }
                if (flexRank) {
                    const current = (flexRank.currentRank).split(" ");
                    const peak = (flexRank.currentRank).split(" ");
                    const lowest = (flexRank.currentRank).split(" ");
                    flexRank.currentRankUrl = `https://res.cloudinary.com/kln/image/upload/v1693310423/${current[0]}.png`;
                    flexRank.peakRankUrl = `https://res.cloudinary.com/kln/image/upload/v1693310423/${lowest[0]}.png`;
                    flexRank.lowestRankUrl = `https://res.cloudinary.com/kln/image/upload/v1693310423/${peak[0]}.png`;
                }

                this.summoner.account.name = summoner;
                this.summoner.account.tagLine = tag;
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
            const puuid = this.summoner.account.puuid;
            try {
                const response = await instance.get(`/api/v1/summoners/${puuid}/match-history`, {
                    params: {
                        puuid: puuid,
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
                console.log(this.summary);
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
            const puuid = this.summoner.account.puuid;

            try {
                const lastMatch = this.summary.matches[this.summary.matches.length - 1];
                const response = await instance.get(`/api/v1/summoners/${puuid}/match-history/load-more`, {
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
        async updateSummoner() {
            const settingsStore = useSettingsStore();
            console.log("Updating summoner data");
            this.summoner.loaded = false;
            this.summary.matchesLoading = true;

            try {
                const puuid = this.summoner.account.puuid;
                const lastMatch = this.summary.matches[this.summary.matches.length - 1];
                const requestData = {
                    puuid: puuid,
                    region: settingsStore.region,
                    summonerName: this.summoner.account.name,
                    tag: this.summoner.account.tagLine,
                    lastMatchId: lastMatch?.metadata.matchId || "",
                    limit: this.summary.GAMES_TO_LOAD,
                };

                const response = await instance.get(`/api/v1/summoners/${puuid}/update`, {
                    params: requestData,
                });

                if (response.data) {
                    // Update summoner data
                    const lookup = response.data.summonerLookupResponse;
                    const soloRank = lookup.ranked.find(
                        (queue: RankedData) => queue.queueType === "RANKED_SOLO_5x5"
                    );
                    const flexRank = lookup.ranked.find(
                        (queue: RankedData) => queue.queueType === "RANKED_FLEX_SR"
                    );

                    if (soloRank) {
                        soloRank.rankUrl = `https://res.cloudinary.com/kln/image/upload/v1693310423/${soloRank.tier}.png`;
                    }
                    if (flexRank) {
                        flexRank.rankUrl = `https://res.cloudinary.com/kln/image/upload/v1693310423/${flexRank.tier}.png`;
                    }

                    this.summoner.account.name = lookup.summonerName;
                    this.summoner.account.tagLine = lookup.tagLine;
                    this.summoner.account.id = lookup.id;
                    this.summoner.account.accountId = lookup.accountId;
                    this.summoner.account.puuid = lookup.puuid;
                    this.summoner.account.profileIconId = lookup.profileIconId;
                    this.summoner.account.revisionDate = lookup.revisionDate;
                    this.summoner.account.summonerLevel = lookup.summonerLevel;

                    this.summoner.ranked.solo = soloRank || null;
                    this.summoner.ranked.flex = flexRank || null;

                    // Update match history
                    const matchHistory = response.data.summonerMatchesResponse;
                    this.summary.matches = matchHistory.matchDetails || [];
                    this.summary.stats = matchHistory.stats || {};
                    this.summary.moreMatchesToFetch = matchHistory.matchIds.length === this.summary.GAMES_TO_LOAD;

                    this.summoner.loaded = true;
                    this.summary.loaded = true;
                    this.summary.matchesLoading = false;

                    return Date.now(); // Return timestamp for last update
                }
            } catch (error: any) {
                console.error("Error updating summoner data:", error);
                this.summoner.loaded = true;
                this.summary.matchesLoading = false;

                if (error.response?.status === 429) {
                    throw new Error("Summoner recently updated. Please wait before trying again.");
                }
                throw error;
            }
        },
    },
});
