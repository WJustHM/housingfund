package com.handge.housingfund.server.controllers.others;

import com.handge.housingfund.common.service.others.IDictionaryService;
import com.handge.housingfund.common.service.others.model.CommonDictionary;
import com.handge.housingfund.common.service.others.model.SingleDictionaryDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@Path("/dictionary")
public class DictionaryController {
	@Autowired
	private IDictionaryService dictionaryService;

	@GET
	@Consumes("application/json; charset=utf-8")
	@Produces("application/json; charset=utf-8")
	@Path(value = "")
	public Response getDictionary() throws IOException {
		HashMap<String, List<CommonDictionary>> result = dictionaryService.getDictionary();
		return Response.ok("getDictionary OK").status(200).entity(result).build();
	}
	@Path("/dictionaryDetail")
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getSingleDictionaryDetail(final @QueryParam("no") String code, final @QueryParam("type") String type) {
		SingleDictionaryDetail result = dictionaryService.getSingleDetail(code,type);
		return Response.ok("getSingleDictionaryDetail OK").status(200).entity(result).build();
	}
}
