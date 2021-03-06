package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;

import core.physics.Point;
import core.physics.Vector;



public class AdvancedGraphics {

	Graphics myPen;
	
	int camX;
	int camY;
	
	public Graphics getBasicPen(){
		return myPen;
	}

	public AdvancedGraphics(Graphics pen){
		
		System.out.println("New AdvancedGraphics object created.");
		myPen = pen;

		camX = 0;
		camY = 0;
	}
	//--------------------------------------

	
	public void setBasicGraphics(Graphics pen){
		myPen = pen;
	}
	
	//-----------------------------------------
	//Note: The position that the camera is set to represents the top-right corner of
	//the screen. A camera position of (0,0) indicates that the top right corner of the view lines up 
	//with the points (0,0).
	
	public int getCamX(){
		return camX;
	}
	
	public int getCamY(){
		return camY;
	}

	public void moveCameraPosition(int x, int y){
		//resetCameraPosition();
		
		camX = x;
		camY = y;
		
		translate(-1*camX, -1*camY);
		
		
	}

	
	
	//Given (x,y) coordinate, an angle, a number of sides, and a radius, an ArrayList<Point> is generated that
	//contains all the points of the regular polygon with the attributes specified in the parameters.
	private ArrayList<Point> getRegularPolygonList(int x, int y, int angle, int sides, int size){
		
		ArrayList<Point> pointList = new ArrayList<Point>();
		ArrayList<Vector> vectorList = new ArrayList<Vector>();
		
		for (int s = 0; s< sides; s++){
			pointList.add(new Point(0,0));
			vectorList.add(new Vector(0,0));
		}
		
		for (int s = 0; s< sides; s++){
			pointList.set(s, new Point(x, y));
			
			double tempAngleDegrees = (angle + ((360.0/sides)*s));
			double tempAngleRad = Math.toRadians(tempAngleDegrees);
			
			double newX = (size/2.0) * Math.cos(tempAngleRad);
			double newY = (size/2.0) * Math.sin(tempAngleRad);
			
			vectorList.set(s, new Vector(newX, newY));
			
			pointList.get(s).move(vectorList.get(s));
		}
		return pointList;
	}
	
	//Given (x,y) coordinate, an angle, a number of "points", and a radius, an ArrayList<Point> is generated that
	//contains all the points of the starred polygon with the attributes specified in the parameters.
	public ArrayList<Point> getStarredPolygonList(int x, int y, int angle, int nPoints, int size){
		
		ArrayList<Point> pointyPoints = new ArrayList<Point>();
		ArrayList<Point> concavePoints = new ArrayList<Point>();
		
		ArrayList<Point> allPoints = new ArrayList<Point>();
		
		ArrayList<Vector> pointyVectors = new ArrayList<Vector>();
		ArrayList<Vector> concaveVectors = new ArrayList<Vector>();
		
		for (int s = 0; s< nPoints; s++){
			pointyPoints.add(new Point(0,0));
			concavePoints.add(new Point(0,0));
			pointyVectors.add(new Vector(0,0));
			concaveVectors.add(new Vector(0,0));
		}
		
		for (int s = 0; s<nPoints; s++){
			pointyPoints.set(s, new Point(x,y));
			double tempAngleDegrees = (angle + ((360.0/nPoints)*s));
			double tempAngleRad = Math.toRadians(tempAngleDegrees);
			
			double newX = (size/2.0) * Math.cos(tempAngleRad);
			double newY = (size/2.0) * Math.sin(tempAngleRad);
			
			pointyVectors.set(s, new Vector(newX, newY));
			pointyPoints.get(s).move(pointyVectors.get(s));
		}
		
		for (int s = 0; s<nPoints; s++){
			concavePoints.set(s, new Point(x,y));
			double tempAngleDegrees = ((180.0/nPoints)+ angle + ((360.0/nPoints)*s));
			double tempAngleRad = Math.toRadians(tempAngleDegrees);
			
			double newX = (size/4.0) * Math.cos(tempAngleRad);
			double newY = (size/4.0) * Math.sin(tempAngleRad);
			
			concaveVectors.set(s, new Vector(newX, newY));
			concavePoints.get(s).move(concaveVectors.get(s));
		}
		
		for (int s=0; s<nPoints; s++){
			allPoints.add(pointyPoints.get(s));
			allPoints.add(concavePoints.get(s));
		}
		
		return allPoints;
	}
	
	public void drawVector(Point start, Vector vec){
		
		Point moved = new Point(start.getX(), start.getY());
		moved.move(vec);
		
		drawLine(start, moved);
	}
	
	//Redirects to the default java Graphics drawPolygon method
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		myPen.drawPolygon(xPoints, yPoints, nPoints);
		
	}
	
	public void drawPolygon(ArrayList<Point> pointList){
		int nPoints = pointList.size();

		int[] xPoints = new int[nPoints];
		int[] yPoints = new int[nPoints];
		
		for (Point p: pointList){
			xPoints[pointList.indexOf(p)] = p.getIntX();
			yPoints[pointList.indexOf(p)] = p.getIntY();
		}
		
		drawPolygon(xPoints, yPoints, nPoints);
	}
	
	//Draws a regular polygon based on the given parameters
	public void drawRegularPolygon(int x, int y, int angle, int sides, int size){
		ArrayList<Point> pointList = getRegularPolygonList(x, y, angle, sides, size);
		int[] xPoints = new int[pointList.size()];
		int[] yPoints = new int[pointList.size()];
		
		for (int i=0; i<pointList.size(); i++){
			xPoints[i] = (int)pointList.get(i).getX();
			yPoints[i] = (int)pointList.get(i).getY();
		}
		
		int nPoints = pointList.size();
		
		myPen.drawPolygon(xPoints, yPoints, nPoints);
		
		
	}
	
	//draws a starred polygon based on the given parameters
	public void drawStarredPolygon(int x, int y, int angle, int nPoints, int size){
		ArrayList<Point> pointList = getStarredPolygonList(x, y, angle, nPoints, size);
		int[] xPoints = new int[pointList.size()];
		int[] yPoints = new int[pointList.size()];
		
		for (int i=0; i<pointList.size(); i++){
			xPoints[i] = (int)pointList.get(i).getX();
			yPoints[i] = (int)pointList.get(i).getY();
		}
		
		int listSize = pointList.size();
		
		myPen.drawPolygon(xPoints, yPoints, listSize);
	}
	
	
	public void fillStarredPolygon(int x, int y, int angle, int nPoints, int size){
		ArrayList<Point> pointList = getStarredPolygonList(x, y, angle, nPoints, size);
		int[] xPoints = new int[pointList.size()];
		int[] yPoints = new int[pointList.size()];
		
		for (int i=0; i<pointList.size(); i++){
			xPoints[i] = (int)pointList.get(i).getX();
			yPoints[i] = (int)pointList.get(i).getY();
		}
		
		int listSize = pointList.size();
		
		myPen.fillPolygon(xPoints, yPoints, listSize);
	}
	
	
	//The following are just drawPolygon, except with specified values for the number of sides parameter.
	public void drawTriangle(int x, int y, int angle, int size){
		drawRegularPolygon(x, y, angle, 3, size);
	}
	
	public void drawSquare(int x, int y, int angle, int size){
		drawRegularPolygon(x, y, angle, 4, size);
	}
	
	public void drawPentagon(int x, int y, int angle, int size){
		drawRegularPolygon(x, y, angle, 5, size);
	}
	
	public void drawHexagon(int x, int y, int angle, int size){
		drawRegularPolygon(x, y, angle, 6, size);
	}
	
	public void drawSeptagon(int x, int y, int angle, int size){
		drawRegularPolygon(x, y, angle, 7, size);
	}
	
	public void drawOctagon(int x, int y, int angle, int size){
		drawRegularPolygon(x, y, angle, 8, size);
	}
	
	//Fills a regular polygon based on the given paramters.
	public void fillRegularPolygon(int x, int y, int angle, int sides, int size){
		ArrayList<Point> pointList = getRegularPolygonList(x, y, angle, sides, size);
		int[] xPoints = new int[pointList.size()];
		int[] yPoints = new int[pointList.size()];
		
		for (int i=0; i<pointList.size(); i++){
			xPoints[i] = (int)pointList.get(i).getX();
			yPoints[i] = (int)pointList.get(i).getY();
		}
		
		int nPoints = pointList.size();
		
		myPen.fillPolygon(xPoints, yPoints, nPoints);
	}
	
	//The following are just redirects to fillPolygon, but with sepcified numbers of sides.
	public void fillTriangle(int x, int y, int angle, int size){
		fillRegularPolygon(x,y,angle, 3, size);
	}
	
	public void fillSquare(int x, int y, int angle, int size){
		fillRegularPolygon(x,y,angle, 4, size);
	}
	
	public void fillPentagon(int x, int y, int angle, int size){
		fillRegularPolygon(x,y,angle, 5, size);
	}
	
	public void fillHexagon(int x, int y, int angle, int size){
		fillRegularPolygon(x,y,angle, 6, size);
	}
	
	public void fillSeptagon(int x, int y, int angle, int size){
		fillRegularPolygon(x,y,angle, 7, size);
	}
	
	public void fillOctagon(int x, int y, int angle, int size){
		fillRegularPolygon(x,y,angle, 8, size);
	}
	//------------------------------------------------

	
	public void clearRect(int x, int y, int width, int height) {
		myPen.clearRect(x, y, width, height);
		
	}

	public void clipRect(int x, int y, int width, int height) {
		myPen.clipRect(x, y, width, height);
		
	}

	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		myPen.copyArea(x, y, width, height, dx, dy);
		
	}

	public Graphics create() {
		myPen.create();
		return null;
	}

	
	public void dispose() {
		myPen.dispose();
		
	}

	
	public void drawArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		myPen.drawArc(x, y, width, height, startAngle, arcAngle);
		
	}

	
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		myPen.drawImage(img, x, y, observer);
		return false;
	}

	
	public boolean drawImage(Image img, int x, int y, Color bgcolor,
			ImageObserver observer) {
		myPen.drawImage(img, x, y, bgcolor, observer);
		return false;
	}

	
	public boolean drawImage(Image img, int x, int y, int width, int height,
			ImageObserver observer) {
		myPen.drawImage(img, x, y, width, height, observer);
		return false;
	}
	
	public boolean drawImage(Image img, int x, int y, int width, int height){
		drawImage(img, x, y, width, height, null);
		return false;
	}
	
	public void drawImage(Image img, Point point, int width, int height){
		drawImage(img, (int)point.getX(), (int)point.getY(), width, height);
	}
	
	public void drawImage(Image img, Point point){
		drawImage(img, point, img.getWidth(null), img.getHeight(null));
	}
	
	public void drawImage(Image img, int x, int y){
		drawImage(img, x, y, img.getWidth(null), img.getHeight(null));
	}

	
	public boolean drawImage(Image img, int x, int y, int width, int height,
			Color bgcolor, ImageObserver observer) {
		myPen.drawImage(img, x, y, width, height, bgcolor, observer);
		return false;
	}

	
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
		myPen.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
		return false;
	}

	
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2, Color bgcolor,
			ImageObserver observer) {
		myPen.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
		return false;
	}
	
	public void drawMirroredImage(Image img, Point point, int width, int height, boolean xFlip, boolean yFlip){

		Image mirroredImg = getMirroredImage(img, xFlip, yFlip);
		drawImage(mirroredImg, (int)point.getX(), (int)point.getY(), width, height, null);
	}
	
	public void drawMirroredImage(Image img, int x, int y, int width, int height, boolean xFlip, boolean yFlip){
		drawMirroredImage(img, new Point(x,y), width, height, xFlip, yFlip);
	}
	
	public void drawMirroredImage(Image img, int x, int y, boolean xFlip, boolean yFlip){
		drawMirroredImage(img, new Point(x,y), img.getWidth(null), img.getHeight(null), xFlip, yFlip);
	}
	
	public Image getMirroredImage(Image img, boolean xFlip, boolean yFlip){

		// Flip the image horizontally
		AffineTransform tx;
		
		if ((xFlip)&&(!yFlip)){
			tx= AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-img.getWidth(null), 0);
		}
		else if ((!xFlip)&&(yFlip)){
			tx= AffineTransform.getScaleInstance(1, -1);
			tx.translate(0, -img.getHeight(null));
		}
		else if ((xFlip)&&(yFlip)){
			tx= AffineTransform.getScaleInstance(-1, -1);
			tx.translate(-img.getWidth(null), -img.getHeight(null));
		}
		else{
			return img;
		}
		
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		img = op.filter((BufferedImage) img, null);
		return img;
	}

	public void drawRotatedImage(BufferedImage image, Point point, int rotX, int rotY, int rotation){
		
		double rotationRequired = Math.toRadians(rotation);
		//rotation about the center of the image
		
		//
		//AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, rotX, rotY);
		//AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

		BufferedImage rotated = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
		
		AffineTransform transform = new AffineTransform();
		transform.rotate(rotationRequired, rotX, rotY);
		
		double offset = (image.getWidth()-image.getHeight())/2;
		transform.translate(offset,offset);

		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		op.filter(image, rotated);
		
		//BufferedImage rotated = op.createCompatibleDestImage(image, image.getColorModel());
		//rotated = op.filter((BufferedImage) image, rotated);
		
		drawImage(rotated, point, rotated.getWidth(), rotated.getHeight());
	}
	
	//rotates about center by default
	public void drawRotatedImage(BufferedImage image, Point point, int rotation){
		
		drawRotatedImage(image, point,  image.getWidth(null)/2, image.getHeight(null)/2, rotation);
	}
	
	
	public void drawLine(int x1, int y1, int x2, int y2) {
		myPen.drawLine(x1, y1, x2, y2);
		
	}
	
	public void drawLine(Point p1, Point p2) {
		myPen.drawLine(p1.getIntX(), p1.getIntY(), p2.getIntX(), p2.getIntY());
	}
	
	public void drawLine(Point p1, Point p2, int thickness){
		for (int i=(-1*thickness/2); i< (thickness/2); i++){
			drawLine(p1.getIntX()+i, p1.getIntY(), p2.getIntX()+i, p2.getIntY());
		}
	}

	
	public void drawOval(int x, int y, int width, int height) {
		myPen.drawOval(x, y, width, height);
		
	}
	
	public void drawCircle(int x, int y, int radius){
		myPen.drawOval(x,y, radius, radius);
	}
	
	public void drawCenteredCircle(int x, int y, int radius){
		drawCircle(x-radius/2, y-radius/2, radius);
	}
	
	public void fillCircle(int x, int y, int radius){
		myPen.fillOval(x,y, radius, radius);
	}

	

	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		myPen.drawPolyline(xPoints, yPoints, nPoints);
		
	}

	
	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		myPen.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
		
	}
	
	public void drawRect(int x, int y, int width, int height){
		myPen.drawRect(x,y,width, height);
	}
	
	
	public void drawString(String str, int x, int y) {
		myPen.drawString(str, x, y);
		
	}
		
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		myPen.drawString(iterator, x, y);
		
	}
	
	public void drawCenteredString(String str, int x, int y, Font font){
		
		FontMetrics metrics = myPen.getFontMetrics(font);
		int drawX = x- metrics.stringWidth(str)/2;
		int drawY = (y - metrics.getHeight()/2) + metrics.getAscent();
		
		myPen.setFont(font);
		drawString(str, drawX, drawY);
	}
	
	public void drawXCenteredString(String str, int x, int y, Font font){
		
		FontMetrics metrics = myPen.getFontMetrics(font);
		int drawX = x - metrics.stringWidth(str)/2;
		int drawY = y;
		
		myPen.setFont(font);
		drawString(str, drawX, drawY);
		
	}
	
	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		myPen.fillArc(x, y, width, height, startAngle, arcAngle);
		
	}

	
	public void fillOval(int x, int y, int width, int height) {
		myPen.fillOval(x, y, width, height);
		
	}

	
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		myPen.fillPolygon(xPoints, yPoints, nPoints);
		
	}
	
	public void fillPolygon(ArrayList<Point> pointList){
		int nPoints = pointList.size();

		int[] xPoints = new int[nPoints];
		int[] yPoints = new int[nPoints];
		
		for (Point p: pointList){
			xPoints[pointList.indexOf(p)] = p.getIntX();
			yPoints[pointList.indexOf(p)] = p.getIntY();
		}
		
		fillPolygon(xPoints, yPoints, nPoints);
	}

	
	public void fillRect(int x, int y, int width, int height) {
		myPen.fillRect(x, y, width, height);
		
	}

	
	public void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		myPen.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
		
	}

	
	public Shape getClip() {
		myPen.getClip();
		return null;
	}

	
	public Rectangle getClipBounds() {
		myPen.getClipBounds();
		return null;
	}

	
	public Color getColor() {
		myPen.getColor();
		return null;
	}

	
	public Font getFont() {
		myPen.getFont();
		return null;
	}

	
	public FontMetrics getFontMetrics(Font f) {
		myPen.getFontMetrics(f);
		return null;
	}

	
	public void setClip(Shape clip) {
		myPen.setClip(clip);
		
	}

	
	public void setClip(int x, int y, int width, int height) {
		myPen.setClip(x, y, width, height);
		
	}

	
	public void setColor(Color c) {
		myPen.setColor(c);
	}
	
	public void setColor(int r, int g, int b){
		setColor(new Color(r,g,b));
	}

	
	public void setFont(Font font) {
		myPen.setFont(font);
	}

	
	public void setPaintMode() {
		myPen.setPaintMode();
	}

	
	public void setXORMode(Color c1) {
		myPen.setXORMode(c1);
		
	}

	public void translate(int x, int y) {
		myPen.translate(x, y);
		
	}
	
	

}
