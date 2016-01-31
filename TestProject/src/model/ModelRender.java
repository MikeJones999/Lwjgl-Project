package model;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
/**
 * This class renders the models from the VAOs
 * @author mikieJ
 *
 */
public class ModelRender {

	//render every frame - prepares openGl to render game
	public void prepare()
	{
		//clear the colour from last frame - using a red colour
		GL11.glClearColor(1, 0,  0, 1);
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	
	}
	
	public void render(RawModel model)
	{
		//any time you want to do something to VAO - MUST BIND IT - 
		//therefore present the models vaO ID
		GL30.glBindVertexArray(model.getVaoID());
		
		
		//activate the attribute list where data is stored - hard coded this to zero at present
		GL20.glEnableVertexAttribArray(0);
		//now render - 1. know what type to render (triangles) 2. number of vertices to render - which is in the model
		//3. type of data -  4. offset where to start 0 at the beginging
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		//now finished - clear vertexAttributeArray - again present 0 to clear
		GL20.glDisableVertexAttribArray(0);
		
		//unbvind VAO
		GL30.glBindVertexArray(0);
		
	}
}
