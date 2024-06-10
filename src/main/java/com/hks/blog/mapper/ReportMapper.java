package com.hks.blog.mapper;

import com.hks.blog.entity.Report;
import com.hks.blog.response.ReportResponse;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class ReportMapper {

    public ReportResponse toReportResponse(Report report) {
        return ReportResponse.builder()
                .report(report.getSubject())
                .username(report.getUser().getFullName())
                .build();
    }
}
