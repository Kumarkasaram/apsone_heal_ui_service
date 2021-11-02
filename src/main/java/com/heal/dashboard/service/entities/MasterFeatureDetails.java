package com.heal.dashboard.service.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MasterFeatureDetails {
    List<MasterFeaturesBean> masterFeaturesBeans;
}
