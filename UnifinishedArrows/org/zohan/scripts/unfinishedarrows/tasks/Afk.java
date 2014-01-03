package org.zohan.scripts.unfinishedarrows.tasks;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Random;
import org.zohan.scripts.unfinishedarrows.UnfinishedArrows;
import org.zohan.scripts.unfinishedarrows.util.Task;


public class Afk extends Task {

    private UnfinishedArrows uf;
    private long lastTime;
    private long nextTime;
    private int max;
    private int min;

    public Afk(MethodContext arg0, int max, int min, UnfinishedArrows uf) {
        super(arg0);
        this.max = max;
        this.min = min;
        this.uf = uf;
        setTimes();
    }

    @Override
    public boolean validate() {
        return nextTime <= System.currentTimeMillis();
    }

    @Override
    public void execute() {
        uf.status = "AFK";
        uf.afkState = 1;
        int sleepTime = Random.nextInt(18000, 35000);
        uf.nextAfk = System.currentTimeMillis() + sleepTime;
        sleep(sleepTime);
        setTimes();
    }

    private void setTimes () {
        uf.afkState = 0;
        lastTime = System.currentTimeMillis();
        int addedTime = Random.nextInt(min, max) * 60 * 1000  + min;
        nextTime = lastTime + addedTime;
        uf.nextAfk = nextTime;
    }
}
