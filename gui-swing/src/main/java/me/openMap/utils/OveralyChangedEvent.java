package me.openMap.utils;

import java.util.EventObject;

public class OveralyChangedEvent extends EventObject
{
	private static final long serialVersionUID = 1L;
	
	public OveralyChangedEvent(Object source) {
		super(source);
	}

}