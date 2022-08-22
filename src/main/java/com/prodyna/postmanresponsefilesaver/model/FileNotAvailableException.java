package com.prodyna.postmanresponsefilesaver.model;

public class FileNotAvailableException extends RuntimeException{

  public FileNotAvailableException(String message) {
    super(message);
  }
}
