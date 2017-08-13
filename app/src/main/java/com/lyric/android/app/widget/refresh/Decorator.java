package com.lyric.android.app.widget.refresh;

public abstract class Decorator implements IDecorator {
    protected IDecorator decorator;
    protected GraceRefreshLayout.CoreProcessor coreProcessor;

    public Decorator(GraceRefreshLayout.CoreProcessor processor, IDecorator iDecorator) {
        coreProcessor = processor;
        decorator = iDecorator;
    }
}
