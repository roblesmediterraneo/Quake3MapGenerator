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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.media.j3d.Transform3D;
import javax.vecmath.*;

class Point extends Vector3f{
	private static final long serialVersionUID = 1L;
	public static final double deg2rad = Math.PI/180.0;
	
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
	
	public boolean colinear(Point V, float EPSILON){
		
		float dot = dot(V);
		
		if( 1 - Math.abs(dot) < EPSILON ){	
			return true;
		}
		else{
			return false;
		}
	}
	
	public void rotateX(float X){
		
		Transform3D rotation = new Transform3D();
		
		rotation.rotX( X * Point.deg2rad );
		rotation.transform(this);
		
	}
	
	public void rotateY(float Y){
		
		Transform3D rotation = new Transform3D();
		
		rotation.rotY( Y * Point.deg2rad );
		rotation.transform(this);
		
	}
	
	public void rotateZ(float Z){
		
		Transform3D rotation = new Transform3D();
		
		rotation.rotZ( -Z * Point.deg2rad );
		rotation.transform(this);
		
	}
	
	public void print(PrintWriter OUT){
		
		OUT.printf("( %d %d %d )", Math.round(x), Math.round(y), Math.round(z));
		
	}
	
}


class Plane{
	public Point p;	
	public Point n; // Plane normal
	
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
		p = new Point(0,0,0);
		n = new Point(1,0,0);
		
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
	
	public Plane(Point P0, Point N){
		p = P0; n = N; texture = "NULL";
		
		sShift = tShift = rotation = 0;
		sScale = tScale = 0.5;
		xx1 = xx2 = xx3 = 0;
		
	}
	
	public Plane(Point P0, Point N, String TEXTURE){
		p = P0; n = N; texture = TEXTURE;
		
		sShift = tShift = rotation = 0;
		sScale = tScale = 0.5;
		xx1 = xx2 = xx3 = 0;

	}
	
	public void scale(float X, float Y, float Z){		
	
	}
	
	public void translate(float X, float Y, float Z){

	}
	
	public void rotateX(float X){
		
	}
	
	public void rotateY(float Y){
		
	}
	
	public void rotateZ(float Z){
				
	}
	
	// xy
	static Plane top(float Z){
		// ( 1 1 1 ) ( 1 -1 1 ) ( -1 1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane(new Point(1,1,Z), new Point(1,-1,Z), new Point(-1,1,Z));
		
		return new Plane(new Point(0,0,Z), new Point(0,0,1));
	}
	
	// xz
	static Plane back(float Y){
		// ( 1 1 1 ) ( -1 1 1 ) ( 1 1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0

		//return new Plane( new Point(1,Y,1), new Point(-1,Y,1), new Point(1,Y,-1));
		
		return new Plane(new Point(0,Y,0), new Point(0,1,0));
	}
		
	// yz
	static Plane right(float X){
		// ( 1 1 1 ) ( 1 1 -1 ) ( 1 -1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane( new Point(X,1,1), new Point(X,1,-1), new Point(X,-1,1));
		
		return new Plane(new Point(X,0,0), new Point(1,0,0));
	}
	
	// xy
	static Plane bottom(float Z){
		// ( -1 -1 -1 ) ( 1 -1 -1 ) ( -1 1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane( new Point(-1,-1,Z), new Point(1,-1,Z), new Point(-1,1,Z));
		
		return new Plane(new Point(0,0,Z), new Point(0,0,-1));
	}
	
	// xz
	static Plane front(float Y){
		// ( -1 -1 -1 ) ( -1 -1 1 ) ( 1 -1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane( new Point(-1,Y,-1),new Point(-1,Y,1),new Point(1,Y,-1));
		
		return new Plane(new Point(0,Y,0), new Point(0,-1,0));
	}
	
	// yz
	static Plane left(float X){
		// 	( -1 -1 -1 ) ( -1 1 -1 ) ( -1 -1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane( new Point(X,-1,-1),new Point(X,1,-1),new Point(X,-1,1));
		
		return new Plane(new Point(X,0,0), new Point(-1,0,0));
	}

	public Point[] find3Points(){
	
		Point[] points = new Point[3];
		
		points[0] = new Point(0,0,0);
		points[1] = new Point(0,0,0);
		points[2] = new Point(0,0,0);
		
		// First choose an axis vector that is not colinear with the normal vector of the plane
		
		// set an epsilon in order to check if the dotproduct is near one (colinear test)
		
		float epsilon = 0.01f;
		float length = p.length();
		
		Point u = new Point(0,0,0);
		Point v = new Point(0,0,0);
		
		// First check the x axis
		u.set(1*length,0,0);
		
		if( n.colinear(u, epsilon) ){
			// n is colinear with x axis, go for the y one
			u.set(0,1*length,0);
			
			if( n.colinear(u, epsilon) ){
				// n is colinear with y axis, go for z
				u.set(0,0,1*length);
				
				if( n.colinear(u,epsilon) ){
					// error can't be colinear with z 
				}
			}
		}
		
		// u is a vector that is not colinear with n, calculate the cross product to find a perpendicular vector to n
		
		v.cross(n, u);
		
		// set u perpendicular to n and v
		
		u.cross(n, v);
		
		// Now we have a orthogonal basis formed by, n,u and v. the points are p and the translation of p over the coplanar planes
		
		points[0].set(p);
		points[1].set(p);
		points[2].set(p);
		
		points[0].add(u);
		points[2].add(v);

		
		
		return points;
	}
	

	public void print(PrintWriter OUT){
		
		Point[] points = find3Points();
		
		//OUT.print(' ');
		points[0].print(OUT);
		
		OUT.print(' ');
		points[1].print(OUT);
		
		OUT.print(' ');
		points[2].print(OUT);
		
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
		
	public void rotateX(float X){
		
		for(Plane p : planes){
			p.rotateX(X);
		}
	}
	
	public void rotateY(float Y){
		
		for(Plane p : planes){
			p.rotateY(Y);
		}
	}
	
	public void rotateZ(float Z){
		
		for(Plane p : planes){
			p.rotateZ(Z);
		}
	}
	
	public void addPlane(Plane P){
		planes.add(P);
	}
	
	public static Brush cube(float LEFT, float RIGHT, float FRONT, float BACK, float BOTTOM, float TOP, String TEXTURE){
		
		Brush b;
		
		b = new Brush();

		/*b.addPlane( Plane.top( TOP ) );
		b.addPlane( Plane.back( BACK ) );
		b.addPlane( Plane.right( RIGHT ) );
		
		b.addPlane( Plane.bottom( BOTTOM ) );
		b.addPlane( Plane.front( FRONT ) );
		b.addPlane( Plane.left( LEFT ) );*/
		
		b.addPlane( Plane.right(RIGHT));
		b.addPlane( Plane.back(BACK));
		b.addPlane( Plane.left(LEFT));
		b.addPlane( Plane.front(FRONT));
		b.addPlane( Plane.top(TOP));
		b.addPlane( Plane.bottom(BOTTOM));
		
		for( Plane p : b.planes )
			p.texture = TEXTURE;
		
		return b;
		
	}
	
	public static Brush cube2(float WIDTH, String TEXTURE){
		
		Brush b;
		Plane p;
		
		b = new Brush();
		

		p = Plane.right(0);
		p.translate(WIDTH, 0, 0);
		b.addPlane(p);
		
		p = Plane.right(0);
		p.rotateZ(90);
		p.translate(0, WIDTH, 0);
		b.addPlane(p);
		
		p = Plane.right(0);
		p.rotateZ(180);
		p.translate(-WIDTH, 0, 0);
		b.addPlane(p);
		
		p = Plane.right(0);
		p.rotateZ(270);
		p.translate(0, -WIDTH, 0);
		b.addPlane(p);
			
		p = Plane.top(0);
		p.translate(0, 0, WIDTH);
		b.addPlane(p);
		
		p = Plane.top(0);
		p.rotateZ(180);
		p.translate(0, 0, -WIDTH);
		b.addPlane(p);		
		
		for( Plane x : b.planes )
			x.texture = TEXTURE;
		
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
	
		try {
			PrintWriter out1 = new PrintWriter(System.out);

			PrintWriter out = new PrintWriter(new File("../../test.map"));
			
			Map mapa = new Map();
			
			Brush b;
		
			//b = Brush.cube(-100, 100, -100, 100, -100, 100, "base_wall/asdfg");
			b = Brush.cube(-32, 32, -32, 32, -32, 32, "base_wall/asdfg");
			mapa.addBrush(b);
			
			mapa.print(out1);
			mapa.print(out);
			
			out1.close();
			out.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*Plane p = Plane.left(1.0f);
		
		PrintWriter out1 = new PrintWriter(System.out);
		
		p.print(out1);
		
		out1.println("");
		
		out1.flush();
		
		p.rotate(0, 180.0f, 0);
		
		p.print(out1);
		
		out1.close();
		
		*/
		
		/*
		Map mapa = new Map();
		
		Brush b;
		
		b = Brush.cube(-100, 100, -100, 100, -100, 100, "base_wall/concrete_dark");
		
		mapa.addBrush(b);
		
		b = Brush.cube(-100, 100, -100, 100, -100, 100, "base_wall/concrete_dark");
		b.rotate(0.0f, 90.0f, 0.0f);
		
		mapa.addBrush(b);
		
		*/
		
		/*
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
		
		
		*/
		
		/*
		try {
			
			PrintWriter map_out = new PrintWriter("out.map");
			PrintWriter console_out = new PrintWriter(System.out);
			
			mapa.print(map_out);
			mapa.print(console_out);
			
			map_out.close();
			console_out.close();
		
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		

	}
}

