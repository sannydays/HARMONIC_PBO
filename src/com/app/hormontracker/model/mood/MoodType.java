package com.app.hormontracker.model.mood;

public enum MoodType {
    HAPPY(new HappyEffect()),
    SAD(new SadEffect()),
    ANGRY(new AngryEffect()),
    TIRED(new TiredEffect()),
    ANXIOUS(new AnxiousEffect()),
    CRAMPS(new CrampsEffect());

    private final MoodEffect effect;

    MoodType(MoodEffect effect) {
        this.effect = effect;
    }

    public MoodEffect getEffect() {
        return effect;
    }
}