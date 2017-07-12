package org.neutrinocms.bo.wrapper.light;

import java.util.List;

public class CompressRequestWrapper{
	private String action;
	private List<String> items;
	private String compressedFilename;
	private String destination;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public List<String> getItems() {
		return items;
	}
	public void setItems(List<String> items) {
		this.items = items;
	}
	public String getCompressedFilename() {
		return compressedFilename;
	}
	public void setCompressedFilename(String compressedFilename) {
		this.compressedFilename = compressedFilename;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
}