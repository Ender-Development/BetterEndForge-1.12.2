//package mod.beethoven92.betterendforge.data;
//
//import java.util.List;
//import java.util.Map;
//import java.util.function.BiConsumer;
//import java.util.function.Consumer;
//import java.util.function.Supplier;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.datafixers.util.Pair;
//
//import net.minecraft.data.DataGenerator;
//import net.minecraft.data.LootTableProvider;
//import net.minecraft.loot.LootParameterSet;
//import net.minecraft.loot.LootParameterSets;
//import net.minecraft.loot.LootTable;
//import net.minecraft.loot.ValidationTracker;
//import net.minecraft.loot.LootTable.Builder;
//import net.minecraft.util.ResourceLocation;
//
//public class ModLootTableProvider extends LootTableProvider
//{
//	public ModLootTableProvider(DataGenerator dataGeneratorIn)
//	{
//		super(dataGeneratorIn);
//	}
//
//	@Override
//	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> getTables()
//	{
//		return ImmutableList.of(Pair.of(ModBlockLootTables::new, LootParameterSets.BLOCK),
//				                Pair.of(ModEntityLootTables::new, LootParameterSets.ENTITY));
//	}
//
//	@Override
//	protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker)
//	{
//	}
//}
