package mod.beethoven92.betterendforge.mixin;

import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MathHelper.class) //RecipeManager.class)
public class RecipeManagerMixin 
{
//	@Shadow
//	private Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes;
//
//	@Inject(method = "apply", at = @At(value = "RETURN"))
//	private void beSetRecipes(Map<ResourceLocation, JsonElement> map, IResourceManager resourceManager,
//			IProfiler profiler, CallbackInfo info)
//	{
//		recipes = ModRecipeManager.getMap(recipes);
//	}
//
//	@Shadow
//	private <C extends IInventory, T extends IRecipe<C>> Map<ResourceLocation, IRecipe<C>> getRecipes(IRecipeType<T> type)
//	{
//		return null;
//	}
//
//	@Overwrite
//	public <C extends IInventory, T extends IRecipe<C>> Optional<T> getRecipe(IRecipeType<T> type,
//			C inventory, World world)
//	{
//		Collection<IRecipe<C>> values = getRecipes(type).values();
//		List<IRecipe<C>> list = new ArrayList<IRecipe<C>>(values);
//		list.sort((v1, v2) -> {
//			boolean b1 = v1.getId().getNamespace().equals("minecraft");
//			boolean b2 = v2.getId().getNamespace().equals("minecraft");
//			return b1 ^ b2 ? (b1 ? 1 : -1) : 0;
//		});
//
//		return list.stream().flatMap((recipe) -> {
//			return Util.streamOptional(type.matches(recipe, world, inventory));
//		}).findFirst();
//	}
}
