package mod.beethoven92.betterendforge.common.tileentity;

import mod.beethoven92.betterendforge.common.block.template.EndFurnaceBlock;
import mod.beethoven92.betterendforge.common.init.ModTileEntityTypes;
import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class EndFurnaceTileEntity extends TileEntityFurnace implements ITickable {

	public EndFurnaceTileEntity() {
		super();
	}

	public void update()
	{
		boolean flag = this.isBurning();
		boolean flag1 = false;

		if (this.isBurning())
		{
			--this.furnaceBurnTime;
		}

		if (!this.world.isRemote)
		{
			ItemStack itemstack = this.furnaceItemStacks.get(1);

			if (this.isBurning() || !itemstack.isEmpty() && !((ItemStack)this.furnaceItemStacks.get(0)).isEmpty())
			{
				if (!this.isBurning() && this.canSmelt())
				{
					this.furnaceBurnTime = getItemBurnTime(itemstack);
					this.currentItemBurnTime = this.furnaceBurnTime;

					if (this.isBurning())
					{
						flag1 = true;

						if (!itemstack.isEmpty())
						{
							Item item = itemstack.getItem();
							itemstack.shrink(1);

							if (itemstack.isEmpty())
							{
								ItemStack item1 = item.getContainerItem(itemstack);
								this.furnaceItemStacks.set(1, item1);
							}
						}
					}
				}

				if (this.isBurning() && this.canSmelt())
				{
					++this.cookTime;

					if (this.cookTime == this.totalCookTime)
					{
						this.cookTime = 0;
						this.totalCookTime = this.getCookTime(this.furnaceItemStacks.get(0));
						this.smeltItem();
						flag1 = true;
					}
				}
				else
				{
					this.cookTime = 0;
				}
			}
			else if (!this.isBurning() && this.cookTime > 0)
			{
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
			}

			if (flag != this.isBurning())
			{
				flag1 = true;
				EndFurnaceBlock.setState(this.isBurning(), this.world, this.pos);
			}
		}

		if (flag1)
		{
			this.markDirty();
		}
	}

}
