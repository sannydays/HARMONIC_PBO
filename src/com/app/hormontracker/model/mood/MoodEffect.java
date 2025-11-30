package com.app.hormontracker.model.mood;

public abstract class MoodEffect {
    public abstract String emotionalEffect(int level);
    public abstract String physicalEffect();


    public static MoodEffect create(MoodType type) {
            return switch (type) {
                case HAPPY -> new HappyEffect();
                case SAD -> new SadEffect();
                case ANGRY -> new AngryEffect();
                case TIRED -> new TiredEffect();
                case ANXIOUS -> new AnxiousEffect();
                case CRAMPS -> new CrampsEffect();
                default -> new HappyEffect(); // Default
            };
    }
}

// Inheritance dari mood yang ada dan memberikan keterangan sesuai level yang dipilih
// physicalEffect untuk menampilkan keterangan dari mood ke fisik

class HappyEffect extends MoodEffect {
    @Override
    public String emotionalEffect(int level) {
        return switch (level) {
            case 5 -> "Super joyful! High motivation ✨";
            case 4 -> "Feeling great and positive.";
            case 3 -> "Calm and content.";
            case 2 -> "A bit cheerful.";
            default -> "Slightly happy.";
        };
    }

    @Override
    public String physicalEffect() {
        return "Lower stress hormone, relaxed muscles.";
    }
}

class SadEffect extends MoodEffect {
    @Override
    public String emotionalEffect(int level) {
        return switch (level) {
            case 5 -> "Deep sadness and heavy emotions 😭";
            case 4 -> "Feeling down and emotional.";
            case 3 -> "Low mood.";
            case 2 -> "Slightly gloomy.";
            default -> "A bit sad.";
        };
    }

    @Override
    public String physicalEffect() {
        return "Low energy, heavy chest.";
    }
}

class AngryEffect extends MoodEffect {
    @Override
    public String emotionalEffect(int level) {
        return switch (level) {
            case 5 -> "Intense anger 😡🔥";
            case 4 -> "Very irritated.";
            case 3 -> "Annoyed.";
            case 2 -> "Slightly irritated.";
            default -> "Minor irritation.";
        };
    }

    @Override
    public String physicalEffect() {
        return "High tension, increased heart rate.";
    }
}

class TiredEffect extends MoodEffect {
    @Override
    public String emotionalEffect(int level) {
        return "Low energy and unmotivated.";
    }

    @Override
    public String physicalEffect() {
        return "Fatigue, weak muscles, low stamina.";
    }
}

class AnxiousEffect extends MoodEffect {
    @Override
    public String emotionalEffect(int level) {
        return "Nervous, overwhelmed, overthinking.";
    }

    @Override
    public String physicalEffect() {
        return "Fast heartbeat, dizziness, tense muscles.";
    }
}

class CrampsEffect extends MoodEffect {
    @Override
    public String emotionalEffect(int level) {
        return "Irritable and sensitive from menstrual pain.";
    }

    @Override
    public String physicalEffect() {
        return "Uterine contractions and period cramps 🩸";
    }
}