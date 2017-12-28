package ru.nsu.ccfit.boltava.tictactoe.model.networking.binding.response;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import ru.nsu.ccfit.boltava.tictactoe.App;

/**
 * Created by alexey on 24.12.17.
 */

public class ErrorUtils {

    public static ErrorResponse parseError(Response<?> response) {

        Converter<ResponseBody, ErrorResponse> converter = App.retrofit()
                .responseBodyConverter(ErrorResponse.class, new Annotation[0]);

        try {
            return converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErrorResponse();
        }
    }

}
