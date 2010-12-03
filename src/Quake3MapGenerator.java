/*
    Name:	Quake3MapGenerator
	Author: José Antonio Robles Ordóñez
	Email: 	robles.mediterraneo(at)gmail.com
	Date: 	2010/11/26

	This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    ****************************************************************
    *
    *
    *     Confirmación de funcionamiento del repositorio:
    *     
    *     José Antonio Robles Ordóñez.
    *     
    *     José Luis Lebrón Lozano.
    *     
    *     Adrián Pérez Heredia.
    *     
    *     Manuel Traverso Oncina.
    *     
    *     
    ****************************************************************
*/

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.vecmath.*;

class Point extends Vector3f{
	private static final long serialVersionUID = 1L;
	
	public Point(float X, float Y, float Z){
		x = X;
		y = Y;
		z = Z;
	}
	
	public Point(int X, int Y, int Z){
		x = (float)X;
		y = (float)Y;
		z = (float)Z;
	}
	
	public Point(Point P){
		x = P.x;
		y = P.y;
		z = P.z;
	}

	public void scale(float X, float Y, float Z){
		x *= X;
		y *= Y;
		z *= Z;
	}
	
	public void print(PrintWriter OUT){
		
		OUT.printf("( %d %d %d )", Math.round(x), Math.round(y), Math.round(z));
		
	}
	
}


class Plane{
	public Point p0;
	public Point p1;
	public Point p2;
	
	public String texture;
	
	public int sShift;
	public int tShift;
	public int rotation;
	public double sScale;
	public double tScale;
	
	public int xx1;
	public int xx2;
	public int xx3;

	public Plane(){
		texture = "NULL";
		
		sShift = tShift = rotation = 0;
		sScale = tScale = 0.5;
		xx1 = xx2 = xx3 = 0;
	}
	
	public Plane(String TEXTURE){
		texture = TEXTURE;
		
		sShift = tShift = rotation = 0;
		sScale = tScale = 0.5;
		xx1 = xx2 = xx3 = 0;
	}
	
	public Plane(Point P0, Point P1, Point P2){
		p0 = P0; p1 = P1; p2 = P2; texture = "NULL";
		
		sShift = tShift = rotation = 0;
		sScale = tScale = 0.5;
		xx1 = xx2 = xx3 = 0;
	}
	
	public Plane(Point P0, Point P1, Point P2, String TEXTURE){
		p0 = P0; p1 = P1; p2 = P2; texture = TEXTURE;
		
		sShift = tShift = rotation = 0;
		sScale = tScale = 0.5;
		xx1 = xx2 = xx3 = 0;
	}
	
	public void scale(float X, float Y, float Z){		
		p0.scale(X,Y,Z);
		p1.scale(X,Y,Z);
		p2.scale(X,Y,Z);
	}
	
	public void translate(float X, float Y, float Z){
		Vector3f dummy = new Vector3f(X,Y,Z);
		
		p0.add(dummy);
		p1.add(dummy);
		p2.add(dummy);
	}
	
	// xy
	static Plane top(float Z){
		// ( 1 1 1 ) ( 1 -1 1 ) ( -1 1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		return new Plane(new Point(1,1,Z), new Point(1,-1,Z), new Point(-1,1,Z));
		
	}
	
	// xz
	static Plane back(float Y){
		// ( 1 1 1 ) ( -1 1 1 ) ( 1 1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0

		return new Plane( new Point(1,Y,1), new Point(-1,Y,1), new Point(1,Y,-1));
		
	}
		
	// yz
	static Plane right(float X){
		// ( 1 1 1 ) ( 1 1 -1 ) ( 1 -1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		return new Plane( new Point(X,1,1), new Point(X,1,-1), new Point(X,-1,1));
		
	}
	
	// xy
	static Plane bottom(float Z){
		// ( -1 -1 -1 ) ( 1 -1 -1 ) ( -1 1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		return new Plane( new Point(-1,-1,Z), new Point(1,-1,Z), new Point(-1,1,Z));
	}
	
	// xz
	static Plane front(float Y){
		// ( -1 -1 -1 ) ( -1 -1 1 ) ( 1 -1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		return new Plane( new Point(-1,Y,-1),new Point(-1,Y,1),new Point(1,Y,-1));
	}
	
	// yz
	static Plane left(float X){
		// 	( -1 -1 -1 ) ( -1 1 -1 ) ( -1 -1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		return new Plane( new Point(X,-1,-1),new Point(X,1,-1),new Point(X,-1,1));
	}
	
	public void print(PrintWriter OUT){
		
		//OUT.print(' ');
		p0.print(OUT);
		
		OUT.print(' ');
		p1.print(OUT);
		
		OUT.print(' ');
		p2.print(OUT);
		
		OUT.print( " " + texture );
		
		OUT.printf(" %d %d %d %.1f %.1f %d %d %d", sShift, tShift, rotation, sScale, tScale, xx1, xx2, xx3 );

	}
	
	
}

class Brush{
	private ArrayList<Plane> planes;
	
	public Brush(){
		
		planes = new ArrayList<Plane>();
		
	}
	
	public void print(PrintWriter OUT){
		
		OUT.println('{');
		
		for( Plane p : planes){
			p.print( OUT );
			OUT.print('\n');
		}
		
		OUT.println('}');
	}
	
	public void scale(float X, float Y, float Z){
		
		for(Plane p : planes){
			p.scale( X, Y, Z );
		}
		
	}
	
	public void translate(int X, int Y, int Z){
		
		for(Plane p : planes){
			p.translate(X, Y, Z);
		}
	}
	
	public void addPlane(Plane P){
		planes.add(P);
	}
	
	public static Brush cube(float LEFT, float RIGHT, float FRONT, float BACK, float BOTTOM, float TOP, String TEXTURE){
		
		Brush b;
		
		b = new Brush();

		b.addPlane( Plane.top( TOP ) );
		b.addPlane( Plane.back( BACK ) );
		b.addPlane( Plane.right( RIGHT ) );
		
		b.addPlane( Plane.bottom( BOTTOM ) );
		b.addPlane( Plane.front( FRONT ) );
		b.addPlane( Plane.left( LEFT ) );
		
		for( Plane p : b.planes )
			p.texture = TEXTURE;
		
		return b;
		
	}
		
}

abstract class Entity{
	public Point origin;
	
	abstract public String className();
	
	public Entity(){
		
		origin = new Point(0.0f,0.0f,0.0f);
		
	}
	
	public void startPrint(PrintWriter OUT){
		OUT.println("{");
	}
	
	public void endPrint(PrintWriter OUT){
		OUT.println("}");
	}
	
	public void print(PrintWriter OUT){
		OUT.printf("\"classname\" \"%s\"\n", className());
		OUT.printf("\"origin\" \"%.6f %.6f %.6f\"\n", origin.x, origin.y, origin.z);
	}

}

class InfoPlayerStart extends Entity{
	
	public String className(){ return "info_player_start"; };
	
	public void print(PrintWriter OUT){
		
		startPrint(OUT);
		super.print(OUT);
		endPrint(OUT);
	}
	
}

class Light extends Entity{
	
	public int intensity;
	
	public Light(){ intensity = 0; };
	
	public String className(){ return "light"; };
	
	public void print(PrintWriter OUT){
		
		startPrint(OUT);
		
		super.print(OUT);
		
		OUT.printf("\"light\" \"%d\"\n", intensity);
		
		endPrint(OUT);
	}
	
}

class Map{
	
	private ArrayList<Brush> brushes;
	private ArrayList<Entity> entities;
	
	public Map(){
		
		brushes = new ArrayList<Brush>();
		entities = new ArrayList<Entity>();
		
	}
	
	public void addBrush(Brush B){
		brushes.add(B);
	}
	
	public void addEntity(Entity E){
		entities.add( E );
	}
	
	public void print(PrintWriter OUT){
		
		int I = 0;
		
		OUT.printf("{\n\"classname\" \"worldspawn\"\n");
		
		for( Brush b : brushes){
			OUT.printf("// brush %d\n", I++);
			b.print( OUT );
		}
		
		OUT.println('}');
		
		// Print the entities
		
		for(Entity e : entities){
			e.print(OUT);
		}
	}

	
	public void addRoom(float WIDTH, float HEIGHT, float DEPTH, Point CENTER, float WALL_WIDTH, String[] TEXTURES){
		// Add a room to the map centered at CENTER
		
		// First find the four corners of the floor
		Point frontLeft, frontRight, backLeft, backRight;
		
		float width2 = WIDTH / 2.0f;
		float height2 = HEIGHT / 2.0f;
		float depth2 = DEPTH / 2.0f;
		
		frontLeft = new Point(CENTER);
		frontLeft.add(new Point(-width2,-depth2,-height2));
		
		frontRight = new Point(CENTER);
		frontRight.add(new Point(width2,-depth2,-height2));
		
		backLeft = new Point(CENTER);
		backLeft.add(new Point(-width2,depth2,-height2));
		
		backRight = new Point(CENTER);
		backRight.add(new Point(width2,depth2,-height2));
		
		// Now build up the brushes using that points
		
		
		
		Brush b;
		
		// First the front wall		
		b = Brush.cube(frontLeft.x-WALL_WIDTH, frontLeft.x, frontLeft.y, backLeft.y, frontLeft.z, frontLeft.z+HEIGHT, TEXTURES[0]);
		addBrush(b);
		
		// Second the back wall
		b = Brush.cube(frontRight.x, frontRight.x+WALL_WIDTH, frontRight.y, backLeft.y, frontLeft.z, frontLeft.z+HEIGHT, TEXTURES[1]);
		addBrush(b);
		
		// Third left wall		
		b = Brush.cube(frontLeft.x, frontRight.x, frontLeft.y-WALL_WIDTH, frontLeft.y, frontLeft.z, frontLeft.z+HEIGHT, TEXTURES[2]);		
		addBrush(b);
		
		// Fourth right wall
		b = Brush.cube(frontLeft.x, frontRight.x, backLeft.y, backLeft.y+WALL_WIDTH, frontLeft.z, frontLeft.z+HEIGHT, TEXTURES[3]);	
		addBrush(b);
		
		// Fifth bottom wall
		b = Brush.cube(frontLeft.x, frontRight.x, frontLeft.y, backLeft.y, frontLeft.z-WALL_WIDTH, frontLeft.z, TEXTURES[4]);
		addBrush(b);
		
		// Finally top wall
		b = Brush.cube(frontLeft.x, frontRight.x, frontLeft.y, backLeft.y, frontLeft.z+HEIGHT, frontLeft.z+HEIGHT+WALL_WIDTH, TEXTURES[5]);
		addBrush(b);
		
		
	}
	
}

public class Quake3MapGenerator {

	public static void main(String[] args) {
		
		Map mapa = new Map();
		
		Brush b;
		
		//b = Brush.cube(-1, 1, -1, 1, -10, 0);		
		//b.scale(1000, 1000, 1);
		
		//mapa.addBrush(b);
		String[] textures = {
				"base_wall/concrete_dark","base_wall/concrete_dark",
				"base_wall/concrete_dark","base_wall/concrete_dark",
				"base_floor/diamond2c","skies/killsky"
		};
		mapa.addRoom(2000, 240, 2000, new Point(0,0,120), 16, textures);
		
		for(int X = -1000; X < 1000; X+=200){
			for( int Y = -1000; Y < 1000; Y+=200){
				
				b = Brush.cube(X, X+50, Y, Y+50, 0, 50, "base_wall/concrete_dark");
				
				mapa.addBrush(b);
			}
		}
		
		// Add some entities
		
		InfoPlayerStart ips = new InfoPlayerStart();
		
		ips.origin.x = 0.0f;
		ips.origin.y = 0.0f;
		ips.origin.z = 60.0f;
		
		mapa.addEntity(ips);
		
		// Add lights
		
		for(int X = -1000; X < 1000; X+=400){
			for( int Y = -1000; Y < 1000; Y+=400){
				
				Light l = new Light();
				
				l.origin.x = X+25;
				l.origin.y = Y+25;
				l.origin.z = 80;
				
				mapa.addEntity(l);
			}
		}
		
		try {
			//FileWriter out = new FileWriter("output.map");
			
			PrintWriter out = new PrintWriter("output.map");
			
			mapa.print(out);
			
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

