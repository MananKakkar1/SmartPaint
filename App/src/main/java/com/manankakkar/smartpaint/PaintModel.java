package com.manankakkar.smartpaint;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.canvas.GraphicsContext;

public class PaintModel extends Observable implements Observer {

	public void save(PrintWriter writer) {
		for (PaintCommand command: commands) {
			if (command.getClass() == CircleCommand.class) {
				writer.println("Circle");
				writer.println(command);
				writer.println("    " + "center:("+((CircleCommand) command).getCentre().x+","+((CircleCommand) command).getCentre().y+")");
				writer.println("    " + "radius:"+((CircleCommand) command).getRadius());
				writer.println("End Circle");
			}
			else if (command.getClass() == RectangleCommand.class) {
				writer.println("Rectangle");
				writer.println(command);
				writer.println("    " + "p1:("+((RectangleCommand) command).getP1().x+","+((RectangleCommand) command).getP1().y + ")");
				writer.println("    " + "p2:("+((RectangleCommand) command).getP2().x+","+((RectangleCommand) command).getP2().y + ")");
				writer.println("End Rectangle");
			}
			else if (command.getClass() == SquiggleCommand.class) {
				writer.println("Squiggle");
				writer.println(command);
				ArrayList<Point> points = ((SquiggleCommand) command).getPoints();
				writer.println("    "+"points");
				for (Point point: points) {
					writer.println("        " + "point:("+point.x+","+point.y+")");
				}
				writer.println("    " + "end points");
				writer.println("End Squiggle");
			}
			else if (command.getClass() == PolylineCommand.class) {
				writer.println("Polyline");
				writer.println(command);
				ArrayList<Point> points = ((PolylineCommand) command).getPoints();
				writer.println("    "+"points");
				for (Point point: points) {
					writer.println("        " + "point:("+point.x+","+point.y+")");
				}
				writer.println("    " + "end points");
				writer.println("End Polyline");
			}

		}
	}
	public void reset(){
		for(PaintCommand c: this.commands){
			c.deleteObserver(this);
		}
		this.commands.clear();
		this.setChanged();
		this.notifyObservers();
	}
	
	public void addCommand(PaintCommand command){
		this.commands.add(command);
		//System.out.println(this.commands);
		command.addObserver(this);
		this.setChanged();
		this.notifyObservers();
	}
	
	private ArrayList<PaintCommand> commands = new ArrayList<PaintCommand>();

	public void executeAll(GraphicsContext g) {
		for(PaintCommand c: this.commands){
			c.execute(g);
		}
	}
	
	/**
	 * We Observe our model components, the PaintCommands
	 */
	@Override
	public void update(Observable o, Object arg) {
		this.setChanged();
		this.notifyObservers();
	}
}
