package com.lyric.android.app.widget.refresh;

public abstract class Decorator implements IDecorator {
    protected GraceRefreshLayout.CoreProcessor coreProcessor;
    protected IDecorator decorator;

    public Decorator(GraceRefreshLayout.CoreProcessor processor, IDecorator iDecorator) {
        coreProcessor = processor;
        decorator = iDecorator;
    }
}
