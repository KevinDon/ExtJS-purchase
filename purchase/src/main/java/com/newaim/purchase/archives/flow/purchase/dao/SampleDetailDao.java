package com.newaim.purchase.archives.flow.purchase.dao;

import com.newaim.purchase.archives.flow.purchase.entity.SampleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleDetailDao extends JpaRepository<SampleDetail, String>, JpaSpecificationExecutor<SampleDetail> {

    List<SampleDetail> findBySampleId(String sampleId);
}
