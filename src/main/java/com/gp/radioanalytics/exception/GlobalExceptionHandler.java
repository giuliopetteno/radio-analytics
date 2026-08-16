package com.gp.radioanalytics.exception;

import com.gp.radioanalytics.analytics.exception.AnalyticsExecutionException;
import com.gp.radioanalytics.analytics.report.exception.ReportNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(AnalyticsExecutionException.class)
	public ProblemDetail handleAnalyticsExecution(AnalyticsExecutionException ex) {
		log.error("Analytics execution failed: {}", ex.getMessage(), ex);

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		problemDetail.setTitle("Analytics execution failed");
		problemDetail.setProperty("timestamp", Instant.now());
		return problemDetail;
	}

	@ExceptionHandler(ReportNotAvailableException.class)
	public ProblemDetail handleReportNotAvailable(ReportNotAvailableException ex) {
		log.error("Analytics report not available: {}", ex.getMessage());

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problemDetail.setTitle("Analytics report not available");
		problemDetail.setProperty("timestamp", Instant.now());
		return problemDetail;
	}
}