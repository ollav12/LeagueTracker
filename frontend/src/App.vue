<template>
  <div>
    <component :is="layout">
      <router-view />
    </component>
  </div>
</template>

<script>
import DefaultLayout from "@/layouts/Default.vue";
import HomeLayout from "@/layouts/Home.vue";

export default {
  computed: {
    layout() {
      return this.$route.meta.layout === "Home" ? HomeLayout : DefaultLayout;
    },
  },
};
</script>
