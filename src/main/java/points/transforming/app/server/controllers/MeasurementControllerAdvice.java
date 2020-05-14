package points.transforming.app.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import points.transforming.app.server.exceptions.MeasurementNotFoundException;
import points.transforming.app.server.exceptions.report.ViolationReport;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice(annotations = MeasurementExceptionProcessing.class)
public class MeasurementControllerAdvice {
    @ExceptionHandler(MeasurementNotFoundException.class)
    ResponseEntity<String> handleMeasurementNotFoundException(MeasurementNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<List<ViolationReport>> handleCreateMeasurementBadRequestException(ConstraintViolationException e) {
        var reports = new ArrayList<ViolationReport>();
        e.getConstraintViolations().forEach((constraintViolation -> {
                    var report = new ViolationReport(
                    constraintViolation.getPropertyPath().toString(),
                    constraintViolation.getMessage(),
                    constraintViolation.getInvalidValue() == null ? "null" : constraintViolation.getInvalidValue().toString()
                    );

            reports.add(report);
        })
        );

        return ResponseEntity.badRequest().body(reports);
    }
}
