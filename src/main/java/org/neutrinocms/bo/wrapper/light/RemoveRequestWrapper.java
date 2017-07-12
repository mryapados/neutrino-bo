package org.neutrinocms.bo.wrapper.light;

import java.util.List;

public class RemoveRequestWrapper{
	private String action;
	private List<String> items;
	public String getAction() {
		return action;
	}
	public List<String> getItems() {
		return items;
	}
	public void setItems(List<String> items) {
		this.items = items;
	}
	public void setAction(String action) {
		this.action = action;
	}
}