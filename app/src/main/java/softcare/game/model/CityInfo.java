package softcare.game.model;

import androidx.annotation.NonNull;

public class EmojiInfo {
    private final int index;
    private final String emoji;
    private final String name;

    public EmojiInfo(int index, String emoji, String name) {
        this.index = index;
        this.emoji = emoji;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%d → %s %s", index, emoji, name);
    }

    // static method to get info by index
    public static EmojiInfo getEmoji(int i) {
        switch (i) {
            case 0: return new EmojiInfo(i, "📍", "Pin - perfect starting point");
            case 1: return new EmojiInfo(i, "🌍", "Earth (Africa/Europe)");
            case 2: return new EmojiInfo(i, "🏙️", "Cityscape");
            case 3: return new EmojiInfo(i, "🏔️", "Mountain");
            case 4: return new EmojiInfo(i, "🏝️", "Island");
            case 5: return new EmojiInfo(i, "🌈", "Rainbow");
            case 6: return new EmojiInfo(i, "🏰", "Castle");
            case 7: return new EmojiInfo(i, "🏖️", "Beach");
            case 8: return new EmojiInfo(i, "🌋", "Volcano");
            case 9: return new EmojiInfo(i, "✈️", "Airplane");
            case 10: return new EmojiInfo(i, "🚀", "Rocket");
            case 11: return new EmojiInfo(i, "🏞️", "National park");
            case 12: return new EmojiInfo(i, "🌆", "City at dusk");
            case 13: return new EmojiInfo(i, "🏕️", "Camping");
            case 14: return new EmojiInfo(i, "🌄", "Sunrise over mountains");
            case 15: return new EmojiInfo(i, "🏯", "Japanese castle");
            case 16: return new EmojiInfo(i, "🕌", "Mosque");
            case 17: return new EmojiInfo(i, "⛩️", "Shinto shrine");
            case 18: return new EmojiInfo(i, "🛕", "Hindu temple");
            case 19: return new EmojiInfo(i, "🏛️", "Classical building");
            case 20: return new EmojiInfo(i, "🏗️", "Construction site");
            case 21: return new EmojiInfo(i, "🚗", "Car");
            case 22: return new EmojiInfo(i, "🚢", "Ship");
            case 23: return new EmojiInfo(i, "🛫", "Plane departure");
            case 24: return new EmojiInfo(i, "🛬", "Plane arrival");
            case 25: return new EmojiInfo(i, "🛳️", "Passenger ship");
            case 26: return new EmojiInfo(i, "🚉", "Train station");
            case 27: return new EmojiInfo(i, "🛣️", "Highway");
            case 28: return new EmojiInfo(i, "🌧️", "Rain");
            case 29: return new EmojiInfo(i, "☀️", "Sun");
            case 30: return new EmojiInfo(i, "🌨️", "Snow");
            case 31: return new EmojiInfo(i, "⛈️", "Thunderstorm");
            case 32: return new EmojiInfo(i, "🌙", "Moon");
            case 33: return new EmojiInfo(i, "⭐", "Star");
            case 34: return new EmojiInfo(i, "🏠", "House");
            case 35: return new EmojiInfo(i, "🏡", "House with garden");
            case 36: return new EmojiInfo(i, "🏢", "Office building");
            case 37: return new EmojiInfo(i, "🏫", "School");
            case 38: return new EmojiInfo(i, "🏥", "Hospital");
            case 39: return new EmojiInfo(i, "🌅", "Sunrise");
            case 40: return new EmojiInfo(i, "🌉", "Bridge at night");
            case 41: return new EmojiInfo(i, "🗺️", "World map");
            case 42: return new EmojiInfo(i, "🌏", "Earth (Asia/Australia)");
            case 43: return new EmojiInfo(i, "🌎", "Earth (Americas)");
            case 44: return new EmojiInfo(i, "🌤️", "Sun behind cloud");
            case 45: return new EmojiInfo(i, "💡", "Idea / Discovery moment");
            case 46: return new EmojiInfo(i, "🎯", "Target");
            case 47: return new EmojiInfo(i, "🔭", "Telescope (exploration)");
            case 48: return new EmojiInfo(i, "🚁", "Helicopter");
            case 49: return new EmojiInfo(i, "🚜", "Tractor (fields)");
            case 50: return new EmojiInfo(i, "🧭", "Compass");
            default: return new EmojiInfo(i, "📌", "Unknown #" + i);
        }
    }

    public static void main(String[] args) {
        // Example usage
        for (int i = 0; i < 10; i++) {
            EmojiInfo info = EmojiInfo.getEmoji(i);
            System.out.println(info);
        }
    }
}

