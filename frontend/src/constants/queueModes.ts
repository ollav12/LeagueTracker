export const QUEUE_MODES = {
  400: "Normal Draft",
  420: "Ranked Solo/Duo",
  430: "Normal Blind",
  440: "Ranked Flex",
  450: "ARAM",
  700: "Clash",
  830: "Co-op vs AI (Intro)",
  840: "Co-op vs AI (Beginner)",
  850: "Co-op vs AI (Intermediate)",
  900: "URF",
  1020: "One for All",
  1300: "Nexus Blitz",
  1400: "Ultimate Spellbook",
  1700: "Arena",
} as const;

export type QueueId = keyof typeof QUEUE_MODES;
export type QueueName = (typeof QUEUE_MODES)[QueueId];

export const QUEUE_FILTER_MAPPING = {
  all: [], // empty array means show all
  solo: [420], // Ranked Solo/Duo
  flex: [440], // Ranked Flex
  aram: [450], // ARAM
  "normal-draft": [400], // Normal Draft
  "normal-blind": [430], // Normal Blind
  clash: [700], // Clash
} as const;

export type QueueFilterType = keyof typeof QUEUE_FILTER_MAPPING;
