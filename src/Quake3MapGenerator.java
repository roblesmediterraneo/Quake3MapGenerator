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
    *     
    ****************************************************************
*/

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

class Point{
	
	public int x;
	public int y;
	public int z;
	
	public Point(){
		x = y = z = 0;
	}
	
	public Point(int X, int Y, int Z){
		x = X; y = Y; z = Z;
	}
	
	public void print(PrintWriter OUT){
		OUT.printf("( %d %d %d )", x,y,z);
	}
	
	public void scale(int X, int Y, int Z){
		x *= X;
		y *= Y;
		z *= Z;
	}
	
	public void translate(int X, int Y, int Z){
		x += X;
		y += Y;
		z += Z;
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
	
	public void scale(int X, int Y, int Z){
		p0.scale(X, Y, Z);
		p1.scale(X, Y, Z);
		p2.scale(X, Y, Z);
	}
	
	public void translate(int X, int Y, int Z){
		p0.translate(X, Y, Z);
		p1.translate(X, Y, Z);
		p2.translate(X, Y, Z);
	}
	
	// xy
	static Plane top(int Z){
		// ( 1 1 1 ) ( 1 -1 1 ) ( -1 1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		return new Plane(new Point(1,1,Z), new Point(1,-1,Z), new Point(-1,1,Z));
		
	}
	
	// xz
	static Plane back(int Y){
		// ( 1 1 1 ) ( -1 1 1 ) ( 1 1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0

		return new Plane( new Point(1,Y,1), new Point(-1,Y,1), new Point(1,Y,-1));
		
	}
		
	// yz
	static Plane right(int X){
		// ( 1 1 1 ) ( 1 1 -1 ) ( 1 -1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		return new Plane( new Point(X,1,1), new Point(X,1,-1), new Point(X,-1,1));
		
	}
	
	// xy
	static Plane bottom(int Z){
		// ( -1 -1 -1 ) ( 1 -1 -1 ) ( -1 1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		return new Plane( new Point(-1,-1,Z), new Point(1,-1,Z), new Point(-1,1,Z));
	}
	
	// xz
	static Plane front(int Y){
		// ( -1 -1 -1 ) ( -1 -1 1 ) ( 1 -1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		return new Plane( new Point(-1,Y,-1),new Point(-1,Y,1),new Point(1,Y,-1));
	}
	
	// yz
	static Plane left(int X){
		// 	( -1 -1 -1 ) ( -1 1 -1 ) ( -1 -1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		return new Plane( new Point(X,-1,-1),new Point(X,1,-1),new Point(X,-1,1));
	}
	
	public void print(PrintWriter OUT){
		
		p0.print(OUT);
		OUT.print(' ');
		
		p1.print(OUT);
		OUT.print(' ');
		
		p2.print(OUT);
		OUT.print(' ');
		
		OUT.print( texture );
		
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
	
	public void scale(int X, int Y, int Z){
		
		for(Plane p : planes){
			p.scale(X, Y, Z);
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
	
	public static Brush cube(int LEFT, int RIGHT, int FRONT, int BACK, int BOTTOM, int TOP){
		
		Brush b;
		
		b = new Brush();

		b.addPlane( Plane.top( TOP ) );
		b.addPlane( Plane.back( BACK ) );
		b.addPlane( Plane.right( RIGHT ) );
		
		b.addPlane( Plane.bottom( BOTTOM ) );
		b.addPlane( Plane.front( FRONT ) );
		b.addPlane( Plane.left( LEFT ) );
		
		return b;
		
	}
	
}

class Map{
	
	private ArrayList<Brush> brushes;
	
	public Map(){
		
		brushes = new ArrayList<Brush>();
		
	}
	
	public void addBrush(Brush B){
		brushes.add(B);
	}
	
	public void print(PrintWriter OUT){
		
		int I = 0;
		
		OUT.printf("{\n\"classname\" \"worldspawn\"\n");
		
		for( Brush b : brushes){
			OUT.printf("// brush %d\n", I++);
			b.print( OUT );
		}
		
		OUT.println('}');
	}	
	
}

public class Quake3MapGenerator {

	public static void main(String[] args) {
		
		Map mapa = new Map();
		
		Brush b;
		
		b = Brush.cube(-1, 1, -1, 1, -10, 0);		
		b.scale(1000, 1000, 1);
		
		mapa.addBrush(b);
		
		for(int X = -1000; X < 1000; X+=100){
			for( int Y = -1000; Y < 1000; Y+=100){
				
				b = Brush.cube(X, X+50, Y, Y+50, 0, 50);
				
				mapa.addBrush(b);
			}
		}
		
		try {
			
			PrintWriter out = new PrintWriter("output.map");
			
			mapa.print(out);
			
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

