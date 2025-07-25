package com.example.demo.service;


public enum GameRewardPolicy {
    GAME1(15, 200, 5000);

    private final int secondsPerRewardUnit;
    private final int goldPerUnit;
    private final int maxReward;

    GameRewardPolicy(int secondsPerRewardUnit, int goldPerUnit, int maxReward) {
        this.secondsPerRewardUnit = secondsPerRewardUnit;
        this.goldPerUnit = goldPerUnit;
        this.maxReward = maxReward;
    }

    public int calculateReward(long seconds) {
        int units = (int) (seconds / secondsPerRewardUnit);
        int reward = units * goldPerUnit;
        return Math.min(reward, maxReward);
    }

    public static GameRewardPolicy fromGameId(int gameId) {
        switch (gameId) {
            case 1: return GAME1;
            // case 2: return GAME2;
            default: throw new IllegalArgumentException("지원하지 않는 게임 ID입니다: " + gameId);
        }
    }
}
