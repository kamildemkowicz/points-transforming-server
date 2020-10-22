package points.transforming.app.server.exceptions.error;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static points.transforming.app.server.exceptions.PointsTransformingException.UNKNOWN_ERROR_CODE;
import static points.transforming.app.server.exceptions.PointsTransformingException.UNKNOWN_ERROR_MESSAGE;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PointsTransformingErrorController implements ErrorController {

    private final MessageSource errorCodes;

    private final MessageSource errorMessages;

    @RequestMapping("/error/RequestRejectedException")
    public ResponseEntity<PointsTransformingErrorResponse> handleRequestRejectedException() {
        log.debug("handleRequestRejectedException");

        final String errorCode = errorCodes.getMessage(new DefaultMessageSourceResolvable(
            new String[] {"RequestRejectedException"}, null, "PTS0000"), Locale.getDefault());
        final String message = errorMessages.getMessage(new DefaultMessageSourceResolvable(
            new String[] {errorCode}, null, "Url malformed"), Locale.getDefault());

        return prepareErrorResponse(400, errorCode, message);
    }

    @RequestMapping("/error")
    public ResponseEntity<PointsTransformingErrorResponse> handleError(final HttpServletRequest request) {
        final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        log.debug("handleError: status={}", status);

        final Integer statusCode = (status != null) ? (Integer) status : 500;

        if (request.getAttribute("javax.servlet.error.exception") != null) {
            log.error("handleUnexpected:{}", request.getAttribute("javax.servlet.error.exception"));
        }

        final String errorCode = errorCodes.getMessage(new DefaultMessageSourceResolvable(
            new String[] {"status." + statusCode}, null, UNKNOWN_ERROR_CODE), Locale.getDefault());
        final String message = errorMessages.getMessage(new DefaultMessageSourceResolvable(
            new String[] {errorCode}, null, UNKNOWN_ERROR_MESSAGE), Locale.getDefault());

        return prepareErrorResponse(statusCode, errorCode, message);
    }

    private ResponseEntity<PointsTransformingErrorResponse> prepareErrorResponse(final Integer statusCode, final String errorCode, final String message) {
        PointsTransformingErrorResponse response = new PointsTransformingErrorResponse
            .Builder()
            .newError()
            .message(message)
            .errorCode(errorCode)
            .endError()
            .build();

        return ResponseEntity.status(statusCode).body(response);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
