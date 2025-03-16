<template>
    <form @submit.prevent="formSubmit" class="home-search-form">
      <div class="">
        <input ref="input" v-model="summoner" spellcheck="false" type="text" placeholder="Search summoner"/>

        <!-- Region Selector -->
        <select v-model="region" required>
          <option value="EUW">EUW</option>
          <option value="EUNE">EUNE</option>
          <option value="NA">NA</option>
        </select>
        <button ref="submit" class="button" type="submit">
          <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#5f6368"><path d="M784-120 532-372q-30 24-69 38t-83 14q-109 0-184.5-75.5T120-580q0-109 75.5-184.5T380-840q109 0 184.5 75.5T640-580q0 44-14 83t-38 69l252 252-56 56ZM380-400q75 0 127.5-52.5T560-580q0-75-52.5-127.5T380-760q-75 0-127.5 52.5T200-580q0 75 52.5 127.5T380-400Z"/></svg>
        </button>
        </div>
    </form> 
</template>
  
<script>
  export default {
    components: {
    },  
    data() {
      return {
        summoner: '',
        region: 'EUW',
      }
    },

    methods: {
    async formSubmit() {
        this.$emit("formSubmit", this.summoner, this.region);
    },
  },
  }
</script>
  
<style scoped>

.button svg {
  width: 30px;
  height: 10px;
  position: relative;
  text-align: center;
  border: none;
}

select {
  width: 70px;
  color: var(--button-color);
}

</style>