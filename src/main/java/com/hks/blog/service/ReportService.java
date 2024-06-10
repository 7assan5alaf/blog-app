package com.hks.blog.service;

import com.hks.blog.dto.ReportDto;
import com.hks.blog.entity.Report;
import com.hks.blog.exception.EntityNotFound;
import com.hks.blog.mapper.ReportMapper;
import com.hks.blog.repository.ReportRepository;
import com.hks.blog.repository.UserRepository;
import com.hks.blog.response.MessageResponse;
import com.hks.blog.response.ReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReportMapper reportMapper;
    public MessageResponse createReport(ReportDto reportDto) {
        var user=userRepository.findByEmail(reportDto.getEmail())
                .orElseThrow(()->new EntityNotFound("User Not Found"));
        var report=Report.builder()
                .user(user)
                .subject(reportDto.getSubject())
                .build();
        reportRepository.save(report);
        return new MessageResponse("Report Created");
    }


    public List<ReportResponse>findAll(){
        var reports=reportRepository.findAll();
        List<ReportResponse>responses=reports
                .stream().map(reportMapper::toReportResponse)
                .toList();
        return responses;
    }

}
