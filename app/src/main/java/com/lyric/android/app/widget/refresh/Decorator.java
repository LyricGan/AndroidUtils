package com.lyric.android.app.widget.refresh;

public abstract class Decorator implements IDecorator {
    protected IDecorator decorator;
    protected GraceRefreshLayout.CoreProcessor coreProcessor;

    public Decorator(GraceRefreshLayout.CoreProcessor processor, IDecorator decorator1) {
        coreProcessor = processor;
        decorator = decorator1;
    }
}
