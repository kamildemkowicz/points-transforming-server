package points.transforming.app.server.controllers;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import points.transforming.app.server.exceptions.report.ViolationReport;
import points.transforming.app.server.exceptions.tachymetry.ControlNetworkPointsException;
import points.transforming.app.server.exceptions.tachymetry.EmptyMeasuringStationsListException;

@RestControllerAdvice(annotations = TachymetryExceptionProcessing.class)
public class TachymetryControllerAdvice {
    @ExceptionHandler(EmptyMeasuringStationsListException.class)
    ResponseEntity<String> handleEmptyMeasuringStationsListException(EmptyMeasuringStationsListException e) {
        return ResponseEntity.badRequest().body(e.toString());
    }

    @ExceptionHandler(ControlNetworkPointsException.class)
    ResponseEntity<String> handleCreateMeasurementBadRequestException(ControlNetworkPointsException e) {
        return ResponseEntity.badRequest().body(e.toString());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<List<ViolationReport>> handleCreateTachymetryBadRequestException(ConstraintViolationException e) {
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ViolationReport> handleDuplicateTachymetryCreationException(DataIntegrityViolationException e) {
        var report = new ViolationReport(
                e.getCause().getCause().getMessage(),
                "The tachymetry with given payload already exists!",
                null
        );

        return ResponseEntity.badRequest().body(report);
    }
}
