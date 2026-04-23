package com.akeir.restaurant.integration.nfe;

import com.akeir.restaurant.dto.NFeEmissionRequest;
import com.akeir.restaurant.dto.NFeEmissionResult;

public interface NFeService {

    NFeEmissionResult emit(NFeEmissionRequest request);
}