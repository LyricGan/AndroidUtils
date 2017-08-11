package com.lyric.android.app.test.logger;

/**
 * @author Orhan Obut
 */
public final class Settings {
    private int methodCount = 2;
    private boolean showThreadInfo = true;
    /**
     * Determines how logs will printed
     */
    private Level level = Level.FULL;

    public Settings hideThreadInfo() {
        showThreadInfo = false;
        return this;
    }

    public Settings setMethodCount(int methodCount) {
        this.methodCount = methodCount;
        return this;
    }

    public Settings setLevel(Level level) {
        this.level = level;
        return this;
    }

    public Settings setLevelFull() {
        return setLevel(Level.FULL);
    }

    public Settings setLevelNone() {
        return setLevel(Level.NONE);
    }

    public int getMethodCount() {
        return methodCount;
    }

    public boolean isShowThreadInfo() {
        return showThreadInfo;
    }

    public Level getLevel() {
        return level;
    }
}
