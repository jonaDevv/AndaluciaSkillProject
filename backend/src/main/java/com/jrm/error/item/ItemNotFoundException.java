package com.jrm.error.item;

public class ItemNotFoundException extends RuntimeException {

    
	public ItemNotFoundException(Long id) {
		super("No se encuentra el item con id: " + id);
	}

}
