package com.dragonomous.fartteleports;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FartSound
{
	CLASSIC("Classic brap", "classic.wav"),
	SQUEAKY("Squeaker", "squeaky.wav"),
	RUMBLER("Long rumbler", "rumbler.wav"),
	RANDOM("Random", null);

	private final String name;
	private final String file;

	@Override
	public String toString()
	{
		return name;
	}
}
