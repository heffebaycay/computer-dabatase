package fr.heffebaycay.cdb.ui;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.heffebaycay.cdb.webservice.ICompanyRESTService;
import fr.heffebaycay.cdb.webservice.IComputerRESTService;

public class ComputerDatabaseCLILauncher {

  public static void main(String[] args) {

    AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
        "/applicationContextCLI.xml");

    ComputerDatabaseCLI cli = ctx.getBean(ComputerDatabaseCLI.class);
    
    IComputerRESTService computerService = ctx.getBean(IComputerRESTService.class);
    ICompanyRESTService companyService = ctx.getBean(ICompanyRESTService.class);
    
    cli.start(computerService, companyService);

    ctx.close();

  }

}
