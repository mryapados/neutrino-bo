package org.neutrinocms.bo.wrapper.light;

import java.util.List;

public class MoveRequestWrapper{
	private String action;
	private List<String> items;
	private String newPath;
	public String getAction() {
		return action;
	}
	public List<String> getItems() {
		return items;
	}
	public void setItems(List<String> items) {
		this.items = items;
	}
	public String getNewPath() {
		return newPath;
	}
	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}
	public void setAction(String action) {
		this.action = action;
	}
}