import { defineStore } from "pinia";

export type QueueType =
  | "all"
  | "solo"
  | "flex"
  | "aram"
  | "normal-draft"
  | "normal-blind"
  | "clash";

interface QueueFilterState {
  activeQueue: QueueType;
}

export const useQueueFilterStore = defineStore("queueFilter", {
  state: (): QueueFilterState => ({
    activeQueue: "all",
  }),

  actions: {
    setActiveQueue(queue: QueueType) {
      this.activeQueue = queue;
    },
  },
});
