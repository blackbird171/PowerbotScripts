package org.zohan.scripts.unfinishedarrows;

import org.powerbot.event.MessageEvent;
import org.powerbot.event.MessageListener;
import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.zohan.scripts.unfinishedarrows.tasks.ClickItems;
import org.zohan.scripts.unfinishedarrows.tasks.ClickWidget;
import org.zohan.scripts.unfinishedarrows.util.Task;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


@Manifest(authors = "Zohan",
        name = "Unfinshed Arrows Fletcher",
        description = "Start with feathers and arrow shafts in your inventory.",
        topic = 0,
        version = 1.00)
public class UnfinishedArrows extends PollingScript implements MessageListener, PaintListener, MouseMotionListener{

    public String status = "Loading...";

    private final ArrayList<Task> tasks = new ArrayList<Task>();

    //Paint variables
    private int amountCrafted = 0;
    private int paintX = 0;
    private int paintY = 0;
    private long startTime;

    @Override
    public void start () {

        tasks.addAll(Arrays.asList(new ClickItems(ctx, this),
                new ClickWidget(ctx, this)));
        startTime = System.currentTimeMillis();
        System.out.println("Tasks Added");
    }

    @Override
    public int poll() {
        for (Task task : tasks) {
            if (task.validate()) {
                task.execute();
                return 50;
            }
        }
        return 0;
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        String message = messageEvent.getMessage();
        if (message.contains("You attach feathers to 15")){
            amountCrafted += 15;
        }
    }

    @Override
    public void repaint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(5, 102, 41, 175));
        g.fillRect(paintX, paintY, 300, 105);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Tahoma", Font.PLAIN, 20));
        g.drawString("Unfinshed Arrow Maker", paintX + 15, paintY + 25);

        g.setFont(new Font("Tahoma", Font.PLAIN, 14));
        g.drawString("Status: " + status, paintX + 15, paintY + 45);
        g.drawString("Fletched: " + amountCrafted, paintX + 15, paintY + 60);
        g.drawString("Fletched/Hour: " + pH(amountCrafted), paintX + 15, paintY + 75);

        String runTime = formatTime(getRuntime());
        g.drawString("Runtime: " + runTime, paintX +15, paintY + 90);

        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(5, 102, 41));
        g.drawLine(ctx.mouse.getLocation().x, ctx.mouse.getLocation().y - 10, ctx.mouse.getLocation().x, ctx.mouse.getLocation().y + 10);
        g.drawLine(ctx.mouse.getLocation().x - 10, ctx.mouse.getLocation().y, ctx.mouse.getLocation().x + 10, ctx.mouse.getLocation().y);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        paintX = e.getX();
        paintY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    private int pH(int val) {
        return (int) ((val) * 3600000D / (System.currentTimeMillis() - startTime));
    }

    private String formatTime (long time) {
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
        return hms;
    }
}
