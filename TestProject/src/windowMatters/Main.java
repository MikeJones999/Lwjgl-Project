package windowMatters;

/**
 * This class handles the creation of the window.
 * @author mikieJ
 *
 */

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;  //access the lwjgl NULL comparison
import input.Input;
import model.ModelLoader;
import model.ModelRender;
import model.RawModel;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;


public class Main implements Runnable {

	//Variables
    private Thread thread;
	private boolean running = false;	
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private long window;
	
	private ModelLoader loader; 
	private ModelRender renderer;
	private RawModel model;
	
	
	public static void main(String[] args) {
		Main game = new Main();		
		game.start();		
	}	
	
	private void start()
	{	
		
		running = true;
		thread = new Thread(this, "ContinuousWindow");
		thread.start();
	}
	
	@Override
	public void run() { 
		
		
		init();
		createAndLoadModelAttributes();
		while(running)
		{	
			update();
			render();
						
			//if window should be closed then exit the loop
			if(glfwWindowShouldClose(window) == GL_TRUE)
			{
				running = false;
			}			
		}
		
		keyCallback.release();
		loader.cleanUpVBOSandVAOs();
	}

	public void createAndLoadModelAttributes()
	{
		   loader = new ModelLoader();
			renderer = new ModelRender();
			
			float[] vertices = {
								-0.5f, 0.5f, 0, //V0
								-0.5f, -0.5f, 0, //V1
								 0.5f, -0.5f, 0, //v2 
				                 0.5f, 0.5f, 0  //v3
							
			};
			
			int[] indices = {
							0,1,3, //Top left triangle
							3,1,2	//bottom right triangle
			};
			
			model= loader.loadToVAO(vertices, indices);
	}
	public void init()
	{
		//if not true after being executed then it will fail. - THIS MUST RETURN TRUE IN ORDER TO CONTINUE
		if(glfwInit() !=GL_TRUE)
		{
			//Once GLFW is initialised - window can be created
			System.err.println("GLFW initialisation has failed");
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		//indicate that the window we wish to create is to be made resizable
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		
		//           glfwCreateWindow(width, height, title, monitor, share)
		window = glfwCreateWindow(800, 600, "Mikes Tester Window", NULL, NULL);
		
		//if window is not populated with necessary bytes - then it fails
		if(window == NULL)
		{
			System.err.println("Failed to create window");
			glfwTerminate();
		    throw new RuntimeException("Failed to create the GLFW window");
		}
		
		
		glfwSetKeyCallback(window, keyCallback = new Input());
		
		
		//Returns the video resolution of primary monitor 
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		//
		
		 int WIDTH = 300;
	     int HEIGHT = 300;
		
		 // Centre the window
        glfwSetWindowPos(window, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2 );
		
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);        
        
        
	     //library can detect the context and make the OpenGL bindings available for use.
        GL.createCapabilities();
        
        // Enable v-sync
        glfwSwapInterval(1);        
        //show the window
        glfwShowWindow(window);
  
        //glClearColor(056f, 0.258f, 0.425f, 1.0f);        
        
        glEnable(GL_DEPTH_TEST);
        
        System.out.println("OPenGL: " + glGetString(GL_VERSION));    
    }
	
	public void update()
	{
		
		//events such as window closing - probably need a call back
		glfwPollEvents();
		
		//test to see if space bar is pressed
		if(Input.keys[GLFW_KEY_SPACE])
		{
			System.out.println("Space bar pressed");
			//glfwTerminate();			
		}
	}
	
	public void render()
	{
		renderer.prepare();
		renderer.render(model);
		//above must go before glfwSwapBuffers
		
		glfwSwapBuffers(window);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
	
	}


	
	
	
}
