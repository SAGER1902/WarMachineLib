package wmlib.loader.api.model;


import wmlib.WMConfig;
import wmlib.loader.ObjModel;
import wmlib.loader.part.ModelObject;
import wmlib.loader.part.Vertex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.BufferUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL21;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import wmlib.client.render.SARenderState;
public class ObjModelRenderer {
    public boolean isHidden;
    private ModelObject model;
    private int vertexCount = 0;
    private boolean compiled;
	private VertexBuffer vertexBuffer;
    private int vaoId;
    public ObjModelRenderer(ObjModel parent, ModelObject modelForRender) {
        this.model = modelForRender;
    }
    public String getName() {
        return model.name;
    }
    @OnlyIn(Dist.CLIENT)
    public void render(int light) {
        if (!this.isHidden) {
            if (!this.compiled){
				compileVAO(light);
            }
			renderVAO();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void compileVAO(int light) {
        List<Float> vertexData = new ArrayList<Float>();
        int stride = model.renderByVAO(vertexData);
		vertexCount = vertexData.size() / stride;
		
        this.vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        FloatBuffer pos_floatBuffer = BufferUtils.createFloatBuffer(vertexCount * 3);
        FloatBuffer tex_floatBuffer = BufferUtils.createFloatBuffer(vertexCount * 2);
        FloatBuffer normal_floatBuffer = BufferUtils.createFloatBuffer(vertexCount * 3);
        int count = 0;
        for (int i = 0; i < vertexData.size(); i++) {
            if (count == 8) {
                count = 0;
            }
            if (count < 3) {
                pos_floatBuffer.put(vertexData.get(i));
            } else if (count < 5) {
                tex_floatBuffer.put(vertexData.get(i));
            } else if (count < 8) {
                normal_floatBuffer.put(vertexData.get(i));
            }
            count++;
        }
        pos_floatBuffer.flip();
        tex_floatBuffer.flip();
        normal_floatBuffer.flip();
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        int pos_vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, pos_vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, pos_floatBuffer, GL15.GL_STATIC_DRAW);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        int tex_vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tex_vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, tex_floatBuffer, GL15.GL_STATIC_DRAW);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);

        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        int normal_vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normal_vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normal_floatBuffer, GL15.GL_STATIC_DRAW);
        GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        GL15.glDeleteBuffers(pos_vbo);
        GL15.glDeleteBuffers(tex_vbo);
        GL15.glDeleteBuffers(normal_vbo);
        this.compiled = true;
    }
	
    private void renderVAO() {
		//renderType.setupRenderState();
        GL30.glBindVertexArray(vaoId);
		//renderType.format().setupBufferState(0);
        GL11.glDrawArrays(model.glDrawingMode, 0, vertexCount);
		//renderType.format().clearBufferState();
        GL30.glBindVertexArray(0);
		//renderType.clearRenderState();
    }
}