package aed.utils;

import java.util.Random;

public class ControlledRandom extends Random {
	
	private static final long serialVersionUID = 1L;
	private Random randomGenerator;
	private int range;
	private boolean limitedRange;
	
	public ControlledRandom(long seed, int range)
	{
		this.randomGenerator = new Random(seed);
		this.range = range;
		this.limitedRange = true;
	}
	
	public ControlledRandom(long seed)
	{
		this.randomGenerator = new Random(seed);
		this.limitedRange = false;
	}
	
	public ControlledRandom()
	{
		this.randomGenerator = new Random();
		this.limitedRange = false;
	}
	
	@Override
	public int nextInt()
	{
		if(this.limitedRange) return this.randomGenerator.nextInt(this.range);
		else return this.randomGenerator.nextInt();
	}
}
