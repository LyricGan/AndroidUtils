package com.lyric.android.app.widget.refresh;

public abstract class Decorator implements IDecorator {
    protected RefreshLayout.CoreProcessor coreProcessor;
    protected IDecorator decorator;

    public Decorator(RefreshLayout.CoreProcessor processor, IDecorator iDecorator) {
        coreProcessor = processor;
        decorator = iDecorator;
    }
}
