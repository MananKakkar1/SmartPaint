package ca.utoronto.utm.paint;

import javafx.scene.paint.Color;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Parse a file in Version 1.0 PaintSaveFile format. An instance of this class
 * understands the paint save file format, storing information about
 * its effort to parse a file. After a successful parse, an instance
 * will have an ArrayList of PaintCommand suitable for rendering.
 * If there is an error in the parse, the instance stores information
 * about the error. For more on the format of Version 1.0 of the paint 
 * save file format, see the associated documentation.
 * 
 * @author 
 *
 */
public class PaintFileParser {
	private int lineNumber = 0; // the current line being parsed
	private String errorMessage =""; // error encountered during parse
	private PaintModel paintModel; 
	
	/**
	 * Below are Patterns used in parsing 
	 */
	private Integer radius;
	private Point centre;
	private Color color;
	private Boolean filled;
	private Point point;
	private Point p1;
	private Point p2;
	private int state=0;

	private Pattern pColor = Pattern.compile("^color:\\d+,\\d+,\\d+$");
	private Pattern pFilled = Pattern.compile("^filled:(true|false)$");
	private Pattern pCenter = Pattern.compile("^center:\\((-?\\d+),(-?\\d+)\\)$");
	private Pattern pRadius = Pattern.compile("^radius:(\\d+)$");
	private Pattern pPointLabel = Pattern.compile("^points$");
	private Pattern pPoint = Pattern.compile("^point:\\((-?\\d+),(-?\\d+)\\)$");
	private Pattern pEndPointLabel = Pattern.compile("^endpoints$");
	private Pattern pP1 = Pattern.compile("^p1:\\((-?\\d+),(-?\\d+)\\)$");
	private Pattern pP2 = Pattern.compile("^p2:\\((-?\\d+),(-?\\d+)\\)$");
	private Pattern pFileStart=Pattern.compile("^PaintSaveFileVersion1.0$");
	private Pattern pFileEnd=Pattern.compile("^EndPaintSaveFile$");
	private Pattern pCircleStart=Pattern.compile("^Circle$");
	private Pattern pCircleEnd=Pattern.compile("^EndCircle$");
	private Pattern pRectangleStart = Pattern.compile("^Rectangle$");
	private Pattern pRectangleEnd=Pattern.compile("^EndRectangle$");
	private Pattern pPolylineStart = Pattern.compile("^Polyline$");
	private Pattern pPolylineEnd=Pattern.compile("^EndPolyline$");
	private Pattern pSquiggleStart = Pattern.compile("^Squiggle$");
	private Pattern pSquiggleEnd=Pattern.compile("^EndSquiggle$");


	// ADD MORE!!
	
	/**
	 * Store an appropriate error message in this, including 
	 * lineNumber where the error occurred.
	 * @param mesg
	 */
	private void error(String mesg){
		this.errorMessage = "Error in line "+lineNumber+" "+mesg;
	}
	
	/**
	 * 
	 * @return the error message resulting from an unsuccessful parse
	 */
	public String getErrorMessage(){
		return this.errorMessage;
	}

	/**
	 * Parse the specified file
	 * @param fileName
	 * @return
	 */
	public boolean parse(String fileName){
		boolean retVal = false;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			PaintModel pm = new PaintModel();
			retVal = this.parse(br, pm);
		} catch (FileNotFoundException e) {
			error("File Not Found: "+fileName);
		} finally {
			try { br.close(); } catch (Exception e){};
		}
		return retVal;
	}

	/**
	 * Parse the specified inputStream as a Paint Save File Format file.
	 * @param inputStream
	 * @return
	 */
	public boolean parse(BufferedReader inputStream){
		PaintModel pm = new PaintModel();
		return this.parse(inputStream, pm);
	}

	/**
	 * Parse the inputStream as a Paint Save File Format file.
	 * The result of the parse is stored as an ArrayList of Paint command.
	 * If the parse was not successful, this.errorMessage is appropriately
	 * set, with a useful error message.
	 * 
	 * @param inputStream the open file to parse
	 * @param paintModel the paint model to add the commands to
	 * @return whether the complete file was successfully parsed
	 */
	public boolean parse(BufferedReader inputStream, PaintModel paintModel) {
		this.paintModel = paintModel;
		this.errorMessage="";
		this.lineNumber=0;
		this.state=0;
		
		// During the parse, we will be building one of the 
		// following commands. As we parse the file, we modify 
		// the appropriate command.
		
		CircleCommand circleCommand = null; 
		RectangleCommand rectangleCommand = null;
		SquiggleCommand squiggleCommand = null;
		PolylineCommand polylineCommand = null;
		String l = "";

		try {	
			Matcher m;
			while ((l = inputStream.readLine()) != null) {
				if (l.isEmpty()) {continue;}
                l = l.replaceAll("\\s+", "");
                this.lineNumber++;
				System.out.println(lineNumber+" "+l+" "+state);
				switch(state){
					case 0:
						m = pFileStart.matcher(l);
						if (m.matches()) {
							state = 1;
							break;
						}
						error("Expected Start of Paint Save File");
						return false;
					case 1:
						m = pFileEnd.matcher(l);
						if (m.matches()) {
							state = 25;
							break;
						}
						m = pCircleStart.matcher(l);
						if (m.matches()) {
							state = 2;
							break;
						}
						m = pSquiggleStart.matcher(l);
						if (m.matches()) {
							squiggleCommand = new SquiggleCommand();
							state = 3;
							break;
						}
						m = pRectangleStart.matcher(l);
						if (m.matches()) {
							state = 4;
							break;
						}
						m = pPolylineStart.matcher(l);
						if (m.matches()) {
							polylineCommand = new PolylineCommand();
							state = 5;
							break;
						}
						error("Expected Start of Shape or End Paint Save File");
						return false;
					case 2:
						m = pColor.matcher(l);
						if (m.matches()) {
							String[] rgbValues = l.substring(6).split(",");

							int red = Integer.parseInt(rgbValues[0].trim());
							int green = Integer.parseInt(rgbValues[1].trim());
							int blue = Integer.parseInt(rgbValues[2].trim());

							if (red > 255 || green > 255 || blue > 255) {
								error("Invalid Color");
								return false;
							}
							this.color = Color.rgb(red, green, blue);
							state = 6;
							break;
						}
						error("Expected Circle color");
						return false;
					case 3:
						m = pColor.matcher(l);
						if (m.matches()) {
							String[] rgbValues = l.substring(6).split(",");

							int red = Integer.parseInt(rgbValues[0].trim());
							int green = Integer.parseInt(rgbValues[1].trim());
							int blue = Integer.parseInt(rgbValues[2].trim());

							if (red > 255 || green > 255 || blue > 255) {
								error("Invalid Color");
								return false;
							}
							this.color = Color.rgb(red, green, blue);
							state = 7;
							break;
						}
						error("Expected Squiggle color");
						return false;
					case 4:
						m = pColor.matcher(l);
						if (m.matches()) {
							String[] rgbValues = l.substring(6).split(",");

							int red = Integer.parseInt(rgbValues[0].trim());
							int green = Integer.parseInt(rgbValues[1].trim());
							int blue = Integer.parseInt(rgbValues[2].trim());

							if (red > 255 || green > 255 || blue > 255) {
								error("Invalid Color");
								return false;
							}
							this.color = Color.rgb(red, green, blue);
							state = 8;
							break;
						}
						error("Expected Rectangle color");
						return false;
					case 5:
						m = pColor.matcher(l);
						if (m.matches()) {
							String[] rgbValues = l.substring(6).split(",");

							int red = Integer.parseInt(rgbValues[0].trim());
							int green = Integer.parseInt(rgbValues[1].trim());
							int blue = Integer.parseInt(rgbValues[2].trim());

							if (red > 255 || green > 255 || blue > 255) {
								error("Invalid Color");
								return false;
							}
							this.color = Color.rgb(red, green, blue);
							state = 9;
							break;
						}
						error("Expected Polyline color");
						return false;
					case 6:
						m = pFilled.matcher(l);
						if (m.matches()) {
							this.filled = Boolean.parseBoolean(m.group(1));
							state = 10;
							break;
						}
						error("Expected Circle filled");
						return false;
					case 7:
						m = pFilled.matcher(l);
						if (m.matches()) {
							this.filled = Boolean.parseBoolean(m.group(1));
							state = 11;
							break;
						}
						error("Expected Squiggle filled");
						return false;
					case 8:
						m = pFilled.matcher(l);
						if (m.matches()) {
							this.filled = Boolean.parseBoolean(m.group(1));
							state = 12;
							break;
						}
						error("Expected Rectangle filled");
						return false;
					case 9:
						m = pFilled.matcher(l);
						if (m.matches()) {
							this.filled = Boolean.parseBoolean(m.group(1));
							state = 13;
							break;
						}
						error("Expected Polyline filled");
						return false;
					case 10:
						m = pCenter.matcher(l);
						if (m.matches()) {
							int x = Integer.parseInt(m.group(1));
							int y = Integer.parseInt(m.group(2));
							this.centre = new Point(x, y);
							state = 14;
							break;
						}
						error("Expected Circle center");
						return false;
					case 11:
						m = pPointLabel.matcher(l);
						if (m.matches()) {
							state = 15;
							break;
						}
						error("Expected Squiggle points");
						return false;
					case 12:
						m = pP1.matcher(l);
						if (m.matches()) {
							this.p1 = new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
							state = 16;
							break;
						}
						error("Expected Rectangle p1");
						return false;
					case 13:
						m = pPointLabel.matcher(l);
						if (m.matches()) {
							state = 17;
							break;
						}
						error("Expected Polyline points");
						return false;
					case 14:
						m = pRadius.matcher(l);
						if (m.matches()) {
							this.radius = Integer.parseInt(m.group(1));
							if (this.radius < 0) {return false;}
							state = 18;
							break;
						}
						error("Expected Circle Radius");
						return false;
					case 15:
						m = pPoint.matcher(l);
						if (m.matches()) {
							int x = Integer.parseInt(m.group(1));
							int y = Integer.parseInt(m.group(2));
							this.point = new Point(x, y);
							squiggleCommand.add(this.point);
							state = 19;
							break;
						}
						m = pEndPointLabel.matcher(l);
						if (m.matches()) {
							state = 22;
							break;
						}
						error("Expected Squiggle point or end points");
						return false;
					case 16:
						m = pP2.matcher(l);
						if (m.matches()) {
							this.p2 = new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
							state = 20;
							break;
						}
						error("Expected Rectangle p2");
						return false;
					case 17:
						m = pPoint.matcher(l);
						if (m.matches()) {
							int x = Integer.parseInt(m.group(1));
							int y = Integer.parseInt(m.group(2));
							this.point = new Point(x, y);
							polylineCommand.add(this.point);
							state = 21;
							break;
						}
						m = pEndPointLabel.matcher(l);
						if (m.matches()) {
							state = 23;
							break;
						}
						error("Expected Polyline point or end points");
						return false;
					case 18:
						m = pCircleEnd.matcher(l);
						if (m.matches()) {
							circleCommand = new CircleCommand(this.centre, this.radius);
							circleCommand.setColor(this.color);
							circleCommand.setFill(this.filled);
							this.paintModel.addCommand(circleCommand);
							state = 24;
							break;
						}
						error("Expected End Circle");
						return false;
					case 19:
						m = pEndPointLabel.matcher(l);
						if (m.matches()) {
							state = 22;
							break;
						}
						m = pPoint.matcher(l);
						if (m.matches()) {
							int x = Integer.parseInt(m.group(1));
							int y = Integer.parseInt(m.group(2));
							this.point = new Point(x, y);
							squiggleCommand.add(this.point);
							state = 19;
							break;
						}
						error("Expected Squiggle point or end points");
						return false;
					case 20:
						m = pRectangleEnd.matcher(l);
						if (m.matches()) {
							if (this.p1 != null && this.p2 != null) {
								rectangleCommand = new RectangleCommand(this.p1, this.p2);
								rectangleCommand.setColor(this.color);
								rectangleCommand.setFill(this.filled);
								this.paintModel.addCommand(rectangleCommand);
							}
							state = 24;
							break;
						}
						error("Expected End Rectangle");
						return false;
					case 21:
						m = pEndPointLabel.matcher(l);
						if (m.matches()) {
							state = 23;
							break;
						}
						m = pPoint.matcher(l);
						if (m.matches()) {
							int x = Integer.parseInt(m.group(1));
							int y = Integer.parseInt(m.group(2));
							this.point = new Point(x, y);
							polylineCommand.add(this.point);
							state = 21;
							break;
						}
						error("Expected Polyline point or end points");
						return false;
					case 22:
						m = pSquiggleEnd.matcher(l);
						if (m.matches()) {
							squiggleCommand.setColor(this.color);
							squiggleCommand.setFill(this.filled);
							this.paintModel.addCommand(squiggleCommand);
							state = 24;
							break;
						}
						error("Expected End Squiggle");
						return false;
					case 23:
						m = pPolylineEnd.matcher(l);
						if (m.matches()) {
							polylineCommand.setColor(this.color);
							polylineCommand.setFill(this.filled);
							this.paintModel.addCommand(polylineCommand);
							state = 24;
							break;
						}
						error("Expected End Polyline");
						return false;
					case 24:
						m = pFileEnd.matcher(l);
						if (m.matches()) {
							this.color = null;
							this.point = null;
							this.p1 = null;
							this.p2 = null;
							this.centre = null;
							this.radius = null;
							state = 25;
							break;
						}
						m = pCircleStart.matcher(l);
						if (m.matches()) {
							state = 2;
							break;
						}
						m = pSquiggleStart.matcher(l);
						if (m.matches()) {
							squiggleCommand = new SquiggleCommand();
							state = 3;
							break;
						}
						m = pRectangleStart.matcher(l);
						if (m.matches()) {
							state = 4;
							break;
						}
						m = pPolylineStart.matcher(l);
						if (m.matches()) {
							polylineCommand = new PolylineCommand();
							state = 5;
							break;
						}
						error("Extra content after End of File");
						return false;
					case 25:
						error("Extra content after End of File");
						return false;


					// ...
					/**
					 * I have around 20+/-5 cases in my FSM. If you have too many
					 * more or less, you are doing something wrong. Too few, and I bet I can find
					 * a bad file that you will say is good. Too many and you are not capturing the right concepts.
					 *
					 * Here are the errors I catch. All of these should be in your code.
					 *
					 	error("Expected Start of Paint Save File");
						error("Expected Start of Shape or End Paint Save File");
						error("Expected Circle color");
						error("Expected Circle filled");
						error("Expected Circle center");
						error("Expected Circle Radius");
						error("Expected End Circle");
						error("Expected Rectangle color");
						error("Expected Rectangle filled");
						error("Expected Rectangle p1");
						error("Expected Rectangle p2");
						error("Expected End Rectangle");
						error("Expected Squiggle color");
						error("Expected Squiggle filled");
						error("Expected Squiggle points");
						error("Expected Squiggle point or end points");
						error("Expected End Squiggle");
						error("Expected Polyline color");
						error("Expected Polyline filled");
						error("Expected Polyline points");
						error("Expected Polyline point or end points");
						error("Expected End Polyline");
						error("Extra content after End of File");
						error("Unexpected end of file");
					 */
				}
			}
		}  catch (Exception e){
			System.out.println("Error in line "+lineNumber+" "+e.getMessage());
		}
		if (state==25) {
			return true;
		}
		else {
			error("Unexpected end of file");
			this.paintModel.reset();
			return false;
		}
	}
}
