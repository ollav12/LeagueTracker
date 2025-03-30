<template>
  <div class="home">
    <HomeHeader />
    <slot />
    <SimpleFooter />
  </div>
</template>

<script>
import HomeHeader from "@/components/Layout/HomeHeader.vue";
import SimpleFooter from "@/components/Layout/SimpleFooter.vue";

export default {
  components: {
    HomeHeader,
    SimpleFooter,
  },
};
</script>
<style>
.home {
  background-color: #5383e9;
  position: fixed;
  width: 100%;
  height: 100%;
}
</style>
