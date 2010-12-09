import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import javax.media.j3d.Transform3D;
import javax.vecmath.*;

/**
 * Class Point define a 3d float point or vector
 */
class Point extends Vector3f{
	private static final long serialVersionUID = 1L;
	public static final double deg2rad = Math.PI/180.0;
	
	/**
	 * Default constructor, sets the coordinates to <0,0,0>
	 */
	public Point(){
		this(0,0,0);
	}
	/**
	 * Constructor that sets the three cartesian coordinates
	 * 
	 * @param X x-axis coordinate (left-right) horizontal
	 * @param Y y-axis coordinate (front-back) depth
	 * @param Z z-axis coordinate (down-up) vertical
	 */
	public Point(float X, float Y, float Z){
		x = X;
		y = Y;
		z = Z;
	}
	/**
	 * Constructor that uses another point to initialize this instance
	 * 
	 * @param P point to clone.
	 */
	public Point(Point P){
		this(P.x, P.y, P.z);
	}
	/**
	 * Scale the point/vector
	 * 
	 * @param X x scale factor
	 * @param Y y scale factor
	 * @param Z z scale factor
	 */
	public void scale(float X, float Y, float Z){
		x *= X;
		y *= Y;
		z *= Z;
	}
	/**
	 * Check if a point is colinear with that vector (it have the same direction
	 * or it is in the same line)
	 * 
	 * @param V Vector or point to check against it
	 * @param EPSILON Is the margin or gap that we use to consider that the point is colinear
	 * @return true when colinear false if not
	 */
	public boolean colinear(Point V, float EPSILON){
		
		float dot = dot(V);
		
		if( 1 - Math.abs(dot) < EPSILON ){	
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * Rotate around the x axis
	 * 
	 * @param X rotation angle in degrees
	 */
	public void rotateX(float X){
		
		Transform3D rotation = new Transform3D();
		
		rotation.rotX( X * Point.deg2rad );
		rotation.transform(this);

	}
	/**
	 * Rotate around the y axis
	 * 
	 * @param Y rotation angle in degrees
	 */	
	public void rotateY(float Y){
		
		Transform3D rotation = new Transform3D();
		
		rotation.rotY( Y * Point.deg2rad );
		rotation.transform(this);
		
	}
	/**
	 * Rotate around the z axis
	 * 
	 * @param Z rotation angle in degrees
	 */
	public void rotateZ(float Z){
		
		Transform3D rotation = new Transform3D();
		
		rotation.rotZ( -Z * Point.deg2rad );
		rotation.transform(this);
		
	}
	
}

/**
 * Implements a plane using a normal and the distance to the origin.
 * 
 * It's follows the ecuation Ax + By + Cz + D = 0, where
 * A is n.x, B is n.y, C is n.z and D is d.
 * 
 * @see <a href="http://mathworld.wolfram.com/Plane.html">Plane Equation</a>
 */
class Plane{	
	public Point n; // Plane normal
	public float d; // Distance from origin (0,0,0)
	
	public Point tmfr; // Texture matrix first row
	public Point tmsr; // Texture matrix second row; third row is alwais 0 0 1
	
	public String texture;
	
	public Point unknown;
	
	/**
	 * Default constructor
	 */
	public Plane(){
		n = new Point(1,0,0);
		d = 0;
		
		tmfr = new Point(0.01f,0,0);
		tmsr = new Point(0,0.01f,0);
		unknown = new Point(0,0,0);
		
		texture = "_default";
	}
	/**
	 * Constructor that sets the texture
	 * 
	 * @param TEXTURE material used in this face
	 */
	public Plane(String TEXTURE){
		this();
		texture = TEXTURE;
	}
	/**
	 * Constructor that initialize the plane with the normal and distance
	 * 
	 * @param N normal of the plane (perpendicular vector of the plane)
	 * @param D nearest distance to the origin
	 */
	public Plane(Point N, float D){
		this();
		n = N; d = D;
	}
	/**
	 * Constructor that initializes the plane with the normal, distance and texture
	 * 
	 * @param N normal of the plane (perpendicular vector of the plane)
	 * @param D nearest distance to the origin
	 * @param TEXTURE material used in this face
	 */
	public Plane(Point N, float D, String TEXTURE){
		this();
		n = N; d = D; texture = TEXTURE;
	}
	
	/**
	 * Translate the plane into another location preserving its direction (normal)
	 * 
	 * @param X x-axis translation factor (horizontal)
	 * @param Y y-axis translation factor (depth)
	 * @param Z z-axis translation factor (vertical)
	 */
	public void translate(float X, float Y, float Z){
		
		d -= n.x * X + n.y * Y + n.z * Z;

	}
	
	/**
	 * Rotate the normal of the plane, rotating the plane over the x axis
	 * 
	 * @param ANGLE angle of rotation in degrees
	 */
	public void rotateX(float ANGLE){
		
		n.rotateX(ANGLE);
		
	}
	/**
	 * Rotate the normal of the plane, rotating the plane over the x axis
	 * 
	 * @param ANGLE angle of rotation in degrees
	 */
	public void rotateY(float ANGLE){
		
		n.rotateY(ANGLE);
		
	}
	/**
	 * Rotate the normal of the plane, rotating the plane over the x axis
	 * 
	 * @param ANGLE angle of rotation in degrees
	 */
	public void rotateZ(float ANGLE){
		
		n.rotateZ(ANGLE);
		
	}
	
	/**
	 * Create a plane parallel to the xy plane (the normal pointing down)
	 * 
	 * @param Z distance from the origin
	 * @return plane parallel to xy
	 */
	static Plane top(float Z){
		// ( 1 1 1 ) ( 1 -1 1 ) ( -1 1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane(new Point(1,1,Z), new Point(1,-1,Z), new Point(-1,1,Z));
		
		return new Plane(new Point(0,0,-1), -Z);
	}
	
	/**
	 * Create a plane parallel to the xz plane (the normal pointing far depth away)
	 * 
	 * @param Y distance from the origin
	 * @return plane parallel to xz
	 */
	static Plane back(float Y){
		// ( 1 1 1 ) ( -1 1 1 ) ( 1 1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0

		//return new Plane( new Point(1,Y,1), new Point(-1,Y,1), new Point(1,Y,-1));
		
		return new Plane(new Point(0,-1,0), -Y);
	}
		
	/**
	 * Create a plane parallel to the yz plane (the normal pointing left)
	 * 
	 * @param X distance from the origin
	 * @return plane parallel to yz
	 */
	static Plane right(float X){
		// ( 1 1 1 ) ( 1 1 -1 ) ( 1 -1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane( new Point(X,1,1), new Point(X,1,-1), new Point(X,-1,1));
		
		return new Plane(new Point(-1,0,0), -X);
	}
	
	/**
	 * Create a plane parallel to the xy plane (the normal pointing up)
	 * 
	 * @param Z distance from the origin
	 * @return plane parallel to xy
	 */
	static Plane bottom(float Z){
		// ( -1 -1 -1 ) ( 1 -1 -1 ) ( -1 1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane( new Point(-1,-1,Z), new Point(1,-1,Z), new Point(-1,1,Z));
		
		return new Plane(new Point(0,0,1), -Z);
	}
	
	/**
	 * Create a plane parallel to the xz plane (the normal pointing towards the screen)
	 * 
	 * @param Y distance from the origin
	 * @return plane parallel to xz
	 */
	static Plane front(float Y){
		// ( -1 -1 -1 ) ( -1 -1 1 ) ( 1 -1 -1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane( new Point(-1,Y,-1),new Point(-1,Y,1),new Point(1,Y,-1));
		
		return new Plane(new Point(0,1,0), -Y);
	}
	
	/**
	 * Create a plane parallel to the yz plane (the normal pointing right)
	 * 
	 * @param Z distance from the origin
	 * @return plane parallel to yz
	 */
	static Plane left(float X){
		// 	( -1 -1 -1 ) ( -1 1 -1 ) ( -1 -1 1 ) NULL 0 0 0 0.5 0.5 0 0 0
		
		//return new Plane( new Point(X,-1,-1),new Point(X,1,-1),new Point(X,-1,1));
		
		return new Plane(new Point(1,0,0), -X);
	}
	
	/**
	 * Prints the plane following the doom3 map specifications
	 * 
	 * @param OUT PrintWriter object
	 */
	public void print(PrintWriter OUT){
		
		// ( 0 0 1 -64 ) ( ( 0.03125 0 0 ) ( 0 0.03125 0 ) ) "_default" 0 0 0
		
		OUT.printf("( %f %f %f %f ) ", n.x, n.y, n.z, d);
		OUT.printf("( ( %f %f %f ) ", tmfr.x, tmfr.y, tmfr.z);
		OUT.printf("( %f %f %f ) ) ", tmsr.x, tmsr.y, tmsr.z);
		OUT.printf("\"%s\" %.0f %.0f %.0f", texture, unknown.x, unknown.y, unknown.z);
		
	}

	
}

/**
 * Models a brush ( a collection of planes )
 */
class Brush{
	private ArrayList<Plane> planes;
	
	/**
	 * Default constructor
	 */
	public Brush(){
		
		planes = new ArrayList<Plane>();
		
	}
	
	/**
	 * Prints out the brush following the doom3 specification.
	 * 
	 * @see Plane#print(PrintWriter) Plane.print
	 * 
	 * @param OUT PrintWriter object
	 */
	public void print(PrintWriter OUT){
		
		OUT.println("{\nbrushDef3\n{");
		
		for( Plane p : planes){
			p.print( OUT );
			OUT.print('\n');
		}
		
		OUT.println("}\n}");
	}
	
	/**
	 * Translates the brush translating each plane
	 * 
	 * @param X x axis factor of translation
	 * @param Y y axis factor of translation
	 * @param Z z axis factor of translation
	 * 
	 * @see Point#translate
	 */
	public void translate(float X, float Y, float Z){
		
		for(Plane p : planes){
			p.translate(X, Y, Z);
		}
	}
	
	/**
	 * Translates the brush translating each plane by P coordinates
	 * 
	 * @param P Coordinates used to translate the brush
	 * 
	 * @see Plane#translate(float, float, float) Plane.translate
	 */
	public void translate(Point P){
		
		for(Plane p : planes){
			p.translate(P.x, P.y, P.z);
		}
		
	}
	/**
	 * Rotates the brush around the x axis by ANGLE degrees
	 * 
	 * @param ANGLE angle of rotation in degrees
	 * 
	 * @see Plane#rotateX(float) Plane.rotateX
	 */
	public void rotateX(float ANGLE){
		
		for(Plane p : planes){
			p.rotateX(ANGLE);
		}
	}
	/**
	 * Rotates the brush around the y axis by ANGLE degrees
	 * 
	 * @param ANGLE angle of rotation in degrees
	 * 
	 * @see Plane#rotateY(float) Plane.rotateY
	 */	
	public void rotateY(float ANGLE){
		
		for(Plane p : planes){
			p.rotateY(ANGLE);
		}
	}
	/**
	 * Rotates the brush around the z axis by ANGLE degrees
	 * 
	 * @param ANGLE angle of rotation in degrees
	 * 
	 * @see Plane#rotateZ(float) Plane.rotateZ
	 */	
	public void rotateZ(float ANGLE){
		
		for(Plane p : planes){
			p.rotateZ(ANGLE);
		}
	}
	/**
	 * Adds a plane to the brush
	 * 
	 * @param P plane to add
	 */
	public void addPlane(Plane P){
		planes.add(P);
	}
	/**
	 * Creates a brush cube
	 * 
	 * @param LEFT left distance from the origin
	 * @param RIGHT right distance from the origin
	 * @param FRONT distance towards the screen and the origin
	 * @param BACK depth of the cube
	 * @param TOP distance up from the origin
	 * @param BOTTOM distance down from the origin
	 * @param TEXTURES materials to use in the faces of the cube
	 * @return A cube brush
	 * 
	 * @see Brush#addPlane(Plane) Brush.addPlane
	 */
	public static Brush cube(float LEFT, float RIGHT, float FRONT, float BACK, float TOP, float BOTTOM, String[] TEXTURES){
		
		Brush b;
		
		b = new Brush();
		
		b.addPlane( Plane.bottom(BOTTOM));
		b.addPlane( Plane.front(FRONT));
		b.addPlane( Plane.left(LEFT));
		
		b.addPlane( Plane.top(TOP));
		b.addPlane( Plane.back(BACK));
		b.addPlane( Plane.right(RIGHT));
			
		int I = 0;
		for( Plane p : b.planes )
			p.texture = TEXTURES[I++];
		
		return b;
		
	}
	/**
	 * Creates a brush cube
	 * 
	 * @param LEFT left distance from the origin
	 * @param RIGHT right distance from the origin
	 * @param FRONT distance towards the screen and the origin
	 * @param BACK depth of the cube
	 * @param TOP distance up from the origin
	 * @param BOTTOM distance down from the origin
	 * @param TEXTURE material to use in the faces of the cube
	 * @return A cube brush
	 */
	public static Brush cube(float LEFT, float RIGHT, float FRONT, float BACK, float TOP, float BOTTOM, String TEXTURE){
		
		String[] textures = new String[]{TEXTURE,TEXTURE,TEXTURE,TEXTURE,TEXTURE,TEXTURE};
		
		return Brush.cube(LEFT,RIGHT,FRONT,BACK,TOP,BOTTOM,textures);
	}
	/**
	 * Creates a brush cube with the same size in all of its faces
	 * 
	 * @param SIZE (left,right,front,back,top,bottom) sizes
	 * @param TEXTURES  materials to use in the faces of the cube
	 * @return A cube brush
	 */
	public static Brush cube(float SIZE, String[] TEXTURES){
		
		return Brush.cube(SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, TEXTURES);
		
	}
	/**
	 * Creates a brush cube with the same size in all of its faces
	 * 
	 * @param SIZE (left,right,front,back,top,bottom) sizes
	 * @param TEXTURES  materials to use in the faces of the cube
	 * @return A cube brush
	 * 
	 */
	public static Brush cube(float SIZE, String TEXTURE){
		
		String[] textures = new String[]{TEXTURE,TEXTURE,TEXTURE,TEXTURE,TEXTURE,TEXTURE};
		
		return Brush.cube(SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, textures);
		
	}
	/**
	 * Creates a brush cube with the same size in all of its faces, but using
	 * a different approach
	 * 
	 * @param SIZE (left,right,front,back,top,bottom) sizes
	 * @param TEXTURES materials to use in the faces of the cube
	 * @return Brush
	 * 
	 * @see Brush#cube(float, String) Brush.cube
	 */
	public static Brush cube2(float SIZE, String[] TEXTURES){
		
		Brush b;
		Plane p;
		
		b = new Brush();
		

		p = Plane.right(0);
		p.translate(SIZE, 0, 0);
		p.texture = TEXTURES[0];
		b.addPlane(p);
		
		p = Plane.right(0);
		p.rotateZ(90);
		p.translate(0, SIZE, 0);
		p.texture = TEXTURES[1];
		b.addPlane(p);
		
		p = Plane.right(0);
		p.rotateZ(180);
		p.translate(-SIZE, 0, 0);
		p.texture = TEXTURES[2];
		b.addPlane(p);
		
		p = Plane.right(0);
		p.rotateZ(270);
		p.translate(0, -SIZE, 0);
		p.texture = TEXTURES[3];
		b.addPlane(p);
			
		p = Plane.top(0);
		p.translate(0, 0, SIZE);
		p.texture = TEXTURES[4];
		b.addPlane(p);
		
		p = Plane.top(0);
		p.rotateZ(180);
		p.translate(0, 0, -SIZE);
		p.texture = TEXTURES[5];
		b.addPlane(p);		
		
		return b;
	}
	/**
	 * Creates a sphere brush
	 * 
	 * @param CENTER where the sphere will be centered
	 * @param RADIUS radius of the sphere
	 * @param HORIZONTAL_STEPS Horizontal resolution of the sphere (greater than 2)
	 * @param VERTICAL_STEPS Vertical resolution of the sphere (greater than 2)
	 * @param TEXTURE Material to be used on the sphere
	 * @return The sphere brush
	 */
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
	
	/**
	 * Creates a sphere brush
	 * 
	 * @param CENTER where the sphere will be centered
	 * @param RADIUS radius of the sphere
	 * @param HORIZONTAL_STEPS Horizontal resolution of the sphere (greater than 2)
	 * @param VERTICAL_STEPS Vertical resolution of the sphere (greater than 2)
	 * @param TEXTURE Material to be used on the sphere
	 * @return The sphere brush
	 * 
	 */
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
	
	/**
	 * Creates a semi-sphere brush
	 * 
	 * @param CENTER where the sphere will be centered
	 * @param RADIUS radius of the sphere
	 * @param HORIZONTAL_STEPS Horizontal resolution of the sphere (greater than 2)
	 * @param VERTICAL_STEPS Vertical resolution of the sphere (greater than 2)
	 * @param TEXTURE Material to be used on the sphere
	 * @return The semi-sphere brush
	 * 
	 * @see Brush#sphere(Point, float, int, int, String) Brush.sphere
	 */
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

/**
 * Entity represents a doom3 game entity.
 * 
 * There are a lot of kind of entities, player, weapons, functions, etc. Basically they are
 * a collection of properties and values associated with that properties.
 * 
 * Every entity have some common properties like name, origin and classname, but there are
 * a lot of different properties for different entities.
 * 
 * This implemntation uses a Map to store each property as one key and its value as the value
 * of the key.
 * 
 * There are some static methods that returns some usefull entities.
 */
class Entity{
	static public int entityCounter = 0;
	public int entityNumber;
	
	// We are going to use a LinkedHashMap to store the properties and values of a entity,
	// the main difference between LinkedHashMap and HashMap is that the iteration is sorted
	// as the keys are inserted in the Map.
	private LinkedHashMap<String,String> properties;
	
	// Default constructor, each entity will hold a entityNumber for using it as the name if
	// no one is specified.
	public Entity(){
		
		entityNumber = entityCounter;
		entityCounter++;
		
		properties = new LinkedHashMap<String,String>();
	
		// All the entities have a classname, name and origin, so set it to a default value
		
		setClassName("no_classname");
		setName("entity_" + entityNumber);
		setOrigin(0,0,0);
	}
	/**
	 * This constructor uses the default one to set the initial values and its parameter
	 * and using the CLASS_NAME to set the classname property.
	 *  
	 * @param CLASS_NAME the classname of the entity (info_player_start, weapon_...)
	 */
	public Entity(String CLASS_NAME){
		
		this();
		
		setClassName(CLASS_NAME);
		
	}
	/**
	 * Same as the above constructor this add ORIGN.
	 * There are a lot of simple entities that only use origin and classname as it's properties
	 * 
	 * @param CLASS_NAME the classname of the entity (info_player_start, weapon_...)
	 * @param ORIGIN where the entity will be positioned
	 */
	public Entity(String CLASS_NAME, Point ORIGIN){
		
		this(CLASS_NAME);
		
		setOrigin(ORIGIN);
		
	}
	
	// Setters & getters
	
	public Entity setProperty(String PROPERTY, String VALUE){ properties.put(PROPERTY, VALUE); return this; }
	
	public String getProperty(String PROPERTY){	return properties.get(PROPERTY); }
	
	/**
	 * Set the a property that is a Point
	 * 
	 * @param PROPERTY property name
	 * @param P point
	 * @return an entity
	 */
	public Entity setPointProperty(String PROPERTY, Point P){
		
		setProperty(PROPERTY, P.x + " " + P.y + " " + P.z);
		
		return this;
	}
	
	/**
	 * Gets a property that is a Point
	 * 
	 * @param PROPERTY name of the property
	 * @return the Point that this property holds
	 */
	public Point getPointProperty(String PROPERTY){
		
		Scanner parser = new Scanner(getProperty("origin"));
		
		return new Point(parser.nextFloat(),parser.nextFloat(),parser.nextFloat());
		
	}
	
	/**
	 * Sets the class name of a property
	 * 
	 * @param CLASSNAME classname value
	 * @return an Entity
	 */
	public Entity setClassName(String CLASSNAME){	setProperty("classname", CLASSNAME);  return this; }
	
	/**
	 * Gets the class name of an Entity
	 * 
	 * @return the class name
	 */
	public String getClassName(){ return getProperty("classname"); }
	/**
	 * Sets the name of the Entity
	 * 
	 * @param NAME entity name
	 * @return an Entity
	 */
	public Entity setName(String NAME){ setProperty("name", NAME); return this; }
	/**
	 * Gets the name of an Entity
	 * 
	 * @return the entity name
	 */
	public String getName(){ return getProperty("classname"); }
	/**
	 * Sets the position of an entity
	 * 
	 * @param X x coordinate
	 * @param Y y coordinate
	 * @param Z z coordinate
	 * @return the Entity
	 */
	public Entity setOrigin(float X, float Y, float Z){ return setOrigin(new Point(X,Y,Z)); }
	/**
	 * Sets the position of an entity
	 * 
	 * @param P position of the entity
	 * @return the Entity
	 */
	public Entity setOrigin(Point P){ return setPointProperty("origin", P); };
	/**
	 * Gets the position/origin of an entity
	 * 
	 * @return Position of the entity
	 */
	public Point getOrigin(){
		
		return getPointProperty("origin");
		
	}
	/**
	 * Prints the entity following the doom3 map specification
	 * 
	 * @param OUT PrintWriter object to write in
	 */
	public void print(PrintWriter OUT){
		
		OUT.println("{");
		
		for( String key : properties.keySet() ){
			
			String value = properties.get(key);	
			
			OUT.println("\"" + key + "\" \"" + value + "\"");
		}
		
		OUT.println("}");
		
	}
	
	// Entities factory / Static entities creators
	
	/**
	 * Returns a weapon_rocketlauncher entity
	 * 
	 * @return a weapon_rocketlauncher entity
	 */
	static Entity WeaponRocketLauncher(){ return new Entity("weapon_rocketlauncher"); }
	/**
	 * Returns a monster_zombie_fat entity
	 * 
	 * @return a monster_zombie_fat entity
	 */
	static Entity MonsterZombieFat(){ return new Entity("monster_zombie_fat"); }
	/**
	 * Returns an info_player_start entity (where the player starts the level)
	 * 
	 * @param ANGLE where the player is aiming at the start
	 * @return an info_player_start entity
	 */
	static Entity InfoPlayerStart(float ANGLE){
		
		Entity entity = new Entity("info_player_start");
		
		entity.setProperty("angle", Float.toString(ANGLE));
		
		return entity;
		
	}
	/**
	 * Returns a light entity
	 * 
	 * @param CENTER center of the light
	 * @param RADIUS radius of the light
	 * @return a light entity
	 */
	static Entity Light(Point CENTER, Point RADIUS){
		
		Entity entity = new Entity("light");
		
		entity.setPointProperty("light_center", CENTER);
		entity.setPointProperty("light_radius", RADIUS);
		
		return entity;
		
	}
	/**
	 * Returns a light entity with default center and radius values
	 * 
	 * @return a light entity
	 */
	static Entity Light(){
		
		return Entity.Light(new Point(0,0,0), new Point(320,320,320));
		
	}
	/**
	 * Returns a fire entity
	 * 
	 * @return a fire entity
	 */
	static Entity FlameJet(){
		
		return new Entity().setClassName("func_emitter").setProperty("model", "flamejet.prt");
		
	}
		
}

/**
 * A map represents a doom3 map or level.
 * 
 * It holds a list of brushes and entities that represents the architectural of the level and the
 * elements that builds up the level.
 * 
 */
class Map{
	
	private ArrayList<Brush> brushes;
	private ArrayList<Entity> entities;
	
	/**
	 * Default constructor. Initializes the brushes and entities ArrayLists
	 */
	public Map(){
		
		brushes = new ArrayList<Brush>();
		entities = new ArrayList<Entity>();
		
	}
	/**
	 * Adds a brush to the map
	 * 
	 * @param B brush to add
	 */
	public void addBrush(Brush B){
		brushes.add(B);
	}
	/**
	 * Adds an entity to the map
	 * 
	 * @param E entity to add
	 */
	public void addEntity(Entity E){
		entities.add( E );
	}
	/**
	 * Print/emmit the content of the map following the doom3 map specification.
	 * 
	 * It prints out a header, followed the main worldspawn entity that holds all the brushes
	 * and finally the rest of entities.
	 * 
	 * @param OUT PrintWriter object to write to
	 */
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

	/**
	 * Add a room to the game centered at CENTER
	 * 
	 * @param WIDTH width of the room
	 * @param HEIGHT height of the room
	 * @param DEPTH depth of the room
	 * @param CENTER where the room will be centered
	 * @param WALL_WIDTH width of the walls
	 * @param TEXTURES materials to use on the walls
	 * 
	 * @see Brush#cube(float, float, float, float, float, float, String) Brush.cube
	 */
	public void addRoom(float WIDTH, float HEIGHT, float DEPTH, Point CENTER, float WALL_WIDTH, String[] TEXTURES){
		// Add a room to the map centered at CENTER
		
		float width2 = WIDTH / 2.0f;
		float height2 = HEIGHT / 2.0f;
		float depth2 = DEPTH / 2.0f;
		
		String[] textures;
		Brush b;
		
		// Left wall
		
		b = Brush.cube(WALL_WIDTH, 0, depth2, depth2, height2, height2, TEXTURES[0]);
		// Center it
		b.translate(CENTER);
		// Move it down
		b.translate(-width2-WALL_WIDTH,0,0);

		this.addBrush(b);
		
		// Right wall
		
		b = Brush.cube(0, WALL_WIDTH, depth2, depth2, height2, height2, TEXTURES[1]);
		// Center it
		b.translate(CENTER);
		// Move it down
		b.translate(width2+WALL_WIDTH,0,0);

		this.addBrush(b);
		
		// Front wall
		
		b = Brush.cube(width2, width2, WALL_WIDTH, 0, height2, height2, TEXTURES[2]);
		// Center it
		b.translate(CENTER);
		// Move it down
		b.translate(0,-depth2-WALL_WIDTH,0);

		this.addBrush(b);
		
		// Back wall
		
		b = Brush.cube(width2, width2, 0, WALL_WIDTH, height2, height2, TEXTURES[3]);
		// Center it
		b.translate(CENTER);
		// Move it down
		b.translate(0,depth2+WALL_WIDTH,0);

		this.addBrush(b);
		
		// The floor
		
		b = Brush.cube(width2, width2, depth2, depth2, 0, WALL_WIDTH, TEXTURES[4]);
		// Center it
		b.translate(CENTER);
		// Move it down
		b.translate(0,0,-height2-WALL_WIDTH);

		this.addBrush(b);
		
		// The top
	
		b = Brush.cube(width2, width2, depth2, depth2, WALL_WIDTH, 0, TEXTURES[5]);
		// Center it
		b.translate(CENTER);
		// Move it down
		b.translate(0,0,height2+WALL_WIDTH);

		this.addBrush(b);

	}
	/**
	 * Adds to the map a sphere with cubes around it, like a star
	 * @param CENTER where the object will be centered
	 * @param RADIUS size of the sphere
	 * @param STEPS resolution of the sphere
	 * @param INITIAL_CUBE_SIZE the cubes start with this size
	 * @param FINAL_CUBE_SIZE the cubes ends with that size
	 * @param CUBES number of cubes in each arm
	 * @param HORIZONTAL_CUBES number of horizontal arms
	 * @param VERTICAL_CUBES number of vertical arms
	 * @param GAP space between cubes
	 * @param SPHERE_TEXTURE material of the sphere
	 * @param CUBES_TEXTURE material of the cubes
	 * 
	 * @see Brush#cube(float, String) Brush.cube
	 * @see Brush#sphere(Point, float, int, int, String) Brush.sphere
	 */
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

/**
 * Application that generates a doom3 game map.
 * 
 * It uses a Map object that holds the brushes and the entities that makes up the level.
 * 
 * Your work is to decide which brushes and entities suits your needs.
 * 
 * 
 * @see Map Map
 * @see Brush Brush
 * @see Entity Entity
 * @see Plane Plane
 * @see Point Point
 * 
 */
public class Doom3MapGenerator {

	public static void main(String[] args) {
	
		Map mapa = new Map();
		Brush b;
				
		// Textures for the main room
		String[] textures = {
				"textures/base_floor/a_bluetex1a_02","textures/base_floor/a_bluetex1a_02",
				"textures/base_floor/a_bluetex1a_02","textures/base_floor/a_bluetex1a_02",
				"textures/base_floor/a_bluetex1a_02","textures/skies/hellsky2"
		};
		
		// The main room
		mapa.addRoom(2048, 384, 2048, new Point(0,0,192), 16, textures);
		
		// Some blocks
		for(int X = -1000; X < 1000; X+=200){
			for( int Y = -1000; Y < 1000; Y+=200){
				
				b = Brush.cube(25, 25, 25, 25, 25, 25, "textures/base_floor/a_bluetex1a_02");
				
				b.translate(X, Y, 25);
							
				mapa.addBrush(b);
			}
		}
		
		// A star in the middle of the room
		mapa.addSphereWithCubes(new Point(0,0,192), 64, 8, 16, 4, 4, 4, 4, 5, "textures/rock/greenrocks2_rockbase", "textures/rock/sharprock_dark");
		
		// Add some entities
		Entity entity;
		
		// Some fire around the star
		mapa.addEntity( Entity.FlameJet().setOrigin(128,128,10) );
		mapa.addEntity( Entity.FlameJet().setOrigin(128,-128,10) );
		mapa.addEntity( Entity.FlameJet().setOrigin(-128,128,10) );
		mapa.addEntity( Entity.FlameJet().setOrigin(-128,-128,10) );
		
		// The player will start in one corner
		entity = Entity.InfoPlayerStart(-135.0f).setOrigin(new Point(900,900,80));
		
		mapa.addEntity(entity);
		
		// A rocket launcher
		mapa.addEntity( Entity.WeaponRocketLauncher().setOrigin(900,800,20) );
		
		// Some bad guys at the opposite side than the player
		for(int X = -900; X < 900; X += 200){
		
			entity = Entity.MonsterZombieFat().setOrigin(X,-900,10);
			
			mapa.addEntity(entity);
			
		}
		
		// Some light to see something
		
		for(int X = -1000; X < 1000; X+=400){
			for( int Y = -1000; Y < 1000; Y+=400){
				
				Entity l = Entity.Light().setOrigin(X+25, Y+25, 80);
				
				mapa.addEntity(l);
			}
		}
		
		// The output stage
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

