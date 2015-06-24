package controller;

public class Timer {
	private long startTime = 0;
	private long endTime = 0;

	public void start() {
		this.startTime = System.currentTimeMillis()/1000;
	}

	public void end() {
		this.endTime = System.currentTimeMillis()/1000;
	}

	public long getStartTime() {
		return this.startTime;
	}

	public long getEndTime() {
		return this.endTime;
	}

	public long getTotalTime() {
		return this.endTime - this.startTime;
	}
}