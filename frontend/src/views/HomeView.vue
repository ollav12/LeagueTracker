<template>
  <div class="homeview">
    <h1 class="title">LeagueTracker</h1>
    <div class="form">
      <SearchForm @formSubmit="redirect" />
    </div>
  </div>
</template>

<script>
import SearchForm from "@/components/Form/SearchForm.vue";
import SimpleFooter from "@/components/Layout/SimpleFooter.vue";

export default {
  components: {
    SearchForm,
    SimpleFooter,
  },
  methods: {
    redirect(region, summoner, tag) {
      if (summoner != "") {
        this.$router.push(`/summoner/${region}/${summoner}-${tag}`);
      }
    },
  },
};
</script>

<style scoped>
.title {
  color: white;
}

.homeview {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  text-align: center;
  padding-top: 200px;
  height: 89%;
}
</style>
