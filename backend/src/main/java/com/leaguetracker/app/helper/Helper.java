package com.leaguetracker.app.helper;

public class Helper {

    public Helper() {
    }

    public enum LeagueRegion {
        BRAZIL("br1"),
        EUROPE_NORTHEAST("eun1"),
        EUROPE_WEST("euw1"),
        KOREA("kr"),
        LATIN_AMERICA_NORTH("la1"),
        LATIN_AMERICA_SOUTH("la2"),
        NORTH_AMERICA("na1"),
        OCEANIA("oc1"),
        RUSSIA("ru"),
        TURKEY("tr1"),
        JAPAN("jp1"),
        PHILIPPINES("ph2"),
        SINGAPORE("sg2"),
        THAILAND("th2"),
        TAIWAN("tw2"),
        VIETNAM("vn2");

        private final String regionCode;

        LeagueRegion(String regionCode) {
            this.regionCode = regionCode;
        }

        public String getRegionCode() {
            return regionCode;
        }
    }

    public enum RiotRegion {
        AMERICAS("americas"),
        ASIA("asia"),
        EUROPE("europe"),
        SEA("sea");

        private final String regionName;

        RiotRegion(String regionName) {
            this.regionName = regionName;
        }

        public String getRegionName() {
            return regionName;
        }
    }

    /**
     * Get riot api region from a region
     * 
     * @param region
     * @return
     */
    public static String getRiotApiRegion(String region) {
        LeagueRegion newRegion = LeagueRegion.valueOf(region.toLowerCase());
        switch (newRegion) {
            case NORTH_AMERICA:
            case BRAZIL:
            case LATIN_AMERICA_NORTH:
            case LATIN_AMERICA_SOUTH:
                return RiotRegion.AMERICAS.getRegionName();
            case OCEANIA:
            case PHILIPPINES:
            case SINGAPORE:
            case THAILAND:
            case TAIWAN:
            case VIETNAM:
                return RiotRegion.SEA.getRegionName();
            case KOREA:
            case JAPAN:
                return RiotRegion.ASIA.getRegionName();
            case EUROPE_NORTHEAST:
                return RiotRegion.EUROPE.getRegionName();
            case EUROPE_WEST:
                return RiotRegion.EUROPE.getRegionName();
            case TURKEY:
                return RiotRegion.EUROPE.getRegionName();
            case RUSSIA:
                return RiotRegion.EUROPE.getRegionName();
            default:
                throw new IllegalArgumentException("Unknown LeagueRegion: " + region);
        }
    }
}
