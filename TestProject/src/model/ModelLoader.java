package model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;


/**
 * Class Deals with loading 3d models into memory
 * Storing positional data about the model in a VAO
 * VAO store VBO, whilst VBO store data of the model
 * VBO - vertex buffer objects - http://wiki.lwjgl.org/wiki/Using_Vertex_Buffer_Objects_(VBO)
 * @author mikieJ
 *
 */

public class ModelLoader {

	//keep track off all vao and vbos in memeory so they are removed correctly
	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();

	
	public RawModel loadToVAO(float[] positions, int[] indices)
	{

		
		//create empty VAO
		int vaoID = createVAO();
		
		//take in the indices VBO then bind the indices buffer to VAO
		bindIndicesBuffer(indices);
		
		
		storeDataInAttributeList(0, positions);
		unbindVAO();
		
		//positions divided by three as each vertex has x,y,z
		return new RawModel(vaoID, indices.length);
	}

	
	/**
	 * Method gets run on completion of program to remove all unnecessary storage of VAO and VBOs - release memory
	 */
	public void cleanUpVBOSandVAOs()
	{
		for(int vao: vbos)
		{
			GL30.glDeleteVertexArrays(vao);
		}
		
		for(int vbo: vbos)
		{
			GL15.glDeleteBuffers(vbo);
		}
	}

	//create a new empty VAO - return ID of this created VAO
	private int createVAO()
	{
		//create an empty VAO and return its ID
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		//activate it so it can be manipulated - this will stay bound until it is unbound - 
		//hence need for unbindVAO 
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	//store data into attribute list of VAO
	private void storeDataInAttributeList(int attributeNumber, float[] data)
	{
		//item gets stored in VAO as a VBO - this there creates an empty VBO
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		//bind the VBO to the vboID so it can be used
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,  vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		//store into VBO, type, the data, and if static and never to be used again - or is it going to be edited.
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER,  buffer,  GL15.GL_STATIC_DRAW);
		
		//now put VBO into VAO
		//param1 - position in VAO, param2 -length of vertices (3 as its x,y,z), param3 - type of data (Float in this case)  
		//param4 - data normalised or not, param5 - distance between vertices - is there data between then - NO = 0;
		//param6 - offset - begin at start = 0;
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0 ,0);
		
		//finished with VBO - unbind it - use zero
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	
	//unbinds the VAO once you have finished using it (this is obligatory)	
	private void unbindVAO() {

		//providing 0 - unbound currently bound VAO
		GL30.glBindVertexArray(0);
	}
	
	//load indices buffer and bind to VAO wish to render.
	public void bindIndicesBuffer(int[] indicies)
	{
		//Id of buffer
		int vboID = GL15.glGenBuffers();
		//adding it to array so that i can be deleted later on in the clear up when game closed
		vbos.add(vboID);
		//bind the buffer. Need to do this to use it - type of vbo is element array buffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		//store indices into vbo - must store in int buffer first. 
		IntBuffer buffer = storeDataInIntBuffer(indicies);
		//store data into vbo - how its going to be used - draws static - never editing data. 
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

	}
	
	//store indices into vbo - must store in int buffer first. 
	private IntBuffer storeDataInIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	
	private FloatBuffer storeDataInFloatBuffer(float[] data)
	{
		//Create empty float buffer size of float[] data
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		//flip means to close the writing get ready for being called
		buffer.flip();
		return buffer;
		
	}
}
