package fr.heffebaycay.cdb.ui;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ComputerDatabaseCLILauncher {

  public static void main(String[] args) {

    AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
        "/applicationContextCLI.xml");

    ComputerDatabaseCLI cli = ctx.getBean(ComputerDatabaseCLI.class);
    cli.start(null);

    ctx.close();

  }

}
