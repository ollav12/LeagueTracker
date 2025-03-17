<template>
    <div class="rank-history-container">
        <div class="queue-nav">
        <button class="queue-nav-item active">All</button>
        <button class="queue-nav-item">Ranked Solo/Duo</button>
        <button class="queue-nav-item">Ranked Flex</button>
        <button class="queue-nav-item">Aram</button>
        <div class="queue-dropdown">
            <button class="queue-nav-item">QueueType <span class="dropdown-arrow">▼</span></button>
            <div class="dropdown-content">
            <a href="#">Normal Draft</a>
            <a href="#">Normal Blind</a>
            <a href="#">Clash</a>
            </div>
        </div>
        </div>


      <div class="rank-history">
        <h3 class="rank-title">Ranked Solo/Duo</h3>
    <!-- Single rank-row containing both current and peak rank -->
    <div class="rank-row">
            <!-- First rank display (current) -->
            <div class="rank-entry">
                <div class="rank-icon-container">
                    <img :src="soloRankIconUrl" alt="Solo Rank" class="rank-icon" />
                </div>
                <div class="rank-info">
                    <div class="rank-status">
                    <span class="rankText">{{ formatRank(soloRank) }}</span>
                    <span class="win-loss-text">{{ soloWins }}W {{ soloLosses }}L</span>
                    </div>
                    <div class="stats-row">
                    <span class="lp-text">{{ soloLp }}</span>
                    <span class="win-percent">Win rate {{ winPercentage }}%</span>
                    </div>
                </div>
            </div>
            
            <!-- Second rank display (peak) -->
            <div class="rank-entry peak-rank">
                <div class="rank-icon-container">
                    <img :src="soloRankIconUrl" alt="Peak Rank" class="rank-icon" />
                </div>
                <div class="rank-info">
                    <div class="rank-status">
                        <span class="rankText">{{ formatRank(soloRank) }}</span>
                        <span class="top-tier">Top Tier</span>
                    </div>
                    <div class="stats-row">
                        <span class="lp-text">{{ soloLp }}</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="rank-row">
          <div class="rank-icon-container">
            <img :src="flexRankIconUrl" alt="Flex Rank" class="rank-icon" />
          </div>
          <div class="rank-info">
            <div class="rank-status">
                <span class="rankText">{{ formatRank(flexRank) }}</span>
                <span class="win-loss-text">{{ flexWins }}W {{ flexLosses }}L</span>
            </div>
            <div class="stats-row">
                <span class="lp-text">{{ flexLp }}</span>
                <span class="win-percent">Win rate {{ winPercentageFlex }}%</span>
            </div>
        </div>
        </div>
      </div>
    </div>
  </template>
<script>
export default {
    props: {
        soloRank: String,
        soloWins: Number,
        soloLosses: Number,
        soloRankIconUrl: String,
        flexRank: String,
        flexWins: Number,
        flexLosses: Number,
        flexRankIconUrl: String
    }, 
    data() {
        return {
            soloLp: "",
            flexLp: ""
        }
    },
    watch: {
        soloRank(newVal) {
            let [tier, rank, lp, text] = newVal.split(" ");
            this.soloLp = lp + " " + text;
        },
        flexRank(newVal) {
            let [tier, rank, lp, text] = newVal.split(" ");
            this.flexLp = lp + " " + text;
        }
    },
    computed: {
    winPercentage() {
      const totalGames = this.soloWins + this.soloLosses;
      if (totalGames === 0) {
        return 0;
      }
      return Math.round((this.soloWins / totalGames) * 100);
    },
    winPercentageFlex() {
      const totalGames = this.flexWins + this.flexLosses;
      if (totalGames === 0) {
        return 0;
      }
      return Math.round((this.flexWins / totalGames) * 100);
    }
  },
  methods: {
    formatRank(soloRank) {
            let [rank, tier, leaguePoints] = soloRank.split(" "); // Use proper array destructuring
        
        // Convert to lowercase, then capitalize first letter
        rank = rank.toLowerCase();
        rank = rank.charAt(0).toUpperCase() + rank.slice(1);
        
        if(tier == "I") {
            tier = "1";
        } else if (tier == "II"){
            tier = "2";
        } else if (tier == "III") {
            tier = "3";
        } else if (tier == "IV") {
            tier = "4";
        } else {
            tier = "";
        }
        return rank + " " + tier; // Return the formatted value
        }
    }
}

</script>
<style scoped>
.queue-nav {
  display: flex;
  background-color: white;
  border-radius: 8px;
  margin-bottom: 0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  position: relative;
  height: 44px;
  width: 1080px;
  margin-bottom: 8px;
  margin-top: -17px;
}

.queue-nav-item {
  background: none;
  border: none;
  padding: 0 12px;
  font-size: 14px;
  color: #777;
  cursor: pointer;
  height: 100%;
  position: relative;
  font-weight: 500;
  transition: color 0.2s;
  white-space: nowrap;
}

.queue-nav-item:hover {
  color: #333;
}

.queue-nav-item:hover::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #ddd;
}

.queue-nav-item.active {
  color: #4f84ea;
  font-weight: 700;
}

.queue-nav-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #4f84ea;
}

.queue-dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-arrow {
  font-size: 10px;
  margin-left: 4px;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: white;
  min-width: 160px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  z-index: 10;
  top: 100%;
  left: 0;
}

.dropdown-content a {
  color: #777;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
  font-size: 14px;
}

.dropdown-content a:hover {
  background-color: #f7f7f9;
}

.queue-dropdown:hover .dropdown-content {
  display: block;
}



.top-tier {
    font-family: 'Roboto';
    font-size: 11px;
    line-height: 14px;
    color: rgb(255, 255, 255);
    font-weight: 700;
    background-color: #4f84ea;  /* Blue background color */
    padding: 3px 8px;           /* Inner spacing */
    border-radius: 10px;         /* Rounded corners */
    display: inline-block;      /* Proper block formatting */
    letter-spacing: 0.5px;      /* Optional: slight letter spacing for better readability */
    
}
.peak-rank .rank-icon-container {
    width: 36px;  /* Smaller than the 72px for main rank */
    height: 36px;
    margin-right: 20px; /* Same as the main icon container */
    margin-left: 18px; /* Add this to center the smaller icon (72-36)/2 = 18px */
    position: relative; /* Use relative instead of invalid 'left' */
    overflow: hidden;
}

.peak-rank .rank-icon {
    width: 65px;  
    height: 65px;
    transform: scale(1.2); /* Scale the icon to fill container better */
    object-position: center 85%; /* Position icon consistently */
}

.peak-rank .rankText {
  font-size: 14px;  /* Smaller than the 20px for main rank */
  line-height: 20px;
  margin-right: 80px;  /* Adjusted spacing */
  font-weight: 700;
  position: relative;
  margin-left: 20px;
  margin-top: -10px;
}

.peak-rank .lp-text {
    margin-left: 20px;
    margin-bottom: 0px;
    margin-top: -10px;
}

.stats-row {
  display: flex;
  justify-content: space-between;
  align-items: left;
  width: 100%;
  margin-top: 0px;
}
.rank-status {
  display: flex;
  justify-content: space-between;
  align-items: left;
  width: 100%;
  margin-top: 10px;
}

.win-percent {
  font-size: 12px;
  color: rgb(154, 164, 175);
  margin: 0 0 4px 0;
  font-weight: 400;
  line-height: 16px;
}

.win-loss-text {
  font-size: 12px;
  color: rgb(154, 164, 175);
  font-weight: 400;
  line-height: 26px;
}

.lp-text {
   font-family: 'Roboto';
   color: rgb(117, 133, 146);
   font-size: 12px;
   line-height: 16px;
   font-weight: 400;
}

.rank-history-container {
  width: auto;
  height: auto;
  background-color: #edeef2; /* Grey background */
  border-radius: 4px;
  margin-bottom: 20px;
  padding: 24px;
  box-sizing: border-box;
  padding-left: 400px;
}

.rank-history {
  display: flex;
  flex-direction: column;
}

.rank-title {
  margin-top: 0;
  margin-bottom: 1px;
  font-size: 14px;
  font-weight: 400;
  color: #202a38;
  background-color: white;
  width: 332px;
  height: 30px;
  border-radius: 4px 4px 0 0;
  padding: 10px;
  font-family: 'roboto';
  line-height: 14px;
}

/* Add white background boxes around each rank row */
.rank-row {
  display: flex;
  flex-direction: column; /* This is the key change - stack items vertically */
  align-items: flex-start;
  margin-bottom: 16px;
  font-weight: 700;
  background-color: white;
  border-radius: 4px;
  padding: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  width: 332px;
  height: auto; /* Change from fixed height to auto */
}

.rank-entry {
  display: flex;
  width: 100%;
  padding: 6px 0;
}

.rank-icon {
  width: 130px;
  height: 130px;
  object-fit: cover;
  object-position: center 85%;
  transform: scale(1.3);
}

.rank-icon-container {
  width: 72px;
  height: 72px;
  border-radius: 50%; 
  overflow: hidden;
  margin-right: 20px;
  background-color: #f7f7f9;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-shrink: 0;
}

.rank-info {
  display: flex;
  flex-direction: column;
}

.queue-type {
  font-size: 14px;
  color: #7d7d7d;
  margin: 0 0 4px 0;
  font-weight: 500;
}

.rankText {
  font-size: 20px;
  line-height: 28px;
  margin: 0;
  font-weight: 700;
  color: #202a38;
  white-space: nowrap;
  margin-right: 50px;
  flex-shrink: 0;
}


@media screen and (max-width: 720px) {
  .rank-history-container {
    width: 100%;
    height: auto;
  }
  
  .rank-icon-container {
    width: 60px;
    height: 60px;
  }
  
  .rank-icon {
    width: 110px;
    height: 110px;
  }
  
  .rankText {
    font-size: 14px;
  }
}
</style>