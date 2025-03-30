<template>
  <DefaultHeader />
  <slot />
  <DefaultFooter />
</template>

<script>
import DefaultHeader from "@/components/Layout/DefaultHeader.vue";
import DefaultFooter from "@/components/Layout/DefaultFooter.vue";

export default {
  components: {
    DefaultHeader,
    DefaultFooter,
  },
  watch: {
    $route(to, from) {
      if (
        !from ||
        from.params.region !== to.params.region ||
        from.params.name !== to.params.name
      ) {
        this.apiCall();
      }
    },
  },
};
</script>
<style scoped></style>
