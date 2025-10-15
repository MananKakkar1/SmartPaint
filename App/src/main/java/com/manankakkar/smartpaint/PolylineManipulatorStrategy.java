package com.manankakkar.smartpaint;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PolylineManipulatorStrategy extends ShapeManipulatorStrategy {
    PolylineManipulatorStrategy(PaintModel paintModel) {
        super(paintModel);
    }
    private PolylineCommand polylineCommand;

    @Override
    public void mousePressed(MouseEvent e) {
        if (polylineCommand == null) {
            polylineCommand = new PolylineCommand();
            this.addCommand(polylineCommand);
        }
        else if (e.getButton().equals(MouseButton.SECONDARY)) {
            Point p1 = new Point((int) e.getX(), (int) e.getY());
            this.polylineCommand.add(p1);
            this.polylineCommand = null;
            return;
        }
        Point p1 = new Point((int) e.getX(), (int) e.getY());
        this.polylineCommand.add(p1);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (polylineCommand != null) {
            Point p1 = new Point((int) e.getX(), (int) e.getY());
            this.polylineCommand.addRemove(p1);
        }

    }
}
