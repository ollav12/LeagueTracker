import { defineStore } from 'pinia';

type RegionsList = {
  [key: string]: string;
};

export const useGlobalStore = defineStore('global', {
  state: () => ({
    regionsList: {
      br: 'br1',
      eune: 'eun1',
      euw: 'euw1',
      jp: 'jp1',
      kr: 'kr',
      lan: 'la1',
      las: 'la2',
      na: 'na1',
      oce: 'oc1',
      tr: 'tr1',
      ru: 'ru',
      ph: 'ph2',
      sg: 'sg2',
      th: 'th2',
      tw: 'tw2',
      vn: 'vn2',
    } as RegionsList, // Type assertion
    roles: ['TOP', 'JUNGLE', 'MIDDLE', 'BOTTOM', 'UTILITY'],
  }),
});
