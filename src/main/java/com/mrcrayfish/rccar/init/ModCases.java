package com.mrcrayfish.rccar.init;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mrcrayfish.rccar.Reference;
import com.mrcrayfish.rccar.object.Case;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModCases 
{
	public static ImmutableList<Case> CASES;
	
	public static void init()
	{
		ImmutableList.Builder<Case> builder = ImmutableList.builder();
		Reader reader = new InputStreamReader(ModCases.class.getResourceAsStream(String.format("/assets/%s/cases.json", Reference.MOD_ID)));
		Gson gson = new Gson();
		ArrayList<Case> cases = gson.fromJson(reader, new TypeToken<ArrayList<Case>>(){}.getType());
		for(int i = 0; i < cases.size(); i++) {
			Case c = cases.get(i);
			c.item = new Item().setUnlocalizedName("case_" + c.id).setRegistryName("case_" + c.id);
			c.ordinal = i;
			builder.add(c);
		}
		CASES = builder.build();
	}
	
	public static Case getCase(String id) 
	{
		if(CASES.size() == 0)
			throw new RuntimeException("No cases exist. This shouldn't happen!");
		
		for (Case c : CASES) 
		{
			if (c.id.equals(id))
			{
				return c;
			}
		}
		return CASES.get(0);
	}
	
	public static int length()
	{
		return CASES.size();
	}
}
