<template>
          <div class="rank-row">
            <div class="rank-icon-container">
              <img :src="soloRankIconUrl" alt="Solo Rank" class="rank-icon" />
            </div>
            <p class="rankText">{{ soloRank }} | {{ soloWins }}W - {{ soloLosses }}L ({{ winPercentage }}%)</p>
          </div>
          
          <div class="rank-row">
            <div class="rank-icon-container">
              <img :src="flexRankIconUrl" alt="Flex Rank" class="rank-icon" />
            </div>
            <p class="rankText">{{ flexRank }} | {{ flexWins }}W - {{ flexLosses}}L ({{ winPercentageFlex }}%)</p>
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
  }
}

</script>
<style scoped>
.rank-row {
  display: flex;
  align-items: center;
  margin-bottom: 0px;
  font-weight: 700;
}

.rank-icon {
  /* Make the image much larger than the container */
  width: 130px;
  height: 130px;
  /* This centers the image and ensures it fills the container */
  object-fit: cover;
  /* Adjust these values to center the important part of the emblem */
  object-position: center 85%;
  /* Prevent the image from being squished */
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
  /* This makes sure the container doesn't resize */
  flex-shrink: 0;
  font-weight: 700;
}

</style>