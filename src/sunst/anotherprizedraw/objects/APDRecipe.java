package sunst.anotherprizedraw.objects;

import org.bukkit.inventory.ItemStack;
import sunst.anotherprizedraw.managers.APDManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class APDRecipe {
	private ItemStack[] matrix;
	private ItemStack result;
	
	public static Map<APDRecipe, ItemStack> allAPDRecipe = new HashMap<>();
	
	public APDRecipe(ItemStack[] matrix, ItemStack result) {
		ItemStack[] addMatrix = new ItemStack[9];
		for(int i=0; i<9; i++){
			if(matrix[i]!=null)
				addMatrix[i] = matrix[i].clone();
		}
		
		this.matrix = addMatrix;
		this.result = result.clone();
	}
	
	public APDRecipe(ItemStack[] matrix) {
		ItemStack[] addMatrix = new ItemStack[9];
		for(int i=0; i<9; i++){
			if(matrix[i]!=null)
				addMatrix[i] = matrix[i].clone();
		}
		
		this.matrix = addMatrix;
	}


	public void setMatrix(ItemStack[] matrix) {
		ItemStack[] addMatrix = new ItemStack[9];
		for(int i=0; i<9; i++){
			if(matrix[i]!=null)
				addMatrix[i] = matrix[i].clone();
		}

		this.matrix = addMatrix;
	}

	public void setResult(ItemStack result) {
		this.result = result.clone();
	}

	public ItemStack[] getMatrix() {
		return matrix;
	}

	public ItemStack getResult() {
		return result;
	}


	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		APDRecipe apdRecipe = (APDRecipe) o;
		
		if(this.result == null || apdRecipe.result == null)
			return APDManager.isItemArrayEquals(this.matrix, apdRecipe.matrix);
		
		return APDManager.isItemArrayEquals(this.matrix, apdRecipe.matrix) &&
				result.equals(apdRecipe.result);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(matrix);
	}
}
