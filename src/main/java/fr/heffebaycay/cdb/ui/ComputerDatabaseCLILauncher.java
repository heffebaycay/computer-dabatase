package fr.heffebaycay.cdb.ui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ComputerDatabaseCLILauncher {

  public static void main(String[] args) {
    
    ApplicationContext ctx = new AnnotationConfigApplicationContext("fr.heffebaycay.cdb");
    
    ComputerDatabaseCLI cli = ctx.getBean(ComputerDatabaseCLI.class);
    cli.start(null);
  }
  
}
