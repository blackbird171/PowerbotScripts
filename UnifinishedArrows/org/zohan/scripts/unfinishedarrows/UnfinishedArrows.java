package org.zohan.scripts.unfinishedarrows;

import org.powerbot.event.MessageEvent;
import org.powerbot.event.MessageListener;
import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.util.GeItem;
import org.zohan.scripts.unfinishedarrows.tasks.Afk;
import org.zohan.scripts.unfinishedarrows.tasks.ClickItems;
import org.zohan.scripts.unfinishedarrows.tasks.ClickWidget;
import org.zohan.scripts.unfinishedarrows.util.Task;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


@Manifest(authors = "Zohan",
        name = "Unfinshed Arrows Fletcher",
        description = "Start with feathers and arrow shafts in your inventory.",
        topic = 0,
        version = 1.02)
public class UnfinishedArrows extends PollingScript implements MessageListener, PaintListener, MouseMotionListener{

    public String status = "Loading...";
    public int state = 0;
    public int afkState = 2;
    public long nextAfk = 0;

    private final ArrayList<Task> tasks = new ArrayList<Task>();

    //Paint variables
    private int amountCrafted = 0;
    private int paintX = 0;
    private int paintY = 0;
    private long startTime;
    private int shaftPrice;
    private int featherPrice;
    private int arrowPrice;
    private int profitPerFletch;

    @Override
    public void start () {
        shaftPrice = getPrice(52);
        featherPrice = getPrice(314);
        arrowPrice = getPrice(53);
        log.info("Shaft Price: " + shaftPrice);
        log.info("Feather Price: " + featherPrice);
        log.info("Arrow Price: " + arrowPrice);
        profitPerFletch = arrowPrice - featherPrice - shaftPrice;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ArrowGui frame = new ArrowGui(UnfinishedArrows.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tasks.addAll(Arrays.asList(new ClickItems(ctx, this),
                new ClickWidget(ctx, this)));
        startTime = System.currentTimeMillis();
    }

    @Override
    public int poll() {
        if (state == 1){
            for (Task task : tasks) {
                if (task.validate()) {
                    task.execute();
                    return 50;
                }
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
        g.fillRect(paintX, paintY, 300, 150);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Tahoma", Font.PLAIN, 20));
        g.drawString("Unfinshed Arrow Maker", paintX + 15, paintY + 25);

        g.setFont(new Font("Tahoma", Font.PLAIN, 14));
        g.drawString("Status: " + status, paintX + 15, paintY + 45);
        g.drawString("Fletched: " + formatNumber(amountCrafted), paintX + 15, paintY + 60);
        g.drawString("Fletched/Hour: " + formatNumber(pH(amountCrafted)), paintX + 15, paintY + 75);

        int profit = amountCrafted * profitPerFletch;
        g.drawString("Profit: " + formatNumber(profit), paintX + 15, paintY + 90);
        g.drawString("Profit: " + formatNumber(pH(profit)), paintX + 15, paintY + 105);

        String runTime = formatTime(getRuntime());
        g.drawString("Runtime: " + runTime, paintX +15, paintY + 120);
        g.drawString(afkMessage(), paintX + 15, paintY + 135);


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

    public void addAfk (int min, int max) {
        tasks.add(new Afk(ctx, max, min, this));
    }

    public String getEstimatedProfit () {
        return formatNumber(profitPerFletch * 30000);
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

    private String formatNumber (int i) {
        return NumberFormat.getIntegerInstance().format(i);
    }

    private String afkMessage () {
        String timeTillAFK = formatTime(nextAfk - System.currentTimeMillis());
        if (afkState == 0) {
            return "Next AFK: " + timeTillAFK;
        } else if (afkState == 1){
            return "Stopping AFK: " + timeTillAFK;
        }
        return "AFK: Disabled";
    }


    //Credits to Coma for price look-up method
    private int getPrice(final int id) {
        return GeItem.getProfile(id).getPrice(GeItem.PriceType.CURRENT).getPrice();
    }

}
