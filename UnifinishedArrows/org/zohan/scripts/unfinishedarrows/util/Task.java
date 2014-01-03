package org.zohan.scripts.unfinishedarrows.util;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;

public abstract class Task extends MethodProvider{
    public Task(MethodContext arg0) {
        super(arg0);
    }

    public abstract boolean validate();

    public abstract void execute();
}
