package fr.heffebaycay.cdb.ui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ComputerDatabaseCLILauncher {

  public static void main(String[] args) {
    
    ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContextCLI.xml");
    
    ComputerDatabaseCLI cli = ctx.getBean(ComputerDatabaseCLI.class);
    cli.start(null);
  }
  
}
