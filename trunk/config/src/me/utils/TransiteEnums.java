package me.utils;

public interface TransiteEnums {

	public enum DistanceUnitType {
		MI(1609.344),
		FT(0.305),
		M(1),
		KM(1000),
		NM(1852),;
		
		private double convertValue = 1;
		
		DistanceUnitType(double valueToMeters)
		{
			convertValue = valueToMeters;
		}
		
		public double toMeters(double value) {
			return value * convertValue;
		}	
	}
	
	
}
