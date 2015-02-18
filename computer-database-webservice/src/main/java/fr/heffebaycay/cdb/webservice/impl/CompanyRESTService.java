package fr.heffebaycay.cdb.webservice.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.heffebaycay.cdb.dto.CompanyDTO;
import fr.heffebaycay.cdb.dto.mapper.CompanyMapper;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.CompanyPageRequest;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.webservice.ICompanyRESTService;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@Service
@Path("/companies")
public class CompanyRESTService implements ICompanyRESTService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyRESTService.class);
  
  private static final Long NB_RESULTS_PAGE = 10L;
  
  @Autowired
  private ICompanyService companyService;
  
  @Autowired
  private CompanyMapper companyMapper;

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public CompanyDTO create(CompanyDTO companyDTO) {
    
    Company company = companyMapper.fromDTO(companyDTO);
    long companyId = companyService.create(company);
    
    companyDTO.setId(companyId);
    
    return companyDTO;
  }

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<CompanyDTO> findAll() {
    
    List<Company> companies = companyService.findAll();
    
    return companyMapper.toDTO(companies);
  }

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id: [0-9]+}")
  public CompanyDTO findById(@PathParam("id") long id) {
    
    Company company = companyService.findById(id);
    
    return companyMapper.toDTO(company);
  }

  @Override
  @DELETE
  @Path("/{id: [0-9]+}")
  public Response remove(@PathParam("id") long id) {
    
    companyService.remove(id);
    
    return Response.status(Status.NO_CONTENT).build();
    
  }
  
  @GET
  @Path("/page/{page: [0-9]+}")
  @Produces(MediaType.APPLICATION_JSON)
  public SearchWrapper<CompanyDTO> findAllPaged(@PathParam("page") long pageNumber) {
    
    Long offset = (pageNumber - 1) * NB_RESULTS_PAGE;

    CompanyPageRequest pageRequest = new CompanyPageRequest.Builder()
        .offset(offset)
        .nbRequested(NB_RESULTS_PAGE)
        .sortCriterion(CompanySortCriteria.ID)
        .sortOrder(SortOrder.ASC)
        .build();

    
    SearchWrapper<Company> companies = companyService.findAll(pageRequest);
    
    SearchWrapper<CompanyDTO> dtoWrapper = companyMapper.convertWrappertoDTO(companies);
    
    return dtoWrapper;
    
  }

}
