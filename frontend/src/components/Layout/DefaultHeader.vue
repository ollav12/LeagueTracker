<template>
  <header>
    <nav>
      <!-- Left-aligned group -->
      <div class="nav-group nav-left">
        <button class="nav-button home-button" @click="goHome">
          LeagueIndex.dev
        </button>
        <button class="nav-button" @click="goToChampions">Champions</button>
      </div>

      <!-- Middle-aligned group (Search Form) -->
      <div class="nav-group nav-middle">
        <div class="searchForm">
          <SearchForm :layout="'Default'" @formSubmit="handleFormSubmit"/>
        </div>
      </div>

      <!-- Right-aligned group -->
      <div class="nav-group nav-right">
        <button class="nav-button" @click="goToLeaderboards">
          Leaderboards
        </button>
        <button class="nav-button" @click="goToStats">Stats</button>
        <button class="nav-button" @click="goToAbout">About</button>
      </div>
    </nav>
  </header>
</template>

<script>
import SearchForm from "../Form/SearchForm.vue";

export default {
  name: "DefaultHeader",
  components: {
    SearchForm,
  },
  methods: {
    goHome() {
      this.$router.push("/");
    },
    goToChampions() {
      this.$router.push("/champions");
    },
    goToLeaderboards() {
      this.$router.push("/leaderboards");
    },
    goToStats() {
      this.$router.push("/stats");
    },
    goToAbout() {
      this.$router.push("/about");
    },
    toggleDarkMode() {
      this.toggle = !this.toggle;
      if (this.toggle) {
        console.log("darkmode active");
        document.body.classList.add("darkmode");
        localStorage.setItem("darkmode", "true"); // Use "true" instead of "active"
      } else {
        console.log("light mode active");
        document.body.classList.remove("darkmode");
        localStorage.setItem("darkmode", "false"); // Use "false" instead of null
      }
    },
    handleFormSubmit(region, summoner, tag) {
      // Validate that summoner name is not empty before navigating
      if (summoner && summoner.trim() !== "") {
        console.log(`Region: ${region}, Summoner: ${summoner}, Tag: ${tag}`);
        this.$router.push(`/summoner/${region}/${summoner}-${tag}`);
      } else {
        console.log("Search field was empty");
      }
    },
  },
};
</script>

<style scoped>
body {
  background-color: var(--background);
}

:root {
  --background: #edeef2;
  --base-variant: #e8e9ed;
  --text-color: #9fabb9;
  --button-color: #1c1c1e;
  --primary-color: #3a435d;
  --nav-bar: #5383e9;
  --grey-background: #edeef2; /* Fixed syntax error */
}

nav {
  position: relative !important;
  width: 100%;
  height: 60px;
  background-color: #1b5850;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0px 30px;
}

.nav-group {
  display: flex;
  align-items: center;
}

.nav-left {
  justify-content: flex-start;
  gap: 20px;
  width: 25%;
}

.nav-middle {
  justify-content: center;
  flex: 1;
  display: flex;
  align-items: center;
}

.nav-right {
  justify-content: flex-end;
  gap: 20px;
  width: 25%;
}

.searchForm {
  width: 400px;
  margin: 0 auto; /* Centers horizontally */
  display: flex;
  justify-content: center;
}

.nav-button {
  background: transparent;
  border: none;
  cursor: pointer;
  font-size: 30px;
  font-family: "Roboto";
  color: white;
  font-weight: 500;
  padding: 8px 12px;
  transition: all 0.2s ease;
  position: relative;
  border-bottom: 3px solid transparent;
}

.nav-button:hover {
  opacity: 0.9;
}

.home-button {
  font-weight: 600;
}
</style>
