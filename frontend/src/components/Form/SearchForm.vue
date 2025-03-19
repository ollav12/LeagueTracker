<template>
  <form @submit.prevent="formSubmit" class="home-search-form">
    <div class="text">
      <!-- Region Selector -->
      <select v-model="region" required>
        <option value="EUW">EUW</option>
        <option value="EUNE">EUN</option>
        <option value="NA">NA</option>
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
          fill="#5f6368"
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
  components: {},
  data() {
    return {
      summoner: "",
      region: "EUW",
    };
  },
  methods: {
    async formSubmit() {
      let [name, tag] = this.summoner.split("#");
      if (tag == null) {
        tag = this.region;
      }
      console.log(name);
      console.log(tag);
      this.$emit("formSubmit", this.region, name, tag);
      this.summoner = "";
      this.$refs.input.blur();
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
.input-wrapper {
  position: relative;
  flex-grow: 1;
}

.text {
  display: flex;
  align-items: left;
  background-color: #fff;
  border-radius: 4px;
  width: 900px;
  height: 32px;
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
  font-family: "Roboto";
  background-color: #eef1ff;
  border-radius: 4px 0px 0px 4px;
  padding-right: 0px;
}

input {
  width: 100%;
  border: none;
  font-size: 12px;
  line-height: 16px;
  padding: 0px 8px;
  color: rgb(0, 0, 0);
  font-family: "Roboto";
  font-weight: 400;
  outline: none;
  background: transparent;
  position: center;
  text-align: left;
  padding-top: 8px;
}

.custom-placeholder {
  position: absolute;
  left: 8px;
  top: 52%;
  transform: translateY(-50%);
  color: rgb(154, 164, 175);
  font-size: 12px;
  pointer-events: none;
  font-family: "Roboto";
}

.region-tag {
  background-color: #f0f2f5; /* Light gray box */
  padding: 2px 4px;
  border-radius: 2px;
  font-weight: 400;
}

input::placeholder {
  color: rgb(154, 164, 175); /* Light grey color for placeholder */
  opacity: 1; /* Firefox sometimes needs this */
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
  fill: #4f84ea;
}

.button:hover svg {
  fill: #2b5cd9;
}

.text:hover {
  box-shadow: 0 1px 6px rgba(32, 33, 36, 0.28);
  border-color: rgba(223, 225, 229, 0);
}
</style>
