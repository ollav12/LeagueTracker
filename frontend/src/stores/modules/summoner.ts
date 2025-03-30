import { defineStore} from 'pinia'
import axios from 'axios'

export const useSummonerStore = defineStore('summoner', {
    state: () => ({
      basic: {
        account: {},
        currentSeason: null,
        ranked: {},
        recentActivity: [],
        seasons: [],
        gamemodes: [],
        status: '',
      },
      // other state properties
    }),
  
    actions: {
      async basicRequest() {
        try {
          const response = await axios.get('/summoner')
          this.basic.account = response.data.account
          this.basic.ranked = response.data.ranked
          // other state updates
        } catch (error) {
          console.error('Error fetching summoner data:', error)
        }
      },
    },
  })