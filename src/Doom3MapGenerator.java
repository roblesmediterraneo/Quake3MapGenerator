import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.media.j3d.Transform3D;
import javax.vecmath.*;

class Point extends Vector3f{
	private static final long serialVersionUID = 1L;
	public static final double deg2rad = Math.PI/180.0;
	
	public Point(){
		x = 0;
		y = 0;
		z = 0;
	}
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
		

		
	}
	
}


class Plane{	
	public Point n; // Plane normal
	public float d; // Distance from origin (0,0,0)
	
	public Point tmfr; // Texture matrix first row
	public Point tmsr; // Texture matrix second row; third row is alwais 0 0 1
	
	public String texture;
	
	public Point unknown;
	
	public Plane(){
		n = new Point(1,0,0);
		d = 0;
		
		tmfr = new Point(0.01f,0,0);
		tmsr = new Point(0,0.01f,0);
		unknown = new Point(0,0,0);
		
		texture = "_default";
	}
	
	public Plane(String TEXTURE){
		this();
		
		texture = TEXTURE;
	}
	
	public Plane(Point N, float D){
		n = N; d = D; texture = "_default";
		
		tmfr = new Point(0.01f,0,0);
		tmsr = new Point(0,0.01f,0);
		unknown = new Point(0,0,0);
	}
	
	public Plane(Point N, float D, String TEXTURE){
		n = N; d = D; texture = TEXTURE;
		
		tmfr = new Point(0.01f,0,0);
		tmsr = new Point(0,0.01f,0);
		unknown = new Point(0,0,0);
	}
	
	public void scale(float X, float Y, float Z){		
	
	}
	
	public void translate(float X, float Y, float Z){
		
		d -= n.x * X + n.y * Y + n.z * Z;

	}
	
	public void rotateX(float X){
		
		n.rotateX(X);
		
	}
	
	public void rotateY(float Y){
		
		n.rotateY(Y);
		
	}
	
	public void rotateZ(float Z){
		
		n.rotateZ(Z);
		
	}
	
	// xy
	static Plane top(float Z){
		// ( 1 1 1 ) ( 1 -1 1 ) ( -1 1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane(new Point(1,1,Z), new Point(1,-1,Z), new Point(-1,1,Z));
		
		return new Plane(new Point(0,0,-1), -Z);
	}
	
	// xz
	static Plane back(float Y){
		// ( 1 1 1 ) ( -1 1 1 ) ( 1 1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0

		//return new Plane( new Point(1,Y,1), new Point(-1,Y,1), new Point(1,Y,-1));
		
		return new Plane(new Point(0,-1,0), -Y);
	}
		
	// yz
	static Plane right(float X){
		// ( 1 1 1 ) ( 1 1 -1 ) ( 1 -1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane( new Point(X,1,1), new Point(X,1,-1), new Point(X,-1,1));
		
		return new Plane(new Point(-1,0,0), -X);
	}
	
	// xy
	static Plane bottom(float Z){
		// ( -1 -1 -1 ) ( 1 -1 -1 ) ( -1 1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane( new Point(-1,-1,Z), new Point(1,-1,Z), new Point(-1,1,Z));
		
		return new Plane(new Point(0,0,1), -Z);
	}
	
	// xz
	static Plane front(float Y){
		// ( -1 -1 -1 ) ( -1 -1 1 ) ( 1 -1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane( new Point(-1,Y,-1),new Point(-1,Y,1),new Point(1,Y,-1));
		
		return new Plane(new Point(0,1,0), -Y);
	}
	
	// yz
	static Plane left(float X){
		// 	( -1 -1 -1 ) ( -1 1 -1 ) ( -1 -1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane( new Point(X,-1,-1),new Point(X,1,-1),new Point(X,-1,1));
		
		return new Plane(new Point(1,0,0), -X);
	}
	

	public void print(PrintWriter OUT){
		
		// ( 0 0 1 -64 ) ( ( 0.03125 0 0 ) ( 0 0.03125 0 ) ) "_default" 0 0 0
		
		OUT.printf("( %f %f %f %f ) ", n.x, n.y, n.z, d);
		OUT.printf("( ( %f %f %f ) ", tmfr.x, tmfr.y, tmfr.z);
		OUT.printf("( %f %f %f ) ) ", tmsr.x, tmsr.y, tmsr.z);
		OUT.printf("\"%s\" %.0f %.0f %.0f", texture, unknown.x, unknown.y, unknown.z);
		
	}

	
}

class Brush{
	private ArrayList<Plane> planes;
	
	public Brush(){
		
		planes = new ArrayList<Plane>();
		
	}
	
	public void print(PrintWriter OUT){
		
		OUT.println("{\nbrushDef3\n{");
		
		for( Plane p : planes){
			p.print( OUT );
			OUT.print('\n');
		}
		
		OUT.println("}\n}");
	}
	
	public void scale(float X, float Y, float Z){
		
		for(Plane p : planes){
			p.scale( X, Y, Z );
		}
		
	}
	
	public void translate(float X, float Y, float Z){
		
		for(Plane p : planes){
			p.translate(X, Y, Z);
		}
	}
	
	public void translate(Point P){
		
		for(Plane p : planes){
			p.translate(P.x, P.y, P.z);
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
	
	public static Brush cube(float LEFT, float RIGHT, float FRONT, float BACK, float TOP, float BOTTOM, String TEXTURE){
		
		Brush b;
		
		b = new Brush();
		
		b.addPlane( Plane.bottom(BOTTOM));
		b.addPlane( Plane.front(FRONT));
		b.addPlane( Plane.left(LEFT));
		
		b.addPlane( Plane.top(TOP));
		b.addPlane( Plane.back(BACK));
		b.addPlane( Plane.right(RIGHT));
			
		for( Plane p : b.planes )
			p.texture = TEXTURE;
		
		return b;
		
	}
	
	public static Brush cube(float SIZE, String TEXTURE){
		
		return Brush.cube(SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, TEXTURE);
		
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
	
	public static Brush sphere2(Point CENTER, float RADIUS, int HORIZONTAL_STEPS, int VERTICAL_STEPS, String TEXTURE){
		
		Brush b;
		Plane p;
		
		Point n;
		
		b = new Brush();
		
		n = new Point();
		
		double x_inc = 2 * Math.PI / HORIZONTAL_STEPS;
		double z_inc = Math.PI / VERTICAL_STEPS;
		
		for(double alfa = 0.0f; alfa < 2*Math.PI; alfa += x_inc){
			for(double omega = 0.0f; omega < Math.PI; omega += z_inc){
				
				n.x = (float) (Math.cos(alfa) * Math.sin(omega));
				n.y = (float) (Math.sin(alfa) * Math.sin(omega));
				n.z = (float) Math.cos(omega);
				
				n.negate();
				
				p = new Plane(n, -RADIUS, TEXTURE);
				
				b.addPlane(p);

			}
		}
		
		return b;
	}
	
	public static Brush sphere(Point CENTER, float RADIUS, int HORIZONTAL_STEPS, int VERTICAL_STEPS, String TEXTURE){
		
		// HORIZOTAL_STEPS and VERTICAL_STEPS must be greater than two.
		
		Brush b;
		Plane p;
		
		b = new Brush();
		
		float x_inc = 360.0f / HORIZONTAL_STEPS;
		float y_inc = 180.0f / VERTICAL_STEPS;
		
		for( float alfa = 0.0f; alfa < 360.0f; alfa += x_inc ){
			for( float omega = -90.0f; omega < 90.0f; omega += y_inc ){
				
				p = Plane.right(RADIUS);
				
				p.rotateY(omega);
				p.rotateZ(alfa);
				
				p.texture = TEXTURE;
				
				b.addPlane(p);
				
			}
		}
		
		b.translate(CENTER);
		
		return b;
	}
	
	public static Brush semiSphere(Point CENTER, float RADIUS, int HORIZONTAL_STEPS, int VERTICAL_STEPS, String TEXTURE){
		
		// HORIZOTAL_STEPS and VERTICAL_STEPS must be greater than two.
		
		Brush b;
		Plane p;
		
		b = new Brush();
		
		float x_inc = 360.0f / HORIZONTAL_STEPS;
		float y_inc = 180.0f / VERTICAL_STEPS;
		
		for( float alfa = 0.0f; alfa < 360.0f; alfa += x_inc ){
			for( float omega = 0.0f; omega < 90.0f; omega += y_inc ){
				
				p = Plane.right(RADIUS);
				
				p.rotateY(omega);
				p.rotateZ(alfa);
				
				p.texture = TEXTURE;
				
				b.addPlane(p);
				
			}
		}
		
		// Add the base
		
		p = Plane.top(CENTER.z);
		p.texture = TEXTURE;
		
		b.addPlane(p);
		
		b.translate(CENTER);
		
		return b;
	}

		
}

abstract class Entity{
	static public int entityCounter = 0;
	public int entityNumber;
	
	public Point origin;
	
	abstract public String className();
	
	public Entity(){
		
		origin = new Point(0.0f,0.0f,0.0f);
		
		entityNumber = Entity.entityCounter;
		entityCounter++;
		
	}
	
	public void startPrint(PrintWriter OUT){
		OUT.println("{");
	}
	
	public void endPrint(PrintWriter OUT){
		OUT.println("}");
	}
	
	public void print(PrintWriter OUT){
		OUT.printf("\"classname\" \"%s\"\n", className());
		OUT.printf("\"name\" \"entity_%d\"\n", entityNumber);
		OUT.printf("\"origin\" \"%.6f %.6f %.6f\"\n", origin.x, origin.y, origin.z);
	}

}

class GenericEntity extends Entity{
	String className;
	
	GenericEntity(String NAME){ className = NAME; };
	
	public String className(){ return className; };
	
	public void print(PrintWriter OUT){
		
		startPrint(OUT);
		super.print(OUT);
		endPrint(OUT);
	}
	
	static GenericEntity WeaponRocketLauncher() { return new GenericEntity("weapon_rocketlauncher"); }
	static GenericEntity MonsterZombieFat() { return new GenericEntity("monster_zombie_fat"); }
}




class InfoPlayerStart extends Entity{
	
	public float angle;
	
	public String className(){ return "info_player_start"; };
	
	public void print(PrintWriter OUT){
		
		startPrint(OUT);
		super.print(OUT);
		OUT.printf("\"angle\" \"%.2f\"\n", angle);
		endPrint(OUT);
	}
	
}

class Light extends Entity{
	Point lightCenter;
	Point lightRadius;
	
	public Light(){ 
		lightCenter = new Point(0,0,0);
		lightRadius = new Point(320,320,320);
	};
	
	public String className(){ return "light"; };
	
	public void print(PrintWriter OUT){
		
		startPrint(OUT);
		super.print(OUT);
		
		OUT.printf("\"light_center\" \"%.2f %.2f %.2f\"\n", lightCenter.x, lightCenter.y, lightCenter.z);
		OUT.printf("\"light_radius\" \"%.2f %.2f %.2f\"\n", lightRadius.x, lightRadius.y, lightRadius.z);
		
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
		
		OUT.printf("Version 2\n// entity 0\n");
		
		
		OUT.printf("{\n\"classname\" \"worldspawn\"\n");
		
		for( Brush b : brushes){
			OUT.printf("// primitive %d\n", I++);
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
		
		float width2 = WIDTH / 2.0f;
		float height2 = HEIGHT / 2.0f;
		float depth2 = DEPTH / 2.0f;
		
		Brush b;
		
		// Left wall
		
		//b = Brush.cube(LEFT, RIGHT, FRONT, BACK, TOP, BOTTOM, TEXTURE)
		b = Brush.cube(WALL_WIDTH, 0, depth2, depth2, height2, height2, TEXTURES[0]);
		// Center it
		b.translate(CENTER);
		// Move it down
		b.translate(-width2-WALL_WIDTH,0,0);

		this.addBrush(b);
		
		// Right wall
		
		//b = Brush.cube(LEFT, RIGHT, FRONT, BACK, TOP, BOTTOM, TEXTURE)
		b = Brush.cube(0, WALL_WIDTH, depth2, depth2, height2, height2, TEXTURES[1]);
		// Center it
		b.translate(CENTER);
		// Move it down
		b.translate(width2+WALL_WIDTH,0,0);

		this.addBrush(b);
		
		// Front wall
		
		//b = Brush.cube(LEFT, RIGHT, FRONT, BACK, TOP, BOTTOM, TEXTURE)
		b = Brush.cube(width2, width2, WALL_WIDTH, 0, height2, height2, TEXTURES[2]);
		// Center it
		b.translate(CENTER);
		// Move it down
		b.translate(0,-depth2-WALL_WIDTH,0);

		this.addBrush(b);
		
		// Back wall
		
		//b = Brush.cube(LEFT, RIGHT, FRONT, BACK, TOP, BOTTOM, TEXTURE)
		b = Brush.cube(width2, width2, 0, WALL_WIDTH, height2, height2, TEXTURES[3]);
		// Center it
		b.translate(CENTER);
		// Move it down
		b.translate(0,depth2+WALL_WIDTH,0);

		this.addBrush(b);
		
		// The floor
		
		//b = Brush.cube(LEFT, RIGHT, FRONT, BACK, TOP, BOTTOM, TEXTURE)
		b = Brush.cube(width2, width2, depth2, depth2, 0, WALL_WIDTH, TEXTURES[4]);
		// Center it
		b.translate(CENTER);
		// Move it down
		b.translate(0,0,-height2-WALL_WIDTH);

		this.addBrush(b);
		
		// The top
		
		//b = Brush.cube(LEFT, RIGHT, FRONT, BACK, TOP, BOTTOM, TEXTURE)
		b = Brush.cube(width2, width2, depth2, depth2, WALL_WIDTH, 0, TEXTURES[5]);
		// Center it
		b.translate(CENTER);
		// Move it down
		b.translate(0,0,height2+WALL_WIDTH);

		this.addBrush(b);

	}
	
	public void addSphereWithCubes(Point CENTER, float RADIUS, int STEPS, float INITIAL_CUBE_SIZE, float FINAL_CUBE_SIZE, int CUBES, int HORIZONTAL_CUBES, int VERTICAL_CUBES, float GAP, String SPHERE_TEXTURE, String CUBES_TEXTURE){
		
		Brush b;
		
		b = Brush.sphere(CENTER, RADIUS, STEPS, STEPS, SPHERE_TEXTURE);
		
		addBrush(b);
		
		for(float alfa = 0.0f; alfa <= 360; alfa += 360.0f / HORIZONTAL_CUBES){
			for(float omega = 0.0f; omega <= 270; omega += 180.0f / VERTICAL_CUBES){
				
				float sizeInc = (INITIAL_CUBE_SIZE - FINAL_CUBE_SIZE) / CUBES;
				float size = INITIAL_CUBE_SIZE;
				float sizeAcc = RADIUS + INITIAL_CUBE_SIZE;
				
				for(int I = 0; I < CUBES; I++)
				{					
					b = Brush.cube(size, CUBES_TEXTURE);
				
					b.translate(sizeAcc, 0, 0);
				
					b.rotateY(omega);
					b.rotateZ(alfa);
					
					b.translate(CENTER);
				
					addBrush(b);
					
					sizeAcc += size*2;
					sizeAcc += GAP;
					
					size -= sizeInc;

	
				}
			}
		}

	}
	
}

public class Doom3MapGenerator {

	public static void main(String[] args) {
	
		Map mapa = new Map();
		Brush b;
				
		
		String[] textures = {
				"textures/base_floor/a_bluetex1a_02","textures/base_floor/a_bluetex1a_02",
				"textures/base_floor/a_bluetex1a_02","textures/base_floor/a_bluetex1a_02",
				"textures/base_floor/a_bluetex1a_02","textures/skies/hellsky2"
		};
		
		mapa.addRoom(2048, 384, 2048, new Point(0,0,192), 16, textures);
		
		for(int X = -1000; X < 1000; X+=200){
			for( int Y = -1000; Y < 1000; Y+=200){
				
				b = Brush.cube(25, 25, 25, 25, 25, 25, "textures/base_floor/a_bluetex1a_02");
				
				b.translate(X, Y, 25);
							
				mapa.addBrush(b);
			}
		}
		
		mapa.addSphereWithCubes(new Point(0,0,192), 64, 8, 16, 4, 4, 4, 4, 5, "textures/rock/greenrocks2_rockbase", "textures/rock/sharprock_dark");
		
		// Add some entities
		
		InfoPlayerStart ips = new InfoPlayerStart();	
		ips.origin.set(900,900,80);
	
		ips.angle = -90-45;
		
		mapa.addEntity(ips);
		
		GenericEntity entity = GenericEntity.WeaponRocketLauncher();
		
		entity.origin.set(900,800,20);
		
		mapa.addEntity(entity);
		
		for(int X = -900; X < 900; X += 200){
		
			entity = GenericEntity.MonsterZombieFat();
			entity.origin.set(X,-900,10);
			
			mapa.addEntity(entity);
			
		}
		
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
			
			PrintWriter map_out = new PrintWriter("../../.doom3/base/maps/test.map");
			PrintWriter console_out = new PrintWriter(System.out);
			
			mapa.print(map_out);
			mapa.print(console_out);
			
			map_out.close();
			console_out.close();
		
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		

	}
}

