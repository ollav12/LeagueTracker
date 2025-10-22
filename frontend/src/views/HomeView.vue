<template>
  <div class="homeview">
    <div class="logo-container">
      <img
          src="@/assets/heimedinger.webp"
          alt="Heimerdinger"
          class="heimerdinger-logo"
      />
    </div>
    <div class="form">
      <SearchForm @formSubmit="redirect"/>
    </div>

    <!-- Added decorative rectangles -->
    <div class="decorative-rectangles">
      <div class="rectangle left-rectangle"></div>
      <div class="rectangle middle-rectangle"></div>
      <div class="rectangle right-rectangle"></div>
    </div>
  </div>
</template>

<script>
import SearchForm from "@/components/Form/SearchForm.vue";

export default {
  name: "HomeView",
  components: {
    SearchForm,
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
  margin-top: 0px;
  height: 89%;
  position: relative;
}

.logo-container {
  margin-bottom: 20px;
  max-width: 170px;
}

.heimerdinger-logo {
  width: 100%;
  height: auto;
}

/* Decorative rectangles styling */
.decorative-rectangles {
  display: flex;
  justify-content: center;
  width: 100%;
  position: relative;
  margin-top: 30px;
}

.rectangle {
  background-color: #43b49b;
  border-radius: 12px;
  height: 120px;
  width: 280px;
  position: absolute;
}

.middle-rectangle {
  z-index: 3;
  margin-top: 20px;
}

.left-rectangle {
  z-index: 2;
  margin-top: 50px;
  margin-right: 180px;
  transform: rotate(-5deg);
}

.right-rectangle {
  z-index: 2;
  margin-top: 50px;
  margin-left: 180px;
  transform: rotate(5deg);
}
</style>
