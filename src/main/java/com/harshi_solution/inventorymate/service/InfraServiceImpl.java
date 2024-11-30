package com.harshi_solution.inventorymate.service;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harshi_solution.inventorymate.models.FreeApiLoginRequest;
import com.harshi_solution.inventorymate.models.FreeApiLoginResponse;
import com.harshi_solution.inventorymate.util.Constants;
import com.harshi_solution.inventorymate.util.ExtRestInvocationUtil;
import com.harshi_solution.inventorymate.util.SequenceIdTypeEnum;
import com.harshi_solution.inventorymate.util.ServiceProperties;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class InfraServiceImpl implements InfraService {
	
    private static final Logger log = LoggerFactory.getLogger(InfraServiceImpl.class);


	@Autowired
	private EntityManager em;

	@Autowired
	private ServiceProperties serviceProperties;

	@Autowired
	private ExtRestInvocationUtil extRestInvocationUtil;

	@Override
	public String generateNewSequence(SequenceIdTypeEnum seqIdTypeEnum) {
		Query nativeQuery = em.createNativeQuery("SELECT sequence_generator():seqIdType");
		String nextSeq = nativeQuery.setParameter("seqIdType", seqIdTypeEnum.getIdType()).getSingleResult().toString();
		log.info("The generated sequence is - {}", nextSeq);
		return nextSeq;
	}

	public FreeApiLoginResponse callFreeApiLogin(FreeApiLoginRequest freeApiLoginRequest) {
		log.info("Start of Open Api getWeather Details");
		String url = this.serviceProperties.getUrl(null, serviceProperties.getFreeApiLoginPath());
		MDC.put(Constants.EXTERNAL_SERVICE_URL, url);
		long startTime = System.currentTimeMillis();
		FreeApiLoginResponse response = extRestInvocationUtil.postData(url, freeApiLoginRequest,
				FreeApiLoginResponse.class);
		long duration = System.currentTimeMillis() - startTime;
		MDC.put("referenceIdForExtOut", response);
		log.info("End of FreeApiLogin");
		return response;

	}

}
