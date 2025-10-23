<template>
  <form
      @submit.prevent="formSubmit"
      :class="[
      'search-form',
      layout === 'Home' ? 'home-search-form' : 'default-search-form',
    ]"
  >
    <div :class="['text', layout === 'Home' ? 'text-home' : 'text-default']">
      <!-- Custom Region Selector for Home layout -->
      <div
          v-if="layout === 'Home'"
          class="custom-select"
          @blur="dropdownOpen = false"
          tabindex="0"
      >
        <div class="selected" @click="toggleDropdown">{{ region }}</div>
        <div class="dropdown" v-if="dropdownOpen">
          <div
              v-for="option in regionOptions"
              :key="option"
              class="option"
              :class="{ active: region === option }"
              @click="selectRegion(option)"
          >
            {{ option }}
          </div>
        </div>
      </div>

      <!-- Default Region Selector for other layouts -->
      <select v-else v-model="region" required>
        <option value="BR">BR</option>
        <option value="EUNE">EUN</option>
        <option value="EUW">EUW</option>
        <option value="JP">JP</option>
      </select>

      <div class="input-wrapper">
        <input
            ref="input"
            v-model="summoner"
            spellcheck="false"
            type="text"
            placeholder=""
        />
        <!-- Custom placeholder with styled region -->
        <div class="custom-placeholder" v-if="!summoner">
          Game name + <span class="region-tag">#{{ region }}</span>
        </div>
      </div>

      <button ref="submit" class="button" type="submit">
        <svg
            xmlns="http://www.w3.org/2000/svg"
            height="24px"
            viewBox="0 -960 960 960"
            width="24px"
            :fill="layout === 'Home' ? '#5f6368' : '#ffffff'"
        >
          <path
              d="M784-120 532-372q-30 24-69 38t-83 14q-109 0-184.5-75.5T120-580q0-109 75.5-184.5T380-840q109 0 184.5 75.5T640-580q0 44-14 83t-38 69l252 252-56 56ZM380-400q75 0 127.5-52.5T560-580q0-75-52.5-127.5T380-760q-75 0-127.5 52.5T200-580q0 75 52.5 127.5T380-400Z"
          />
        </svg>
      </button>
    </div>
  </form>
</template>

<script>
export default {
  props: {
    layout: {
      type: String,
      default: "Home",
      validator: (value) => ["Home", "Default"].includes(value),
    },
  },
  data() {
    return {
      summoner: "",
      region: "EUW",
      dropdownOpen: false,
      regionOptions: ["BR", "EUNE", "EUW", "JP"],
    };
  },
  methods: {
    async formSubmit() {
      let [name, tag] = this.summoner.split("#");
      if (tag == null) {
        tag = this.region;
      }
      console.log("Form submitted");
      this.$emit("formSubmit", this.region, name, tag);
      this.summoner = "";
      this.$refs.input.blur();
    },
    // New methods for custom dropdown
    toggleDropdown() {
      this.dropdownOpen = !this.dropdownOpen;
    },
    selectRegion(option) {
      this.region = option;
      this.dropdownOpen = false;
    },
  },
  computed: {
    placeholderText() {
      return `Game name + #${this.region}`;
    },
  },
};
</script>

<style scoped>
/* Add this new class styling */
.home-search-form {
}

.input-wrapper {
  position: relative;
  flex-grow: 1;
}

/* Home layout styles */
.text-home {
  display: flex;
  align-items: left;
  background-color: #fff;
  border-radius: 4px;
  width: 900px;
  height: 45px;
}

/* Default layout styles - updated to match Home but with less height */
.text-default {
  display: flex;
  align-items: left;
  background-color: #fff;
  border-radius: 4px;
  width: 600px;
  height: 35px; /* 5px less than Home's 45px */
}

select {
  width: fit-content;
  padding: 5px 5px;
  margin-right: 5px;
  color: #4f75da;
  font-size: 14px;
  font-weight: 400;
  border: none;
  outline: none;
  background-color: transparent;
  cursor: pointer;
  font-family: "Inter";
  background-color: #eef1ff;
  border-radius: 4px 0px 0px 4px;
  padding-right: 0px;
}

.default-search-form select {
  /* Match Home select styling */
  color: #1b5850;
  background-color: #d1efe9;
  font-size: 15px;
  font-weight: 500;
  padding: 5px 10px;
  border-radius: 6px 0px 0px 6px;
}

input {
  width: 100%;
  border: none;
  font-size: 12px;
  line-height: 16px;
  padding: 0px 8px;
  color: #1b5850;
  font-family: "Inter";
  font-weight: 400;
  outline: none;
  background: transparent;
  position: center;
  text-align: left;
  padding-top: 15px;
}

.default-search-form input {
  /* Match Home input styling */
  color: #1b5850;
  font-size: 12px;
  line-height: 16px;
  padding-top: 10px;
  font-family: "Inter";
  font-weight: 400;
  background: transparent;
}

.custom-placeholder {
  position: absolute;
  left: 8px;
  top: 52%;
  transform: translateY(-50%);
  color: #1b5850;
  font-size: 12px;
  pointer-events: none;
  font-family: "Inter";
}

.default-search-form .custom-placeholder {
  color: #1b5850;
  font-size: 12px;
}

.region-tag {
  background-color: #43b49b; /* Light gray box */
  padding: 2px 4px;
  border-radius: 2px;
  font-weight: 400;
}

.default-search-form .region-tag {
  background-color: #43b49b;
  padding: 2px 4px;
  border-radius: 2px;
  font-weight: 400;
}

input::placeholder {
  color: rgb(154, 164, 175); /* Light grey color for placeholder */
  opacity: 1; /* Firefox sometimes needs this */
}

.default-search-form input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.button svg {
  width: 20px;
  height: 20px;
  fill: #43b49b;
}

.default-search-form .button svg {
  fill: #43b49b;
}

.button:hover svg {
  fill: #1b5850;
}

.default-search-form .button:hover svg {
  fill: #1b5850;
}

.text-home:hover {
  box-shadow: 0 1px 6px rgba(32, 33, 36, 0.28);
  border-color: rgba(223, 225, 229, 0);
}

.text-default:hover {
  box-shadow: 0 1px 6px rgba(32, 33, 36, 0.28);
  border-color: rgba(223, 225, 229, 0);
}

/* Home layout region dropdown styling */
.home-search-form select {
  color: #1b5850;

  background-color: #d1efe9;

  font-size: 15px;
  font-weight: 500;
  padding: 5px 10px;
  border-radius: 6px 0px 0px 6px;
}

/* Dropdown focus state for home layout */
.home-search-form select:focus {
  outline: 2px solid rgba(67, 180, 155, 0.3);
}

/* Dropdown options styling */
.home-search-form select option {
  background-color: white;
  color: #1b5850;
}

/* Attempt to style option hover state - note this has limited browser support */
.home-search-form select option:hover,
.home-search-form select option:focus {
  background-color: #1b5850 !important;
  color: white !important;
}

/* Style the selected option */
.home-search-form select option:checked {
  background-color: #1b5850 !important;
  color: white !important;
  box-shadow: 0 0 10px 100px #1b5850 inset !important;
}

/* Custom dropdown styles for Home layout */
.custom-select {
  position: relative;
  width: fit-content;
  font-family: "Inter";
  outline: none;
}

.custom-select .selected {
  color: #1b5850;
  background-color: #d1efe9;
  font-size: 15px;
  font-weight: 500;
  padding: 0 10px; /* Reduced vertical padding */
  border-radius: 6px 0px 0px 6px;
  cursor: pointer;
  min-width: 50px;
  text-align: center;
  user-select: none;
  height: 45px; /* Match the height of text-home */
  line-height: 45px; /* Center text vertically */
  display: flex;
  align-items: center;
  justify-content: center;
}

.custom-select:focus .selected {
  outline: 2px solid rgba(67, 180, 155, 0.3);
}

.custom-select .dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  background: white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  border-radius: 4px;
  width: 100%;
  z-index: 100;
}

.custom-select .option {
  padding: 8px 10px;
  cursor: pointer;
  color: #1b5850;
}

.custom-select .option:hover {
  background-color: #1b5850;
  color: white;
}

.custom-select .option.active {
  background-color: #1b5850;
  color: white;
}

/* Custom dropdown for default layout */
.default-search-form .custom-select .selected {
  height: 40px; /* 5px shorter than home version */
  line-height: 40px;
}

/* Keep your original select styles for Default layout */
</style>
