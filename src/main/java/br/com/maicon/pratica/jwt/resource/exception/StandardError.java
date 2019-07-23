package br.com.maicon.pratica.jwt.resource.exception;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class StandardError {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timeStamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public static final class Builder {
        private StandardError standardError;

        private Builder() {
            standardError = new StandardError();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder now() {
            return timeStamp(LocalDateTime.now());
        }

        public Builder timeStamp(LocalDateTime timeStamp) {
            standardError.setTimeStamp(timeStamp);
            return this;
        }

        public Builder status(Integer status) {
            standardError.setStatus(status);
            return this;
        }

        public Builder error(String error) {
            standardError.setError(error);
            return this;
        }

        public Builder message(String message) {
            standardError.setMessage(message);
            return this;
        }

        public Builder path(String path) {
            standardError.setPath(path);
            return this;
        }

        public StandardError build() {
            return standardError;
        }
    }
}