package points.transforming.app.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import points.transforming.app.server.exceptions.MeasurementBadRequestException;
import points.transforming.app.server.exceptions.MeasurementNotFoundException;

@RestControllerAdvice(annotations = MeasurementExceptionProcessing.class)
public class MeasurementControllerAdvice {
    @ExceptionHandler(MeasurementNotFoundException.class)
    ResponseEntity<String> handleMeasurementNotFoundException(MeasurementNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MeasurementBadRequestException.class)
    ResponseEntity<String> handleCreateMeasurementBadRequestException(MeasurementBadRequestException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
