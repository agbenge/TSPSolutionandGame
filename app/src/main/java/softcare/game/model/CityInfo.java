package softcare.game.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CityInfo {
    private final int index;
    private final String name;
    private final String description;

    public CityInfo(int index, String name, String description ) {
        this.index = index;
        this.name = name;
        this.description = description ;
    }

    public static List<String> getCitiesInfoNames(List<CityInfo> cityInfos) {
        List<String> names = new ArrayList<>();
        for (CityInfo cityInfo  : cityInfos) {
            names.add(cityInfo.getName());
        }
        return names;
    }

    public static List<CityInfo> getCitiesFromNames(List<String> cities) {
        List<CityInfo> cityInfos = new ArrayList<>();
        int index = 0;
        for (String city : cities) {
            cityInfos.add(new CityInfo(index, city, city+"["+index+"]"));
            index++;
        }
        return cityInfos;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%d → %s %s", index, name, description);
    }

    // static method to get info by index
    public static CityInfo getEmoji(int i) {
        switch (i) {
            case 0: return new CityInfo(i, "📍", "Pin - perfect starting point");
            case 1: return new CityInfo(i, "🌍", "Earth (Africa/Europe)");
            case 2: return new CityInfo(i, "🏙️", "Cityscape");
            case 3: return new CityInfo(i, "🏔️", "Mountain");
            case 4: return new CityInfo(i, "🏝️", "Island");
            case 5: return new CityInfo(i, "🌈", "Rainbow");
            case 6: return new CityInfo(i, "🏰", "Castle");
            case 7: return new CityInfo(i, "🏖️", "Beach");
            case 8: return new CityInfo(i, "🌋", "Volcano");
            case 9: return new CityInfo(i, "✈️", "Airplane");
            case 10: return new CityInfo(i, "🚀", "Rocket");
            case 11: return new CityInfo(i, "🏞️", "National park");
            case 12: return new CityInfo(i, "🌆", "City at dusk");
            case 13: return new CityInfo(i, "🏕️", "Camping");
            case 14: return new CityInfo(i, "🌄", "Sunrise over mountains");
            case 15: return new CityInfo(i, "🏯", "Japanese castle");
            case 16: return new CityInfo(i, "🕌", "Mosque");
            case 17: return new CityInfo(i, "⛩️", "Shinto shrine");
            case 18: return new CityInfo(i, "🛕", "Hindu temple");
            case 19: return new CityInfo(i, "🏛️", "Classical building");
            case 20: return new CityInfo(i, "🏗️", "Construction site");
            case 21: return new CityInfo(i, "🚗", "Car");
            case 22: return new CityInfo(i, "🚢", "Ship");
            case 23: return new CityInfo(i, "🛫", "Plane departure");
            case 24: return new CityInfo(i, "🛬", "Plane arrival");
            case 25: return new CityInfo(i, "🛳️", "Passenger ship");
            case 26: return new CityInfo(i, "🚉", "Train station");
            case 27: return new CityInfo(i, "🛣️", "Highway");
            case 28: return new CityInfo(i, "🌧️", "Rain");
            case 29: return new CityInfo(i, "☀️", "Sun");
            case 30: return new CityInfo(i, "🌨️", "Snow");
            case 31: return new CityInfo(i, "⛈️", "Thunderstorm");
            case 32: return new CityInfo(i, "🌙", "Moon");
            case 33: return new CityInfo(i, "⭐", "Star");
            case 34: return new CityInfo(i, "🏠", "House");
            case 35: return new CityInfo(i, "🏡", "House with garden");
            case 36: return new CityInfo(i, "🏢", "Office building");
            case 37: return new CityInfo(i, "🏫", "School");
            case 38: return new CityInfo(i, "🏥", "Hospital");
            case 39: return new CityInfo(i, "🌅", "Sunrise");
            case 40: return new CityInfo(i, "🌉", "Bridge at night");
            case 41: return new CityInfo(i, "🗺️", "World map");
            case 42: return new CityInfo(i, "🌏", "Earth (Asia/Australia)");
            case 43: return new CityInfo(i, "🌎", "Earth (Americas)");
            case 44: return new CityInfo(i, "🌤️", "Sun behind cloud");
            case 45: return new CityInfo(i, "💡", "Idea / Discovery moment");
            case 46: return new CityInfo(i, "🎯", "Target");
            case 47: return new CityInfo(i, "🔭", "Telescope (exploration)");
            case 48: return new CityInfo(i, "🚁", "Helicopter");
            case 49: return new CityInfo(i, "🚜", "Tractor (fields)");
            case 50: return new CityInfo(i, "🧭", "Compass");
            default: return new CityInfo(i, "📌", "Unknown #" + i);
        }
    }
    public static List<CityInfo> getCitiesEmoji(int size) {
        List<CityInfo> cities = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            cities.add(getEmoji(i));
        }
        return cities;
    }
    public static List<CityInfo> getCitiesInfo(int size) {
        List<CityInfo> cities = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            cities.add(getEmoji(i));
        }
        return cities;
    }
    public static CityInfo getCityInfo(int number) {
        // Generate Excel-like name (A, B, ..., Z, AA, AB, etc.)
        StringBuilder columnName = new StringBuilder();
        int n = number;

        // 0-based index version
        do {
            int remainder = n % 26;
            columnName.insert(0, (char) ('A' + remainder));
            n = (n / 26) - 1;
        } while (n >= 0);

        String name = columnName.toString();

        // Example description
        String description = "City " + number + " [" + name + "]";

        return new CityInfo(number, name, description);
    }

}

