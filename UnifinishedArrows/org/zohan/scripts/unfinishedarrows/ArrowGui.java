package org.zohan.scripts.unfinishedarrows;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JSeparator;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ArrowGui extends JFrame {

    private JPanel contentPane;
    private JTextField afkMin;
    private JTextField afkMax;

    /**
     * Create the frame.
     */
    public ArrowGui(final UnfinishedArrows uf) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 434, 261);
        contentPane.add(tabbedPane);

        JLayeredPane antibanPane = new JLayeredPane();
        tabbedPane.addTab("Antiban", null, antibanPane, null);

        final JRadioButton btnAfk = new JRadioButton("AFK");
        btnAfk.setBackground(Color.WHITE);
        btnAfk.setBounds(6, 7, 109, 23);
        antibanPane.add(btnAfk);

        afkMin = new JTextField();
        afkMin.setText("3");
        afkMin.setBounds(121, 7, 62, 23);
        antibanPane.add(afkMin);
        afkMin.setColumns(10);

        JLabel lblTo = new JLabel("to");
        lblTo.setBounds(195, 11, 46, 19);
        antibanPane.add(lblTo);

        afkMax = new JTextField();
        afkMax.setText("5");
        afkMax.setBounds(214, 7, 62, 23);
        antibanPane.add(afkMax);
        afkMax.setColumns(10);

        JLabel lblMinutes = new JLabel("minutes");
        lblMinutes.setBounds(286, 11, 46, 19);
        antibanPane.add(lblMinutes);

        JSeparator separator = new JSeparator();
        separator.setBounds(16, 37, 403, 2);
        antibanPane.add(separator);

        JLayeredPane startPane = new JLayeredPane();
        tabbedPane.addTab("Start", null, startPane, null);

        JLabel lblThanksForUsing = new JLabel("Thanks for using Unfinshed Arrow Maker");
        lblThanksForUsing.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblThanksForUsing.setBounds(10, 11, 409, 29);
        startPane.add(lblThanksForUsing);

        JLabel lblMakeSureYou = new JLabel("Make sure you do not over-bot and get yourself banned.");
        lblMakeSureYou.setBounds(10, 51, 409, 37);
        startPane.add(lblMakeSureYou);

        JLabel lblBeSureTo = new JLabel("Be sure to post proggies and give feed back in the forums");
        lblBeSureTo.setBounds(10, 83, 409, 14);
        startPane.add(lblBeSureTo);

        JButton btnBegin = new JButton("Begin");
        btnBegin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (btnAfk.isSelected()) {
                    int min = Integer.parseInt(afkMin.getText());
                    int max = Integer.parseInt(afkMax.getText());
                    uf.addAfk(min, max);
                } else {
                    uf.afkState = 2;
                }
                uf.state = 1;
                dispose();
            }
        });
        btnBegin.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnBegin.setBounds(303, 175, 116, 47);
        startPane.add(btnBegin);
        setVisible(true);
    }
}
