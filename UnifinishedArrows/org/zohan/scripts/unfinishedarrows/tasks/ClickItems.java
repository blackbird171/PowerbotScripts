package org.zohan.scripts.unfinishedarrows.tasks;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Item;
import org.zohan.scripts.unfinishedarrows.UnfinishedArrows;
import org.zohan.scripts.unfinishedarrows.util.Task;

import javax.swing.*;

public class ClickItems extends Task{

    UnfinishedArrows ufa;
    private final int FEATHER_ID = 314;
    private final int SHAFT_ID = 52;

    public ClickItems(MethodContext arg0, UnfinishedArrows uf) {
        super(arg0);
        ufa = uf;
    }

    @Override
    public boolean validate() {
        return !ctx.widgets.get(1370).isValid()
                && !ctx.widgets.get(1251).isValid();
    }

    @Override
    public void execute() {
        Item feathers = ctx.backpack.select().id(FEATHER_ID).poll();
        Item shafts = ctx.backpack.select().id(SHAFT_ID).poll();

        if (feathers.isValid() && shafts.isValid()) {
            ufa.status = "Clicking Feathers";
            if (feathers.click(true)){
                sleep(50, 150);
                ufa.status = "Clicking Shafts";
                if (shafts.click(true)){
                    sleep(500, 1250);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No more supplies found, stopping script.");
            ufa.getController().stop();
        }
    }
}
