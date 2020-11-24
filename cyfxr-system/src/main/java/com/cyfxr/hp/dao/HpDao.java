package com.cyfxr.hp.dao;

import java.util.List;
import java.util.Map;

public interface HpDao {

    public List<Map<String, Object>> getCYFXBB_SY_Line();

    public List<Map<String, Object>> getCYFXBB_SY_Bar();

    public List<Map<String, Object>> getCYFXBB_SY_Scroll();
}
