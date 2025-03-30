import { createRouter, createWebHistory } from "vue-router";
//import SummonerView from '../views/SummonerView.vue'
//import LeaderboardView from '../views/LeaderboardView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: () => import("../views/HomeView.vue"),
      meta: {
        layout: "Home",
      },
    },
    {
      path: "/summoner/:region/:summoner-:tag",
      name: "summoner",
      component: () => import("../views/SummonerView.vue"),
      meta: {
        layout: "Default",
      },
    },
    {
      path: "/leaderboard",
      name: "leaderboard",
      component: () => import("../views/LeaderboardView.vue"),
      meta: {
        layout: "Default",
      },
    },
    {
      path: "/about",
      name: "about",
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import("../views/AboutView.vue"),
      meta: {
        layout: "Default",
      },
    },
  ],
});

export default router;
