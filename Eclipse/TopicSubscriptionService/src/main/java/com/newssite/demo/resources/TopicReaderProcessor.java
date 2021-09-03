package com.newssite.demo.resources;

import java.util.LinkedList;
import java.util.List;

import com.newssite.demo.interfaces.TopicProcessor;

public class TopicReaderProcessor<T> implements TopicProcessor {

	// Attributes
	private List<T> events;
	private Class<T> clazz;

	public TopicReaderProcessor(Class<T> clazz) {
		super();
		this.events = new LinkedList<T>();
		this.clazz = clazz;
	}

	// Getters
	public List<T> getEvents() {
		return events;
	}

	// Method
	@Override
	public void process(String key, Object message) {
		if (message != null && message.getClass() == clazz) {
			events.add(clazz.cast(message));
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + ((events == null) ? 0 : events.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TopicReaderProcessor other = (TopicReaderProcessor) obj;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		if (events == null) {
			if (other.events != null)
				return false;
		} else if (!events.equals(other.events))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TopicReaderProcessor [events=" + events + ", clazz=" + clazz + "]";
	}
}
