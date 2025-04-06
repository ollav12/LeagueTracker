<template>
  <header>
    <nav>
      <!-- Left-aligned group -->
      <div class="nav-group nav-left">
        <button class="nav-button home-button" @click="goHome">
          LeagueIndex.GG
        </button>
        <button class="nav-button" @click="goToChampions">Champions</button>
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
export default {
  name: "HomeHeader",
  components: {},
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
        localStorage.setItem("darkmode", "true");
      } else {
        console.log("light mode active");
        document.body.classList.remove("darkmode");
        localStorage.setItem("darkmode", "false");
      }
    },
  },
};
</script>

<style scoped>
/* Navigation Bar Styling */
* {
  margin: 0px;
  padding: 0px;
  box-sizing: border-box;
}

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
  --grey-background: #edeef2;
}

/* Dark Mode Toggle Button */
.darkmode {
  --background: #1c1c1e;
  --base-variant: #101425;
  --text-color: #9fabb9;
  --button-color: #1c1c1e;
  --primary-color: #3a435d;
  --nav-bar: #5383e9;
  --grey-background: #1a1c23;
}

nav {
  position: relative !important;
  width: 100%;
  height: 60px;
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
}

.nav-right {
  justify-content: flex-end;
  gap: 20px;
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
  position: relative; /* Added to position the underline */
  border-bottom: 3px solid transparent; /* Transparent border initially */
}

.nav-button:hover {
  opacity: 0.9;
  border-bottom: 3px solid #43b49b; /* Green underline on hover */
}
</style>
