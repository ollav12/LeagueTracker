<template>
  <header>
    <nav>
      <!-- Back to Home Button -->
      <button class="home-button" @click="goHome">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#5f6368"
        >
          <path
            d="M160-120v-375l-72 55-48-64 440-336 440 336-48 63-72-54v375H160Zm160-240q-17 0-28.5-11.5T280-400q0-17 11.5-28.5T320-440q17 0 28.5 11.5T360-400q0 17-11.5 28.5T320-360Zm160 0q-17 0-28.5-11.5T440-400q0-17 11.5-28.5T480-440q17 0 28.5 11.5T520-400q0 17-11.5 28.5T480-360Zm160 0q-17 0-28.5-11.5T600-400q0-17 11.5-28.5T640-440q17 0 28.5 11.5T680-400q0 17-11.5 28.5T640-360Z"
          />
        </svg>
      </button>

      <!-- Search Form -->
      <div class="searchForm">
        <SearchForm @formSubmit="handleFormSubmit" />
      </div>

      <!-- Dark Mode Toggle Button -->
      <button id="theme-switch" @click="toggleDarkMode">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#5f6368"
        >
          <path
            d="M480-280q-83 0-141.5-58.5T280-480q0-83 58.5-141.5T480-680q83 0 141.5 58.5T680-480q0 83-58.5 141.5T480-280ZM200-440H40v-80h160v80Zm720 0H760v-80h160v80ZM440-760v-160h80v160h-80Zm0 720v-160h80v160h-80ZM256-650l-101-97 57-59 96 100-52 56Zm492 496-97-101 53-55 101 97-57 59Zm-98-550 97-101 59 57-100 96-56-52ZM154-212l101-97 55 53-97 101-59-57Z"
          />
        </svg>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          height="24px"
          viewBox="0 -960 960 960"
          width="24px"
          fill="#5f6368"
        >
          <path
            d="M480-120q-150 0-255-105T120-480q0-150 105-255t255-105q14 0 27.5 1t26.5 3q-41 29-65.5 75.5T444-660q0 90 63 153t153 63q55 0 101-24.5t75-65.5q2 13 3 26.5t1 27.5q0 150-105 255T480-120Z"
          />
        </svg>
      </button>
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
  --grey-background: #edeef2; /* Fixed syntax error */
}

/* Dark Mode Toggle Button */
.darkmode {
  --background: #1c1c1e;
  --base-variant: #101425;
  --text-color: #9fabb9;
  --button-color: #1c1c1e;
  --primary-color: #3a435d;
  --nav-bar: #5383e9;
  --grey-background: #1a1c23; /* Add dark mode grey */
}

#theme-switch {
  height: 50px;
  width: 50px;
  padding: 0;
  border-radius: 50%;
  background-color: var(--nav-bar);
  display: flex;
  justify-content: center;
  align-items: center;
  border: none;
}

#theme-switch svg {
  fill: var(--button-color);
}

#theme-switch svg:last-child {
  display: none;
}

.darkmode #theme-switch svg:first-child {
  display: none;
}

.darkmode #theme-switch svg:last-child {
  display: block;
}

nav {
  position: relative !important; /* Force fixed positioning */
  width: 100%;
  height: 60px;
  background-color: #5383e9;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0px 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* Home Button */
.home-button svg {
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  color: var(--button-color);
  background-color: #5383e9;
  border: none;
  fill: var(--button-color);
}

.home-button {
  border: none;
}
</style>
