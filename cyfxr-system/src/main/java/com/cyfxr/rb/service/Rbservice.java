package com.cyfxr.rb.service;


import com.cyfxr.system.domain.CyfxSearchModel;

import java.util.List;
import java.util.Map;

public interface Rbservice {
    List<Object[]> getRb1(CyfxSearchModel searchModel);

    List<Object[]> getRb2(CyfxSearchModel searchModel);

    List<Object[]> getRb3(CyfxSearchModel searchModel);

    List<Object[]> getRb4(CyfxSearchModel searchModel);
}
