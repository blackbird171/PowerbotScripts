package org.zohan.scripts.unfinishedarrows.tasks;

import org.powerbot.script.methods.MethodContext;
import org.zohan.scripts.unfinishedarrows.UnfinishedArrows;
import org.zohan.scripts.unfinishedarrows.util.Task;

public class ClickWidget extends Task{

    UnfinishedArrows ufa;
    private final int WIDGET_ID = 1370;
    private final int COMPONENT_ID = 38;

    public ClickWidget(MethodContext arg0, UnfinishedArrows uf) {
        super(arg0);
        ufa = uf;
    }

    @Override
    public boolean validate() {
        return ctx.widgets.get(WIDGET_ID).isValid();
    }

    @Override
    public void execute() {
        if (ctx.widgets.get(WIDGET_ID).getComponent(COMPONENT_ID).click(true)) {
            ufa.status = "Clicking Widget";
            sleep(400, 1000);
            ufa.status = "Waiting...";
        }
    }
}
