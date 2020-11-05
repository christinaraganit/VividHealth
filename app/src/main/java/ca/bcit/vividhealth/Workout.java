package ca.bcit.vividhealth;

import android.media.Image;

public class Workout {

    String workoutId;
    String workoutName;
    int workoutReps;
    int workoutSets;
    int workoutBreak;
    Image workoutGuide;

    public Workout() {}

    public String getWorkoutId() {
        return workoutId;
    }

    public Workout(String workoutId, String workoutName, int workoutReps, int workoutSets, int workoutBreak, Image workoutGuide) {
        this.workoutId = workoutId;
        this.workoutName = workoutName;
        this.workoutReps = workoutReps;
        this.workoutSets = workoutSets;
        this.workoutBreak = workoutBreak;
        this.workoutGuide = workoutGuide;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public int getWorkoutReps() {
        return workoutReps;
    }

    public void setWorkoutReps(int workoutReps) {
        this.workoutReps = workoutReps;
    }

    public int getWorkoutSets() {
        return workoutSets;
    }

    public void setWorkoutSets(int workoutSets) {
        this.workoutSets = workoutSets;
    }

    public int getWorkoutBreak() {
        return workoutBreak;
    }

    public void setWorkoutBreak(int workoutBreak) {
        this.workoutBreak = workoutBreak;
    }

    public Image getWorkoutGuide() {
        return workoutGuide;
    }

    public void setWorkoutGuide(Image workoutGuide) {
        this.workoutGuide = workoutGuide;
    }
}
