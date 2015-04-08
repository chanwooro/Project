package com.bowlingclub;

import java.util.ArrayList;
import java.util.List;

public class SimpleMaths {


	public SimpleMaths ()
	{
		
	}
	
	public String removeZero (String CommaNumbers)
	{
		String out = "";
		String[] sets = CommaNumbers.split(",");
		for (int i = 0; i < sets.length; i++)
		{
			String temp = sets[i].replaceAll("\\s+", "");
			
			if (Integer.parseInt(temp) != 0)
			{
				out += temp + ",";
			}
		}
		
		if (out.endsWith(",")) {
			out = out.substring(0, out.length() - 1);
		}
		
		return out.replaceAll("\\s+", "");
	}

	public boolean inspectCommaNum(String scoreSet) {
		// TODO Auto-generated method stub
		if (scoreSet.matches(""))
		{
			return true;
		}
		
		String[] sets = scoreSet.split(",");
		
		for (int i = 0; i < sets.length; i++){
			String temp = sets[i].replaceAll("\\s+", "");
			try {
				   int num = Integer.parseInt(temp);
				   //Log.d("Math", "Item: " + num);
				   if ((num <= 0) || (num > 300))
				   {
					   return false;
				   }
				} catch (NumberFormatException e) {
					return false;
				}
		}
		
		return true;
	}
	
	public int getAverage (List<Integer> nums)
	{
		int sum = 0;
		int count = 0;
	
		for (int a=0; a < nums.size(); a++)
		{
			sum += nums.get(a);
			if (nums.get(a) != 0)
			{
				count++;	
			}
		}
		
		if (count == 0) return 0;
		sum = sum / count;
		return sum;
	}
	
	public int getMedian (List<Integer> nums)
	{
		List<Integer> numbers = new ArrayList<Integer>();
		int mid = 0;
		
		for (int a=0; a < nums.size(); a++)
		{
			if (nums.get(a) != 0)
			{
				numbers.add(nums.get(a));
			}
		}
		
		if (numbers.size() == 0) return 0;
		
		for (int x=0; x<numbers.size();x++)
		{
			for (int y=0; y<numbers.size()-1;y++)
			{
				if (numbers.get(y) > numbers.get(y+1))
				{
					int temp = numbers.get(y);
					numbers.set(y, numbers.get(y+1));
					numbers.set(y+1, temp);
				}
			}
		}
	
		
		int index = numbers.size()/2;
		
		mid = numbers.get(index);
		return mid;
	}
	
	public int getBest (List<Integer> nums)
	{
		int max = 0;
		
		for (int a=0; a < nums.size(); a++)
		{
			if (max < nums.get(a))
			{
				max = nums.get(a);
			}
		}
		
		return max;
	}
	
	public int getWorst (List<Integer> nums)
	{
		int min = 301;
			
		for (int a=0; a < nums.size(); a++)
		{
			if ((min > nums.get(a)) && (nums.get(a) != 0))
			{
				min = nums.get(a);
			}
		}
		
		if (min == 301) return 0;
		return min;
	}
}

