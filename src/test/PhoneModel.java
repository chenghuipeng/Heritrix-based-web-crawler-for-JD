package test;

public class PhoneModel extends Model
{
	public String System;
	public String Color;

	public PhoneModel(String id, String name, String brand, String time,
			String orign, String system, String color, String price, String url)
	{
		super(id, name, brand, time, orign, price, url);
		this.System = system;
		this.Color = color;

	}

}
