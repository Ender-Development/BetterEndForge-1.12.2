package mod.beethoven92.betterendforge.common.util.sdf.operator;

import mod.beethoven92.betterendforge.common.util.sdf.vector.Vector3f;

import java.util.function.Consumer;


public class SDFCoordModify extends SDFUnary
{
	private static final Vector3f POS = new Vector3f();
	private Consumer<Vector3f> function;

	public SDFCoordModify setFunction(Consumer<Vector3f> function)
	{
		this.function = function;
		return this;
	}

	@Override
	public float getDistance(float x, float y, float z)
	{
		POS.set(x, y, z);
		function.accept(POS);
		return this.source.getDistance(POS.getX(), POS.getY(), POS.getZ());
	}

}
