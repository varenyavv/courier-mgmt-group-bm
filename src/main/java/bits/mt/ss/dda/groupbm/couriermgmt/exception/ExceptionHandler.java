package bits.mt.ss.dda.groupbm.couriermgmt.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.owasp.html.Sanitizers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import bits.mt.ss.dda.groupbm.couriermgmt.model.base.BaseErrorResponse;
import bits.mt.ss.dda.groupbm.couriermgmt.model.base.Links;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    LOG.error("In handleHttpRequestMethodNotSupported method: ", ex);

    return buildErrorResponseEntity(
        HttpStatus.METHOD_NOT_ALLOWED,
        new ApiError(CommonErrors.REQUEST_METHOD_NOT_SUPPORTED),
        request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    LOG.error("In handleMissingServletRequestParameter method: ", ex);

    return buildErrorResponseEntity(
        HttpStatus.BAD_REQUEST, new ApiError(CommonErrors.MISSING_REQUIRED_PARAMETER), request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    LOG.error("In handleHttpMessageNotReadable method: ", ex);
    Throwable cause = ex.getCause();

    if (cause instanceof MismatchedInputException) {
      MismatchedInputException ife = (MismatchedInputException) cause;
      String targetType = ife.getTargetType().getSimpleName();
      String fieldName = "";

      for (JsonMappingException.Reference ref : ife.getPath()) {
        fieldName = ref.getFieldName();
      }

      if (cause instanceof InvalidFormatException) {
        Object value = ((InvalidFormatException) ife).getValue();

        return buildErrorResponseEntity(
            HttpStatus.BAD_REQUEST,
            new ApiError(CommonErrors.FIELD_VALIDATION, value, targetType, fieldName),
            request);
      } else {
        return buildErrorResponseEntity(
            HttpStatus.BAD_REQUEST,
            new ApiError(CommonErrors.INVALID_VALUE, fieldName, targetType),
            request);
      }
    } else if (cause instanceof JsonParseException) {
      return buildErrorResponseEntity(
          HttpStatus.BAD_REQUEST,
          new ApiError(CommonErrors.UNABLE_TO_PARSE_JSON, cause.getMessage()),
          request);
    } else {
      return buildErrorResponseEntity(
          HttpStatus.BAD_REQUEST, new ApiError(CommonErrors.INVALID_JSON_FORMAT), request);
    }
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    LOG.error("In handleExceptionInternal method: ", ex);
    Throwable cause = ex.getCause();

    if (cause instanceof NumberFormatException) {
      return buildErrorResponseEntity(
          HttpStatus.NOT_FOUND, new ApiError(CommonErrors.NUMERIC_VALUES_ONLY), request);
    } else {
      return buildErrorResponseEntity(
          HttpStatus.INTERNAL_SERVER_ERROR,
          new ApiError(CommonErrors.GENERIC_EXCEPTION, ExceptionUtils.getRootCauseMessage(ex)),
          request);
    }
  }

  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(
      MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    LOG.error("In handleMissingPathVariable method: ", ex);

    return buildErrorResponseEntity(
        HttpStatus.BAD_REQUEST, new ApiError(CommonErrors.MISSING_PATH_VARIABLE), request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    LOG.error("In handleMethodArgumentNotValid method: ", ex);

    List<ApiError> apiErrors = new ArrayList<>();

    for (FieldError er : ex.getBindingResult().getFieldErrors()) {
      apiErrors.add(new ApiError(CommonErrors.FIELD_ERROR, er.getField(), er.getDefaultMessage()));
    }

    for (ObjectError er : ex.getBindingResult().getGlobalErrors()) {
      apiErrors.add(
          new ApiError(CommonErrors.GLOBAL_ERROR, er.getObjectName(), er.getDefaultMessage()));
    }

    return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, apiErrors, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    LOG.error("In handleHttpMediaTypeNotSupported method", ex);

    return buildErrorResponseEntity(
        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
        new ApiError(CommonErrors.MEDIA_TYPE_NOT_SUPPORTED),
        request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
      HttpMediaTypeNotAcceptableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    LOG.error("In handleHttpMediaTypeNotAcceptable method", ex);

    return buildErrorResponseEntity(
        HttpStatus.NOT_ACCEPTABLE, new ApiError(CommonErrors.MEDIA_TYPE_NOT_ACCEPTABLE), request);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(BaseException.class)
  protected ResponseEntity<Object> handleBaseException(BaseException ex, WebRequest webRequest) {
    LOG.error("In handleBaseException method: ", ex);
    return buildErrorResponseEntity(ex.getStatus(), ex.getErrors(), webRequest);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<Object> handleIllegalArgumentException(
      IllegalArgumentException ex, WebRequest webRequest) {
    LOG.error("In handleIllegalArgumentException method: ", ex);
    ApiError error = new ApiError(CommonErrors.ILLEGAL_ARGUMENT_EXCEPTION, ex.getMessage());
    return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, error, webRequest);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(UnprocessableEntityException.class)
  protected ResponseEntity<Object> handleUnprocessableEntityException(
      UnprocessableEntityException ex, WebRequest webRequest) {
    LOG.error("In handleUnprocessableEntityException method: ", ex);
    return buildErrorResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, ex.getError(), webRequest);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFoundException(
      EntityNotFoundException ex, WebRequest webRequest) {
    LOG.error("In handleEntityNotFoundException method: ", ex);
    return buildErrorResponseEntity(HttpStatus.NOT_FOUND, ex.getError(), webRequest);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(ServiceUnavailableException.class)
  protected ResponseEntity<Object> handleServiceUnavailableException(
      ServiceUnavailableException ex, WebRequest webRequest) {
    LOG.error("In handleServiceUnavailableException method: ", ex);
    return buildErrorResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, ex.getError(), webRequest);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedException.class)
  protected ResponseEntity<Object> handleUnauthorizedException(
      UnauthorizedException ex, WebRequest webRequest) {
    LOG.error("In handleUnauthorizedException method: ", ex);
    return buildErrorResponseEntity(HttpStatus.UNAUTHORIZED, ex.getError(), webRequest);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
  public ResponseEntity<Object> unhandledException(Exception ex, WebRequest webRequest) {
    LOG.error("In unhandledException method: ", ex);
    return buildErrorResponseEntity(
        HttpStatus.INTERNAL_SERVER_ERROR,
        new ApiError(CommonErrors.UNHANDLED_EXCEPTION, ex.getMessage()),
        webRequest);
  }

  private ResponseEntity<Object> buildErrorResponseEntity(
      HttpStatus status, ApiError apiError, WebRequest webRequest) {
    return buildErrorResponseEntity(status, Collections.singletonList(apiError), webRequest);
  }

  private ResponseEntity<Object> buildErrorResponseEntity(
      HttpStatus status, List<ApiError> apiErrors, WebRequest webRequest) {
    BaseErrorResponse response = new BaseErrorResponse();
    response.setErrors(apiErrors);

    String uri =
        Sanitizers.FORMATTING.sanitize(
            ((ServletWebRequest) webRequest).getRequest().getRequestURI());
    response.setLinks(new Links(uri));

    if (status == null) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    return new ResponseEntity<>(response, status);
  }
}
