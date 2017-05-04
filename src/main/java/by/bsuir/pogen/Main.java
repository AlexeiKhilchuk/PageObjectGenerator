package by.bsuir.pogen;

import by.bsuir.pogen.forms.FetcherForm;

import javax.swing.*;

/**
 * Created by Alexei Khilchuk on 13.02.2017.
 */
public class Main {
    public static void main(String[] args) {
        FetcherForm mainForm = new FetcherForm();
        mainForm.setContentPane(mainForm.mainPanel);
        mainForm.setSize(500, 400);
        mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainForm.setVisible(true);
        mainForm.setResizable(false);
        mainForm.setLocationRelativeTo(null);
        mainForm.pack();
    }
}
