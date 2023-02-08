package gcat.editor.controller;

import gcat.editor.view.dialog.InfoDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoController implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        new InfoDialog();
    }
}
