import "./assets/base.css";

import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";

const app = createApp(App);
const pinia = createPinia();
pinia.use(({ store }) => {
  store.$onAction(() => {}, false); // false means no subscription debug messages
});
app.use(pinia);
app.use(router);

app.mount("#app");
